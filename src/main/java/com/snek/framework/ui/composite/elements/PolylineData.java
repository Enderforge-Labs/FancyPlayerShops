package com.snek.framework.ui.composite.elements;

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
    private final float edge;
    private final @NotNull List<@NotNull Vector2f> points;
    private final float totLen;

    // Getters
    public @NotNull Vector3i                getColor () { return color;  }
    public int                              getAlpha () { return alpha;  }
    public float                            getWidth () { return width;  }
    public float                            getEdge  () { return edge;   }
    public @NotNull List<@NotNull Vector2f> getPoints() { return points; }
    public float                            getTotLen() { return totLen; }


    /**
     * Creates a new PolylineData.
     * @param _color The color of the line.
     * @param _alpha The opacity of the line.
     * @param _width The width of the line.
     * @param _edge  The additional width to add on each end of each line
     * @param _point1 The list of points that defines the line's segments (first point).
     * @param _point2 The list of points that defines the line's segments (second point).
     * @param _points The list of points that defines the line's segments (additional points, optional).
     */
    public PolylineData(final @NotNull Vector3i _color, final int _alpha, final float _width, final float _edge, final @NotNull Vector2f _point1, final @NotNull Vector2f _point2, final @NotNull Vector2f... _points) {

        // Save basic data
        color = _color;
        alpha = _alpha;
        width = _width;
        edge = _edge;

        // Save points
        points = new ArrayList<>(_points.length + 2);
        points.add(_point1);
        points.add(_point2);
        Collections.addAll(points, _points);

        // Calculate total length
        float _totLen = 0;
        for(int i = 0; i < points.size() - 1; ++i) {
            _totLen += points.get(i).distance(points.get(i + 1));
        }
        totLen = _totLen;
    }
}
