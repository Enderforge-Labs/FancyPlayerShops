package com.snek.fancyplayershops.graphics.hud._mainmenu_.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.core.styles.CanvasBorder_S;
import com.snek.frameworklib.graphics.functional.styles.FancyButtonElmStyle;
import com.snek.frameworklib.graphics.functional.styles.__base_ButtonElmStyle;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.network.chat.Component;




public class _MainMenu_LargeButton_S extends FancyButtonElmStyle {
    public static final Vector3i HOVERED_COLOR = new Vector3i(64, 64, 64);
    public static final Vector3i DEFAULT_COLOR = Utils.interpolateRGB(CanvasBorder_S.COLOR, HOVERED_COLOR, 0.5f);

    final @NotNull String displayedText;


    public _MainMenu_LargeButton_S(final @NotNull String displayedText) {
        super();
        this.displayedText = displayedText;
    }




    @Override
    public @NotNull Vector3i getDefaultBgColor() {
        return new Vector3i(DEFAULT_COLOR);
    }

    @Override
    public int getDefaultBgAlpha() {
        return 255;
    }

    @Override
    public @NotNull Component getDefaultText() {
        return new Txt("\n\n\n\n" + displayedText).white().get();
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




    @Override //TODO replace with font size override
    public @NotNull Transform getDefaultTransformFg() {
        return super.getDefaultTransform().scale(0.5f);
    }
}