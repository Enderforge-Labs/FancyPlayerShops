package com.snek.fancyplayershops.ui.misc;

import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.styles.ShopUiBorder_S;
import com.snek.framework.data_types.ui.AlignmentX;








/**
 * An element that can display a full-width, horizontally centered line of a predetermined color.
 */
public class ShopUiBorder extends ShopPanelElm {
    public static final float DEFAULT_HEIGHT = 0.02f;


    /**
     * Creates a new ShopUiBorder of the specified height.
     * @param _shop The target shop.
     */
    public ShopUiBorder(Shop _shop, float h) {
        super(_shop, new ShopUiBorder_S());
    }


    /**
     * Creates a new ShopUiBorder of default height.
     * @param _shop The target shop.
     * @param h The height of the line.
     */
    public ShopUiBorder(Shop _shop) {
        this(_shop, DEFAULT_HEIGHT);
    }
}
