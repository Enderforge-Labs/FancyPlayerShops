package com.snek.fancyplayershops.graphics.ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;
import com.snek.framework.old.ui.functional.styles.SimpleButtonElmStyle;








/**
 * The style of the generic SimpleShopButton UI element.
 */
public class SimpleShopButton_S extends SimpleButtonElmStyle {
    protected final @NotNull Shop shop;


    /**
     * Creates a new SimpleShopButton_S.
     */
    public SimpleShopButton_S(final @NotNull Shop _shop) {
        super();
        shop = _shop;
    }


    @Override
    public Vector3i getDefaultColor() {
        return shop.getThemeColor1();
    }
}
