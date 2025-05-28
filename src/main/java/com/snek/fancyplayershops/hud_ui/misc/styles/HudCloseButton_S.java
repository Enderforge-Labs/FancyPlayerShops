package com.snek.fancyplayershops.hud_ui.misc.styles;

import org.jetbrains.annotations.Nullable;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.ui.functional.styles.SimpleButtonElmStyle;
import com.snek.framework.utils.Easings;








public class HudCloseButton_S extends SimpleButtonElmStyle {


    public HudCloseButton_S() {
        super();
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransform(new Transform().scaleY(HIDDEN_W))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation () {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransform(new Transform().scaleY(1f / HIDDEN_W))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation () {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransform(new Transform().scaleY(HIDDEN_W))
        );
    }
}
