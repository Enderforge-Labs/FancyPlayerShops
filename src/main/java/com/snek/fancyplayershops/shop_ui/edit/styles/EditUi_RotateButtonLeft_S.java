package com.snek.fancyplayershops.shop_ui.edit.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui._elements.UiCanvas;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;








/**
 * The style of the EditUi_PriceButton UI element.
 */
public class EditUi_RotateButtonLeft_S extends EditUi_RotateButtonRight_S {
    public static final float SHIFT_X = UiCanvas.SQUARE_BUTTON_SIZE * (1 - HIDDEN_W);


    /**
     * Creates a new EditUi_LeftRotateButton_S.
     */
    public EditUi_RotateButtonLeft_S(final @NotNull Shop _shop) {
        super(_shop);
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransform(new Transform().scaleX(HIDDEN_W).moveX(SHIFT_X))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransform(new Transform().scaleX(1f / HIDDEN_W).moveX(-SHIFT_X))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransform(new Transform().scaleX(HIDDEN_W).moveX(SHIFT_X))
        );
    }
}
