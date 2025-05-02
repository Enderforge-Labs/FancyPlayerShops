package com.snek.framework.ui.elements.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.utils.Easings;








/**
 * The default style of the generic PanelElm UI element.
 */
public class PanelElmStyle extends ElmStyle {
    private @NotNull Flagged<@NotNull Vector3i> color = null;
    private @NotNull Flagged<@NotNull Integer>  alpha = null;




    /**
     * Creates a new PanelElmStyle.
     */
    public PanelElmStyle() {
        super();
    }


    @Override
    public void resetAll() {
        resetColor();
        resetAlpha();
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
            .targetBgAlpha(0)
            .targetBgColor(getDefaultColor())
        );
    }


    @Override
    public @Nullable Animation getDefaultSpawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.S_TIME, Easings.sineOut)
            .targetBgAlpha(getDefaultAlpha())
        );
    }


    @Override
    public @Nullable Animation getDefaultDespawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.D_TIME, Easings.sineOut)
            .targetBgAlpha(0)
        );
    }




    public @NotNull Vector3i getDefaultColor() { return new Vector3i(2, 20, 20); }
    public          int      getDefaultAlpha() { return 255; }


    public void resetColor() { color = Flagged.from(getDefaultColor()); }
    public void resetAlpha() { alpha = Flagged.from(getDefaultAlpha()); }


    public void setColor(final @NotNull Vector3i _color ) { color.set(_color); }
    public void setAlpha(final          int      _alpha ) { alpha.set(_alpha); }


    public @NotNull Flagged<@NotNull Vector3i> getFlaggedColor() { return color; }
    public @NotNull Flagged<@NotNull Integer>  getFlaggedAlpha() { return alpha; }


    public @NotNull Vector3i getColor() { return color.get(); }
    public          int      getAlpha() { return alpha.get(); }


    public @NotNull Vector3i editColor     () { return color.edit(); }
    //!                      editViewRange Primitive types cannot be edited
}
