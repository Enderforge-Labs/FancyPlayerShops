package com.snek.fancyplayershops.graphics.ui.edit.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.edit.EditCanvas;
import com.snek.fancyplayershops.graphics.ui.misc.styles.ShopTextInput_S;
import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.functional.styles.__base_ButtonElmStyle;
import com.snek.frameworklib.utils.Easings;








/**
 * The style of EditUi's text input elements.
 */
public class Edit_Input_S extends ShopTextInput_S {
    public static final float SHIFT_X  = EditCanvas.INPUT_W / 2f;
    public static final float ADJUST_X = 1 - EditCanvas.INPUT_W;


    /**
     * Creates a new EditUi_Input_S.
     */
    public Edit_Input_S(final @NotNull ProductDisplay _shop) {
        super(_shop);
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransformBg(new Transform().scaleX(__base_ButtonElmStyle.HIDDEN_W).moveX(SHIFT_X + ADJUST_X / 2))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation () {
        return new Animation(
            new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(1f / __base_ButtonElmStyle.HIDDEN_W).moveX(-(SHIFT_X + ADJUST_X)))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation () {
        return new Animation(
            new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(__base_ButtonElmStyle.HIDDEN_W).moveX(SHIFT_X + ADJUST_X))
        );
    }
}
