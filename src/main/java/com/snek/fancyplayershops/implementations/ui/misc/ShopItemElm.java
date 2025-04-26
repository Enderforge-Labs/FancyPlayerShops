package com.snek.fancyplayershops.implementations.ui.misc;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.Shop;
import com.snek.framework.ui.elements.ItemElm;
import com.snek.framework.ui.styles.ItemElmStyle;








/**
 * A generic ItemElm with a target shop.
 */
public class ShopItemElm extends ItemElm {
    protected final @NotNull Shop shop;


    /**
     * Creates a new ShopItemElm using a custom style.
     * @param _shop The target shop.
     * @param _style The custom style.
     */
    public ShopItemElm(@NotNull Shop _shop, ItemElmStyle _style) {
        super(_shop.getWorld(), _style);
        shop = _shop;
    }


    /**
     * Creates a new ShopItemElm using the default style.
     * @param _shop The target shop.
     */
    public ShopItemElm(@NotNull Shop _shop) {
        this(_shop, new ItemElmStyle());
    }
}
