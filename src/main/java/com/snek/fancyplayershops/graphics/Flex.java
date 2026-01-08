package com.snek.fancyplayershops.graphics;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.frameworklib.graphics.layout.Div;



/**
 * A {@link Div} that keeps its child elements centered and aligned on the specified axis.
 * <p>
 * This element always copies the dimensions of its parent element, so setting its size has no effect.
 * <p>
 * The position on the alignment axis of children is also managed by the flex element. Changing it manually can cause visual issues.
 * <p>
 * The alignment options of child elements are not affected. For the Flex to work properly, they must use alignment:NONE on the alignment axis.
 * Alignment options on the other axis can be changed freely.
 */
public class Flex extends Div {
    private final @NotNull Axis2 axis;


    public Flex(final @NotNull Axis2 axis) {
        super();
        this.axis = axis;
    }


    @Override
    public @NotNull Div addChild(final @NotNull Div elm) {
        super.addChild(elm);
        updateLayout();
        return elm;
    }


    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        updateLayout();
        super.spawn(pos, animate);
    }


    //! Override updateAbsSizeSelf to force a 1, 1 local size
    @Override
    public void updateAbsSizeSelf() {
        absSize.set(parent == null ? new Vector2f(1f) : new Vector2f(parent.getAbsSize()));
    }


    public void updateLayout() {

        // Calculate total axis-wise length
        float totLen = 0f;
        for(final Div c : children) {
            totLen += axis.get(c.getAbsSize());
        }

        // Reposition elements
        float curPos = 0f - totLen / 2f;
        for(final Div c : children) {
            final float len = axis.get(c.getLocalSize());
            final float pos = curPos + len / 2f;
            if(axis == Axis2.X) c.setPosX(pos); else c.setPosY(pos);
            curPos += len;
        }
    }
}
