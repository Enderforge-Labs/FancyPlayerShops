package com.snek.fancyplayershops.graphics.ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.graphics.functional.styles.ButtonStyle;








/**
 * The style of product display simple button elements.
 */
public class ProductDisplay_SipleButton_S extends ButtonStyle {
    protected final @NotNull ProductDisplay display;


    /**
     * Creates a new ProductDisplay_SipleButton_S.
     */
    public ProductDisplay_SipleButton_S(final @NotNull ProductDisplay display) {
        super(false);
        this.display = display;
        resetAll();
    }


    @Override
    public Vector3i getDefaultColor() {
        return display.getThemeColor1();
    }
}
