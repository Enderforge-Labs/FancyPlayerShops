package com.snek.fancyplayershops.graphics;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;




public enum Axis3 {
    X, Y, Z;

    public long toIndex() {
        return switch(this) {
            case X -> 0;
            case Y -> 1;
            case Z -> 2;
        };
    }

    public @NotNull String getName() {
        return switch(this) {
            case X -> "X";
            case Y -> "Y";
            case Z -> "Z";
        };
    }

    public float get(final @NotNull Vector3f v) {
        return switch(this) {
            case X -> v.x;
            case Y -> v.y;
            case Z -> v.z;
        };
    }

    public double get(final @NotNull Vector3d v) {
        return switch(this) {
            case X -> v.x;
            case Y -> v.y;
            case Z -> v.z;
        };
    }

    public int get(final @NotNull Vector3i v) {
        return switch(this) {
            case X -> v.x;
            case Y -> v.y;
            case Z -> v.z;
        };
    }

    public void set(final @NotNull Vector3f v, final float n) {
        switch(this) {
            case X: v.x = n; break;
            case Y: v.y = n; break;
            case Z: v.z = n; break;
        }
    }

    public void set(final @NotNull Vector3d v, final double n) {
        switch(this) {
            case X: v.x = n; break;
            case Y: v.y = n; break;
            case Z: v.z = n; break;
        }
    }

    public void set(final @NotNull Vector3i v, final int n) {
        switch(this) {
            case X: v.x = n; break;
            case Y: v.y = n; break;
            case Z: v.z = n; break;
        }
    }

    public @NotNull Vector3f toVector3f() {
        return switch(this) {
            case X -> new Vector3f(1f, 0f, 0f);
            case Y -> new Vector3f(0f, 1f, 0f);
            case Z -> new Vector3f(0f, 0f, 1f);
        };
    }

    public @NotNull Vector3d toVector3d() {
        return switch(this) {
            case X -> new Vector3d(1d, 0d, 0d);
            case Y -> new Vector3d(0d, 1d, 0d);
            case Z -> new Vector3d(0d, 0d, 1d);
        };
    }

    public @NotNull Vector3i toVector3i() {
        return switch(this) {
            case X -> new Vector3i(1, 0, 0);
            case Y -> new Vector3i(0, 1, 0);
            case Z -> new Vector3i(0, 0, 1);
        };
    }
}
