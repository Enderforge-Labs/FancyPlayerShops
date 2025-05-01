package com.snek.framework.data_types.animations;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import net.minecraft.util.math.AffineTransformation;








/**
 * A single transformation specified as local rotation, translation, scale and global rotation.
 * It can be converted to a Minecraft AffineTransformation.
 */
public class Transform {
    protected @NotNull Vector3f    _pos;
    protected @NotNull Quaternionf _lrot;
    protected @NotNull Vector3f    _scale;
    protected @NotNull Quaternionf _grot;


    /**
     * Creates a new Matrix4f using the current translation, local rotation, scale and global rotation values.
     * @return The transformation matric.
     */
    public Matrix4f toMatrixTransform() {
        Matrix4f m = new Matrix4f();
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
    public AffineTransformation toMinecraftTransform() {
        return new AffineTransformation(toMatrixTransform());
    }


    /**
     * Creates a new Transform with default data:
     *     No local rotation.
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
    public Transform(@NotNull Vector3f __pos, @NotNull Quaternionf __rot, @NotNull Vector3f __scale, @NotNull Quaternionf __globalRot) {
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
    public Transform set(@NotNull Transform t) {
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
    public Transform apply(Transform t) {
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
    public Transform delta(Transform t) {
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
    public Transform interpolate(Transform target, float factor) {
        _pos  .lerp (target._pos,   factor);
        _lrot .slerp(target._lrot,  factor);
        _scale.lerp (target._scale, factor);
        _grot .slerp(target._grot,  factor);
        return this;
    }




    // Left rotation
    public Transform rotX         (float x                  ) { _lrot.rotateX(x);                   return this; }
    public Transform rotY         (float y                  ) { _lrot.rotateY(y);                   return this; }
    public Transform rotZ         (float z                  ) { _lrot.rotateZ(z);                   return this; }
    public Transform rot          (float x, float y, float z) { rotX(x); rotY(y); rotZ(z);          return this; }
    public Transform rot          (Quaternionf r            ) { _lrot.mul(r);                       return this; }
    public Transform setRot       (Quaternionf r            ) { _lrot.set(r);                       return this; }




    // Translation
    public Transform moveX        (float x                  ) { _pos.x += x;                        return this; }
    public Transform moveY        (float y                  ) { _pos.y += y;                        return this; }
    public Transform moveZ        (float z                  ) { _pos.z += z;                        return this; }
    public Transform move         (float x, float y, float z) { moveX(x); moveY(y); moveZ(z);       return this; }
    public Transform move         (Vector3f s               ) { _pos.add(s);                        return this; }

    public Transform setPosX      (float x                  ) { _pos.x = x;                         return this; }
    public Transform setPosY      (float y                  ) { _pos.y = y;                         return this; }
    public Transform setPosZ      (float z                  ) { _pos.z = z;                         return this; }
    public Transform setPos       (float x, float y, float z) { setPosX(x); setPosY(y); setPosZ(z); return this; }
    public Transform setPos       (Vector3f s               ) { _pos.set(s);                        return this; }




    // Scale
    public Transform scaleX       (float x                  ) { _scale.x *= x;                            return this; }
    public Transform scaleY       (float y                  ) { _scale.y *= y;                            return this; }
    public Transform scaleZ       (float z                  ) { _scale.z *= z;                            return this; }
    public Transform scale        (float x, float y, float z) { scaleX(x); scaleY(y); scaleZ(z);          return this; }
    public Transform scale        (float n                  ) { scale(n, n, n);                           return this; }
    public Transform scale        (Vector3f s               ) { _scale.mul(s);                            return this; }

    public Transform setScaleX    (float x                  ) { _scale.x = x;                             return this; }
    public Transform setScaleY    (float y                  ) { _scale.y = y;                             return this; }
    public Transform setScaleZ    (float z                  ) { _scale.z = z;                             return this; }
    public Transform setScale     (float x, float y, float z) { setScaleX(x); setScaleY(y); setScaleZ(z); return this; }
    public Transform setScale     (float n                  ) { setScale(n, n, n);                        return this; }
    public Transform setScale     (Vector3f s               ) { _scale.set(s);                            return this; }




    // Right rotation
    public Transform rotGlobalX   (float x                  ) { _grot.rotateX(x);                                     return this; }
    public Transform rotGlobalY   (float y                  ) { _grot.rotateY(y);                                     return this; }
    public Transform rotGlobalZ   (float z                  ) { _grot.rotateZ(z);                                     return this; }
    public Transform rotGlobal    (float x, float y, float z) { rotGlobalX(x); rotGlobalY(y); rotGlobalZ(z);          return this; }
    public Transform rotGlobal    (Quaternionf r            ) { _grot.mul(r);                                         return this; }
    public Transform setGlobalRot (Quaternionf r            ) { _grot.set(r);                                         return this; }




    // Getters
    public @NotNull Vector3f    getPos      () { return _pos;   }
    public @NotNull Quaternionf getRot      () { return _lrot;  }
    public @NotNull Vector3f    getScale    () { return _scale; }
    public @NotNull Quaternionf getGlobalRot() { return _grot;  }
}
