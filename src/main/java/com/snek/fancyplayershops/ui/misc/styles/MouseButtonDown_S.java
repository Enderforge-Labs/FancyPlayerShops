package com.snek.fancyplayershops.ui.misc.styles;

import org.joml.Vector4i;

import com.snek.framework.ui.styles.PanelElmStyle;




public class MouseButtonDown_S extends ShopPanelElm_S {

    public MouseButtonDown_S(){
        super();
    }

    @Override
    public Vector4i getDefaultColor(){
        return new Vector4i(255, 255, 0, 0);
    }
}
