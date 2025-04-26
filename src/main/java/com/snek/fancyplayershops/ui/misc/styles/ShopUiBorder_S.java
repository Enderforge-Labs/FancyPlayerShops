package com.snek.fancyplayershops.ui.misc.styles;

import org.joml.Vector4i;








/**
 * The style of the ShopUiBorder UI element.
 */
public class ShopUiBorder_S extends ShopPanelElm_S {
    public static final Vector4i COLOR = new Vector4i(255, 38, 38, 40);


    /**
     * Creates a new ShopUiBorderStyle
     */
    public ShopUiBorder_S() {
        super();
    }


    @Override
    public Vector4i getDefaultColor() {
        return new Vector4i(COLOR);
    }
}
