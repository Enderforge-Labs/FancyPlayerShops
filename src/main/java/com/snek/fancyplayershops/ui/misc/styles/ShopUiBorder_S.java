package com.snek.fancyplayershops.ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;








/**
 * The style of the ShopUiBorder UI element.
 */
public class ShopUiBorder_S extends ShopPanelElm_S {
    public static final @NotNull Vector3i COLOR = new Vector3i(33, 33, 35);


    /**
     * Creates a new ShopUiBorderStyle_S.
     */
    public ShopUiBorder_S() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(COLOR);
    }
}
