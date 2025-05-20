package com.snek.fancyplayershops.ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;








public class UiCanvasBackground_S extends ShopPanelElm_S {
    private final @NotNull Shop shop;


    public UiCanvasBackground_S(final @NotNull Shop _shop){
        super();
        shop = _shop;
    }


    @Override
    public Vector3i getDefaultColor(){
        return shop.getThemeColor2();
    }


    @Override
    public int getDefaultAlpha() {
        return 100;
    }
}
