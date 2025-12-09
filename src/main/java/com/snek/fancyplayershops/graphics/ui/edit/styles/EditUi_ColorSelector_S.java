package com.snek.fancyplayershops.graphics.ui.edit.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.edit.EditUi;
import com.snek.fancyplayershops.graphics.ui.misc.styles.SimpleShopButton_S;
import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.functional.styles.__base_ButtonElmStyle;
import com.snek.frameworklib.utils.Easings;








/**
 * The style of the EditUi_ColorSelector UI element.
 */
public class EditUi_ColorSelector_S extends SimpleShopButton_S {
    public static final float SHIFT_X = EditUi.COLOR_SELECTOR_W * (1 - EditUi.COLOR_SELECTOR_HIDDEN_W);


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
            .additiveTransform(new Transform().scaleX(EditUi.COLOR_SELECTOR_HIDDEN_W).moveX(SHIFT_X))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransform(new Transform().scaleX(1f / EditUi.COLOR_SELECTOR_HIDDEN_W).moveX(-SHIFT_X))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransform(new Transform().scaleX(EditUi.COLOR_SELECTOR_HIDDEN_W).moveX(SHIFT_X))
        );
    }
}
