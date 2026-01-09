package com.snek.fancyplayershops.graphics.ui.buy.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.misc.styles.ProductDisplay_TextInput_S;
import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.functional.styles.__base_ButtonStyle;
import com.snek.frameworklib.utils.Easings;








/**
 * The style of BuyUi's text input elements.
 */
public class Buy_Input_S extends ProductDisplay_TextInput_S {


    /**
     * Creates a new TransferUi_Input_S.
     */
    public Buy_Input_S(final @NotNull ProductDisplay display) {
        super(display);
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransformBg(new Transform().scaleX(__base_ButtonStyle.HIDDEN_W).moveX(0.5f))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation () {
        return new Animation(
            new Transition(__base_ButtonStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(1f / __base_ButtonStyle.HIDDEN_W).moveX(-0.5f))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation () {
        return new Animation(
            new Transition(__base_ButtonStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(__base_ButtonStyle.HIDDEN_W).moveX(0.5f))
        );
    }
}
