package com.snek.fancyplayershops.input;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.world.entity.player.Player;








/**
 * A utility class that can detect player messages and execute callbacks based on the sender.
 */
public abstract class MessageReceiver {
    private MessageReceiver() {}
    private static final @NotNull Map<@NotNull UUID, @Nullable Predicate<@NotNull String>> callbacks = new HashMap<>();


    /**
     * Removes the callback for the specified player.
     * <p> Future messages from this player will not be detected nor blocked.
     * @param player The player.
     */
    public static void removeCallback(final @NotNull Player player) {
        callbacks.remove(player.getUUID());
    }


    /**
     * Sets a callback that is fired the next time the player sends a message in chat.
     * <p> Commands are ignored.
     * @param player The player.
     * @param callback The callback function. It must take a String and return a boolean.
     *     The return value controls whether the message is blocked.
     *     Returning true will let the server broadcast the message in chat.
     */
    public static void setCallback(final @NotNull Player player, final @NotNull Predicate<@NotNull String> callback) {
        callbacks.put(player.getUUID(), callback);
    }


    /**
     * Runs the callback associated with the player and either blocks or broadcasts the message based on the callback's return value.
     * @param message The message.
     * @param player The player that sent the message.
     * @return Whether the message should be blocked.
     */
    public static boolean onMessage(final @NotNull PlayerChatMessage message, final @NotNull Player player) {
        final Predicate<String> callback = callbacks.get(player.getUUID()); // Find callback for the specified player
        if(callback != null) {                                              // If present
            if(callback.test(message.decoratedContent().getString())) {         // Execute the callback. If it returns true
                callbacks.remove(player.getUUID());                                 // Remove the callback
                return true;                                                        // Block the message
            }                                                                   // If not
            return false;                                                           // Broadcast the message
        }

        // Broadcast the message if the player has no associated callback
        return false;
    }
}