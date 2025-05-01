package com.snek.framework.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.utils.Easings;








/**
 * The default style of the generic PanelElm UI element.
 */
public class PanelElmStyle extends ElmStyle {
    private @NotNull Flagged<@NotNull Vector4i> color = null;




    /**
     * Creates a new PanelElmStyle.
     */
    public PanelElmStyle() {
        super();
    }


    @Override
    public void resetAll() {
        resetColor();
        super.resetAll();
    }




    @Override
    public Transform getDefaultTransform() {
        return new Transform()
        ;
    }


    @Override
    public @Nullable Animation getDefaultPrimerAnimation() {
        return new Animation(
            new Transition(ElmStyle.S_TIME, Easings.sineOut)
            .targetBackground(new Vector4i(getDefaultColor().mul(new Vector4i(0, 1, 1, 1))))
        );
    }


    @Override
    public @Nullable Animation getDefaultSpawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.S_TIME, Easings.sineOut)
            .targetBackground(getDefaultColor())
        );
    }


    @Override
    public @Nullable Animation getDefaultDespawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.D_TIME, Easings.sineOut)
            .targetBackground(new Vector4i(getDefaultColor().mul(new Vector4i(0, 1, 1, 1))))
        );
    }




    public @NotNull Vector4i getDefaultColor () { return new Vector4i(180, 12, 20, 20); }
    public void resetColor () { color = Flagged.from(getDefaultColor()); }
    public void setColor (final @NotNull Vector4i _color ) { color.set(_color); }
    public @NotNull Flagged<@NotNull Vector4i> getFlaggedColor () { return color; }
    public @NotNull Vector4i getColor () { return color.get(); }
    public @NotNull Vector4i editColor () { return color.edit(); }
}
