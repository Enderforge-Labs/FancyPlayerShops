package com.snek.fancyplayershops.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.snek.fancyplayershops.data.data_types.Shop;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.utils.UtilityClassBase;

import net.minecraft.server.level.ServerPlayer;
















/**
 * A class that handles player shops.
 */
public class ShopManager extends UtilityClassBase {
    public static final UUID   DEFAULT_SHOP_UUID = UUID.fromString("def00000-0000-0000-0000-000000000000");
    public static final String DEFAULT_SHOP_NAME = "Uncategorized";
    private ShopManager() {}


    // Player shop data
    private static final @NotNull Map<@NotNull UUID, @Nullable List<@NotNull Shop>> shopsList = new HashMap<>();
    private static boolean dataLoaded = false;


    // The list of shops scheduled for saving
    private static @NotNull List<@NotNull Shop> scheduledForSaving = new ArrayList<>();


    /**
     * Calculates the path to the directory where shops are saved.
     * @return The path to the save file directory.
     */
    public static @NotNull Path calcShopDirPath() {
        return FancyPlayerShops.getStorageDir().resolve("shops");
    }

    /**
     * Calculates the path to the save file of the specified shop.
     * @param shop The shop.
     * @return The path to the save file of {@code shop}.
     */
    public static @NotNull Path calcShopFilePath(final @NotNull Shop shop) {
        return calcShopDirPath().resolve(shop.getUuid().toString() + ".json");
    }








    /**
     * Creates a new shop and associates it with the specified player.
     * <p>
     * Shops with duplicate UUIDs are not added.
     * @param shop The shop to add.
     */
    public static void createShop(final @NotNull Shop shop) {

        // Get the list of shops
        final List<Shop> shops = shopsList.computeIfAbsent(shop.getOwnerUuid(), uuid -> new ArrayList<>());


        // Return if UUID exists, add shop otherwise
        for(final Shop g : shops) {
            if(g.getUuid().equals(shop.getUuid())) return;
        }
        shops.add(shop);
        scheduleShopSave(shop);
    }




    /**
     * Deletes a shop and its associated data file.
     * @param shop The shop to delete.
     */
    public static void deleteShop(final @NotNull Shop shop) {

        // Get the list of shops
        final List<Shop> shops = shopsList.computeIfAbsent(shop.getOwnerUuid(), uuid -> new ArrayList<>());

        // Remove shop from the map, then dissolve it and remove the file
        shops.remove(shop);
        shop.dissolve();

        // Delete the config file
        calcShopFilePath(shop).toFile().delete();
    }




    public static Shop registerDisplay(final @NotNull ProductDisplay display, final @NotNull UUID shopUUID) {
        final UUID ownerUUID = display.getOwnerUuid();


        // Create default shop if needed
        //! This special shop is not stored to file or loaded
        if(shopUUID.equals(DEFAULT_SHOP_UUID)) {
            createShop(new Shop(DEFAULT_SHOP_NAME, DEFAULT_SHOP_UUID, ownerUUID));
            //TODO use italic grey once colors are implemented
            //TODO allow players to use &[0-9a-gulomkr&]
        }


        // Find shop
        final List<Shop> shops = shopsList.get(ownerUUID);
        if(shops != null) {
            final Optional<Shop> shopOpt = shops.stream().filter(e -> e.getUuid().equals(shopUUID)).findFirst();

            // Add display to the shop if it exists
            if(shopOpt.isPresent()) {
                final Shop shop = shopOpt.get();
                shop.addBalance(display.getBalance());
                shop.addDisplay(display);
                return shop;
            }
        }

        //TODO add error detection and logging
        return null;
    }




    public static void unregisterDisplay(final @NotNull ProductDisplay display) {

        // Find shop
        final List<Shop> shops = shopsList.get(display.getOwnerUuid());
        if(shops != null) {
            final Optional<Shop> shopOpt = shops.stream().filter(e -> e.getUuid().equals(display.getShop().getUuid())).findFirst();

            // Remove display from the shop if it exists
            if(shopOpt.isPresent()) {
                final Shop shop = shopOpt.get();
                shop.subBalance(display.getBalance());
                shop.removeDisplay(display);
            }
        }
    }







    /**
     * Schedules the specified shop for saving.
     * <p> Call {@link #saveScheduledShops()} to save all scheduled shops.
     * <p> Notice: The default shop (with UUID {@link #DEFAULT_SHOP_UUID}) cannot be saved to file.
     *     This special shop is recreated whenever needed.
     * @param playerUUID The UUID of the player.
     * @param shop The shop to save.
     */
    public static void scheduleShopSave(final @NotNull Shop shop) {
        if(!shop.isScheduledForSave()) {
            scheduledForSaving.add(shop);
            shop.setScheduledForSave(true);
        }
    }




    /**
     * Saves the scheduled shops in their config files.
     */
    public static void saveScheduledShops() {

        // Create directory for the shops
        try {
            Files.createDirectories(calcShopDirPath());
        } catch(final IOException e) {
            FancyPlayerShops.LOGGER.error("Couldn't create storage directory for player shops", e);
        }


        // Iterate shops. Save them if they are not dissolved and their UUID doesn't match the default shop
        for(final Shop shop : scheduledForSaving) {
            if(!shop.isDissolved() && !shop.getUuid().equals(DEFAULT_SHOP_UUID)) {

                // Create the JSON objects that contains the shop data
                final JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("ownerUUID",   shop.getOwnerUuid().toString());
                jsonObject.addProperty("uuid",        shop.getUuid().toString());
                jsonObject.addProperty("displayName", shop.getDisplayName());


                // Create this shop's config file if absent, then save the JSON in it
                try(final Writer writer = new FileWriter(calcShopFilePath(shop).toFile())) {
                    new Gson().toJson(jsonObject, writer);
                } catch(final IOException e) {
                    FancyPlayerShops.LOGGER.error("Couldn't create storage file for the shop {}", shop.getDisplayName(), e);
                }
            }


            // Flag the shops as not scheduled
            shop.setScheduledForSave(false);
        }
        scheduledForSaving = new ArrayList<>();
    }








    /**
     * Loads all the shops into the runtime map, if needed.
     * <p>
     * Must be called on server started event (After the levels are loaded!).
     * <p>
     * If the data has already been loaded, the call will have no effect.
     */
    public static void loadShops() {
        if(dataLoaded) return;
        dataLoaded = true;

        // For each shop storage file
        final File[] shopStorageFiles = calcShopDirPath().toFile().listFiles();
        if(shopStorageFiles != null) for(final File shopStorageFile : shopStorageFiles) {

            // Read the file
            final String fileName = shopStorageFile.getName();
            final UUID shopUUID = UUID.fromString(fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName);
            try(FileReader reader = new FileReader(shopStorageFile)) {

                // Load the data into the runtime map
                final JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
                createShop(new Shop(
                    jsonObject.get("displayName").getAsString(),
                    shopUUID,
                    UUID.fromString(jsonObject.get("ownerUUID").getAsString())
                ));
            } catch(final IOException e) {
                FancyPlayerShops.LOGGER.error("Couldn't read the storage file for the shop {}", shopStorageFile.getName(), e);
            }
        }
    }








    public static List<Shop> getShops(final @NotNull ServerPlayer player) {
        return shopsList.get(player.getUUID());
    }
}

