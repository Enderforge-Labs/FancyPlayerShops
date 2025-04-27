package com.snek.fancyplayershops.ui.misc.styles;

import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.ui.styles.TextElmStyle;








public class InputIndicatorText_S extends TextElmStyle {
    public InputIndicatorText_S() {
        super();
    }


    public Transform getDefaultTransform() {
        return super.getTransform().scale(0.5f);
    }
}
