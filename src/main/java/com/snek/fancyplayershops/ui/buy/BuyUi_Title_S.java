package com.snek.fancyplayershops.ui.buy;

import org.joml.Vector4i;

import com.snek.fancyplayershops.ui.misc.styles.ShopUiBorder_S;
import com.snek.framework.ui.styles.FancyTextElmStyle;








/**
 * The stlye of the BuyUiTitle UI element.
 */
public class BuyUi_Title_S extends FancyTextElmStyle {

    /**
     * Creates a new default BuyUiTitleStyle.
     */
    public BuyUi_Title_S() {
        super();
    }


    @Override
    public Vector4i getDefaultBackground(){
        return new Vector4i(ShopUiBorder_S.COLOR);
    }
}
