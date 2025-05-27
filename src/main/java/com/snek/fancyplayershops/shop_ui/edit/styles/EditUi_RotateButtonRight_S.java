package com.snek.fancyplayershops.shop_ui.edit.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.shop_ui.misc.styles.SimpleShopButton_S;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;








/**
 * The style of the EditUi_PriceButton UI element.
 */
public class EditUi_RotateButtonRight_S extends SimpleShopButton_S {


    /**
     * Creates a new EditUi_RightRotateButton_S.
     */
    public EditUi_RotateButtonRight_S(final @NotNull Shop _shop) {
        super(_shop);
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransformBg(new Transform().scaleX(HIDDEN_W))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(1f / HIDDEN_W))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(HIDDEN_W))
        );
    }
}
