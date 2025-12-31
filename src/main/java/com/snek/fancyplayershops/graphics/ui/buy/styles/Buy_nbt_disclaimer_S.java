package com.snek.fancyplayershops.graphics.ui.buy.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.functional.styles.FancyButtonElmStyle;
import com.snek.frameworklib.graphics.functional.styles.__base_ButtonElmStyle;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;




public class Buy_nbt_disclaimer_S extends FancyButtonElmStyle {
    public static final Vector3i DEFAULT_COLOR = new Vector3i(255, 0, 0);
    public static final Vector3i HOVERED_COLOR = new Vector3i(255, 96, 96);


    public Buy_nbt_disclaimer_S() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultBgColor() {
        return new Vector3i(DEFAULT_COLOR);
    }


    @Override
    public int getDefaultBgAlpha() {
        return 64;
    }


    @Override
    public @NotNull Component getDefaultText() {
            return new Txt("⚠ Mixed NBTs").red().get();
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return null;
    }

    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .targetBgColor(HOVERED_COLOR)
        );
    }

    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .targetBgColor(DEFAULT_COLOR)
        );
    }

    @Override
    public @Nullable Animation getDefaultHoverInversePrimerAnimation() {
        return null;
    }

}
