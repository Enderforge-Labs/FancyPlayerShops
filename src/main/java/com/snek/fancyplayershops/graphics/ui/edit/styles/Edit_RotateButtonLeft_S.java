package com.snek.fancyplayershops.graphics.ui.edit.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.frameworklib.graphics.core.Canvas;
import com.snek.frameworklib.graphics.functional.styles.__base_ButtonElmStyle;
import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.utils.Easings;








/**
 * The style of the EditUi_PriceButton UI element.
 */
public class Edit_RotateButtonLeft_S extends Edit_RotateButtonRight_S {
    public static final float SHIFT_X = FancyPlayerShops.SQUARE_BUTTON_SIZE * (1 - __base_ButtonElmStyle.HIDDEN_W);


    /**
     * Creates a new EditUi_LeftRotateButton_S.
     */
    public Edit_RotateButtonLeft_S(final @NotNull Shop _shop) {
        super(_shop);
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransform(new Transform().scaleX(__base_ButtonElmStyle.HIDDEN_W).moveX(SHIFT_X))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransform(new Transform().scaleX(1f / __base_ButtonElmStyle.HIDDEN_W).moveX(-SHIFT_X))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransform(new Transform().scaleX(__base_ButtonElmStyle.HIDDEN_W).moveX(SHIFT_X))
        );
    }
}
