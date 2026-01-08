package com.snek.fancyplayershops.graphics;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;




public enum Axis2 {
    X, Y;

    public long toIndex() {
        return switch(this) {
            case X -> 0;
            case Y -> 1;
        };
    }

    public @NotNull String getName() {
        return switch(this) {
            case X -> "X";
            case Y -> "Y";
        };
    }

    public @NotNull Axis3 toAxis3() {
        return switch(this) {
            case X -> Axis3.X;
            case Y -> Axis3.Y;
        };
    }

    public float get(final @NotNull Vector2f v) {
        return switch(this) {
            case X -> v.x;
            case Y -> v.y;
        };
    }

    public double get(final @NotNull Vector2d v) {
        return switch(this) {
            case X -> v.x;
            case Y -> v.y;
        };
    }

    public int get(final @NotNull Vector2i v) {
        return switch(this) {
            case X -> v.x;
            case Y -> v.y;
        };
    }

    public float get(final @NotNull Vector3f v) {
        return switch(this) {
            case X -> v.x;
            case Y -> v.y;
        };
    }

    public double get(final @NotNull Vector3d v) {
        return switch(this) {
            case X -> v.x;
            case Y -> v.y;
        };
    }

    public int get(final @NotNull Vector3i v) {
        return switch(this) {
            case X -> v.x;
            case Y -> v.y;
        };
    }

    @SuppressWarnings("java:S1301")
    public void set(final @NotNull Vector2f v, final float n) {
        switch(this) {
            case X: v.x = n; break;
            case Y: v.y = n; break;
        }
    }

    @SuppressWarnings("java:S1301")
    public void set(final @NotNull Vector2d v, final double n) {
        switch(this) {
            case X: v.x = n; break;
            case Y: v.y = n; break;
        }
    }

    @SuppressWarnings("java:S1301")
    public void set(final @NotNull Vector2i v, final int n) {
        switch(this) {
            case X: v.x = n; break;
            case Y: v.y = n; break;
        }
    }

    @SuppressWarnings("java:S1301")
    public void set(final @NotNull Vector3f v, final float n) {
        switch(this) {
            case X: v.x = n; break;
            case Y: v.y = n; break;
        }
    }

    @SuppressWarnings("java:S1301")
    public void set(final @NotNull Vector3d v, final double n) {
        switch(this) {
            case X: v.x = n; break;
            case Y: v.y = n; break;
        }
    }

    @SuppressWarnings("java:S1301")
    public void set(final @NotNull Vector3i v, final int n) {
        switch(this) {
            case X: v.x = n; break;
            case Y: v.y = n; break;
        }
    }

    public @NotNull Vector2f toVector2f() {
        return switch(this) {
            case X -> new Vector2f(1f, 0f);
            case Y -> new Vector2f(0f, 1f);
        };
    }

    public @NotNull Vector2d toVector2d() {
        return switch(this) {
            case X -> new Vector2d(1d, 0d);
            case Y -> new Vector2d(0d, 1d);
        };
    }

    public @NotNull Vector2i toVector2i() {
        return switch(this) {
            case X -> new Vector2i(1, 0);
            case Y -> new Vector2i(0, 1);
        };
    }

    public @NotNull Vector3f toVector3f() {
        return switch(this) {
            case X -> new Vector3f(1f, 0f, 0f);
            case Y -> new Vector3f(0f, 1f, 0f);
        };
    }

    public @NotNull Vector3d toVector3d() {
        return switch(this) {
            case X -> new Vector3d(1d, 0d, 0f);
            case Y -> new Vector3d(0d, 1d, 0f);
        };
    }

    public @NotNull Vector3i toVector3i() {
        return switch(this) {
            case X -> new Vector3i(1, 0, 0);
            case Y -> new Vector3i(0, 1, 0);
        };
    }
}
