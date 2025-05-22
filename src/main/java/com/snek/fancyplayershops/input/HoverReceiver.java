package com.snek.fancyplayershops.input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.hud_ui.HudCanvas;
import com.snek.fancyplayershops.main.Configs;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.framework.debug.DebugCheck;
import com.snek.framework.debug.UiDebugWindow;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.utils.MinecraftUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
















/**
 * Utility class containing methods to detect shops players are looking at and handle hover events.
 */
public abstract class HoverReceiver {
    private HoverReceiver() {}


    // The list of shops that were targeted in the previous tick
    private static @NotNull Set<@NotNull Shop> targetedShopsOld = new LinkedHashSet<>();


    // Partial ray casting batch data
    private static @Nullable List<Player> playerListSnapshot = null;
    private static @Nullable Set<Shop> targetedShops         = null;
    private static @Nullable Set<Shop> shopsWithTargetedElm  = null;
    private static int updateIndex = 0;


    // Optimization structures
    private static @NotNull Map<@NotNull Player, @Nullable Elm> targetedElms = new HashMap<>();








    /**
     * Tick operations. This function spawns and removes the focus displays depending on what players are currently looking at.
     * @param serverWorlds The list of worlds to process. Only shops in these worlds are updated.
     */
    public static void tick() {


        // If this is the first iteration ever or all the batches have been processed
        if(updateIndex == 0) {

            // Reset the lists
            playerListSnapshot   = new ArrayList<>();
            targetedShops        = new LinkedHashSet<>();
            shopsWithTargetedElm = new LinkedHashSet<>();

            // Recalculate player list snapshot
            for(final ServerLevel serverWorld : FancyPlayerShops.getServer().getAllLevels()) {
                for(final Player player : serverWorld.players()) {
                    playerListSnapshot.add(player);
                }
            }

            // Set all previously focused shops' next focus state to false
            for(final Shop shop : targetedShopsOld) {
                shop.setFocusStateNext(false);
            }
        }




        // Find currently focused shops and their viewers
        final int batchSize = Math.max(1, playerListSnapshot.size() / Configs.perf.ray_casting_batches.getValue());
        for(int i = 0; i < batchSize && updateIndex < playerListSnapshot.size(); ++i, ++updateIndex) {
            final Player player = playerListSnapshot.get(updateIndex);


            // Skip player if they are dead or in spectator mode or have a HUD open or they aren't looking at any shop
            if(player.isSpectator() || player.isDeadOrDying()) continue;
            if(HudCanvas.hasOpenHud(player)) continue;
            final Shop shop = HoverReceiver.getLookedAtShop(player);
            if(shop == null) continue;


            // Add shop to list of shops with looked at elements if the player is in the looked at elements key list
            if(targetedElms.containsKey(player)) {
                shopsWithTargetedElm.add(shop);
            }

            // Try to add a shop to the focused shops list. If it's not already in it, set its next focus state to true
            final boolean isShopNew = targetedShops.add(shop);
            if(isShopNew) {
                shop.setFocusStateNext(true);
                shop.setViewer(player);
            }

            // If the shop is already in the list, check if the current player has a higher priority. If that's the case, update the viewer value
            else {
                if(getPlayerPriority(shop, player) > getPlayerPriority(shop, shop.getViewer())) {
                    shop.setViewer(player);
                }
            }
        }




        // If all the batches have been processed, reset update index and finalize tick operations
        if(updateIndex >= playerListSnapshot.size()) {
            updateIndex = 0;
            finalizeTick();
        }
    }








    /**
     * Part of tick operations.
     * Called after all the ray casting batches have been processed.
     */
    public static void finalizeTick() {


        //! Debug window
        if(DebugCheck.isDebug()) {
            UiDebugWindow.getW().clear();
        }


        // Unfocus all the shops that don't have any viewer anymore. Set their viewer to null
        targetedShopsOld.removeAll(targetedShops);
        for(final Shop shop : targetedShopsOld) {
            if(!shop.isDeleted()) {
                shop.setViewer(null);
                shop.updateFocusState();
            }
            //FIXME it might be necessary to remove unfocused shops from the shops with elements set
        }


        // Update looked-at shops
        for(final Shop shop : targetedShops) {
            if(!shop.isDeleted() && shop.getuser() != null) {

                // If the shop doesnt have a targeted element, find the currently targeted element
                if(shop.getActiveCanvas() != null) {
                    if(!shopsWithTargetedElm.contains(shop)) {
                        final Elm targetedElm = shop.getActiveCanvas().findTargetedElement(shop.getuser());
                        if(targetedElm != null) {
                            targetedElms.put(shop.getuser(), targetedElm);
                        }
                    }
                }

                // If the user isn't looking at the shop anymore, unfocus it
                if(shop.getViewer() != shop.getuser()) {
                    shop.setFocusStateNext(false);
                }
            }
            shop.updateFocusState();
            shop.updateCanvasRotation();
        }
        targetedShopsOld = targetedShops;


        // Send hover events to the currently hovered elements
        final List<Player> toRemove = new ArrayList<>();
        for(final Entry<Player, Elm> entry : targetedElms.entrySet()) {
            entry.getValue().updateHoverState(entry.getKey());
            if(!entry.getValue().isHovered()) {
                toRemove.add(entry.getKey());
            }
        }
        // Remove elements that aren't being hovered on anymore and their parent shop from the maps
        for(final Player player : toRemove) {
            targetedElms.remove(player);
        }


        //! Debug window update
        if(DebugCheck.isDebug()) {
            UiDebugWindow.getW().revalidate();
            UiDebugWindow.getW().paintImmediately(0, 0, UiDebugWindow.getW().getWidth(), UiDebugWindow.getW().getHeight());
        }
    }








    /**
     * Calculates the priority the provided player has on a shop display.
     * @param shop The shop.
     * @param player The player.
     * @return The priority.
     *     <p> User:          Highest priority
     *     <p> Owner:         Lower priority
     *     <p> Other players: Lowest priority
     */
    public static int getPlayerPriority(final @NotNull Shop shop, final @NotNull Player player){
        if(player == shop.getuser()) return 0;
        if(player.getUUID() == shop.getOwnerUuid()) return -1;
        return -2;
    }
















    /**
     * Returns the position of the block targeted by a player.
     * <p> The ray casting is hitbox-based, so it can go through gaps in non-full blocks and it ignores fluids.
     * @param player The player.
     * @return The position of the targeted block, or null if no block is found.
     */
    private static @Nullable Vec3 getTargetBlockPrecise(final @NotNull Player player) {
        final Level world = player.level();

        // Perform ray cast
        final Vec3 eyePos = player.getEyePosition();
        final Vec3 lookDirection = player.getViewVector(1.0F);
        final Float reach = Configs.perf.reach_distance.getValue();
        final BlockHitResult result = world.clip(new ClipContext(
            eyePos,
            eyePos.add(lookDirection.multiply(reach, reach, reach)),
            ClipContext.Block.OUTLINE,
            ClipContext.Fluid.NONE,
            player
        ));

        if(result != null) return result.getLocation();
        else return null;
    }




    /**
     * Calculates the first location in which each block collides with the player's view.
     * @param player The player to cast the ray from.
     * @return A list of coordinates, one for each full block the ray collides with. Not sorted.
     */
    private static @NotNull List<@NotNull Vec3> getViewCollisisons(final @NotNull Player player, final double maxDistance) {
        final List<Vec3> blockPositions = new ArrayList<>();
        Vec3i lastBlockPosition = new Vec3i(0, 0, 0);

        final Vec3 lookDirection = player.getViewVector(1f);
        final Float stepSize = Configs.perf.ray_casting_step.getValue();
        final Vec3 step = lookDirection.normalize().multiply(stepSize, stepSize, stepSize);
        Vec3 currentPos = player.getEyePosition();
        double distanceTraveled = 0;


        // Manually perform ray casting
        while(distanceTraveled < maxDistance) {

            // Add block to list if not present
            final Vec3i currentPosInt = new Vec3i((int)currentPos.x, (int)currentPos.y, (int)currentPos.z);
            if(lastBlockPosition != currentPosInt) {
                lastBlockPosition = currentPosInt;
                blockPositions.add(currentPos);
            }

            // Update current position and distance
            currentPos = currentPos.add(step);
            distanceTraveled += stepSize;
        }

        return blockPositions;
    }




    /**
     * Finds the closest shop block that directly collides with the player's view.
     * <p> The ray casting is hitbox-based, so it can go through gaps in non-full blocks and it ignores fluids.
     * @param player The player.
     * @return The Shop instance of the shop block, or null if the player is not looking at one.
     */
    public static Shop getLookedAtShop(final @NotNull Player player) {
        final Vec3 playerEyePos = player.getEyePosition();


        // Check the number of shops in the chunks the player can reach
        final BlockPos playerPos = player.blockPosition();
        final ChunkPos playerChunk = new ChunkPos(playerPos);
        boolean check = ShopManager.chunkHasShops(playerChunk);
        final int reach = Math.round(Configs.perf.reach_distance.getValue());
        int minX = playerPos.getX() - reach;
        int maxX = playerPos.getX() + reach;
        int minZ = playerPos.getZ() - reach;
        int maxZ = playerPos.getZ() + reach;
        if(!check) { final ChunkPos targetChunk = new ChunkPos(new BlockPos(minX, 0, minZ)); if(!targetChunk.equals(playerChunk)) check = ShopManager.chunkHasShops(targetChunk); }
        if(!check) { final ChunkPos targetChunk = new ChunkPos(new BlockPos(maxX, 0, maxZ)); if(!targetChunk.equals(playerChunk)) check = ShopManager.chunkHasShops(targetChunk); }
        if(!check) { final ChunkPos targetChunk = new ChunkPos(new BlockPos(minX, 0, maxZ)); if(!targetChunk.equals(playerChunk)) check = ShopManager.chunkHasShops(targetChunk); }
        if(!check) { final ChunkPos targetChunk = new ChunkPos(new BlockPos(maxX, 0, minZ)); if(!targetChunk.equals(playerChunk)) check = ShopManager.chunkHasShops(targetChunk); }


        // If they contain at least one shop
        if(check) {

            // Calculate ray casting max distance, then find and sort colliding blocks
            final Vec3 targetBlock = getTargetBlockPrecise(player);
            final double maxDistance = targetBlock != null ? targetBlock.distanceTo(playerEyePos) + Configs.perf.ray_casting_step.getValue() * 1.2 : Configs.perf.reach_distance.getValue();
            final List<Vec3> collidingBlocks = getViewCollisisons(player, maxDistance);
            Collections.sort(collidingBlocks, Comparator.comparingDouble(b -> b.distanceToSqr(playerEyePos)));

            // Find target shop block
            for(final Vec3 pos : collidingBlocks) {
                final Vec3i blockPos = MinecraftUtils.doubleToBlockCoords(pos);
                final Shop shop = ShopManager.findShop(new BlockPos(blockPos), player.level());
                if(shop != null) return shop;
            }
        }


        // Return null if no item display entity is near the player or no shop block is targeted
        return null;
    }
}
