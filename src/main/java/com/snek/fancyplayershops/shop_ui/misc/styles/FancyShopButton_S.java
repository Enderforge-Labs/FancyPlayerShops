package com.snek.fancyplayershops.shop_ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;
import com.snek.framework.ui.functional.styles.FancyButtonElmStyle;








/**
 * The style of the generic ShopButton UI element.
 */
public class FancyShopButton_S extends FancyButtonElmStyle {
    protected final @NotNull Shop shop;


    /**
     * Creates a new ShopButton_S.
     */
    public FancyShopButton_S(final @NotNull Shop _shop) {
        super();
        shop = _shop;
    }


    @Override
    public Vector3i getDefaultBgColor() {
        return shop.getThemeColor1();
    }
}
