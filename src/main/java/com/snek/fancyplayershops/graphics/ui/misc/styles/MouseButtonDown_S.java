package com.snek.fancyplayershops.shop_ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.framework.old.utils.Txt;








/**
 * The style of the MouseButtonDown UI element.
 */
public class MouseButtonDown_S extends ShopPanelElm_S {

    /**
     * Creates a new MouseButtonDown_S.
     */
    public MouseButtonDown_S() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(Txt.COLOR_LIGHTGRAY);
    }
}
