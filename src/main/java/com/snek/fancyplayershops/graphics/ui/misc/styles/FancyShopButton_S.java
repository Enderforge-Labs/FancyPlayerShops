package com.snek.fancyplayershops.graphics.ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.graphics.functional.styles.FancyButtonElmStyle;








/**
 * The style of shop fancy button elements.
 */
public class FancyShopButton_S extends FancyButtonElmStyle {
    protected final @NotNull ProductDisplay shop;


    /**
     * Creates a new ShopButton_S.
     */
    public FancyShopButton_S(final @NotNull ProductDisplay _shop) {
        super();
        shop = _shop;
    }


    @Override
    public Vector3i getDefaultBgColor() {
        return shop.getThemeColor1();
    }
}
