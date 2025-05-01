package com.snek.framework.ui.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;








/**
 * An interface that provides a hover callback method.
 */
public interface Hoverable {

    /**
     * Processes a hover enter event.
     * <p> Called when the element is first looked at by a player.
     * @param player The player that triggered the event.
     */
    public void onHoverEnter(@NotNull PlayerEntity player);


    /**
     * Tick callback.
     * <p> This method is called once for each player that is currently being checked, regardless of the result of said check.
     * @param player The player.
     */
    public default void onCheckTick(@NotNull PlayerEntity player) {}


    /**
     * Tick callback.
     * <p> This method is called once for each player that is currently looking at this element.
     * @param player The player.
     */
    public default void onHoverTick(@NotNull PlayerEntity player) {}


    /**
     * Processes a hover exit event.
     * <p> Called when the element stops being looked at by players.
     * @param player The player that triggered the event (the last player that looked at this element).
     */
    public void onHoverExit(@Nullable PlayerEntity player);
}
