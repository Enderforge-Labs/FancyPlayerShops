package com.snek.fancyplayershops.graphics.hud.core.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;
import com.snek.frameworklib.graphics.core.styles.CanvasBorder_S;







public class HudCanvasBack_S extends PanelElmStyle {

    public HudCanvasBack_S() {
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
