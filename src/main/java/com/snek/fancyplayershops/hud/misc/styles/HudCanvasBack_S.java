package com.snek.fancyplayershops.hud.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.hud.styles.CanvasBorder_S;
import com.snek.fancyplayershops.ui.misc.styles.ShopPanelElm_S;








public class HudCanvasBack_S extends ShopPanelElm_S {

    public HudCanvasBack_S(){
        super();
    }


    @Override
    public int getDefaultAlpha() {
        return 255;
    }


    @Override
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(CanvasBorder_S.COLOR);
    }
}
