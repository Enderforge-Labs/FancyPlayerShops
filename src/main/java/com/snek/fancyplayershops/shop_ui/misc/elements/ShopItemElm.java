package com.snek.fancyplayershops.shop_ui.misc.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.framework.old.ui.basic.elements.ItemElm;
import com.snek.framework.old.ui.basic.styles.ItemElmStyle;








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
    public ShopItemElm(final @NotNull Shop _shop, final @NotNull ItemElmStyle _style) {
        super(_shop.getWorld(), _style);
        shop = _shop;
    }


    /**
     * Creates a new ShopItemElm using the default style.
     * @param _shop The target shop.
     */
    public ShopItemElm(final @NotNull Shop _shop) {
        this(_shop, new ItemElmStyle());
    }
}
