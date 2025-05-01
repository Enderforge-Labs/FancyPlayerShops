package com.snek.fancyplayershops.ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;








/**
 * The style of the ShopUiBorder UI element.
 */
public class ShopUiBorder_S extends ShopPanelElm_S {
    public static final @NotNull Vector4i COLOR = new Vector4i(255, 33, 33, 35);


    /**
     * Creates a new ShopUiBorderStyle_S.
     */
    public ShopUiBorder_S() {
        super();
    }


    @Override
    public @NotNull Vector4i getDefaultColor() {
        return new Vector4i(COLOR);
    }
}
