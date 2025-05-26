package com.snek.framework.ui.interfaces;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.entity.player.Player;








/**
 * An interface for elements that can be scrolled using the mouse wheel.
 * It provides a scroll callback method.
 */
public interface Scrollable {

    /**
     * Called when a player scrolls while looking at this element.
     * @param player The player that scrolled.
     * @param amount The amount of steps scrolled. Negative values mean that the player scrolled down instead of up.
     */
    public void onScroll(@NotNull Player player, int amount);
}
