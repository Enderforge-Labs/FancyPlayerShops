package com.snek.fancyplayershops.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.herrkatze.solsticeEconomy.modules.economy.EconomyManager;
import com.herrkatze.solsticeEconomy.modules.economy.Notification;
import com.herrkatze.solsticeEconomy.modules.economy.NotificationManager;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.server.level.ServerPlayer;
















/**
 * A class that handles player balances.
 */
public abstract class BalanceManager {
    private BalanceManager() {}


    // Player balance data
    private static final @NotNull Map<@NotNull UUID, Long> balances = new HashMap<>();
    private static boolean dataLoaded = false;








    /**
     * Adds the specified amount of money to the balance of the player.
     * @param playerUUID The UUID of the player.
     * @param count The amount of money to add.
     */
    public static void addBalance(final @NotNull UUID playerUUID, final long amount) {
        balances.merge(playerUUID, amount, Long::sum);
    }








    /**
     * Saves the balance of the specified player in its config file.
     * @param playerUUID The UUID of the player.
     */
    public static void saveBalance(final @NotNull UUID playerUUID) {
        final Long balance = balances.get(playerUUID);
        if(balance == null) return;


        // Create directory for the world
        final Path levelStorageDir = FancyPlayerShops.getStorageDir().resolve("balance");
        try {
            Files.createDirectories(levelStorageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Create this player's config file if absent, then save the JSON in it
        final File balanceStorageFile = new File(levelStorageDir + "/" + playerUUID.toString() + ".json");
        try (final Writer writer = new FileWriter(balanceStorageFile)) {
            new Gson().toJson(balance, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }








    /**
     * Moves the shop balance of a player to their personal balance.
     * Sends a feedback message to the player indicating how much was claimed, or that the balance is empty in case there was nothing to claim.
     * @param player The player.
     */
    public static void claim(final @NotNull ServerPlayer player) {
        final Long balance = balances.put(player.getUUID(), 0l);
        saveBalance(player.getUUID());

        if(balance == null || balance == 0) {
            player.displayClientMessage(new Txt("Your shop balance is empty. There is nothing to claim right now.").lightGray().get(), false);
        }
        else {
            EconomyManager.addCurrency(player.getUUID(), balance);
            NotificationManager.sendNotification(
                new Notification(new Txt("You claimed " + Utils.formatPrice(balance) + " from your shop balance.").gold().get()),
                player
            );
        }
    }
}
