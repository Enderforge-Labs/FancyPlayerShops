package com.snek.fancyplayershops.graphics.ui.edit.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.graphics.core.Canvas;
import com.snek.frameworklib.graphics.functional.styles.__base_ButtonStyle;
import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.utils.Easings;








/**
 * The style of the EditUi_PriceButton UI element.
 */
public class Edit_RotateButtonLeft_S extends Edit_RotateButtonRight_S {
    public static final float SHIFT_X = Canvas.TOOLBAR_H * (1 - __base_ButtonStyle.HIDDEN_W);


    /**
     * Creates a new EditUi_LeftRotateButton_S.
     */
    public Edit_RotateButtonLeft_S(final @NotNull ProductDisplay display) {
        super(display);
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransform(new Transform().scaleX(__base_ButtonStyle.HIDDEN_W).moveX(SHIFT_X))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition(__base_ButtonStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransform(new Transform().scaleX(1f / __base_ButtonStyle.HIDDEN_W).moveX(-SHIFT_X))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition(__base_ButtonStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransform(new Transform().scaleX(__base_ButtonStyle.HIDDEN_W).moveX(SHIFT_X))
        );
    }
}
