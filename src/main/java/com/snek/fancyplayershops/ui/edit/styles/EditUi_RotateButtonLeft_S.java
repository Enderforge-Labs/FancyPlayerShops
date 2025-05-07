package com.snek.fancyplayershops.ui.edit.styles;

import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.ui.edit.EditUi;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;








/**
 * The style of the EditUiPriceButton UI element.
 */
public class EditUi_RotateButtonLeft_S extends EditUi_RotateButtonRight_S {
    public static final float SHIFT_X = EditUi.SQUARE_BUTTON_SIZE * (1 - HIDDEN_W);


    /**
     * Creates a new EditUiLeftRotateButton_S.
     */
    public EditUi_RotateButtonLeft_S() {
        super();
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransformBg(new Transform().scaleX(HIDDEN_W).moveX(SHIFT_X))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(1f / HIDDEN_W).moveX(-SHIFT_X))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(HIDDEN_W).moveX(SHIFT_X))
        );
    }
}
