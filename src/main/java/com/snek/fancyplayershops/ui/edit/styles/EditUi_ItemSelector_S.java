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
 * The style of the EditUiItemSelector UI element.
 */
public class EditUi_ItemSelector_S extends ShopButton_S {


    /**
     * Creates a new EditUiItemSelector_S.
     */
    public EditUi_ItemSelector_S() {
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
            .targetBgAlpha(0)
            .additiveTransformBg(new Transform().scaleY(ShopButton_S.UNHOVERED_W))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition()
            .targetBgAlpha(getDefaultBgAlpha()),
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleY(1f / ShopButton_S.UNHOVERED_W))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleY(ShopButton_S.UNHOVERED_W)),
            new Transition()
            .targetBgAlpha(0)
        );
    }
}
