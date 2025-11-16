package com.snek.framework.old.data_types.animations;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.mojang.math.Transformation;








/**
 * A single transformation specified as local rotation, translation, scale and global rotation.
 * <p> It can be converted to a Minecraft AffineTransformation.
 */
public class Transform {
    protected final @NotNull Vector3f    _pos;
    protected final @NotNull Quaternionf _lrot;
    protected final @NotNull Vector3f    _scale;
    protected final @NotNull Quaternionf _grot;


    /**
     * Creates a new Matrix4f using the current translation, local rotation, scale and global rotation values.
     * @return The transformation matric.
     */
    public @NotNull Matrix4f toMatrixTransform() {
        final Matrix4f m = new Matrix4f();
        m.rotate   (_grot );
        m.translate(_pos  );
        m.rotate   (_lrot );
        m.scale    (_scale);
        return m;
    }


    /**
     * Creates a new AffineTransformation using the current translation, local rotation, scale and global rotation values.
     * @return The transformation.
     */
    public @NotNull Transformation toMinecraftTransform() {
        return new Transformation(toMatrixTransform());
    }


    /**
     * Creates a new Transform with default data:
     * <p> No local rotation.
     *     No translation.
     *     Scale 1.
     *     No global rotation.
     */
    public Transform() {
        _pos   = new Vector3f(0.0f);
        _lrot  = new Quaternionf();
        _scale = new Vector3f(1.0f);
        _grot  = new Quaternionf();
    }


    /**
     * Creates a new Transform.
     * @param __pos The translation.
     * @param __rot The local rotation.
     * @param __scale The scale.
     * @param __globalRot The global rotation
     */
    public Transform(final @NotNull Vector3f __pos, final @NotNull Quaternionf __rot, final @NotNull Vector3f __scale, final @NotNull Quaternionf __globalRot) {
        _pos   = new Vector3f(__pos);
        _lrot  = new Quaternionf(__rot);
        _scale = new Vector3f(__scale);
        _grot  = new Quaternionf(__globalRot);
    }


    /**
     * Creates a copy of this transform.
     * @return A copy of this transform.
     */
    public Transform copy() {
        return new Transform(
            new Vector3f(_pos),
            new Quaternionf(_lrot),
            new Vector3f(_scale),
            new Quaternionf(_grot)
        );
    }


    /**
     * Sets this transform to the value of t.
     * @param t The new value.
     * @return This transform.
     */
    public @NotNull Transform set(final @NotNull Transform t) {
        _pos   .set(t._pos);
        _lrot  .set(t._lrot);
        _scale .set(t._scale);
        _grot  .set(t._grot);
        return this;
    }




    /**
     * Applies a transformation to this transform.
     * @param t The transform to apply.
     * @return this transform.
     */
    public @NotNull Transform apply(final @NotNull Transform t) {
        move(t._pos);
        rot(t._lrot);
        scale(t._scale);
        rotGlobal(t._grot);
        return this;
    }




    /**
     * Calculates the transform that would have to be applied to this transform in order to reach the provided one.
     * @param t The target transform.
     * @return The calculated transform.
     */
    public @NotNull Transform delta(final @NotNull Transform t) {
        return new Transform(
            t._pos.sub(_pos, new Vector3f()),
            t._lrot.mul(_lrot.invert(new Quaternionf()), new Quaternionf()),
            t._scale.div(_scale, new Vector3f()),
            t._grot.mul(_grot.invert(new Quaternionf()), new Quaternionf())
        );
    }




    /**
     * Applies a linear interpolation to this transform.
     * @param target The target transform.
     * @param factor The factor. Using 0 will return a copy of this, using 1 will return a copy of target.
     * @return this transform.
     */
    public @NotNull Transform interpolate(final @NotNull Transform target, final float factor) {
        _pos  .lerp (target._pos,   factor);
        _lrot .slerp(target._lrot,  factor);
        _scale.lerp (target._scale, factor);
        _grot .slerp(target._grot,  factor);
        return this;
    }




    // Left rotation
    public @NotNull Transform rotX         (final float x                  ) { _lrot.rotateX(x); return this; }
    public @NotNull Transform rotY         (final float y                  ) { _lrot.rotateY(y); return this; }
    public @NotNull Transform rotZ         (final float z                  ) { _lrot.rotateZ(z); return this; }
    public @NotNull Transform rot          (final @NotNull Quaternionf r   ) { _lrot.mul(r);     return this; }
    public @NotNull Transform setRot       (final @NotNull Quaternionf r   ) { _lrot.set(r);     return this; }
    public @NotNull Transform rot          (final float x, final float y, final float z) { rotX(x); rotY(y); rotZ(z); return this; }




    // Translation
    public @NotNull Transform moveX        (final float x                  ) { _pos.x += x; return this; }
    public @NotNull Transform moveY        (final float y                  ) { _pos.y += y; return this; }
    public @NotNull Transform moveZ        (final float z                  ) { _pos.z += z; return this; }
    public @NotNull Transform move         (final @NotNull Vector3f s      ) { _pos.add(s); return this; }
    public @NotNull Transform move         (final float x, final float y, final float z) { moveX(x); moveY(y); moveZ(z); return this; }

    public @NotNull Transform setPosX      (final float x                  ) { _pos.x = x;  return this; }
    public @NotNull Transform setPosY      (final float y                  ) { _pos.y = y;  return this; }
    public @NotNull Transform setPosZ      (final float z                  ) { _pos.z = z;  return this; }
    public @NotNull Transform setPos       (final @NotNull Vector3f s      ) { _pos.set(s); return this; }
    public @NotNull Transform setPos       (final float x, final float y, final float z) { setPosX(x); setPosY(y); setPosZ(z); return this; }




    // Scale
    public @NotNull Transform scaleX       (final float x                  ) { _scale.x *= x;  return this; }
    public @NotNull Transform scaleY       (final float y                  ) { _scale.y *= y;  return this; }
    public @NotNull Transform scaleZ       (final float z                  ) { _scale.z *= z;  return this; }
    public @NotNull Transform scale        (final float n                  ) { scale(n, n, n); return this; }
    public @NotNull Transform scale        (final @NotNull  Vector3f s     ) { _scale.mul(s);  return this; }
    public @NotNull Transform scale        (final float x, final float y, final float z) { scaleX(x); scaleY(y); scaleZ(z); return this; }

    public @NotNull Transform setScaleX    (final float x                  ) { _scale.x = x;       return this; }
    public @NotNull Transform setScaleY    (final float y                  ) { _scale.y = y;       return this; }
    public @NotNull Transform setScaleZ    (final float z                  ) { _scale.z = z;       return this; }
    public @NotNull Transform setScale     (final float n                   ) { setScale(n, n, n); return this; }
    public @NotNull Transform setScale     (final @NotNull Vector3f s     ) { _scale.set(s);       return this; }
    public @NotNull Transform setScale     (final float x, final float y, final float z) { setScaleX(x); setScaleY(y); setScaleZ(z); return this; }




    // Right rotation
    public @NotNull Transform rotGlobalX   (final float x                  ) { _grot.rotateX(x); return this; }
    public @NotNull Transform rotGlobalY   (final float y                  ) { _grot.rotateY(y); return this; }
    public @NotNull Transform rotGlobalZ   (final float z                  ) { _grot.rotateZ(z); return this; }
    public @NotNull Transform rotGlobal    (final @NotNull Quaternionf r   ) { _grot.mul(r);     return this; }
    public @NotNull Transform setGlobalRot (final @NotNull Quaternionf r   ) { _grot.set(r);     return this; }
    public @NotNull Transform rotGlobal    (final float x, final float y, final float z) { rotGlobalX(x); rotGlobalY(y); rotGlobalZ(z); return this; }




    // Getters
    public @NotNull Vector3f    getPos      () { return _pos;   }
    public @NotNull Quaternionf getRot      () { return _lrot;  }
    public @NotNull Vector3f    getScale    () { return _scale; }
    public @NotNull Quaternionf getGlobalRot() { return _grot;  }
}
