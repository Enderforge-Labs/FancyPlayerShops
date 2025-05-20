package com.snek.fancyplayershops.ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.hud.styles.HudCanvasBorder_S;








public class UiCanvasBack_S extends ShopPanelElm_S {

    public UiCanvasBack_S(){
        super();
    }


    @Override
    public int getDefaultAlpha() {
        return 255;
    }


    @Override
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(HudCanvasBorder_S.COLOR);
    }
}
