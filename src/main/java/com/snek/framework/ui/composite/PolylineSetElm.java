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
 * <p> Each polyline is defined by a list of 2 or more points and has a configurable thickness and color.
 */
public class PolylineSetElm extends Div {
    // final @NotNull List<@NotNull PolylineData> polylines;


    /**
     * Creates a new PolylineSetElm.
     * @param _polylines The list of polylines.
     */
    public PolylineSetElm(ServerWorld _world, final @NotNull PolylineData... _polylines) {
        super();
        // polylines = List.of(_polylines);

        // For each polyline
        for(final PolylineData l : _polylines) {

            // For each line
            final List<Vector2f> points = l.getPoints();
            for(int i = 0; i < points.size() - 1; ++i) {

                // Create a new panel element
                final PanelElm e = (PanelElm)addChild(new PanelElm(_world));
                e.setSize(new Vector2f(1f, 1f));

                // Change its color and make it a line of the specified width using a linear transformation
                final Vector2f a = points.get(i);
                final Vector2f b = points.get(i + 1);
                final Vector2f dir = b.sub(a, new Vector2f());
                final float len = dir.length();
                final float angle = (float)Math.atan2(dir.y, dir.x);
                e.applyAnimationNow(new Animation(
                    new Transition()
                    .targetBgColor(l.getColor())
                    .targetBgAlpha(l.getAlpha())
                    .additiveTransform(
                        new Transform()
                        .scale(len, l.getWidth(), 1)
                        .rotZ(angle)
                        .move((a.x + b.x) / 2, (a.y + b.y) / 2, 0)
                    )
                ));
                System.out.println(e.getEntity(CustomTextDisplay.class).getBackground());
            }
        }
    }
}
