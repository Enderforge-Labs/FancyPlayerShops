package com.snek.framework.old.ui.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.player.Player;








/**
 * An interface that provides a hover callback method.
 */
public interface Hoverable {

    /**
     * Processes a hover enter event.
     * <p> Called when the element is first looked at by a player.
     * @param player The player that triggered the event.
     */
    public void onHoverEnter(@NotNull Player player);


    /**
     * Tick callback.
     * <p> This method is called once for each player that is currently being checked, regardless of the result of said check.
     * @param player The player.
     */
    public default void onHoverTick(@NotNull Player player) {}


    /**
     * Processes a hover exit event.
     * <p> Called when the element stops being looked at by players.
     * @param player The player that triggered the event (the last player that looked at this element).
     */
    public void onHoverExit(@Nullable Player player);
}
