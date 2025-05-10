package com.snek.fancyplayershops.ui.edit.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.edit.EditUi;
import com.snek.fancyplayershops.ui.misc.styles.ShopButton_S;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;








/**
 * The style of the EditUi_ColorSelector UI element.
 */
public class EditUi_ColorSelector_S extends ShopButton_S {
    public static final float SHIFT_X = EditUi.COLOR_SELECTOR_SIZE.x * (1 - EditUi.COLOR_SELECTOR_HIDDEN_W);


    /**
     * Creates a new EditUi_ColorSelector_S.
     */
    public EditUi_ColorSelector_S(final @NotNull Shop _shop) {
        super(_shop);
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransformBg(new Transform().scaleX(EditUi.COLOR_SELECTOR_HIDDEN_W).moveX(SHIFT_X))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(1f / EditUi.COLOR_SELECTOR_HIDDEN_W).moveX(-SHIFT_X))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(EditUi.COLOR_SELECTOR_HIDDEN_W).moveX(SHIFT_X))
        );
    }
}
