package com.snek.framework.ui.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3i;








/**
 * This class identifies a single polyline.
 */
public class PolylineData {

    // Data
    private final @NotNull Vector3i color;
    private final int alpha;
    private final float width;
    private final @NotNull List<@NotNull Vector2f> points;

    // Getters
    public @NotNull Vector3i                getColor () { return color;  }
    public int                              getAlpha () { return alpha;  }
    public float                            getWidth () { return width;  }
    public @NotNull List<@NotNull Vector2f> getPoints() { return points; }


    /**
     * Creates a new PolylineData.
     * @param _color The color of the line.
     * @param _alpha The opacity of the line.
     * @param _width The width of the line.
     * @param _point1 The list of points that defines the line's segments (first point).
     * @param _point2 The list of points that defines the line's segments (second point).
     * @param _points The list of points that defines the line's segments (additional points, optional).
     */
    public PolylineData(final @NotNull Vector3i _color, final int _alpha, final float _width, final @NotNull Vector2f _point1, final @NotNull Vector2f _point2, final @NotNull Vector2f... _points) {
        color = _color;
        alpha = _alpha;
        width = _width;

        points = new ArrayList<>(_points.length + 2);
        points.add(_point1);
        points.add(_point2);
        Collections.addAll(points, _points);
    }
}
