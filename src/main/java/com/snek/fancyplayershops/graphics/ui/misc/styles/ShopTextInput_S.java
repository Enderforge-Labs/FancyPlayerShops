package com.snek.fancyplayershops.graphics.ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;
import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.graphics.functional.styles.TextInputElmStyle;








/**
 * The style of shop text input elements.
 */
public class ShopTextInput_S extends TextInputElmStyle {
    protected final @NotNull Shop shop;


    /**
     * Creates a new ShopTextInput_S.
     */
    public ShopTextInput_S(final @NotNull Shop _shop) {
        super();
        shop = _shop;
    }


    @Override
    public @NotNull Vector3i getDefaultBgColor() {
        return shop.getThemeColor1();
    }


    @Override
    public @NotNull TextOverflowBehaviour getTextOverflowBehaviour() {
        return TextOverflowBehaviour.SCROLL;
    }
}
