package com.snek.fancyplayershops.ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;








public class CanvasBack_S extends ShopPanelElm_S {

    public CanvasBack_S(){
        super();
    }


    @Override
    public int getDefaultAlpha() {
        return 255;
    }


    @Override
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(ShopUiBorder_S.COLOR);
    }
}
