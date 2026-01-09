package com.snek.fancyplayershops.graphics.ui.edit.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.edit.EditCanvas;
import com.snek.fancyplayershops.graphics.ui.misc.styles.ProductDisplay_SipleButton_S;
import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.functional.styles.__base_ButtonStyle;
import com.snek.frameworklib.utils.Easings;








/**
 * The style of the EditUi_ColorSelector UI element.
 */
public class Edit_ColorSelector_S extends ProductDisplay_SipleButton_S {
    public static final float SHIFT_X = EditCanvas.COLOR_SELECTOR_W * (1 - EditCanvas.COLOR_SELECTOR_HIDDEN_W);


    /**
     * Creates a new EditUi_ColorSelector_S.
     */
    public Edit_ColorSelector_S(final @NotNull ProductDisplay display) {
        super(display);
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransform(new Transform().scaleX(EditCanvas.COLOR_SELECTOR_HIDDEN_W).moveX(SHIFT_X))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition(__base_ButtonStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransform(new Transform().scaleX(1f / EditCanvas.COLOR_SELECTOR_HIDDEN_W).moveX(-SHIFT_X))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition(__base_ButtonStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransform(new Transform().scaleX(EditCanvas.COLOR_SELECTOR_HIDDEN_W).moveX(SHIFT_X))
        );
    }
}
