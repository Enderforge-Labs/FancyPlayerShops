package com.snek.fancyplayershops.graphics.ui;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.graphics.core.UiContext;

import net.minecraft.world.entity.player.Player;





/**
 * The UI context for shop displays
 */
public class ProductDisplayContext extends UiContext {
    private final @NotNull ProductDisplay shop;
    public @NotNull ProductDisplay getDisplay() { return shop; }


    public ProductDisplayContextlayContext(final @NotNull ProductDisplay _shop, final @NotNull Player _player) {
        super(_player);
        shop = _shop;
    }
}
