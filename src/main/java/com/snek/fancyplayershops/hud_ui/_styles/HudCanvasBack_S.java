package com.snek.fancyplayershops.hud_ui._styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.hud_ui._elements.__HudElm;
import com.snek.fancyplayershops.shop_ui.misc.styles.ShopPanelElm_S;
import com.snek.fancyplayershops.ui._styles.UiBorder_S;








public class HudCanvasBack_S extends ShopPanelElm_S implements __HudElm {

    public HudCanvasBack_S() {
        super();
    }


    @Override
    public int getDefaultAlpha() {
        return 255;
    }


    @Override
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(UiBorder_S.COLOR);
    }
}
