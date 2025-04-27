package com.snek.fancyplayershops.ui.misc;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.framework.ui.elements.FancyTextElm;
import com.snek.framework.ui.styles.FancyTextElmStyle;








/**
 * A generic FancyTextElm with a target shop.
 */
public class ShopFancyTextElm extends FancyTextElm {
    public static final float LINE_H = 0.1f;
    protected final @NotNull Shop shop;


    /**
     * Creates a new ShopFancyTextElm ustin a custom style.
     * @param _shop The target shop.
     * @param _style The custom style.
     */
    public ShopFancyTextElm(@NotNull Shop _shop, FancyTextElmStyle _style) {
        super(_shop.getWorld(), _style);
        shop = _shop;
    }


    /**
     * Creates a new ShopFancyTextElm using the default style.
     * @param _shop The target shop.
     */
    public ShopFancyTextElm(@NotNull Shop _shop) {
        this(_shop, new FancyTextElmStyle());
    }
}
