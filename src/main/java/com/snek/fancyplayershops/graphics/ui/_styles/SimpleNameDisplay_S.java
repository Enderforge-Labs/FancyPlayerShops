package com.snek.fancyplayershops.graphics.ui._styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.ui.old._styles.UiBorder_S;
import com.snek.framework.old.ui.basic.styles.FancyTextElmStyle;




//TODO move to ui/misc/styles



public class SimpleNameDisplay_S extends FancyTextElmStyle {

    public SimpleNameDisplay_S() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultBgColor() {
        return UiBorder_S.COLOR;
    }


    @Override
    public int getDefaultBgAlpha() {
        return 180;
    }
}
