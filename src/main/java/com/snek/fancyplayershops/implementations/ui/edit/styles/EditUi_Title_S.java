package com.snek.fancyplayershops.implementations.ui.edit.styles;

import org.joml.Vector4i;

import com.snek.fancyplayershops.implementations.ui.misc.styles.ShopUiBorder_S;
import com.snek.framework.ui.styles.FancyTextElmStyle;








/**
 * The stlye of the EditUiTitle UI element.
 */
public class EditUi_Title_S extends FancyTextElmStyle {

    /**
     * Creates a new default EditUiTitleStyle.
     */
    public EditUi_Title_S() {
        super();
    }


    @Override
    public Vector4i getDefaultBackground(){
        return new Vector4i(ShopUiBorder_S.COLOR);
    }
}
