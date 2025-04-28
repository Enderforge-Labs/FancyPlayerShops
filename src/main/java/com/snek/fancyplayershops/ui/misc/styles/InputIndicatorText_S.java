package com.snek.fancyplayershops.ui.misc.styles;

import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.ui.styles.TextElmStyle;








public class InputIndicatorText_S extends TextElmStyle {
    // Workaround for the height issues
    public static final float Y_ADJUSTMENT = -0.01f;


    public InputIndicatorText_S() {
        super();
    }


    @Override
    public Transform getDefaultTransform() {
        return super.getDefaultTransform().scale(0.5f).moveY(Y_ADJUSTMENT);
    }
}
