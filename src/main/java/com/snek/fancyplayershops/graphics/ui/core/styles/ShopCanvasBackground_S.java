package com.snek.fancyplayershops.graphics.ui.core.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;
import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;








public class ShopCanvasBackground_S extends PanelElmStyle {
    private final @NotNull Shop shop;


    public ShopCanvasBackground_S(final @NotNull Shop _shop) {
        super();
        shop = _shop;
    }


    @Override
    public Vector3i getDefaultColor() {
        return shop.getThemeColor2();
    }


    @Override
    public int getDefaultAlpha() {
        return 100;
    }
}
