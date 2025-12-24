package com.snek.fancyplayershops.graphics.ui;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.graphics.core.UiContext;

import net.minecraft.world.entity.player.Player;





/**
 * The UI context for shop displays
 */
public class ProductDisplay_Context extends UiContext {
    private final @NotNull ProductDisplay display;
    public @NotNull ProductDisplay getDisplay() { return display; }


    public ProductDisplay_Context(final @NotNull ProductDisplay display, final @NotNull Player _player) {
        super(_player);
        this.display = display;
    }
}
