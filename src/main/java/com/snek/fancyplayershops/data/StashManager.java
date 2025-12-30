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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.snek.fancyplayershops.data.data_types.PlayerStash;
import com.snek.fancyplayershops.data.data_types.StashEntry;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.frameworklib.data_types.containers.Pair;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.UtilityClassBase;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
















/**
 * A class that handles player stashes.
 */
public final class StashManager extends UtilityClassBase {
    private StashManager() {}


    // Player stash data
    private static final @NotNull Map<@NotNull UUID, @Nullable PlayerStash> stashes = new HashMap<>();
    private static boolean dataLoaded = false;


    // The list of stashes scheduled for saving
    private static @NotNull List<@NotNull Pair<@NotNull UUID, @Nullable PlayerStash>> scheduledForSaving = new ArrayList<>();


    /**
     * Calculates the path to the directory where stashes are saved.
     * @return The path to the save file directory.
     */
    public static @NotNull Path calcStashDirPath() {
        return FancyPlayerShops.getStorageDir().resolve("stash");
    }

    /**
     * Calculates the path to the save file of the specified player.
     * @param playerUuid The uuid of the player.
     * @return The path to the save file of the player's stash.
     */
    public static @NotNull Path calcStashFilePath(final @NotNull UUID playerUuid) {
        return calcStashDirPath().resolve(playerUuid.toString() + ".json");
    }








    /**
     * Adds an item to the stash of the specified player.
     * <p> Air is never stashed.
     * @param playerUUID The UUID of the player.
     * @param item The item to add.
     * @param count The amount of items to add.
     */
    public static void stashItem(final @NotNull UUID playerUUID, final @NotNull UUID itemUUID, final @NotNull ItemStack item, final int count) {
        if(count == 0) return;
        if(item.is(Items.AIR)) return;
        final PlayerStash stash = stashes.computeIfAbsent(playerUUID, k -> new PlayerStash());
        final StashEntry stashEntry = stash.computeIfAbsent(itemUUID, k -> new StashEntry(item));
        stashEntry.add(count);
        StashManager.scheduleStashSave(playerUUID);
    }


    /**
     * Adds an item with known UUID to the stash of the specified player.
     * <p> Air is never stashed.
     * @param playerUUID The UUID of the player.
     * @param itemUUID The UUID of the item.
     * @param item The item to add.
     * @param count The amount of items to add.
     * @param playerFeedback Whether to send the player a feedback message.
     */
    public static void stashItem(final @NotNull UUID playerUUID, final @NotNull ItemStack item, final int count, final boolean playerFeedback) {
        if(count == 0) return;
        if(item.is(Items.AIR)) return;
        stashItem(playerUUID, MinecraftUtils.calcItemUUID(item), item, count);

        // Send feedback to player
        if(playerFeedback) {
            final @Nullable Player player = MinecraftUtils.getPlayerByUUID(playerUUID);
            if(player != null) {
                player.displayClientMessage(new Txt()
                    .cat(Utils.formatAmount(count, false, true) + "x ")
                    .cat(MinecraftUtils.getFancyItemName(item).getString())
                    .cat(" " + (count == 1 ? "has" : "have") + " been sent to your stash.")
                .lightGray().get(), false);
            }
        }
    }


    /**
     * Gives the specified player {@code amount} items.
     * <p>
     * Items that don't fit in the inventory are sent to the player's stash.
     * @param player The player to give items to.
     * @param item The item to give to the player.
     * @param amount The number of items to give.
     * @param playerFeedback Whether to send the player a feedback message.
     * @return A pair of integers representing the amount of items sent to the player's inventory and the amount of items sent to their stash.
     */
    public static @NotNull Pair<Integer, Integer> giveItem(final @NotNull Player player, final @NotNull ItemStack item, final int amount, final boolean playerFeedback) {

        // Try to put items in the inventory
        final ItemStack _item = item.copyWithCount(amount);
        MinecraftUtils.attemptGive(player, _item);
        final int stashedAmount = _item.getCount();

        // Send remaining items to the stash
        if(stashedAmount > 0) {
            StashManager.stashItem(player.getUUID(), _item, _item.getCount(), playerFeedback);
        }

        // Return stats
        return Pair.from(amount - stashedAmount, stashedAmount);
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
     * Saves the scheduled stashes in their config files.
     */
    public static void saveScheduledStashes() {

        // Create directory for the stashes
        final Path levelStorageDir = calcStashDirPath();
        try {
            Files.createDirectories(levelStorageDir);
        } catch(final IOException e) {
            FancyPlayerShops.LOGGER.error("Couldn't create storage directory for player stashes", e);
        }


        for(final Pair<UUID, PlayerStash> pair : scheduledForSaving) {

            // Create the JSON array that contains the player's stash entries
            final JsonArray jsonEntries = new JsonArray();
            for(final Entry<UUID, StashEntry> entry : pair.getSecond().entrySet()) {
                final JsonObject jsonEntry = new JsonObject();

                final @Nullable String serializedItem = MinecraftUtils.serializeItem(entry.getValue().item);
                if(serializedItem == null) {
                    final Player player = MinecraftUtils.getPlayerByUUID(pair.getFirst());
                    if(player != null) player.displayClientMessage(new Txt(
                        "An item in your stash couldn't be saved. You should contact a server admin. " +
                        "Item ID: " + MinecraftUtils.getItemId(entry.getValue().item) + ", " +
                        "Count: " + entry.getValue().item.getCount()
                    ).red().get(), false);
                }
                else {
                    jsonEntry.addProperty("uuid", entry.getKey().toString());
                    jsonEntry.addProperty("item", serializedItem);
                    jsonEntry.addProperty("count", entry.getValue().getCount());
                    jsonEntries.add(jsonEntry);
                }
            }


            // Create this player's config file if absent, then save the JSON in it
            final File stashStorageFile = calcStashFilePath(pair.getFirst()).toFile();
            try (final Writer writer = new FileWriter(stashStorageFile)) {
                new Gson().toJson(jsonEntries, writer);
            } catch(final IOException e) {
                FancyPlayerShops.LOGGER.error("Couldn't create storage file for the stash of the player {}", pair.getFirst().toString(), e);
            }


            // Flag the stash as not scheduled
            pair.getSecond().setScheduledForSave(false);
        }
        scheduledForSaving = new ArrayList<>();
    }




    /**
     * Loads all the player stashes into the runtime map if needed.
     * <p> Must be called on server started event (After the levels are loaded!).
     * <p> If the data has already been loaded, the call will have no effect.
     */
    public static void loadStashes() {
        if(dataLoaded) return;
        dataLoaded = true;


        // For each stash storage file
        final File[] stashStorageFiles = calcStashDirPath().toFile().listFiles();
        if(stashStorageFiles != null) for(final File stashStorageFile : stashStorageFiles) {

            // Read the file
            final String fileName = stashStorageFile.getName();
            final UUID playerUUID = UUID.fromString(fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName);
            final JsonArray jsonEntries;
            try(FileReader reader = new FileReader(stashStorageFile)) {
                jsonEntries = new Gson().fromJson(reader, JsonArray.class);

                // Load the data into the runtime map
                for(final JsonElement _jsonEntry : jsonEntries.asList()) {
                    final JsonObject jsonEntry = _jsonEntry.getAsJsonObject();
                    final @NotNull ItemStack deserializedItem = MinecraftUtils.deserializeItem(jsonEntry.get("item").getAsString());
                    if(deserializedItem == null) {
                        final Player player = MinecraftUtils.getPlayerByUUID(playerUUID);
                        if(player != null) player.displayClientMessage(new Txt("An item in your stash couldn't be loaded.").red().get(), false);
                    }
                    else stashItem(playerUUID,
                        UUID.fromString(jsonEntry.get("uuid").getAsString()),
                        deserializedItem,
                        jsonEntry.get("count").getAsInt()
                    );
                }
            } catch(final IOException e) {
                FancyPlayerShops.LOGGER.error("Couldn't read the storage file for the stash of the player {}", playerUUID.toString(), e);
            }
        }
    }




    public static PlayerStash getStash(final @NotNull ServerPlayer player) {
        return stashes.get(player.getUUID());
    }
}
