package com.snek.fancyplayershops.ui.edit.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.edit.EditUi;
import com.snek.fancyplayershops.ui.misc.styles.ShopTextInput_S;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;








/**
 * The style of EditUi's text input elements.
 */
public class EditUi_Input_S extends ShopTextInput_S {
    public static final float SHIFT_X  = EditUi.INPUT_W / 2f;
    public static final float ADJUST_X = 1 - EditUi.INPUT_W;


    /**
     * Creates a new EditUi_Input_S.
     */
    public EditUi_Input_S(final @NotNull Shop _shop) {
        super(_shop);
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        System.out.println("shift x: " + ADJUST_X);
        return new Animation(
            new Transition()
            .additiveTransformBg(new Transform().scaleX(HIDDEN_W).moveX(SHIFT_X + ADJUST_X / 2))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation () {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(1f / HIDDEN_W).moveX(-(SHIFT_X + ADJUST_X)))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation () {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(HIDDEN_W).moveX(SHIFT_X + ADJUST_X))
        );
    }
}
