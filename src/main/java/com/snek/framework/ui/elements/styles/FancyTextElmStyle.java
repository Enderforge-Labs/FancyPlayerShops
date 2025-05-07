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
 * The default style of the generic FancyTextElm UI element.
 */
public class FancyTextElmStyle extends TextElmStyle {
    private @NotNull Flagged<@NotNull Vector3i>  bgColor     = null;
    private @NotNull Flagged<@NotNull Integer>   bgAlpha     = null;
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
            .targetBgAlpha(0)
            .targetOpacity(0)
        );
    }


    @Override
    public @Nullable Animation getDefaultSpawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.S_TIME, Easings.sineOut)
            .targetBgAlpha(getDefaultBgAlpha())
            .targetOpacity(255)
        );
    }


    @Override
    public @Nullable Animation getDefaultDespawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.D_TIME, Easings.sineOut)
            .targetBgAlpha(0)
            .targetOpacity(0)
        );
    }


    @Override
    public @NotNull Transform getDefaultTransform () {
        return new Transform();
    }


    @Override
    public void resetAll() {
        resetBgColor();
        resetBgAlpha();
        resetTransformFg();
        resetTransformBg();
        super.resetAll();
    }





    public @NotNull Vector3i  getDefaultBgColor    () { return new Vector3i(20, 2, 20); }
    public          int       getDefaultBgAlpha    () { return 130; }
    public @NotNull Transform getDefaultTransformFg() { return new Transform(); }
    public @NotNull Transform getDefaultTransformBg() { return new Transform(); }


    public void resetBgColor     () { bgColor     = Flagged.from(getDefaultBgColor    ()); }
    public void resetBgAlpha     () { bgAlpha     = Flagged.from(getDefaultBgAlpha    ()); }
    public void resetTransformFg () { transformFg = Flagged.from(getDefaultTransformFg()); }
    public void resetTransformBg () { transformBg = Flagged.from(getDefaultTransformBg()); }


    public void setBgColor     (final @NotNull Vector3i  _bgColor    ) { bgColor    .set(_bgColor    ); }
    public void setBgAlpha     (final          int       _bgAlpha    ) { bgAlpha    .set(_bgAlpha    ); }
    public void setTransformFg (final @NotNull Transform _transformFg) { transformFg.set(_transformFg); }
    public void setTransformBg (final @NotNull Transform _transformBg) { transformBg.set(_transformBg); }


    public @NotNull Flagged<@NotNull Vector3i>  getFlaggedBgColor    () { return bgColor; }
    public @NotNull Flagged<@NotNull Integer>   getFlaggedBgAlpha    () { return bgAlpha; }
    public @NotNull Flagged<@NotNull Transform> getFlaggedTransformFg() { return transformFg; }
    public @NotNull Flagged<@NotNull Transform> getFlaggedTransformBg() { return transformBg; }


    public @NotNull Vector3i  getBgColor    () { return bgColor    .get(); }
    public          int       getBgAlpha    () { return bgAlpha    .get(); }
    public @NotNull Transform getTransformFg() { return transformFg.get(); }
    public @NotNull Transform getTransformBg() { return transformBg.get(); }


    public @NotNull Vector3i  editBgColor    () { return bgColor    .edit(); }
    //!                       editBgAlpha    Primitive types cannot be edited
    public @NotNull Transform editTransformFg() { return transformFg.edit(); }
    public @NotNull Transform editTransformBg() { return transformBg.edit(); }
}
