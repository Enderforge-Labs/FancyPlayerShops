package com.snek.fancyplayershops.data.shop;

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
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.frameworkconfig.data.DataManager;
import com.snek.fancyplayershops.data.display.ProductDisplay;
import com.snek.fancyplayershops.data.display.ProductDisplay_Manager;
import com.snek.frameworklib.data_types.containers.Option;
import com.snek.frameworklib.utils.UtilityClassBase;
import com.snek.frameworklib.utils.common.MinecraftUtils;

import net.minecraft.world.entity.player.Player;
















/**
 * A class that handles player shops.
 */
public class Shop_Manager extends DataManager<Shop> {

    // Static manager reference
    public static final Shop_Manager REF = new Shop_Manager();


    // Static data
    public static final String DEFAULT_SHOP_NAME = "Uncategorized";


    // Player shop data
    private static final @NotNull Map<UUID, List<Shop>> shopsByOwner = new HashMap<>();
    public  static final @NotNull Map<UUID, List<Shop>> getShopsByOwner() { return shopsByOwner; }




    private Shop_Manager() {
        super(FancyPlayerShops.MOD_ID, "shops", new Shop_Serializer());
    }




    //FIXME replace shop list with a hashmap
    // private static boolean dataLoaded = false;


    // // The list of shops scheduled for saving
    // private static @NotNull List<@NotNull Shop> scheduledForSaving = new ArrayList<>();


    // /**
    //  * Calculates the path to the directory where shops are saved.
    //  * @return The path to the save file directory.
    //  */
    // public static @NotNull Path calcShopDirPath() {
    //     return FancyPlayerShops.getStorageDir().resolve("shops");
    // }

    // /**
    //  * Calculates the path to the save file of the specified shop.
    //  * @param shop The shop.
    //  * @return The path to the save file of {@code shop}.
    //  */
    // public static @NotNull Path calcShopFilePath(final @NotNull Shop shop) {
    //     return calcShopDirPath().resolve(shop.getUuid().toString() + ".json");
    // }








    @Override
    public void afterPut(final @NotNull UUID uuid, final @NotNull Shop shop) {

        // Update the list of shops of the owner
        // final List<Shop> playerShops = shopsByOwner.computeIfAbsent(shop.getOwnerUuid(), _uuid -> new ArrayList<>());
        shopsByOwner.computeIfAbsent(shop.getOwnerUuid(), _uuid -> new ArrayList<>()).add(shop);


        // // Return if UUID exists, add shop otherwise
        // for(final Shop g : playerShops) {
        //     if(g.getUuid().equals(shop.getUuid())) return;
        // }
        // playerShops.add(shop);
        // // scheduleShopSave(shop);
    }




    // /**
    //  * Deletes a shop and its associated data file.
    //  * @param shop The shop to delete.
    //  */
    // @SuppressWarnings({ "java:S899", "java:S4042" }) //! Return value of file.delete() ignored //TODO remove
    @Override
    public void afterRemove(final @NotNull UUID uuid, final @NotNull Shop shop) {

        // Get the list of shops
        // final List<Shop> shops = shopsByOwner.computeIfAbsent(shop.getOwnerUuid(), uuid -> new ArrayList<>());

        // Update the list of shops of the owner
        //! We expect this data to be valid. No need to check if the entry exists.
        //! A missing entry in shopsByOwner means something went wrong in the code.
        final var playerShops = shopsByOwner.get(shop.getOwnerUuid());
        playerShops.remove(shop);

        // Remove shop from the map, then dissolve it and remove the file
        // shops.remove(shop);
        // shop.dissolve();

        for(final ProductDisplay display : shop.getDisplays()) {
            Shop_Manager.unregisterDisplay(display);
        }
        // // Delete the config file
        // calcShopFilePath(shop).toFile().delete();
    }




    /**
     * Registers a display into the specified shop.
     * <p>
     * This updates the shop's stats and any context displaying its data.
     * @param display The display to register.
     * @param shopUUID The UUID of the shop.
     *     This must match an existing shop's UUID, or be null.
     *     Passing null will assign the display to its owner's default shop.
     * @return The Shop instance the display was assigned to.
     */
    public static Shop registerDisplay(final @NotNull ProductDisplay display, final @Nullable UUID shopUUID) {
        Shop shop = null;


        // Create or get default shop if needed
        //! This special shop is not stored to file or loaded
        if(shopUUID == null) {
            final var playerShops = shopsByOwner.get(display.getOwnerUuid());
            if(playerShops != null) for(final var playerShop : playerShops) {
                if(playerShop.isDefault()) {
                    shop = playerShop;
                    break;
                }
            }
            if(shop == null) {
                shop = new Shop(DEFAULT_SHOP_NAME, display.getOwnerUuid(), true);
                REF.put(shop.getUuid(), shop);
            }
            //TODO use italic grey once colors are implemented
            //TODO allow players to use &[0-9a-gulomkr&]
        }


        // Find shop and assign the display to it if valid
        if(shop == null) {
            shop = REF.get(shopUUID);
        }
        if(shop != null) {
            if(shop.getOwnerUuid().equals(display.getOwnerUuid())) {
        // final List<Shop> playerShops = shopsByOwner.get(ownerUUID);
        // if(playerShops != null) {
        //     final UUID shopUuidCopy = shopUUID;
        //     final Optional<Shop> shopOpt = playerShops.stream().filter(e -> e.getUuid().equals(shopUuidCopy)).findFirst();

        //     // Add display to the shop if it exists
        //     if(shopOpt.isPresent()) {
                // final Shop shop = shopOpt.get();
                shop.addBalance(display.getBalance());
                shop.addDisplay(display);
                return shop;
            // }
            }


            // Print an error if the shop isn't owned by the right player, then assign to the default shop
            else {
                final @Nullable Player displayOwner = MinecraftUtils.getPlayerByUUID(display.getOwnerUuid());
                final @Nullable Player shopOwner    = MinecraftUtils.getPlayerByUUID(shop.getOwnerUuid());
                FancyPlayerShops.LOGGER.error(
                    "Display registration failed: the display is owned by {}, but the requested shop is owned by {}",
                    displayOwner == null ? "<unknown>" : displayOwner.getName().getString(),
                    shopOwner    == null ? "<unknown>" : shopOwner   .getName().getString(),
                    new RuntimeException()
                );
                return registerDisplay(display, null);
            }
        // }
        }


        // Print an error if the shop cannot be found, then assign to the default shop
        else {
            FancyPlayerShops.LOGGER.error("Display registration failed: the requested shop does not exist: {}", shopUUID, new RuntimeException());
            return registerDisplay(display, null);
        }
    }




    public static void unregisterDisplay(final @NotNull ProductDisplay display) {

        final Shop shop = display.getShop();
        // // Find shop
        // final List<Shop> playerShops = shopsByOwner.get(display.getOwnerUuid());
        // if(playerShops != null) {
        //     final Optional<Shop> shopOpt = playerShops.stream().filter(e -> e.getUuid().equals(display.getShop().getUuid())).findFirst();

        //     // Remove display from the shop if it exists
        //     if(shopOpt.isPresent()) {
        //         final Shop shop = shopOpt.get();
                shop.subBalance(display.getBalance());
                shop.removeDisplay(display);
        //     }
        // }
    }







    // /**
    //  * Schedules the specified shop for saving.
    //  * <p> Call {@link #saveScheduledShops()} to save all scheduled shops.
    //  * <p> Notice: The default shop (with UUID {@link #DEFAULT_SHOP_UUID}) cannot be saved to file.
    //  *     This special shop is recreated whenever needed.
    //  * @param playerUUID The UUID of the player.
    //  * @param shop The shop to save.
    //  */
    // public static void scheduleShopSave(final @NotNull Shop shop) {
    //     if(!shop.isScheduledForSave()) {
    //         scheduledForSaving.add(shop);
    //         shop.setScheduledForSave(true);
    //     }
    // }




    // /**
    //  * Saves the scheduled shops in their config files.
    //  */
    // public static void saveScheduledShops() {

    //     // Create directory for the shops
    //     try {
    //         Files.createDirectories(calcShopDirPath());
    //     }
    //     catch(final IOException e) {
    //         FancyPlayerShops.LOGGER.error("Couldn't create storage directory for player shops", e);
    //     }


    //     // Iterate shops. Save them if they are not dissolved and their UUID doesn't match the default shop
    //     for(final Shop shop : scheduledForSaving) {
    //         if(!shop.isDissolved() && !shop.getUuid().equals(DEFAULT_SHOP_UUID)) {
    //             //BUG write this in canBeSaved() when replacing with frameworkConfig

    //             // Create the JSON objects that contains the shop data
    //             final JsonObject jsonObject = new JsonObject(); //TODO move to dedicated serializer
    //             jsonObject.addProperty("ownerUUID",   shop.getOwnerUuid().toString());
    //             jsonObject.addProperty("uuid",        shop.getUuid().toString());
    //             jsonObject.addProperty("displayName", shop.getDisplayName());


    //             // Create this shop's config file if absent, then save the JSON in it
    //             try(final Writer writer = new FileWriter(calcShopFilePath(shop).toFile())) {
    //                 new Gson().toJson(jsonObject, writer);
    //             }
    //             catch(final IOException e) {
    //                 FancyPlayerShops.LOGGER.error("Couldn't create storage file for the shop {}", shop.getDisplayName(), e);
    //             }
    //         }


    //         // Flag the shops as not scheduled
    //         shop.setScheduledForSave(false);
    //     }
    //     scheduledForSaving = new ArrayList<>();
    // }








    // /**
    //  * Loads all the shops into the runtime map, if needed.
    //  * <p>
    //  * Must be called on server started event (After the levels are loaded!).
    //  * <p>
    //  * If the data has already been loaded, the call will have no effect.
    //  */
    // public static void loadShops() {
    //     if(dataLoaded) return;
    //     dataLoaded = true;

    //     // For each shop storage file
    //     final File[] shopStorageFiles = calcShopDirPath().toFile().listFiles();
    //     if(shopStorageFiles != null) for(final File shopStorageFile : shopStorageFiles) {

    //         // Read the file
    //         final String fileName = shopStorageFile.getName();
    //         final UUID shopUUID = UUID.fromString(fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName);
    //         try(FileReader reader = new FileReader(shopStorageFile)) {

    //             // Load the data into the runtime map
    //             final JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
    //             createShop(new Shop(
    //                 jsonObject.get("displayName").getAsString(),//TODO move to dedicated serializer
    //                 shopUUID,
    //                 UUID.fromString(jsonObject.get("ownerUUID").getAsString())
    //             ));
    //         }
    //         catch(final IOException e) {
    //             FancyPlayerShops.LOGGER.error("Couldn't read the storage file for the shop {}", shopStorageFile.getName(), e);
    //         }
    //     }
    // }








    // public static List<Shop> getShops(final @NotNull Player player) {
    //     return shopsByOwner.get(player.getUUID());
    // }








    /**
     * Checks if the provided string is a valid shop display name.
     * @param s The display name of the shop.
     * @return An Option containing a description of the error if the string is not a valid name, or an empty Option otherwise.
     */
    public static Option<String> validateShopName(final @NotNull String s) {
        final char c = s.charAt(0);
        if(c == '.' || c == ' ' || c == ',') {
            return Option.Some("Shop names can't start with \"" + c + "\"");
        }
        else if(Character.isDigit(c)) {
            return Option.Some("Shop names can't start with a number");
        }
        else {
            return Option.None();
        }
    }
}

