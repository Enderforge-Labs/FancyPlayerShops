package com.snek.framework.ui.composite;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.displays.CustomTextDisplay;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.PanelElm;

import net.minecraft.server.world.ServerWorld;








/**
 * A composite UI element that can display many polylines.
 * <p> Each polyline is defined by a list of 2 or more points and has configurable color, opacity and width.
 */
public class PolylineSetElm extends Div {


    /**
     * Creates a new PolylineSetElm.
     * @param _polylines The list of polylines.
     */
    public PolylineSetElm(ServerWorld _world, final @NotNull PolylineData... _polylines) {
        super();

        // Create lines and add them to the children list
        for(final PolylineData l : _polylines) {
            final List<Vector2f> points = l.getPoints();
            for(int i = 0; i < points.size() - 1; ++i) {
                createLine(_world, l, points.get(i), points.get(i + 1));
            }
        }
    }




    /**
     * Creates a new line and adds it to this element's children.
     * @param _world The world to spawn the display entities in.
     * @param l The polyline data that specifies color, opacity and width.
     * @param a The first point of the line.
     * @param b The second point of the line.
     */
    private void createLine(final @NotNull ServerWorld _world, final @NotNull PolylineData l, final @NotNull Vector2f a, final @NotNull Vector2f b) {

        // Calculate line direction, length and angle
        final Vector2f dir    = b.sub(a, new Vector2f());           // The direction of the line
        final Vector2f normal = dir.normalize(new Vector2f());      // The normalized direction of the line
        final float    len    = dir.length();                       // The length of the lines
        final float    angle  = (float)Math.atan2(dir.y, dir.x);    // The angle of the line


        // Create the panel and set its size and position
        final PanelElm e = (PanelElm) addChild(new PanelElm(_world));
        e.setSize(new Vector2f(len, l.getWidth()));
        e.setPos(a.add(b, new Vector2f()).mul(0.5f).add(normal.mul(-len / 2f, new Vector2f())));
        e.move(new Vector2f(normal.y, -normal.x).mul(l.getWidth() / 2f));
        //! ^ Adjust position (account for the width)


        // Change its color and rotate it by overwriting the primer animation
        e.getStyle().setPrimerAnimation(new Animation(
            new Transition()
            .targetBgColor(l.getColor())
            .targetBgAlpha(l.getAlpha())
            .additiveTransform(new Transform().rotZ(angle))
        ));
    }
}
