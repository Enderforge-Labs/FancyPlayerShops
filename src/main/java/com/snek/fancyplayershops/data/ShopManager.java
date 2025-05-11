package com.snek.fancyplayershops.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import com.google.gson.Gson;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
















/**
 * A class that handles active shops and takes care of loading and saving their data.
 */
public abstract class ShopManager {
    private ShopManager() {}
    private static final Random rnd = new Random();
    public static final int PULL_UPDATES_PER_TICK = 1;




    // Stores the shops of players, identifying them by their owner's UUID and their coordinates and world in the format "x,y,z,worldId"
    private static final @NotNull Map<@NotNull String, @Nullable Shop> shopsByCoords = new HashMap<>();
    private static final @NotNull Map<@NotNull String, @Nullable HashSet<@NotNull Shop>> shopsByOwner  = new HashMap<>();
    private static boolean dataLoaded = false;

    // Async update list
    private static int updateIndex = 0;
    private static @NotNull List<@NotNull Shop> updateSnapshot = List.of();

    // Keeps track of the amount of shops in each chunk
    private static final @NotNull Map<@NotNull ChunkPos, @Nullable Integer> chunkShopNumber = new HashMap<>();







    /**
     * Registers the shop in the runtime maps.
     * <p> Calling this method on a shop that's already registered will have no effect.
     */
    public static void registerShop(final @NotNull Shop shop) {
        if(shopsByCoords.put(shop.getIdentifier(), shop) == null){
            shopsByOwner.putIfAbsent(shop.getOwnerUuid().toString(), new HashSet<>());
            shopsByOwner.get(shop.getOwnerUuid().toString()).add(shop);

            final ChunkPos chunkPos = new ChunkPos(shop.getPos());
            chunkShopNumber.putIfAbsent(chunkPos, 0);
            chunkShopNumber.put(chunkPos, chunkShopNumber.get(chunkPos) + 1);
        }
    }

    /**
     * Unregisters the shop from the runtime maps.
     * <p> Calling this method on a shop that's already not registered will have no effect.
     */
    public static void unregisterShop(final @NotNull Shop shop) {
        if(shopsByCoords.remove(shop.getIdentifier(), shop)) {
            final HashSet<Shop> set = shopsByOwner.get(shop.getOwnerUuid().toString());
            if(set != null) set.remove(shop);

            final ChunkPos chunkPos = new ChunkPos(shop.getPos());
            chunkShopNumber.put(chunkPos, chunkShopNumber.get(chunkPos) - 1);
        }
    }

    public static boolean chunkHasShops(final @NotNull ChunkPos chunkPos) {
        final Integer n = chunkShopNumber.get(chunkPos);
        return n != null && n > 0;
    }








    /**
     * Saves the data of the specified shop in its config file.
     * @param shop The shop to save.
     */
    public static void saveShop(final @NotNull Shop shop) {

        // Create directory for the world
        final Path levelStorageDir = FancyPlayerShops.getStorageDir().resolve("shops/" + shop.getWorldId());
        try {
            Files.createDirectories(levelStorageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Create this shop's config file if absent, then save the JSON in it
        final File shopStorageFile = new File(levelStorageDir + "/" + shop.getIdentifierNoWorld() + ".json");
        try (final Writer writer = new FileWriter(shopStorageFile)) {
            new Gson().toJson(shop, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    /**
     * Loads all the player shops into the runtime map if needed.
     * <p> Must be called on server started event (After the worlds are loaded!).
     * <p> If the data has already been loaded, the call will have no effect.
     */
    public static void loadShops() {
        if(dataLoaded) return;
        dataLoaded = true;


        for(final File levelStorageDir : FancyPlayerShops.getStorageDir().resolve("shops").toFile().listFiles()) {

            // For each shop file
            final File[] shopStorageFiles = levelStorageDir.listFiles();
            if(shopStorageFiles != null) for(final File shopStorageFile : shopStorageFiles) {

                // Read file and deserialize the data
                Shop retrievedShop = null;
                try (final Reader reader = new FileReader(shopStorageFile)) {
                    retrievedShop = new Gson().fromJson(reader, Shop.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Recalculate transient members and update shop maps
                if(retrievedShop != null) {
                    if(retrievedShop.reinitTransient()) {
                        registerShop(retrievedShop);
                    }
                }
            }
        }
    }








    /**
     * Returns the Shop instance present at a certain block position.
     * @param pos The block position.
     * @param worldId The ID of the world the shop is in.
     * @return The shop, or null if no shop is there.
    */
    public static Shop findShop(final @NotNull BlockPos pos, final @NotNull String worldId) {
        return shopsByCoords.get(Shop.calcShopIdentifier(pos, worldId));
    }

    /**
     * Returns the Shop instance present at a certain block position.
     * @param pos The block position.
     * @param world The world the shop is in.
     * @return The shop, or null if no shop is there.
    */
    public static Shop findShop(final @NotNull BlockPos pos, final @NotNull Level world) {
        return findShop(pos, world.dimension().location().toString());
    }




    /**
     * Deletes the data associated with this hop instance.
     * @param shop The shop to delete.
     */
    public static void deleteShop(final @NotNull Shop shop) {

        // Remove shop from the runtime maps
        unregisterShop(shop);

        // Delete the config file
        final File shopStorageFile = FancyPlayerShops.getStorageDir().resolve("shops/" + shop.getWorldId() + "/" + shop.getIdentifierNoWorld() + ".json").toFile();
        shopStorageFile.delete();
    }








    /**
     * Updates PULL_UPDATES_PER_TICK shops each call, making them pull items from nearby inventories.
     * <p> Must be called each server tick.
     */
    public static void pullItems(){

        // Refresh snapshot if needed
        if(updateIndex == 0) {
            updateSnapshot = new ArrayList<>(shopsByCoords.values());
        }

        // Update shops
        for(int i = 0; i < PULL_UPDATES_PER_TICK && updateIndex < updateSnapshot.size(); i++, updateIndex++) {
            updateSnapshot.get(updateIndex).pullItems();
        }

        // Reset snapshop if this iteration reached its end
        if(updateIndex >= updateSnapshot.size()) {
            updateIndex = 0;
        }
    }







    /**
     * Removes all shops near the specified position, sending all the items to their owner's stash.
     * @param world The target world.
     * @param pos The center of the purge radius.
     * @param radius The maximum distance from pos shops can have in order to be purged.
     * @return The number of shops that were removed.
     */
    public static int purge(final @NotNull ServerLevel world, final @NotNull Vector3f pos, final float radius) {
        int r = 0;
        final List<Shop> shops = new ArrayList<>(shopsByCoords.values());
        for(final Shop shop : shops) {
            if(shop.getWorld() == world && shop.calcDisplayPos().sub(pos).length() <= radius) {

                // Send feedback to affected player if they are online
                final Player owner = FancyPlayerShops.getServer().getPlayerList().getPlayer(shop.getOwnerUuid());
                if(owner != null && shop.getItem().getItem() != Items.AIR) owner.displayClientMessage(new Txt()
                    .cat(new Txt("Your shop \"")).red()
                    .cat(MinecraftUtils.getFancyItemName(shop.getItem()).getString())
                    .cat(new Txt("\" has been deleted by an admin.").red())
                .get(), false);

                // Stash and delete the shop, then increase the deleted shops counter
                shop.stash();
                shop.delete();
                ++r;
            }
        }
        return r;
    }




    /**
     * Fill an area around the specified position with shop blocks.
     * @param world The target world.
     * @param pos The center of the fill area.
     * @param radius The maximum distance to reach on each cardinal direction.
     * @param owner The owner of the newly created shops.
     * @return The number of shops that were created.
     */
    public static int fill(final @NotNull ServerLevel world, final @NotNull Vector3f pos, final float radius, final @NotNull Player owner) {
        int r = 0;
        for(float i = pos.x - radius; i < pos.x + radius; ++i) {
            for(float j = pos.y - radius; j < pos.y + radius; ++j) {
                for(float k = pos.z - radius; k < pos.z + radius; ++k) {
                    final BlockPos blockPos = new BlockPos(MinecraftUtils.doubleToBlockCoords(new Vec3(i, j, k)));
                    if(world.getBlockState(blockPos).isAir()) {
                        final Shop shop = new Shop(world, blockPos, owner);
                        shop.changeItem(Items.STONE.getDefaultInstance());
                        shop.addDefaultRotation((float)Math.toRadians(45f) * (rnd.nextInt() % 8));
                        shop.invalidateItemDisplay();
                        ++r;
                    }
                }
            }
        }
        return r;
    }
}
