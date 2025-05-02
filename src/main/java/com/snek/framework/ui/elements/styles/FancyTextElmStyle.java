package com.snek.framework.ui.elements.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.utils.Easings;








/**
 * The default style of the generic FancyTextElm UI element.
 */
public class FancyTextElmStyle extends TextElmStyle {
    private @NotNull Flagged<@NotNull Vector4i>  background  = null;
    private @NotNull Flagged<@NotNull Transform> transformFg = null;
    private @NotNull Flagged<@NotNull Transform> transformBg = null;


    /**
     * Creates a new FancyTextElmStyle.
     */
    public FancyTextElmStyle() {
        super();
    }


    @Override
    public @Nullable Animation getDefaultPrimerAnimation() {
        return new Animation(
            new Transition(ElmStyle.D_TIME, Easings.sineOut)
            .targetBackground(new Vector4i(0))
            .targetOpacity(0)
        );
    }


    @Override
    public @Nullable Animation getDefaultSpawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.S_TIME, Easings.sineOut)
            .targetBackground(background.get())
            .targetOpacity(255)
        );
    }


    @Override
    public @Nullable Animation getDefaultDespawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.D_TIME, Easings.sineOut)
            .targetBackground(new Vector4i(0))
            .targetOpacity(0)
        );
    }


    @Override
    public @NotNull Transform getDefaultTransform () {
        return new Transform();
    }


    @Override
    public void resetAll() {
        resetBackground();
        resetTransformFg();
        resetTransformBg();
        super.resetAll();
    }





    public @NotNull Vector4i  getDefaultBackground () { return new Vector4i(130, 2, 20, 20); }
    public @NotNull Transform getDefaultTransformFg() { return new Transform(); }
    public @NotNull Transform getDefaultTransformBg() { return new Transform(); }

    public void resetBackground  () { background  = Flagged.from(getDefaultBackground() ); }
    public void resetTransformFg () { transformFg = Flagged.from(getDefaultTransformFg() ); }
    public void resetTransformBg () { transformBg = Flagged.from(getDefaultTransformBg() ); }

    public void setBackground  (final @NotNull Vector4i  _background ) { background .set(_background ); }
    public void setTransformFg (final @NotNull Transform _transformFg) { transformFg.set(_transformFg); }
    public void setTransformBg (final @NotNull Transform _transformBg) { transformBg.set(_transformBg); }

    public @NotNull Flagged<@NotNull Vector4i>  getFlaggedBackground () { return background; }
    public @NotNull Flagged<@NotNull Transform> getFlaggedTransformFg() { return transformFg; }
    public @NotNull Flagged<@NotNull Transform> getFlaggedTransformBg() { return transformBg; }

    public @NotNull Vector4i  getBackground () { return background .get(); }
    public @NotNull Transform getTransformFg() { return transformFg.get(); }
    public @NotNull Transform getTransformBg() { return transformBg.get(); }

    public @NotNull Vector4i  editBackground () { return background .edit(); }
    public @NotNull Transform editTransformFg() { return transformFg.edit(); }
    public @NotNull Transform editTransformBg() { return transformBg.edit(); }
}
