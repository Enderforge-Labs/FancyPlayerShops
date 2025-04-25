package com.snek.fancyplayershops.implementations.ui.styles;

import org.joml.Vector4i;

import com.snek.framework.ui.styles.FancyTextElmStyle;








/**
 * The stlye of the BuyUiTitle UI element.
 */
public class BuyUiTitleStyle extends FancyTextElmStyle {

    /**
     * Creates a new default BuyUiTitleStyle.
     */
    public BuyUiTitleStyle() {
        super();
    }


    @Override
    public Vector4i getDefaultBackground(){
        return new Vector4i(ShopUiBorderStyle.COLOR);
    }
}
