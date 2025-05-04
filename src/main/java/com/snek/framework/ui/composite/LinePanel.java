package com.snek.framework.ui.composite;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.framework.ui.elements.PanelElm;

import net.minecraft.server.world.ServerWorld;








public final class LinePanel extends PanelElm {
    final Vector2f absPosOg = new Vector2f();


    public LinePanel(final @NotNull ServerWorld _world) {
        super(_world);
    }




    @Override
    protected final void updateAbsPosSelf() {
        // super.updateAbsPosSelf();

        // Calculate unrestricted position
        final Vector2f p = parent == null ? new Vector2f(0, 0) : parent.getAbsPos();
        final Vector2f s = parent == null ? new Vector2f(1, 1) : parent.getAbsSize();

        // Apply horizontal alignment
        final float x = switch(alignmentX) {
            case LEFT   -> p.x - (s.x - absSize.x) / 2;
            case RIGHT  -> p.x + (s.x - absSize.x) / 2;
            case CENTER -> p.x;
            case NONE   -> p.x + localPos.x * s.x;
        };

        // Apply vertical alignment
        final float y = switch(alignmentY) {
            case TOP    -> p.y + (s.y - absSize.y);
            case BOTTOM -> p.y;
            case CENTER -> p.y + (s.y - absSize.y) / 2;
            case NONE   -> p.y + localPos.y * s.y;
        };

        // Update the value
        absPos.set(x, y);
    }




    // @Override
    // public void setPos(final @NotNull Vector2f _pos) {
    //     localPosOg.set(_pos);
    //     super.setPos(_pos);
    // }

    // @Override
    // public void setPosX(final float x) {
    //     localPosOg.x = x;
    //     super.setPosX(x);
    // }

    // @Override
    // public void setPosY(final float y) {
    //     localPosOg.y = y;
    //     super.setPosY(y);
    // }

    // @Override
    // public void move(final @NotNull Vector2f _pos) {
    //     localPosOg.add(_pos);
    //     super.move(_pos);
    // }

    // @Override
    // public void moveX(final float x) {
    //     localPosOg.x += x;
    //     super.moveX(x);
    // }

    // @Override
    // public void moveY(final float y) {
    //     localPosOg.y += y;
    //     super.moveY(y);
    // }
}
