package com.snek.fancyplayershops.graphics.ui;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.frameworklib.graphics.core.UiContext;

import net.minecraft.world.entity.player.Player;





/**
 * The UI context for shop displays
 */
public class ShopContext extends UiContext {
    private final @NotNull Shop shop;
    public @NotNull Shop getShop() { return shop; }


    public ShopContext(final @NotNull Shop _shop, final @NotNull Player _player) {
        super(_player);
        shop = _shop;
    }
}
