package com.snek.fancyplayershops.graphics.ui;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.frameworklib.graphics.ui._elements.UI;

import net.minecraft.world.entity.player.Player;





/**
 * The UI context for shop displays
 */
public class ShopUI extends UI {
    private final @NotNull Shop shop;
    public @NotNull Shop getShop() { return shop; }


    public ShopUI(final @NotNull Shop _shop, final @NotNull Player _player) {
        super(_player);
        shop = _shop;
    }
}
