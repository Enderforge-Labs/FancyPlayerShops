package com.snek.fancyplayershops.shop_ui.buy.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.shop_ui.misc.styles.ShopButton_S;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;








/**
 * The style of the EditUi_ItemSelector UI element.
 */
public class BuyUi_ItemInspector_S extends ShopButton_S {


    /**
     * Creates a new EditUi_ItemSelector_S.
     */
    public BuyUi_ItemInspector_S(final @NotNull Shop _shop) {
        super(_shop);
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
