package com.snek.fancyplayershops.graphics.ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.graphics.functional.styles.ChatInputStyle;








/**
 * The style of product display text input elements.
 */
public class ProductDisplay_TextInput_S extends ChatInputStyle {
    protected final @NotNull ProductDisplay display;


    /**
     * Creates a new ProductDisplay_TextInput_S.
     */
    public ProductDisplay_TextInput_S(final @NotNull ProductDisplay display) {
        super(false);
        this.display = display;
        resetAll();
    }


    @Override
    public @NotNull Vector3i getDefaultBgColor() {
        return display.getThemeColor1();
    }
}
