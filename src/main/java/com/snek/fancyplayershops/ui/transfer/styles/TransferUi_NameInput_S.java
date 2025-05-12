package com.snek.fancyplayershops.ui.transfer.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.styles.ShopTextInput_S;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;








/**
 * The style of the TransferUi_NameInput UI element.
 */
public class TransferUi_NameInput_S extends ShopTextInput_S {

    /**
     * Creates a new TransferUi_NameInput_S.
     */
    public TransferUi_NameInput_S(final @NotNull Shop _shop) {
        super(_shop);
    }


    @Override
    public @NotNull Vector3i getDefaultBgColor() {
        return shop.getThemeColor1();
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .targetBgColor(getBgColor())
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .targetBgColor(getBgColor().add(20, 20, 20, new Vector3i()).min(new Vector3i(255)))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .targetBgColor(getBgColor())
        );
    }
}
