package com.snek.fancyplayershops.graphics.ui.core.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;








public class ProductCanvasBackground_S extends PanelElmStyle {
    private final @NotNull ProductDisplay display;


    public ProductCanvasBackground_S(final @NotNull ProductDisplay display) {
        super();
        this.display = display;
    }


    @Override
    public Vector3i getDefaultColor() {
        return display.getThemeColor2();
    }


    @Override
    public int getDefaultAlpha() {
        return 100;
    }
}
