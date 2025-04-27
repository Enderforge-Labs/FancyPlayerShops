package com.snek.fancyplayershops.ui.misc;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.framework.ui.Div;








public class ShopDiv extends Div {
    protected final @NotNull Shop shop;

    public ShopDiv(Shop _shop) {
        super();
        shop = _shop;
    }
}
