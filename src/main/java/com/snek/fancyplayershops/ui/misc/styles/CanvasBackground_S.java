package com.snek.fancyplayershops.ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;








public class CanvasBackground_S extends ShopPanelElm_S {
    final Shop shop;


    public CanvasBackground_S(final @NotNull Shop _shop){
        super();
        shop = _shop;
    }


    @Override
    public Vector3i getDefaultColor(){
        return shop.getThemeColor2();
    }


    @Override
    public int getDefaultAlpha() {
        return 180;
    }
}
