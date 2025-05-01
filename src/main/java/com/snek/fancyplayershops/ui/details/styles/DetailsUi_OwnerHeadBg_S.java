package com.snek.fancyplayershops.ui.details.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4i;

import com.snek.fancyplayershops.ui.details.DetailsUi;
import com.snek.fancyplayershops.ui.misc.styles.ShopPanelElm_S;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.utils.Easings;








/**
 * The style of the DetailsUi_OwnerHeadBg UI element.
 */
public class DetailsUi_OwnerHeadBg_S extends ShopPanelElm_S {
    public static final float PRIMER_H = 0.001f;


    /**
     * Creates a new DetailsUi_OwnerHeadBg_S.
     */
    public DetailsUi_OwnerHeadBg_S() {
        super();
    }


    @Override
    public @NotNull Vector4i getDefaultColor() {
        return new Vector4i(50, 0, 0, 0);
    }


    @Override
    public @Nullable Animation getDefaultPrimerAnimation() {
        return new Animation(
            new Transition(ElmStyle.S_TIME, Easings.sineOut)
            .targetBackground(new Vector4i(getDefaultColor().mul(new Vector4i(0, 1, 1, 1))))
            .additiveTransform(new Transform().scaleY(PRIMER_H).moveY(DetailsUi.HEAD_BG_SIZE.y))
        );
    }
    @Override
    public @Nullable Animation getDefaultSpawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.S_TIME, Easings.sineOut)
            .targetBackground(getDefaultColor())
            .additiveTransform(new Transform().scaleY(1f / PRIMER_H).moveY(-DetailsUi.HEAD_BG_SIZE.y))
        );
    }
}
