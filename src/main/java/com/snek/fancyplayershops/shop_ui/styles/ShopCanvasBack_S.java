package com.snek.fancyplayershops.shop_ui.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.shop_ui.misc.styles.ShopPanelElm_S;
import com.snek.fancyplayershops.ui.styles.UiBorder_S;








public class ShopCanvasBack_S extends ShopPanelElm_S {

    public ShopCanvasBack_S(){
        super();
    }


    @Override
    public int getDefaultAlpha() {
        return 255;
    }


    @Override
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(UiBorder_S.COLOR);
    }
}
