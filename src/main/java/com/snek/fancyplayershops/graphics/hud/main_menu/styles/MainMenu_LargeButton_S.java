package com.snek.fancyplayershops.graphics.hud.main_menu.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.core.styles.CanvasBorder_S;
import com.snek.frameworklib.graphics.functional.styles.TextButtonStyle;
import com.snek.frameworklib.graphics.functional.styles.__base_ButtonStyle;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;



//TODO idk if overriding get methods is fine, this needs to be checked

public class MainMenu_LargeButton_S extends TextButtonStyle {
    public static final Vector3i HOVERED_COLOR = new Vector3i(64, 64, 64);
    public static final Vector3i DEFAULT_COLOR = Utils.interpolateRGB(CanvasBorder_S.COLOR, HOVERED_COLOR, 0.5f);



    public MainMenu_LargeButton_S(final @NotNull String displayedText) {
        super();
        setText(new Txt("\n\n\n\n" + displayedText).white().get());
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
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return null;
    }

    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition(__base_ButtonStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .targetBgColor(HOVERED_COLOR)
        );
    }

    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition(__base_ButtonStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .targetBgColor(DEFAULT_COLOR)
        );
    }

    @Override
    public @Nullable Animation getDefaultHoverInversePrimerAnimation() {
        return null;
    }




    @Override
    public int getDefaultFontSize() {
        return 6;
    }
}