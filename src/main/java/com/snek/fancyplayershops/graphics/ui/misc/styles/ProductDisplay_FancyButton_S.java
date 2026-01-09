package com.snek.fancyplayershops.graphics.ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.graphics.functional.styles.TextButtonStyle;








/**
 * The style of product display fancy button elements.
 */
public class ProductDisplay_FancyButton_S extends TextButtonStyle {
    protected final @NotNull ProductDisplay display;


    /**
     * Creates a new ProductDisplay_FancyButton_S.
     */
    public ProductDisplay_FancyButton_S(final @NotNull ProductDisplay display) {
        super(false);
        this.display = display;
        resetAll();
    }


    @Override
    public Vector3i getDefaultBgColor() {
        return display.getThemeColor1();
    }
}
