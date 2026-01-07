package com.snek.fancyplayershops.input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.configs.Configs;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.utils.MinecraftUtils;

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
 * Utility class containing methods to detect displays players are looking at and handle hover events.
 */
public abstract class HoverReceiver {
    private HoverReceiver() {}


    // The list of displays that were targeted in the previous tick
    private static @NotNull Set<@NotNull ProductDisplay> targetedDisplaysOld = new LinkedHashSet<>();


    // Partial ray casting batch data
    private static @Nullable List<Player> playerListSnapshot = null;
    private static @Nullable Set<ProductDisplay> targetedDisplays = null;
    private static int updateIndex = 0;








    /**
     * Tick operations. This function spawns and removes the focus displays depending on what players are currently looking at.
     */
    public static void tick() {


        // If this is the first iteration ever or all the batches have been processed
        if(updateIndex == 0) {

            // Reset the lists
            playerListSnapshot   = new ArrayList<>();
            targetedDisplays        = new LinkedHashSet<>();

            // Recalculate player list snapshot
            for(final ServerLevel level : FrameworkLib.getServer().getAllLevels()) {
                for(final Player player : level.players()) {
                    playerListSnapshot.add(player);
                }
            }

            // Set all previously focused displays' next focus state to false
            for(final ProductDisplay display : targetedDisplaysOld) {
                display.setFocusStateNext(false);
            }
        }




        // Find currently focused displays and their viewers
        final int batchSize = Math.max(1, playerListSnapshot.size() / Configs.getPerf().ray_casting_batches.getValue());
        for(int i = 0; i < batchSize && updateIndex < playerListSnapshot.size(); ++i, ++updateIndex) {
            final Player player = playerListSnapshot.get(updateIndex);


            // Skip player if they are dead or in spectator mode or have a HUD open or they aren't looking at any display
            if(player.isSpectator() || player.isDeadOrDying()) continue;
            final ProductDisplay display = HoverReceiver.getLookedAtDisplay(player);
            if(display == null) continue;


            // Try to add a display to the focused displays list. If it's not already in it, set its next focus state to true
            final boolean isDisplayNew = targetedDisplays.add(display);
            if(isDisplayNew) {
                display.setFocusStateNext(true);
                display.setViewer(player);
            }

            // If the display is already in the list, check if the current player has a higher priority. If that's the case, update the viewer value
            else {
                if(getPlayerPriority(display, player) > getPlayerPriority(display, display.getViewer())) {
                    display.setViewer(player);
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

        // Unfocus all the displays that don't have any viewer anymore. Set their viewer to null
        targetedDisplaysOld.removeAll(targetedDisplays);
        for(final ProductDisplay display : targetedDisplaysOld) {
            if(!display.isRemoved()) {
                display.setViewer(null);
                display.updateFocusState();
            }
        }


        // Update looked-at displays
        for(final ProductDisplay display : targetedDisplays) {
            if(!display.isRemoved() && display.getuser() != null) {

                // If the user isn't looking at the display anymore, unfocus it
                if(display.getViewer() != display.getuser()) {
                    display.setFocusStateNext(false);
                }
            }
            display.updateFocusState();
        }
        targetedDisplaysOld = targetedDisplays;
    }








    /**
     * Calculates the priority the provided player has on a display.
     * @param display The display.
     * @param player The player.
     * @return The priority.
     *     <p> User:          Highest priority
     *     <p> Owner:         Lower priority
     *     <p> Other players: Lowest priority
     */
    public static int getPlayerPriority(final @NotNull ProductDisplay display, final @NotNull Player player) {
        if(player == display.getuser()) return 0;
        if(player.getUUID().equals(display.getOwnerUuid())) return -1;
        return -2;
    }
















    /**
     * Returns the position of the block targeted by a player.
     * <p> The ray casting is hitbox-based, so it can go through gaps in non-full blocks and it ignores fluids.
     * @param player The player.
     * @return The position of the targeted block, or null if no block is found.
     */
    private static @Nullable Vec3 getTargetBlockPrecise(final @NotNull Player player) {
        final Level level = player.level();

        // Perform ray cast
        final Vec3 eyePos = player.getEyePosition();
        final Vec3 lookDirection = player.getViewVector(1.0F);
        final Float reach = Configs.getPerf().reach_distance.getValue();
        final BlockHitResult result = level.clip(new ClipContext(
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
    private static @NotNull List<@NotNull Vec3> getViewCollisions(final @NotNull Player player, final double maxDistance) {
        final List<Vec3> blockPositions = new ArrayList<>();
        Vec3i lastBlockPosition = new Vec3i(0, 0, 0);

        final Vec3 lookDirection = player.getViewVector(1f);
        final Float stepSize = Configs.getPerf().ray_casting_step.getValue();
        final Vec3 step = lookDirection.normalize().multiply(stepSize, stepSize, stepSize);
        Vec3 currentPos = player.getEyePosition();
        double distanceTraveled = 0;


        // Manually perform ray casting
        while(distanceTraveled < maxDistance) {

            // Add block to list if not present
            final Vec3i currentPosInt = new Vec3i((int)currentPos.x, (int)currentPos.y, (int)currentPos.z);
            if(!lastBlockPosition.equals(currentPosInt)) {
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
     * Finds the closest display that directly collides with the player's view.
     * <p> The ray casting is hitbox-based, so it can go through gaps in non-full blocks and it ignores fluids.
     * @param player The player.
     * @return The Product display instance, or null if the player is not looking at one.
     */
    public static ProductDisplay getLookedAtDisplay(final @NotNull Player player) {
        final Vec3 playerEyePos = player.getEyePosition();


        // Check the number of displays in the chunks the player can reach
        final BlockPos playerPos = player.blockPosition();
        final ChunkPos playerChunk = new ChunkPos(playerPos);
        boolean check = ProductDisplayManager.chunkHasDisplays(playerChunk);
        final int reach = Math.round(Configs.getPerf().reach_distance.getValue());
        final int minX = playerPos.getX() - reach;
        final int maxX = playerPos.getX() + reach;
        final int minZ = playerPos.getZ() - reach;
        final int maxZ = playerPos.getZ() + reach;
        if(!check) { final ChunkPos targetChunk = new ChunkPos(new BlockPos(minX, 0, minZ)); if(!targetChunk.equals(playerChunk)) check = ProductDisplayManager.chunkHasDisplays(targetChunk); }
        if(!check) { final ChunkPos targetChunk = new ChunkPos(new BlockPos(maxX, 0, maxZ)); if(!targetChunk.equals(playerChunk)) check = ProductDisplayManager.chunkHasDisplays(targetChunk); }
        if(!check) { final ChunkPos targetChunk = new ChunkPos(new BlockPos(minX, 0, maxZ)); if(!targetChunk.equals(playerChunk)) check = ProductDisplayManager.chunkHasDisplays(targetChunk); }
        if(!check) { final ChunkPos targetChunk = new ChunkPos(new BlockPos(maxX, 0, minZ)); if(!targetChunk.equals(playerChunk)) check = ProductDisplayManager.chunkHasDisplays(targetChunk); }


        // If they contain at least one display
        if(check) {

            // Calculate ray casting max distance, then find and sort colliding blocks
            final Vec3 targetBlock = getTargetBlockPrecise(player);
            final double maxDistance = targetBlock != null ? targetBlock.distanceTo(playerEyePos) + Configs.getPerf().ray_casting_step.getValue() * 1.2 : Configs.getPerf().reach_distance.getValue();
            final List<Vec3> collidingBlocks = getViewCollisions(player, maxDistance);
            Collections.sort(collidingBlocks, Comparator.comparingDouble(b -> b.distanceToSqr(playerEyePos)));

            // Find target display
            for(final Vec3 pos : collidingBlocks) {
                final Vec3i blockPos = MinecraftUtils.doubleToBlockCoords(new Vector3d(pos.toVector3f()));
                final ProductDisplay display = ProductDisplayManager.findDisplay(new BlockPos(blockPos), player.level());
                if(display != null) return display;
            }
        }


        // Return null if no item display entity is near the player or no product display is targeted
        return null;
    }
}
