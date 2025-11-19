package com.snek.fancyplayershops.graphics.hud.core.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;








public class HudCanvasBackground_S extends PanelElmStyle {
    public static final @NotNull Vector3i COLOR = new Vector3i(3, 3, 7);


    public HudCanvasBackground_S() {
        super();
    }


    @Override
    public Vector3i getDefaultColor() {
        return new Vector3i(COLOR);
    }


    @Override
    public int getDefaultAlpha() {
        return 180;
    }
}
