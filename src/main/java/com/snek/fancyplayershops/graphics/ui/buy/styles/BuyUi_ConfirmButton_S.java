package com.snek.fancyplayershops.graphics.ui.buy.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.misc.styles.FancyShopButton_S;
import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.functional.styles.SimpleButtonElmStyle;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.Utils;








public class BuyUi_ConfirmButton_S extends FancyShopButton_S {
    public static final @NotNull Vector3i BASE_COLOR = Utils.HSVtoRGB(new Vector3f(120f, 0.2f, 0.6f));
    public static final @NotNull Vector3i BASE_COLOR_INACTIVE = Utils.toBW(BASE_COLOR);

    private @NotNull Vector3i defaultBgColor = new Vector3i(BASE_COLOR);
    public void setDefaultColor(final @NotNull Vector3i _color) { defaultBgColor.set(_color); }




    public BuyUi_ConfirmButton_S(final @NotNull Shop _shop) {
        super(_shop);
    }


    @Override
    public @NotNull Vector3i getDefaultBgColor() {
        return new Vector3i(defaultBgColor);
    }


    @Override
    public @Nullable Animation getHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .targetBgColor(getDefaultBgColor())
        );
    }
    @Override
    public @Nullable Animation getHoverEnterAnimation() {
        return new Animation(
            new Transition(SimpleButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .targetBgColor(getDefaultBgColor().add(20, 20, 20).min(new Vector3i(255)))
        );
    }
    @Override
    public @Nullable Animation getHoverLeaveAnimation() {
        return new Animation(
            new Transition(SimpleButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .targetBgColor(getDefaultBgColor())
        );
    }
}
