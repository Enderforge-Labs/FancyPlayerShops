package com.snek.fancyplayershops.graphics.ui.misc.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.framework.old.ui.basic.elements.SimpleTextElm;
import com.snek.framework.old.ui.basic.styles.ElmStyle;
import com.snek.framework.old.ui.basic.styles.SimpleTextElmStyle;








/**
 * A generic TextElm with a target shop.
 */
public class ShopTextElm extends SimpleTextElm {
    public static final float LINE_H = 0.1f;
    protected final @NotNull Shop shop;


    /**
     * Creates a new ShopTextElm using a custom style.
     * @param _shop The target shop.
     * @param _style The custom style.
     */
    public ShopTextElm(final @NotNull Shop _shop, final @NotNull ElmStyle _style) {
        super(_shop.getWorld(), _style);
        shop = _shop;
    }


    /**
     * Creates a new ShopTextElm using the default style.
     * @param _shop The target shop.
     */
    public ShopTextElm(final @NotNull Shop _shop) {
        this(_shop, new SimpleTextElmStyle());
    }
}
