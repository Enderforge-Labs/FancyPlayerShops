package com.snek.fancyplayershops.graphics.ui._styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.misc.styles.ShopPanelElm_S;





//TODO move to ui/misc/styles


public class ShopCanvasBackground_S extends ShopPanelElm_S {
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
