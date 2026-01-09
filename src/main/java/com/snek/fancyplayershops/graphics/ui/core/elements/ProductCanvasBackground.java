package com.snek.fancyplayershops.graphics.ui.core.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.graphics.ui.core.styles.ProductCanvasBackground_S;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.graphics.basic.elements.PanelElm;




public class ProductCanvasBackground extends PanelElm {
    public ProductCanvasBackground(@NotNull ProductDisplay display) {
        super(display.getLevel(), new ProductCanvasBackground_S(display));
    }

    @Override
    public float getInteractionSizeTop() {
        return super.getInteractionSizeTop() + ProductCanvasBase.DEFAULT_DISTANCE + ProductCanvasBase.DEFAULT_HEIGHT;
    }
}
