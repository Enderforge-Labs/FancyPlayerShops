package com.snek.fancyplayershops.ui.misc;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.styles.ShopPanelElm_S;
import com.snek.framework.ui.elements.PanelElm;








/**
 * A generic PanelElm with a target shop.
 */
public class ShopPanelElm extends PanelElm {
    protected final @NotNull Shop shop;


    /**
     * Creates a new ShopPanelElm using a custom style.
     * @param _shop The target shop.
     */
    public ShopPanelElm(final @NotNull Shop _shop, final @NotNull ShopPanelElm_S _style) {
        super(_shop.getWorld(), _style);
        shop = _shop;
    }


    /**
     * Creates a new ShopPanelElm using the default style.
     * @param _shop The target shop.
     */
    public ShopPanelElm(final @NotNull Shop _shop) {
        super(_shop.getWorld(), new ShopPanelElm_S());
        shop = _shop;
    }
}
