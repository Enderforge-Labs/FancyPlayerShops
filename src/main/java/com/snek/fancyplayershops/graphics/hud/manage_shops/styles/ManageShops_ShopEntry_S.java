package com.snek.fancyplayershops.graphics.hud.manage_shops.styles;

import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.graphics.hud.misc.styles.Hud_SimpleButton_S;
import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.functional.styles.__base_ButtonStyle;
import com.snek.frameworklib.utils.Easings;








public class ManageShops_ShopEntry_S extends Hud_SimpleButton_S {


    /**
     * Creates a new ManageShops_ShopEntry_S.
     */
    public ManageShops_ShopEntry_S() {
        super();
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransform(new Transform().scaleX(__base_ButtonStyle.HIDDEN_W))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition(__base_ButtonStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransform(new Transform().scaleX(1f / __base_ButtonStyle.HIDDEN_W))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition(__base_ButtonStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransform(new Transform().scaleX(__base_ButtonStyle.HIDDEN_W))
        );
    }
}