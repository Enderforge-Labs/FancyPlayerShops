package com.snek.fancyplayershops.ui.misc;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.framework.ui.Div;








/**
 * A generic div class with a target shop.
 */
public class ShopDiv extends Div {
    protected final @NotNull Shop shop;


    /**
     * Creates a new ShopDiv.
     * @param _shop The target shop.
     */
    public ShopDiv(final @NotNull Shop _shop) {
        super();
        shop = _shop;
    }
}
