package com.snek.fancyplayershops.ui.misc;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.framework.ui.elements.TextElm;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.TextElmStyle;








/**
 * A generic TextElm with a target shop.
 */
public class ShopTextElm extends TextElm {
    public static final float LINE_H = 0.1f;
    protected final @NotNull Shop shop;


    /**
     * Creates a new ShopTextElm using a custom style.
     * @param _shop The target shop.
     * @param _style The custom style.
     */
    public ShopTextElm(@NotNull Shop _shop, ElmStyle _style) {
        super(_shop.getWorld(), _style);
        shop = _shop;
    }


    /**
     * Creates a new ShopTextElm using the default style.
     * @param _shop The target shop.
     */
    public ShopTextElm(@NotNull Shop _shop) {
        this(_shop, new TextElmStyle());
    }
}
