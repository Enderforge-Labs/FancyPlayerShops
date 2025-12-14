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
import com.snek.fancyplayershops.data.data_types.ShopGroup;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.frameworklib.data_types.containers.Pair;
import com.snek.frameworklib.utils.UtilityClassBase;

import net.minecraft.server.level.ServerPlayer;
















/**
 * A class that handles player shop groups.
 */
public class ShopGroupManager extends UtilityClassBase {
    public static final UUID DEFAULT_GROUP_UUID = UUID.fromString("def00000-0000-0000-0000-000000000000");
    private ShopGroupManager() {}


    // Player group data
    private static final @NotNull Map<@NotNull UUID, @Nullable List<@NotNull ShopGroup>> groupsList = new HashMap<>();
    private static boolean dataLoaded = false;


    // The list of groups scheduled for saving
    private static @NotNull List<@NotNull Pair<@NotNull UUID, @NotNull ShopGroup>> scheduledForSaving = new ArrayList<>();








    /**
     * Adds a new shop group and associates it with the specified player.
     * @param playerUUID The UUID of the player.
     * @param group The group to add.
     */
    public static void addGroup(final @NotNull UUID playerUUID, final @NotNull ShopGroup group) {
        final List<ShopGroup> groups = groupsList.computeIfAbsent(playerUUID, uuid -> new ArrayList<>());
        groups.add(group);
    }




    public static ShopGroup registerShop(final @NotNull Shop shop, final @NotNull UUID ownerUUID, final @NotNull UUID groupUUID) {

        // Create default group if needed //! This special group is not stored to file or loaded
        if(groupUUID.equals(DEFAULT_GROUP_UUID)) {
            addGroup(ownerUUID, new ShopGroup("Unassigned", DEFAULT_GROUP_UUID)); //TODO use italic gray once colors are implemented
        }

        // Find group list
        final List<ShopGroup> groups = groupsList.get(ownerUUID);

        // Find group
        if(groups != null) {
            final Optional<ShopGroup> groupOpt = groups.stream().filter(e -> e.getUuid().equals(groupUUID)).findFirst();

            // Add shop to the group
            if(groupOpt.isPresent()) {
                final ShopGroup group = groupOpt.get();
                group.addShop(shop);
                return group;
            }
        }

        //TODO add error detection and logging
        return null;
    }







    /**
     * Schedules the specified group for saving.
     * <p> Call {@link #saveScheduledGroups} to save all scheduled groups.
     * <p> Notice: The default group (with UUID {@link #DEFAULT_GROUP_UUID}) cannot be saved to file.
     *     This special group is recreated whenever needed.
     * @param playerUUID The UUID of the player.
     * @param group The shop group to save.
     */
    public static void scheduleGroupSave(final @NotNull UUID playerUUID, final @NotNull ShopGroup group) {
        if(!group.isScheduledForSave()) {
            scheduledForSaving.add(Pair.from(playerUUID, group));
            group.setScheduledForSave(true);
        }
    }




    /**
     * Saves the scheduled groups in their config files.
     */
    public static void saveScheduledGroups() {

        // Create directory for the groups
        final Path levelStorageDir = FancyPlayerShops.getStorageDir().resolve("shop groups");
        try {
            Files.createDirectories(levelStorageDir);
        } catch(final IOException e) {
            FancyPlayerShops.LOGGER.error("Couldn't create storage directory for player shop groups", e);
        }


        for(final Pair<UUID, ShopGroup> pair : scheduledForSaving) {
            //FIXME CHECK IF THE GROUP HAS BEEN DELETED. DON'T SAVE DELETED GROUPS
            //BUG   CHECK IF THE GROUP HAS BEEN DELETED. DON'T SAVE DELETED GROUPS

            // Create the JSON objects that contains the group data
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("ownerUUID",   pair.getFirst ().toString());
            jsonObject.addProperty("uuid",        pair.getSecond().getUuid().toString());
            jsonObject.addProperty("displayName", pair.getSecond().getDisplayName());


            // Create this group's config file if absent, then save the JSON in it
            final File groupStorageFile = new File(levelStorageDir + "/" + pair.getSecond().getUuid().toString() + ".json");
            try(final Writer writer = new FileWriter(groupStorageFile)) {
                new Gson().toJson(jsonObject, writer);
            } catch(final IOException e) {
                FancyPlayerShops.LOGGER.error("Couldn't create storage file for the shop group {}", pair.getSecond().getDisplayName(), e);
            }


            // Flag the groups as not scheduled
            pair.getSecond().setScheduledForSave(false);
        }
        scheduledForSaving = new ArrayList<>();
    }








    /**
     * Loads all the shop groups into the runtime map, if needed.
     * <p>
     * Must be called on server started event (After the levels are loaded!).
     * <p>
     * If the data has already been loaded, the call will have no effect.
     */
    public static void loadGroups() {
        if(dataLoaded) return;
        dataLoaded = true;

        // For each group storage file
        final File[] groupStorageFiles = FancyPlayerShops.getStorageDir().resolve("shop groups").toFile().listFiles();
        if(groupStorageFiles != null) for(final File groupStorageFile : groupStorageFiles) {

            // Read the file
            final String fileName = groupStorageFile.getName();
            final UUID groupUUID = UUID.fromString(fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName);
            try(FileReader reader = new FileReader(groupStorageFile)) {

                // Load the data into the runtime map
                final JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
                addGroup(UUID.fromString(jsonObject.get("ownerUUID").getAsString()), new ShopGroup(
                    jsonObject.get("displayName").getAsString(),
                    groupUUID
                ));
            } catch(final IOException e) {
                FancyPlayerShops.LOGGER.error("Couldn't read the storage file for the shop group {}", groupStorageFile.getName(), e);
            }
        }
    }








    public static List<ShopGroup> getShopGroups(final @NotNull ServerPlayer player) {
        return groupsList.get(player.getUUID());
    }
}

