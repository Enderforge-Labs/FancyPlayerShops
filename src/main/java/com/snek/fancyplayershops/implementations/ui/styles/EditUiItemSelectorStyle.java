package com.snek.fancyplayershops.implementations.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;






//FIXME fix stuttering hover animations. for some reason this doesnt happen on other buttons

/**
 * The style of the EditUiItemSelector UI element.
 */
public class EditUiItemSelectorStyle extends ShopButtonStyle {


    /**
     * Creates a new EditUiItemSelectorStyle.
     */
    public EditUiItemSelectorStyle(){
        super();
    }




    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransformBg(new Transform().scaleY(ShopButtonStyle.UNHOVERED_W))
        );
    }

    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition()
            .targetBackground(HOVER_COLOR),
            new Transition(HOVER_ANIMATION_TIME, Easings.cubicOut)
            .additiveTransformBg(new Transform().scaleY(1f / ShopButtonStyle.UNHOVERED_W))
        );
    }

    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition()
            .targetBackground(getDefaultBackground()),
            new Transition(HOVER_ANIMATION_TIME, Easings.cubicOut)
            .additiveTransformBg(new Transform().scaleY(ShopButtonStyle.UNHOVERED_W))
        );
    }
}
