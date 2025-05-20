package com.snek.fancyplayershops.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.hud.styles.HudCanvasBorder_S;
import com.snek.framework.ui.elements.styles.FancyTextElmStyle;








public class SimpleNameDisplay_S extends FancyTextElmStyle {

    public SimpleNameDisplay_S() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultBgColor(){
        return HudCanvasBorder_S.COLOR;
    }


    @Override
    public int getDefaultBgAlpha(){
        return 180;
    }
}
