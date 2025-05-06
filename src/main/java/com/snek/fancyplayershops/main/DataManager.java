package com.snek.fancyplayershops.main;

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
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
















/**
 * A class that takes care of saving and loading shops and other player data.
 */
public abstract class DataManager {
    private DataManager() {}
    public static final int PULL_UPDATES_PER_TICK = 4;




    // Storage files
    private static final @NotNull Path SHOP_STORAGE_DIR;
    static {
        SHOP_STORAGE_DIR = FabricLoader.getInstance().getConfigDir().resolve(FancyPlayerShops.MOD_ID + "/shops");
        try {
            Files.createDirectories(SHOP_STORAGE_DIR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(SHOP_STORAGE_DIR == null) throw new RuntimeException("Shops could not be loaded: Storage directory is null.");
    }


    // Stores the shops of players, identifying them by their owner's UUID and their coordinates and world in the format "x,y,z,worldId"
    private static final @NotNull Map<@NotNull String, @NotNull Shop> shopsByCoords = new HashMap<>();
    private static final @NotNull Map<@NotNull String, @NotNull Shop> shopsByOwner  = new HashMap<>();
    private static boolean dataLoaded = false;

    // Async update list
    private static int updateIndex = 0;
    private static List<Shop> updateSnapshot = List.of();








    /**
     * Saves the shop data in its config file.
     */
    public static void saveShop(final @NotNull Shop shop) {

        // Create map entry if absent, then add the new shop to the player's shops
        shopsByOwner .put(shop.getOwnerUuid().toString(), shop);
        shopsByCoords.put(shop.getIdentifier(),           shop);


        // Create directory for the world
        final Path shopStorageDir = SHOP_STORAGE_DIR.resolve(shop.getWorldId());
        try {
            Files.createDirectories(shopStorageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Create this shop's config file if absent, then save the JSON in it
        final File shopStorageFile = new File(shopStorageDir + "/" + shop.getIdentifierNoWorld() + ".json");
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


        // For each world directory
        for(final File shopStorageDir : SHOP_STORAGE_DIR.toFile().listFiles()) {

            // For each shop file
            final File[] shopStorageFiles = shopStorageDir.listFiles();
            if(shopStorageFiles != null) for(File shopStorageFile : shopStorageFiles) {

                // Read file
                Shop retrievedShop = null;
                try (final Reader reader = new FileReader(shopStorageFile)) {
                    retrievedShop = new Gson().fromJson(reader, Shop.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Recalculate transient members and update shop maps
                if(retrievedShop != null) {
                    if(retrievedShop.reinitTransient()) {
                        shopsByOwner .put(retrievedShop.getOwnerUuid().toString(), retrievedShop);
                        shopsByCoords.put(retrievedShop.getIdentifier(),           retrievedShop);
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
    public static Shop findShop(final @NotNull BlockPos pos, final @NotNull World world) {
        return findShop(pos, world.getRegistryKey().getValue().toString());
    }




    /**
     * Deletes the data associated with this hop instance.
     * @param shop The shop to delete.
     */
    public static void deleteShop(final @NotNull Shop shop) {

        // Remove shop from the runtime maps
        shopsByCoords.remove(shop.getIdentifier());
        shopsByOwner.remove(shop.getOwnerUuid().toString());

        // Delete the config file
        final File shopStorageFile = new File(SHOP_STORAGE_DIR + "/" + shop.getWorldId() + "/" + shop.getIdentifierNoWorld() + ".json");
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
}
