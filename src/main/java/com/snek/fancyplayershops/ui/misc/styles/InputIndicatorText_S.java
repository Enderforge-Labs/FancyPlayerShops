package com.snek.fancyplayershops.ui.misc.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.ui.elements.styles.TextElmStyle;








/**
 * The style of the InputIndicatorText UI element.
 */
public class InputIndicatorText_S extends TextElmStyle {
    public static final float Y_ADJUSTMENT = -0.01f; //! Workaround for the height issues


    /**
     * Creates a new InputIndicatorText_S.
     */
    public InputIndicatorText_S() {
        super();
    }


    @Override
    public @NotNull Transform getDefaultTransform() {
        return super.getDefaultTransform().scale(0.5f).moveY(Y_ADJUSTMENT);
    }
}
