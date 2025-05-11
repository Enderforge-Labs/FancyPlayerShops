package com.snek.fancyplayershops.ui.transfer.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.styles.ShopButton_S;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.network.chat.Component;








public class TransferUi_ConfirmButton_S extends ShopButton_S {
    public static final Vector3i COLOR       = Utils.HSVtoRGB(new Vector3f(120f, 0.2f, 0.6f));
    // public static final Vector3i COLOR_HOVER = Utils.HSVtoRGB(new Vector3f(120f, 0.2f, 0.8f));
    public static final Vector3i COLOR_HOVER = Utils.HSVtoRGB(new Vector3f(0f, 0.5f, 0.5f));


    public TransferUi_ConfirmButton_S(final @NotNull Shop _shop){
        super(_shop);
    }


    @Override
    public @NotNull Component getDefaultText() {
        return new Txt("Confirm").get();
    }


    @Override
    public @NotNull Vector3i getDefaultBgColor() {
        return COLOR;
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .targetBgColor(COLOR)
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .targetBgColor(HOVER_COLOR)
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .targetBgColor(COLOR)
        );
    }
}
