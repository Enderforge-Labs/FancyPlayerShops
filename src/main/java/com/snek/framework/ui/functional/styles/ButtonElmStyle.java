package com.snek.framework.ui.functional.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.ui.elements.styles.FancyTextElmStyle;
import com.snek.framework.utils.Easings;








/**
 * The style of the generic ButtonElm UI element.
 */
public class ButtonElmStyle extends FancyTextElmStyle {
    public static final @NotNull Vector3i HOVER_COLOR          = new Vector3i(110, 160, 130);
    public static final          float    UNHOVERED_W          = 0.05f;
    public static final          int      HOVER_ANIMATION_TIME = 10;

    private @Nullable Flagged<Animation> hoverPrimerAnimation = null;
    private @Nullable Flagged<Animation> hoverEnterAnimation  = null;
    private @Nullable Flagged<Animation> hoverLeaveAnimation  = null;




    /**
     * Creates a new ButtonElmStyle.
     */
    public ButtonElmStyle() {
        super();
    }




    @Override
    public void resetAll() {
        super.resetAll();
        resetHoverPrimerAnimation();
        resetHoverEnterAnimation();
        resetHoverLeaveAnimation();
    }




    @Override
    public @NotNull Vector3i getDefaultBgColor() {
        return new Vector3i(HOVER_COLOR);
    }

    @Override
    public int getDefaultBgAlpha() {
        return 255;
    }



    // Default value providers
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransformBg(new Transform().scaleX(UNHOVERED_W))
        );
    }
    public @Nullable Animation getDefaultHoverEnterAnimation () {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(1f / UNHOVERED_W))
        );
    }
    public @Nullable Animation getDefaultHoverLeaveAnimation () {
        return new Animation(
            new Transition(HOVER_ANIMATION_TIME, Easings.expOut)
            .additiveTransformBg(new Transform().scaleX(UNHOVERED_W))
        );
    }


    // Reset functions
    public void resetHoverPrimerAnimation() { hoverPrimerAnimation = Flagged.from(getDefaultHoverPrimerAnimation()); }
    public void resetHoverEnterAnimation () { hoverEnterAnimation  = Flagged.from(getDefaultHoverEnterAnimation ()); }
    public void resetHoverLeaveAnimation () { hoverLeaveAnimation  = Flagged.from(getDefaultHoverLeaveAnimation ()); }


    // Setters
    public void setHoverPrimerAnimation(final @Nullable Animation _hoverPrimerAnimation) { hoverPrimerAnimation.set(_hoverPrimerAnimation); }
    public void setHoverEnterAnimation (final @Nullable Animation _hoverEnterAnimation ) { hoverEnterAnimation .set(_hoverEnterAnimation ); }
    public void setHoverLeaveAnimation (final @Nullable Animation _hoverLeaveAnimation ) { hoverLeaveAnimation .set(_hoverLeaveAnimation ); }


    // Flagged getters
    public @Nullable Flagged<@NotNull Animation> getFlaggedHoverPrimerAnimation() { return hoverPrimerAnimation;}
    public @Nullable Flagged<@NotNull Animation> getFlaggedHoverEnterAnimation () { return hoverEnterAnimation ;}
    public @Nullable Flagged<@NotNull Animation> getFlaggedHoverLeaveAnimation () { return hoverLeaveAnimation ;}


    // Getters
    public @Nullable Animation getHoverPrimerAnimation() { return hoverPrimerAnimation.get(); }
    public @Nullable Animation getHoverEnterAnimation () { return hoverEnterAnimation .get(); }
    public @Nullable Animation getHoverLeaveAnimation () { return hoverLeaveAnimation .get(); }


    // Edit getters
    public @Nullable Animation editHoverPrimerAnimation() { return hoverPrimerAnimation.edit(); }
    public @Nullable Animation editHoverEnterAnimation () { return hoverEnterAnimation .edit(); }
    public @Nullable Animation editHoverLeaveAnimation () { return hoverLeaveAnimation .edit(); }
}
