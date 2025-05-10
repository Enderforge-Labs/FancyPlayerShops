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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.framework.utils.MinecraftUtils;

import net.minecraft.world.item.ItemStack;
















/**
 * A class that handles player stashes.
 */
public abstract class StashManager {
    private StashManager() {}


    // Player stash data
    private static final @NotNull Map<
        @NotNull UUID,
        @Nullable List<@NotNull StashEntry>
    > stashes = new HashMap<>();
    private static boolean dataLoaded = false;








    /**
     * Adds an item to the stash of the specified player.
     * @param playerUUID The UUID of the player.
     * @param item The item to add.
     * @param count The amount of items to add.
     */
    public static void stashItem(final @NotNull UUID playerUUID, final @NotNull ItemStack item, final int count) {
        stashes.computeIfAbsent(playerUUID, k -> new ArrayList<>()).add(new StashEntry(item, count));
    }








    /**
     * Saves the stash data of the specified player in its config file.
     * @param playerUUID The UUID of the player.
     */
    public static void saveStash(final @NotNull UUID playerUUID) {
        final List<StashEntry> entries = stashes.get(playerUUID);
        if(entries == null) return;


        // Create directory for the world
        final Path levelStorageDir = FancyPlayerShops.getStorageDir().resolve("stash");
        try {
            Files.createDirectories(levelStorageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Create the JSON array that contains the player's stash entries
        final JsonArray jsonEntries = new JsonArray();
        for (final StashEntry entry: entries) {
            final JsonObject jsonEntry = new JsonObject();
            jsonEntry.addProperty("item", MinecraftUtils.serializeItem(entry.item));
            jsonEntry.addProperty("count", entry.count);
            jsonEntries.add(jsonEntry);
        }


        // Create this player's config file if absent, then save the JSON in it
        final File stashStorageFile = new File(levelStorageDir + "/" + playerUUID.toString() + ".json");
        try (final Writer writer = new FileWriter(stashStorageFile)) {
            new Gson().toJson(jsonEntries, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    /**
     * Loads all the player stashes into the runtime map if needed.
     * <p> Must be called on server started event (After the worlds are loaded!).
     * <p> If the data has already been loaded, the call will have no effect.
     */
    public static void loadStashes() {
        if(dataLoaded) return;
        dataLoaded = true;


        for(final File levelStorageDir : FancyPlayerShops.getStorageDir().resolve("stash").toFile().listFiles()) {

            // For each stash file
            final File[] stashStorageFiles = levelStorageDir.listFiles();
            if(stashStorageFiles != null) for(final File stashStorageFile : stashStorageFiles) {

                // Read the file
                final String fileName = levelStorageDir.getName();
                final UUID playerUID = UUID.fromString(fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName);
                final JsonArray jsonEntries;
                try(FileReader reader = new FileReader(stashStorageFile)) {
                    jsonEntries = new Gson().fromJson(reader, JsonArray.class);

                    // Load the data into the runtime map
                    for (final JsonElement _jsonEntry : jsonEntries.asList()) {
                        final JsonObject jsonEntry = _jsonEntry.getAsJsonObject();
                        stashItem(playerUID,
                            MinecraftUtils.deserializeItem(jsonEntry.get("item").getAsString()),
                            jsonEntry.get("count").getAsInt()
                        );
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
