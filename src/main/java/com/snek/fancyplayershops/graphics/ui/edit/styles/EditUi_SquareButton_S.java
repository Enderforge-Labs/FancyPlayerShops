package com.snek.fancyplayershops.graphics.ui.edit.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.misc.styles.SimpleShopButton_S;
import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.functional.styles.__base_ButtonElmStyle;
import com.snek.frameworklib.utils.Easings;







//TODO move to misc? only if other canvases use this style
/**
 * The style of the buttons in the toolbar.
 */
public class EditUi_SquareButton_S extends SimpleShopButton_S {


    /**
     * Creates a new EditUi_SquareButton_S.
     */
    public EditUi_SquareButton_S(final @NotNull Shop _shop) {
        super(_shop);
    }


    //FIXME merge with HudSquareButton_S
    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransform(new Transform().scaleY(__base_ButtonElmStyle.HIDDEN_W))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverEnterAnimation() {
        return new Animation(
            new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransform(new Transform().scaleY(1f / __base_ButtonElmStyle.HIDDEN_W))
        );
    }
    @Override
    public @Nullable Animation getDefaultHoverLeaveAnimation() {
        return new Animation(
            new Transition(__base_ButtonElmStyle.HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransform(new Transform().scaleY(__base_ButtonElmStyle.HIDDEN_W))
        );
    }
}
