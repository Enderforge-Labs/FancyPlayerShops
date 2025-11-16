package com.snek.fancyplayershops.graphics.ui.details.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.fancyplayershops.graphics.ui.details.DetailsUi;
import com.snek.fancyplayershops.graphics.ui.misc.styles.ShopPanelElm_S;
import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.basic.styles.ElmStyle;
import com.snek.frameworklib.utils.Easings;








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
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(0, 0, 0);
    }


    @Override
    public int getDefaultAlpha() {
        return 50;
    }


    @Override
    public @Nullable Animation getDefaultPrimerAnimation() {
        return new Animation(
            new Transition(ElmStyle.S_TIME, Easings.sineOut)
            .targetBgAlpha(0)
            .additiveTransform(new Transform().scaleY(PRIMER_H).moveY(DetailsUi.HEAD_BG_SIZE.y))
        );
    }
    @Override
    public @Nullable Animation getDefaultSpawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.S_TIME, Easings.sineOut)
            .targetBgAlpha(getDefaultAlpha())
            .additiveTransform(new Transform().scaleY(1f / PRIMER_H).moveY(-DetailsUi.HEAD_BG_SIZE.y))
        );
    }
}
