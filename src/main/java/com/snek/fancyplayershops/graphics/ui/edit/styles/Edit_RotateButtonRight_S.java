package com.snek.fancyplayershops.graphics.ui.edit.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.misc.styles.ProductDisplay_SipleButton_S;
import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.functional.styles.__base_ButtonElmStyle;
import com.snek.frameworklib.utils.Easings;








/**
 * The style of the EditUi_PriceButton UI element.
 */
public class Edit_RotateButtonRight_S extends ProductDisplay_SipleButton_S {


    /**
     * Creates a new EditUi_RightRotateButton_S.
     */
    public Edit_RotateButtonRight_S(final @NotNull ProductDisplay display) {
        super(display);
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransform(new Transform().scaleX(__base_ButtonElmStyle.HIDDEN_W))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransform(new Transform().scaleX(1f / __base_ButtonElmStyle.HIDDEN_W))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransform(new Transform().scaleX(__base_ButtonElmStyle.HIDDEN_W))
        );
    }
}
