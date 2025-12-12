package com.snek.fancyplayershops.graphics.ui.misc.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;








/**
 * The style of the InputIndicatorText UI element.
 */
public class Any_InputIndicator_Text_S extends SimpleTextElmStyle {
    public static final float Y_ADJUSTMENT = -0.01f; //! Workaround for the height issues


    /**
     * Creates a new InputIndicatorText_S.
     */
    public Any_InputIndicator_Text_S() {
        super();
    }


    @Override
    public @NotNull Transform getDefaultTransform() {
        return super.getDefaultTransform().scale(0.5f).moveY(Y_ADJUSTMENT);
    }
}
