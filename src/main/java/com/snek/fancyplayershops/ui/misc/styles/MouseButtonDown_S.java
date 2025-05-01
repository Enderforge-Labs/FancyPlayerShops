package com.snek.fancyplayershops.ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;
import org.joml.Vector4i;

import com.snek.framework.utils.Txt;








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
    public @NotNull Vector4i getDefaultColor() {
        final Vector3i color = Txt.COLOR_LIGHTGRAY;
        return new Vector4i(255, color.x, color.y, color.z);
    }
}
