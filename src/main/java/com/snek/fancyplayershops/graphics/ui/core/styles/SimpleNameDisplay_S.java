package com.snek.fancyplayershops.graphics.ui.core.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.frameworklib.graphics.core.styles.CanvasBorder_S;
import com.snek.frameworklib.graphics.basic.styles.FancyTextElmStyle;








public class SimpleNameDisplay_S extends FancyTextElmStyle {

    public SimpleNameDisplay_S() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultBgColor() {
        return CanvasBorder_S.COLOR;
    }


    @Override
    public int getDefaultBgAlpha() {
        return 180;
    }
}
