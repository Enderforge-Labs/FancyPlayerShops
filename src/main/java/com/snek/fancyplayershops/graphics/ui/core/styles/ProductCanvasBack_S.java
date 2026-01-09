package com.snek.fancyplayershops.graphics.ui.core.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.frameworklib.graphics.basic.styles.PanelStyle;
import com.snek.frameworklib.graphics.core.styles.CanvasBorder_S;








public class ProductCanvasBack_S extends PanelStyle {

    public ProductCanvasBack_S() {
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
