package com.snek.fancyplayershops.hud_ui._styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.hud_ui._elements.__HudElm;
import com.snek.fancyplayershops.shop_ui.misc.styles.ShopPanelElm_S;








public class HudCanvasBackground_S extends ShopPanelElm_S implements __HudElm {
    public static final @NotNull Vector3i COLOR = new Vector3i(3, 3, 7);


    public HudCanvasBackground_S() {
        super();
    }


    @Override
    public Vector3i getDefaultColor() {
        return new Vector3i(COLOR);
    }


    @Override
    public int getDefaultAlpha() {
        return 180;
    }
}
