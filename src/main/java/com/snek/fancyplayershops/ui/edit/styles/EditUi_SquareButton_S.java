package com.snek.fancyplayershops.ui.edit.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.fancyplayershops.ui.misc.styles.ShopButton_S;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;








/**
 * The style of the buttons in the toolbar.
 */
public class EditUi_SquareButton_S extends ShopButton_S {


    /**
     * Creates a new EditUiSquareButton_S.
     */
    public EditUi_SquareButton_S() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultBgColor() {
        return new Vector3i(HOVER_COLOR);
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransformBg(new Transform().scaleY(HIDDEN_W))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleY(1f / HIDDEN_W))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleY(HIDDEN_W))
        );
    }
}
