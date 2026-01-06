package com.snek.fancyplayershops.graphics.ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3i;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.functional.styles.FancyButtonElmStyle;
import com.snek.frameworklib.graphics.functional.styles.__base_ButtonElmStyle;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.Utils;








public class ProductDisplay_TogglableButton_S extends FancyButtonElmStyle {
    public static final @NotNull Vector3i BASE_COLOR = Utils.HSVtoRGB(new Vector3f(120f, 0.2f, 0.6f));
    public static final @NotNull Vector3i BASE_COLOR_INACTIVE = Utils.toBW(BASE_COLOR);

    private final @NotNull Vector3i bgColor = new Vector3i(BASE_COLOR);
    public void setColor(final @NotNull Vector3i _color) {
        bgColor.set(_color);
        resetHoverPrimerAnimation();
        resetHoverEnterAnimation();
        resetHoverLeaveAnimation();
    }
    public Vector3i getColor() {
        return new Vector3i(bgColor);
    }




    public ProductDisplay_TogglableButton_S() {
        super(false);
        resetAll();
    }


    // @Override
    // public @NotNull Vector3i getDefaultBgColor() {
    //     return new Vector3i(bgColor);
    // }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .targetBgColor(bgColor)
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .targetBgColor(bgColor.add(20, 20, 20).min(new Vector3i(255)))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .targetBgColor(bgColor)
        );
    }
}
