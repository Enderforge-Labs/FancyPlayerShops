package com.snek.framework.ui.interfaces;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








/**
 * An interface that provides a click callback method.
 */
public interface Clickable {

    /**
     * Processes a click event.
     * <p> Calling this method on an element that hasn't been spawned yet is allowed and has no effect.
     * <p> NOTICE: Click detection is only available for elements with Fixed billboard mode.
     *     Calling this function on elements with a different billboard mode is allowed and has no effect.
     * @param player The player.
     * @param click The type of click.
     * @return Whether the function consumed the click.
     */
    public boolean onClick(@NotNull Player player, @NotNull ClickAction click);
    //FIXME use "attemptClick" to check and make onClick void. only call onClick if attemptClick returns true. do something similar for hoverable
}
