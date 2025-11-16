package com.snek.fancyplayershops.graphics.ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.graphics.ui.misc.elements.DualInputIndicator;
import com.snek.fancyplayershops.graphics.ui.misc.elements.InputIndicator;
import com.snek.frameworklib.data_types.animations.Transform;








/**
 * The style of the MouseButtonUp UI element.
 */
public class MouseButtonUp_S extends ShopPanelElm_S {
    public static final float BUTTON_SCALE = 1.2f;


    /**
     * Creates a new MouseButtonUp_S.
     */
    public MouseButtonUp_S() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(240, 240, 240);
    }


    @Override
    public @NotNull Transform getDefaultTransform() {
        return super.getDefaultTransform()
            .scale(BUTTON_SCALE)
            .moveX(-(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE.x * InputIndicator.MOUSE_SIZE.x * InputIndicator.BUTTON_SIZE.x * (BUTTON_SCALE - 1)) / 2)
        ;
    }
}
