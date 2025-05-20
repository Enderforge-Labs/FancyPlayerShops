package com.snek.fancyplayershops.hud.misc.styles;

import org.joml.Vector3i;

import com.snek.fancyplayershops.ui.misc.styles.ShopPanelElm_S;








public class HudCanvasBackground_S extends ShopPanelElm_S {


    public HudCanvasBackground_S(){
        super();
    }


    @Override
    public Vector3i getDefaultColor(){
        return new Vector3i(0, 0, 0); //FIXME replace with shop color in shop canvas bg
    }


    @Override
    public int getDefaultAlpha() {
        return 100;
    }
}
