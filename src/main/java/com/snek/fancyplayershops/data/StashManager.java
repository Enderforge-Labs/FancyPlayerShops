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
import java.util.UUID;
import java.util.Map.Entry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.snek.fancyplayershops.hud_ui._elements.Hud;
import com.snek.fancyplayershops.hud_ui.stash.StashHud;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.framework.data_types.containers.Pair;
import com.snek.framework.utils.MinecraftUtils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
















/**
 * A class that handles player stashes.
 */
public abstract class StashManager {
    private StashManager() {}


    // Player stash data
    private static final @NotNull Map<@NotNull UUID, @Nullable PlayerStash> stashes = new HashMap<>();
    private static boolean dataLoaded = false;


    // The list of stashes scheduled for saving
    private static @NotNull List<@NotNull Pair<@NotNull UUID, @Nullable PlayerStash>> scheduledForSaving = new ArrayList<>();








    /**
     * Adds an item to the stash of the specified player.
     * <p> Air is never stashed.
     * @param playerUUID The UUID of the player.
     * @param item The item to add.
     * @param count The amount of items to add.
     */
    public static void stashItem(final @NotNull UUID playerUUID, final @NotNull UUID itemUUID, final @NotNull ItemStack item, final int count) {
        if(item.getItem() == Items.AIR) return;
        final PlayerStash stash = stashes.computeIfAbsent(playerUUID, k -> new PlayerStash());
        final StashEntry stashEntry = stash.computeIfAbsent(itemUUID, k -> new StashEntry(item));
        stashEntry.add(count);
    }
    /**
     * Adds an item with known UUID to the stash of the specified player.
     * <p> Air is never stashed.
     * @param playerUUID The UUID of the player.
     * @param itemUUID The UUID of the item.
     * @param item The item to add.
     * @param count The amount of items to add.
     */
    public static void stashItem(final @NotNull UUID playerUUID, final @NotNull ItemStack item, final int count) {
        if(count == 0) return;
        if(item.getItem() == Items.AIR) return;
        stashItem(playerUUID, MinecraftUtils.calcItemUUID(item), item, count);
    }








    /**
     * Schedules the specified stash for saving.
     * Call saveScheduledStashes to save all scheduled stashes.
     * @param playerUUID The UUID of the player.
     */
    public static void scheduleStashSave(final @NotNull UUID playerUUID) {
        final PlayerStash stash = stashes.get(playerUUID);
        if(stash != null && !stash.isScheduledForSave()) {
            scheduledForSaving.add(Pair.from(playerUUID, stash));
            stash.setScheduledForSave(true);
        }
    }

    /**
     * Saves the stash data of the specified player in its config file.
     * @param playerUUID The UUID of the player.
     */
    public static void saveScheduledStashes() {

        // Create directory for the stashes
        final Path levelStorageDir = FancyPlayerShops.getStorageDir().resolve("stash");
        try {
            Files.createDirectories(levelStorageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }


        for(final Pair<UUID, PlayerStash> pair : scheduledForSaving) {

            // Create the JSON array that contains the player's stash entries
            final JsonArray jsonEntries = new JsonArray();
            for (final Entry<UUID, StashEntry> entry : pair.getSecond().entrySet()) {
                final JsonObject jsonEntry = new JsonObject();
                jsonEntry.addProperty("uuid", entry.getKey().toString());
                jsonEntry.addProperty("item", MinecraftUtils.serializeItem(entry.getValue().item));
                jsonEntry.addProperty("count", entry.getValue().getCount());
                jsonEntries.add(jsonEntry);
            }


            // Create this player's config file if absent, then save the JSON in it
            final File stashStorageFile = new File(levelStorageDir + "/" + pair.getFirst().toString() + ".json");
            try (final Writer writer = new FileWriter(stashStorageFile)) {
                new Gson().toJson(jsonEntries, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }


            // Flag the stash as not scheduled
            pair.getSecond().setScheduledForSave(false);
        }
        scheduledForSaving = new ArrayList<>();
    }




    /**
     * Loads all the player stashes into the runtime map if needed.
     * <p> Must be called on server started event (After the worlds are loaded!).
     * <p> If the data has already been loaded, the call will have no effect.
     */
    public static void loadStashes() {
        if(dataLoaded) return;
        dataLoaded = true;


        // For each stash storage file
        final File[] stashStorageFiles = FancyPlayerShops.getStorageDir().resolve("stash").toFile().listFiles();
        if(stashStorageFiles != null) for(final File stashStorageFile : stashStorageFiles) {

            // Read the file
            final String fileName = stashStorageFile.getName();
            final UUID playerUID = UUID.fromString(fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName);
            final JsonArray jsonEntries;
            try(FileReader reader = new FileReader(stashStorageFile)) {
                jsonEntries = new Gson().fromJson(reader, JsonArray.class);

                // Load the data into the runtime map
                for (final JsonElement _jsonEntry : jsonEntries.asList()) {
                    final JsonObject jsonEntry = _jsonEntry.getAsJsonObject();
                    stashItem(playerUID,
                        UUID.fromString(jsonEntry.get("uuid").getAsString()),
                        MinecraftUtils.deserializeItem(jsonEntry.get("item").getAsString()),
                        jsonEntry.get("count").getAsInt()
                    );
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    /**
     * Opens the stash view for the specified player.
     * @param player The player.
     */
    public static void openStashView(final @NotNull ServerPlayer player) {

        final Vec3 pos = player.getPosition(1f);
        Hud.closeHud(player);
        final Hud hud = Hud.getOpenHudOrCreate(player);
        hud.changeCanvas(new StashHud(hud));
        hud.spawn(new Vector3d(pos.x, pos.y, pos.z));
    }
}
