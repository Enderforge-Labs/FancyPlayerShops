package com.snek.fancyplayershops.implementations.ui.edit;

import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.implementations.ui.misc.ShopButton_S;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;








/**
 * The style of the EditUiPriceButton UI element.
 */
public class EditUi_RotateButtonLeft_S extends EditUi_RotateButtonRight_S {
    public static final float SHIFT_X = EditUi.SQUARE_BUTTON_SIZE * (1 - ShopButton_S.UNHOVERED_W);


    /**
     * Creates a new EditUiLeftRotateButtonStyle.
     */
    public EditUi_RotateButtonLeft_S(){
        super();
    }




    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransformBg(new Transform().scaleX(ShopButton_S.UNHOVERED_W).moveX(SHIFT_X))
        );
    }

    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition()
            .targetBackground(HOVER_COLOR),
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(1f / ShopButton_S.UNHOVERED_W).moveX(-SHIFT_X))
        );
    }

    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(ShopButton_S.UNHOVERED_W).moveX(SHIFT_X)),
            new Transition()
            .targetBackground(getDefaultBackground())
        );
    }
}
