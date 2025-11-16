package com.snek.framework.old.data_types.animations;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.framework.old.utils.Easing;
import com.snek.framework.old.utils.Easings;








/**
 * A single interpolated transition.
 */
public class Transition {

    // Transition data
    private final int duration;
    private final @NotNull Easing easing;

    // Optional data
    private boolean additive;
    public final @NotNull InterpolatedData d;




    /**
     * Creates a new Transition.
     * @param _duration The total duration of the transition.
     * @param _easing The type of easing to use.
     */
    public Transition(final int _duration, final @NotNull Easing _easing) {
        duration  = _duration;
        easing    = _easing;

        additive   = false;
        d = new InterpolatedData(null, null, null, null);
    }


    /**
     * Creates a new Transition with 0 duration and linear easing.
     */
    public Transition() {
        this(0, Easings.linear);
    }




    /**
     * Specifies that the transform of the element this transition is applied to needs to reach a certain value.
     * @param _transform The transform value.
     * @return This transition.
     */
    public @NotNull Transition targetTransform(final @Nullable Transform _transform) {
        d.setTransform(_transform);
        additive = false;
        return this;
    }
    /**
     * Specifies that a certain transform value has to be applied to the transform of the element this transition is applied to.
     * @param _transform The transform value.
     * @return This transition.
     */
    public @NotNull Transition additiveTransform(final @Nullable Transform _transform) {
        d.setTransform(_transform);
        additive = true;
        return this;
    }


    /**
     * Specifies that the foreground transform of the element this transition is applied to needs to reach a certain value.
     * @param _transform The transform value.
     * @return This transition.
     */
    public @NotNull Transition targetTransformFg(final @Nullable Transform _transform) {
        d.setTransformFg(_transform);
        additive = false;
        return this;
    }
    /**
     * Specifies that a certain transform value has to be applied to the foreground transform of the element this transition is applied to.
     * @param _transform The transform value.
     * @return This transition.
     */
    public @NotNull Transition additiveTransformFg(final @Nullable Transform _transform) {
        d.setTransformFg(_transform);
        additive = true;
        return this;
    }


    /**
     * Specifies that the background transform of the element this transition is applied to needs to reach a certain value.
     * @param _transform The transform value.
     * @return This transition.
     */
    public @NotNull Transition targetTransformBg(final @Nullable Transform _transform) {
        d.setTransformBg(_transform);
        additive = false;
        return this;
    }
    /**
     * Specifies that a certain transform value has to be applied to the background transform of the element this transition is applied to.
     * @param _transform The transform value.
     * @return This transition.
     */
    public @NotNull Transition additiveTransformBg(final @Nullable Transform _transform) {
        d.setTransformBg(_transform);
        additive = true;
        return this;
    }


    /**
     * Specifies that the background color of the element this transition is applied to needs to reach a certain value.
     * @param _color The background color value.
     * @return This transition.
     */
    public @NotNull Transition targetBgColor(final @Nullable Vector3i _bgColor) {
        d.setBgColor(_bgColor);
        return this;
    }


    /**
     * Specifies that the background transparency of the element this transition is applied to needs to reach a certain value.
     * @param _alpha The background transparency value.
     * @return This transition.
     */
    public @NotNull Transition targetBgAlpha(final @Nullable Integer _bgAlpha) {
        d.setBgAlpha(_bgAlpha);
        return this;
    }


    /**
     * Specifies that the text opacity of the element this transition is applied to needs to reach a certain value.
     * @param _opacity The text opacity value.
     * @return This transition.
     */
    public @NotNull Transition targetOpacity(final @Nullable Integer _opacity) {
        d.setOpacity(_opacity);
        return this;
    }




    /**
     * Creates an animation step from this transition based on the interpolation factor.
     * @param factor The interpolation factor.
     * @return The animation step.
     */
    public @NotNull TransitionStep createStep(final float factor) {
        return new TransitionStep(factor, additive, d);
    }




    // Getters
    public          int     getDuration() { return duration; }
    public @NotNull Easing  getEasing  () { return easing;   }
    public          boolean isAdditive () { return additive; }
}
