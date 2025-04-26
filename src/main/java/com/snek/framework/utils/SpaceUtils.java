package com.snek.framework.utils;

import java.awt.Polygon;
import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.snek.framework.data_types.containers.Pair;
import com.snek.framework.debug.DebugCheck;
import com.snek.framework.debug.UiDebugWindow;

import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;








/**
 * A utility class providing functions for 2D and 3D Euclidean geometry calculations.
 */
public abstract class SpaceUtils {
    private SpaceUtils(){}


    /**
     * Checks whether a line intersects a sphere.
     * The line is assumed to be infinite in both directions, regardless of its length.
     * @param lineOrigin The starting point of the line.
     * @param lineDirection The direction of the line. Must be normalized.
     * @param targetCenter The center of the target sphere.
     * @param targetRadius The radius of the target sphere. Must be positive.
     * @return True if the line intersects the sphere, false otherwise.
     */
    public static boolean checkLineSphereIntersection(Vector3f lineOrigin, Vector3f lineDirection, Vector3f targetCenter, float targetRadius) {

        // Calculate the direction vector from lineOrigin to targetCenter
        Vector3f toCenter = new Vector3f(targetCenter).sub(lineOrigin);

        // Find the point on the line that is closest to targetCenter
        float t = toCenter.dot(lineDirection);
        Vector3f closestPoint = new Vector3f(lineDirection).mul(t).add(lineOrigin);

        // Calculate its distance from targetCenter and return true if it's smaller than the radiud
        return targetCenter.distanceSquared(closestPoint) <= targetRadius * targetRadius;
    }




    /**
     * Checks whether a line intersects a rectangle in a 3D space.
     * The line is assumed to be infinite in both directions, regardless of its length.
     * @param lineOrigin The starting point of the line.
     * @param lineDirection The direction of the line. Must be normalized.
     * @param corners The four corners of the rectangle.
     * @return True if the line intersects the rectangle, false otherwise.
     */
    public static boolean checkLineRectangleIntersection(Vector3f lineOrigin, Vector3f lineDirection, Vector3f[] corners) {

        // Calculate corner coordinates when projected onto the screen's plane. Use lineOrigin as coordinates origin and lineDirection
        Quaternionf negativeDir = new Quaternionf();
        lineDirection.rotationTo(new Vector3f(0, 0, 1), negativeDir);
        Vector3f c1 = new Vector3f(corners[0]).sub(lineOrigin).rotate(negativeDir);
        Vector3f c2 = new Vector3f(corners[1]).sub(lineOrigin).rotate(negativeDir);
        Vector3f c3 = new Vector3f(corners[2]).sub(lineOrigin).rotate(negativeDir);
        Vector3f c4 = new Vector3f(corners[3]).sub(lineOrigin).rotate(negativeDir);

        // //! Debugging window draws
        if(DebugCheck.isDebug()) {
            UiDebugWindow.getW().add(new Vector2f(c1.x, c1.y));
            UiDebugWindow.getW().add(new Vector2f(c2.x, c2.y));
            UiDebugWindow.getW().add(new Vector2f(c3.x, c3.y));
            UiDebugWindow.getW().add(new Vector2f(c4.x, c4.y));
        }

        // Check intersection on the projection plane
        return isPointInQuad(new Vector2f(0, 0), new Vector2f[]{
            new Vector2f(c1.x, c1.y),
            new Vector2f(c2.x, c2.y),
            new Vector2f(c3.x, c3.y),
            new Vector2f(c4.x, c4.y)
        });
    }




    /**
     * Checks whether a point is within the quadrilateral polygon defined by the list of vertices.
     * @param point The coordinates of  the point.
     * @param quad A list of 4 vectors identifying the corners of the polygon.
     * @return True if the point is within the polygon, false otherwise.
     */
    static boolean isPointInQuad(@NotNull Vector2f point, @NotNull Vector2f[] quad) {
        int sign = 0;
        for(int i = 0; i < 4; i++) {
            Vector2f a = quad[i];
            Vector2f b = quad[(i + 1) % 4];
            float cross = (b.x - a.x) * (point.y - a.y) - (b.y - a.y) * (point.x - a.x);
            if(cross != 0) {
                int newSign = cross > 0 ? 1 : -1;
                if(sign == 0) sign = newSign;
                else if(newSign != sign) return false;
            }
        }
        return true;
    }
}
