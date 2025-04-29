package com.snek.fancyplayershops.ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.fancyplayershops.ui.misc.DualInputIndicator;
import com.snek.fancyplayershops.ui.misc.InputIndicator;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.ui.styles.PanelElmStyle;








public class MouseButtonUp_S extends ShopPanelElm_S {
    public static final float BUTTON_SCALE = 1.2f;


    public MouseButtonUp_S() {
        super();
    }


    @Override
    public @NotNull Vector4i getDefaultColor() {
        return new Vector4i(255, 240, 240, 240);
    }

    @Override
    public @NotNull Transform getDefaultTransform() {
        return super.getDefaultTransform()
            .scale(BUTTON_SCALE)
            .moveX(-(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE.x * InputIndicator.MOUSE_SIZE.x * InputIndicator.BUTTON_SIZE.x * (BUTTON_SCALE - 1)) / 2)
        ;
    }
}
