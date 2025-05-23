package com.snek.framework.ui.composite;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.ui.Div;
import com.snek.framework.utils.Easings;

import net.minecraft.server.level.ServerLevel;








/**
 * A composite UI element that can display many polylines.
 * <p> Each polyline is defined by a list of 2 or more points and has configurable color, opacity and width.
 */
public class PolylineSetElm extends Div {
    public static final float LINE_SPAWNING_SCALE  = 0.00001f;
    public static final int   SPAWN_ANIMATION_TIME = 10;




    /**
     * Creates a new PolylineSetElm.
     * @param _polylines The list of polylines.
     */
    public PolylineSetElm(ServerLevel _world, final @NotNull PolylineData... _polylines) {
        super();

        // Create lines and add them to the children list
        for(final PolylineData l : _polylines) {
            float previousLen = 0f;
            final List<Vector2f> points = l.getPoints();
            for(int i = 0; i < points.size() - 1; ++i) {
                final Vector2f a = points.get(i);
                final Vector2f b = points.get(i + 1);
                createLine(_world, l, a, b, previousLen);
                previousLen += a.distance(b);
            }
        }
    }




    /**
     * Creates a new line and adds it to this element's children.
     * @param _world The world to spawn the display entities in.
     * @param l The polyline data that specifies color, opacity and width.
     * @param _a The first point of the line.
     * @param _b The second point of the line.
     * @param previousLen The total length of the lines that precede this line.
     */
    private void createLine(final @NotNull ServerLevel _world, final @NotNull PolylineData l, final @NotNull Vector2f _a, final @NotNull Vector2f _b, final float previousLen) {

        // Calculate the normalized direction of the line and add the new point positions taking into account the edge value
        final Vector2f normal = _b.sub(_a, new Vector2f()).normalize(new Vector2f());
        final Vector2f directionalEdge = normal.mul(l.getEdge(), new Vector2f());
        final Vector2f a = _a.sub(directionalEdge, new Vector2f());
        final Vector2f b = _b.add(directionalEdge, new Vector2f());


        // Calculate line direction, length and angle
        final Vector2f dir    = b.sub(a, new Vector2f());                   // The direction of the line
        final float    len    = dir.length();                               // The length of the line
        final float    angle  = (float)Math.atan2(dir.y, dir.x);            // The angle of the line


        // Create the panel and set its size and position
        final LinePanel e = (LinePanel)addChild(new LinePanel(_world));     // Create the panel
        e.setSize(new Vector2f(len, l.getWidth()));                         // Set the size to match the line's length and width
        e.setPos(                                                           // Set the position
            new Vector2f(a)                                                     // Start by moving the origin (center of lower edge) the first point
            .add(new Vector2f(-(1 - len) / 2f, 0f))                             // Move it horizontally to align the bottom left edge with the point
            .add(new Vector2f(normal.y, -normal.x).mul(l.getWidth() / 2f))      // Center the line to its width
        );


        // Change its color and rotate it by overwriting the primer animation and spawning animation
        e.getStyle().setPrimerAnimation(new Animation(
            new Transition()
            .targetBgColor(l.getColor())
            .targetBgAlpha(l.getAlpha())
            .additiveTransform(new Transform().rotZ(angle).scaleX(LINE_SPAWNING_SCALE))
        ));
        final float waitTime      = SPAWN_ANIMATION_TIME / (l.getTotLen() / previousLen);
        final float animationTime = SPAWN_ANIMATION_TIME / (l.getTotLen() / len);
        e.getStyle().setSpawnAnimation(new Animation(
            new Transition(            (int)(waitTime     ),  Easings.linear),
            new Transition(Math.max(1, (int)(animationTime)), Easings.sineOut)
            .additiveTransform(new Transform().scaleX(1f / LINE_SPAWNING_SCALE))
        ));
    }
}
