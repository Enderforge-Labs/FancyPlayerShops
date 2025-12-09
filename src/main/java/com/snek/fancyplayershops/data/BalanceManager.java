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

import com.google.gson.Gson;
import com.herrkatze.solsticeEconomy.modules.economy.EconomyManager;
import com.herrkatze.solsticeEconomy.modules.economy.Notification;
import com.herrkatze.solsticeEconomy.modules.economy.NotificationManager;
import com.snek.fancyplayershops.data.data_types.PlayerShopBalance;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.frameworklib.data_types.containers.Pair;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.UtilityClassBase;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.server.level.ServerPlayer;
















//FIXME make subclasses and move the generic code to framework lib. automate data saving
//FIXME - PersistentData
//FIXME - PersistentDataManager
/**
 * A class that handles player balances.
 */
public final class BalanceManager extends UtilityClassBase {
    private BalanceManager() {}


    // Player balance data
    private static final @NotNull Map<@NotNull UUID, PlayerShopBalance> balances = new HashMap<>();
    private static boolean dataLoaded = false;

    // The list of balances scheduled for saving
    private static @NotNull List<@NotNull Pair<@NotNull UUID, @NotNull PlayerShopBalance>> scheduledForSaving = new ArrayList<>();








    /**
     * Adds the specified amount of money to the balance of the player.
     * @param playerUUID The UUID of the player.
     * @param count The amount of money to add.
     */
    public static void addBalance(final @NotNull UUID playerUUID, final long amount) {
        balances.merge(playerUUID, new PlayerShopBalance(amount), PlayerShopBalance::merge);
    }








    /**
     * Schedules a balance for data saving.
     * Call saveScheduledBalances() to save schedules balances.
     * @param playerUUID The UUID of the player.
     */
    public static void saveBalance(final @NotNull UUID playerUUID) {
        final PlayerShopBalance balance = balances.get(playerUUID);
        if(balance == null) return;
        if(!balance.isScheduledForSave()) {
            scheduledForSaving.add(Pair.from(playerUUID, balance));
            balance.setScheduledForSave(true);
        }
    }

    /**
     * Saves the data of all the balances schedules for saving in their config files.
     */
    public static void saveScheduledBalances() {

        // Create directory for the balances
        final Path levelStorageDir = FancyPlayerShops.getStorageDir().resolve("balance");
        try {
            Files.createDirectories(levelStorageDir);
        } catch(final IOException e) {
            FancyPlayerShops.LOGGER.error("Couldn't create storage directory for player balances", e);
        }


        for(final Pair<UUID, PlayerShopBalance> pair : scheduledForSaving) {

            // Create this player's config file if absent, then save the JSON in it
            final File balanceStorageFile = new File(levelStorageDir + "/" + pair.getFirst().toString() + ".json");
            try (final Writer writer = new FileWriter(balanceStorageFile)) {
                new Gson().toJson(pair.getSecond().getValue(), writer);
            } catch(final IOException e) {
                FancyPlayerShops.LOGGER.error("Couldn't create storage file for the balance of the player {}", pair.getFirst().toString(), e);
            }


            // Flag the balance as not shceduled
            pair.getSecond().setScheduledForSave(false);
        }
        scheduledForSaving = new ArrayList<>();
    }




    /**
     * Loads all the player balances into the runtime map if needed.
     * <p> Must be called on server started event (After the worlds are loaded!).
     * <p> If the data has already been loaded, the call will have no effect.
     */
    public static void loadBalances() {
        if(dataLoaded) return;
        dataLoaded = true;


        // For each balance file
        final File[] balanceStorageFiles = FancyPlayerShops.getStorageDir().resolve("balance").toFile().listFiles();
        if(balanceStorageFiles != null) for(final File balanceStorageFile : balanceStorageFiles) {

            // Read the file
            final String fileName = balanceStorageFile.getName();
            final UUID playerUUID = UUID.fromString(fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName);
            try(FileReader reader = new FileReader(balanceStorageFile)) {
                final Long balance = new Gson().fromJson(reader, Long.class);
                addBalance(playerUUID, balance);
            } catch(final IOException e) {
                FancyPlayerShops.LOGGER.error("Couldn't read the storage file for the balance of the player {}", playerUUID.toString(), e);
            }
        }
    }








    /**
     * Moves the shop balance of a player to their personal balance.
     * Sends a feedback message to the player indicating how much was claimed, or that the balance is empty in case there was nothing to claim.
     * @param player The player.
     */
    public static void claim(final @NotNull ServerPlayer player) {
        final PlayerShopBalance balance = balances.put(player.getUUID(), new PlayerShopBalance());
        saveBalance(player.getUUID());


        if(balance == null || balance.getValue() == 0) {
            player.displayClientMessage(new Txt("Your shop balance is empty. There is nothing to claim right now.").lightGray().get(), false);
        }
        else {
            EconomyManager.addCurrency(player.getUUID(), balance.getValue());
            NotificationManager.sendNotification(
                new Notification(new Txt("You claimed " + Utils.formatPrice(balance.getValue()) + " from your shop balance.").gold().get()),
                player
            );
        }
    }
}
