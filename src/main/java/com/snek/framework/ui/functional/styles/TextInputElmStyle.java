package com.snek.framework.ui.functional.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;

import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity.TextAlignment;








/**
 * The style of the generic TextInputElm UI element.
 */
public class TextInputElmStyle extends ButtonElmStyle {
    public static final float UNHOVERED_W = 0.05f;


    /**
     * Creates a new TextInputElmStyle.
     */
    public TextInputElmStyle() {
        super();
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransformBg(new Transform().scaleX(UNHOVERED_W))
            .additiveTransformFg(new Transform().moveX(UNHOVERED_W * 2))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation () {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(1f / UNHOVERED_W))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation () {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(UNHOVERED_W))
        );
    }


    @Override
    public @NotNull TextAlignment getDefaultTextAlignment() {
        return TextAlignment.LEFT;
    }
}
