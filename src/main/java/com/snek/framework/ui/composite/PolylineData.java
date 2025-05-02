package com.snek.framework.ui.composite;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector4i;








/**
 * This class identifies a single polyline.
 */
public class PolylineData {

    // Data
    private final @NotNull Vector4i color;
    private final float width;
    private final @NotNull List<@NotNull Vector2f> points;

    // Getters
    public @NotNull Vector4i                getColor () { return color;  }
    public float                            getWidth () { return width;  }
    public @NotNull List<@NotNull Vector2f> getPoints() { return points; }


    /**
     * Creates a new PolylineData.
     * @param _color The color of the line.
     * @param _width The width of the line.
     * @param _points The list of points that defined the line's segments.
     */ //FIXME force at least 2 vectors
    public PolylineData(final @NotNull Vector4i _color, final float _width, final @NotNull Vector2f... _points) {
        color = _color;
        width = _width;
        points = List.of(_points);
    }
}
