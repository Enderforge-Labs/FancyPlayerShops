package com.snek.framework.ui;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.ui.interfaces.Clickable;
import com.snek.framework.ui.interfaces.Hoverable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
















/**
 * The most basic UI element. It can contain and manage any amount of UI elements.
 * <p> By default, divs are invisible and don't exist in the minecraft world nor on the client.
 *     They have a 2D size, a 2D position and alignment options.
 */
public class Div {


    // Tree data
    protected @Nullable Div parent = null;
    protected final @NotNull List<@NotNull Div> children = new ArrayList<>();
    public void setParent(final @Nullable Div _parent) { parent = _parent; }
    public @Nullable Div getParent() { return parent; }




    // UI data
    protected final @NotNull Vector2f   localSize  = new Vector2f(1, 1);
    protected final @NotNull Vector2f   localPos   = new Vector2f(0, 0);

    protected final @NotNull Vector2f   absSize    = new Vector2f(1, 1);
    protected final @NotNull Vector2f   absPos     = new Vector2f(0, 0);

    protected @NotNull AlignmentX alignmentX = AlignmentX.NONE;
    protected @NotNull AlignmentY alignmentY = AlignmentY.NONE;

    protected int zIndex = 0;




    /**
     * Creates a new Div with size (1,1)
     */
    public Div() {
        // Empty
    }




    /**
     * Adds a child to this Div.
     * @param elm The new element.
     * @return elm
     */
    public Div addChild(final @NotNull Div elm) {
        elm.parent = this;
        elm.updateAbsPos();
        elm.updateZIndex();
        children.add(elm);
        return elm;
    }

    /**
     * Removes a child from this Div.
     * @param elm The removed element.
     * @return elm
     */
    public Div removeChild(final @NotNull Div elm) {
        elm.parent = null;
        elm.updateAbsPos();
        elm.updateZIndex();
        children.remove(elm);
        return elm;
    }

    /**
     * Removes all children from this Div.
     */
    public void clearChildren() {
        for(final Div elm : children) {
            elm.parent = null;
            elm.updateAbsPos();
            elm.updateZIndex();
        }
        children.clear();
    }

    /**
     * Returns the list of children.
     * @return The list of children.
     */
    public @NotNull List<@NotNull Div> getChildren() {
        return children;
    }




    /**
     * Applies an animation to this element.
     * <p> Partial steps at the end of the animation are expanded to cover the entire step.
     * @param animation The animation to apply.
     */
    public void applyAnimation(final @NotNull Animation animation) {
        // Empty
    }
    /**
     * Applies a transition to this element.
     * <p> Partial steps at the end of the transition are expanded to cover the entire step.
     * @param transition The transition to apply.
     */
    public final void applyAnimation(final @NotNull Transition transition) {
        applyAnimation(new Animation(transition));
    }




    /**
     * Instantly applies an animation to this element, ignoring transition times and easings.
     * @param animation The animation to apply.
     */
    public void applyAnimationNow(final @NotNull Animation animation) {
        // Empty
    }
    /**
     * Instantly applies a transition to this element, ignoring transition times and easings.
     * @param transition The transition to apply.
     */
    public final void applyAnimationNow(final @NotNull Transition transition) {
        applyAnimationNow(new Animation(transition));
    }




    /**
     * Applies an animation to this element and all of its children.
     * <p> Partial steps at the end of the animation are expanded to cover the entire step.
     * @param animation The animation to apply.
     */
    public final void applyAnimationRecursive(final @NotNull Animation animation) {
        applyAnimation(animation);
        for(final Div elm : children) {
            elm.applyAnimationRecursive(animation);
        }
    }
    /**
     * Applies a transition to this element and all of its children.
     * <p> Partial steps at the end of the transition are expanded to cover the entire step.
     * @param transition The transition to apply.
     */
    public final void applyAnimationRecursive(final @NotNull Transition transition) {
        applyAnimationRecursive(new Animation(transition));
    }




    /**
     * Instantly applies an animation to this element and all of its children, ignoring transition times and easings.
     * @param animation The animation to apply.
     */
    public final void applyAnimationNowRecursive(final @NotNull Animation animation) {
        applyAnimationNow(animation);
        for(final Div elm : children) {
            elm.applyAnimationNowRecursive(animation);
        }
    }
    /**
     * Instantly applies a transition to this element and all of its children, ignoring transition times and easings.
     * @param transition The transition to apply.
     */
    public final void applyAnimationNowRecursive(final @NotNull Transition transition) {
        applyAnimationNowRecursive(new Animation(transition));
    }




    /**
     * Forwards a click to this element and all its children.
     * <p> This method stops at the first clickable element that consumes a click.
     * @param player The player that clicked.
     * @param clickType The type of click.
     * @return Whether the click was accepted by this element.
     */
    public boolean forwardClick(final @NotNull Player player, final @NotNull ClickAction clickType) {
        if(this instanceof Clickable e && e.attemptClick(player, clickType)) {
            e.onClick(player, clickType);
            return true;
        }
        final List<Div> _children = new ArrayList<>(children);
        for(final Div elm : _children) {
            if(elm.forwardClick(player, clickType)) return true;
        }
        return false;
    }




    /**
     * Finds the element that the player is looking at.
     * <p> This method skips non-Elm elements and elements that are both not hoverable and not clickable.
     * @param player The player.
     * @return The element being looked at, or null if the player isnt looking at any of the elements.
     */
    public @Nullable Elm findTargetedElement(final @NotNull Player player) {
        if((this instanceof Hoverable || this instanceof Clickable) && this instanceof Elm e) {
            if(e.checkIntersection(player)) return e;
        }
        for(final Div elm : children) {
            final Elm r = elm.findTargetedElement(player);
            if(r != null) return r;
        }
        return null;
    }




    /**
     * Spawns the element and its associated entities into the world.
     * @param pos The position of the spawned entities.
     */
    public void spawn(final @NotNull Vector3d pos) {
        for(final Div elm : children) {
            elm.spawn(pos);
        }
    }




    /**
     * Removes the element and its associated entities from the world.
     */
    public void despawn() {
        for(final Div elm : children) {
            elm.despawn();
        }
    }




    /**
     * Instantly removes the entities associated with this element from the world.
     */
    public void despawnNow() {
        for(final Div elm : children) {
            elm.despawnNow();
        }
    }








    /**
     * Updates the absolute size of this element
     * using the parent's absolute size and the element's local size.
     */
    public void updateAbsSizeSelf() {
        absSize.set(parent == null ? localSize : new Vector2f(parent.getAbsSize()).mul(localSize));
        updateAbsPosSelf();
    }
    /**
     * Updates the local size of this element based on the current absolute size.
     */
    public void updateAbsSizeInverseSelf() {
        localSize.set(parent == null ? absSize : new Vector2f(absSize).div(parent.getAbsSize()));
        updateAbsPosSelf();
    }


    /**
     * Updates the absolute size of this element and its children, recursively,
     * using the parent's absolute size and the element's local size.
     */
    public void updateAbsSize() {
        updateAbsSizeSelf();
        for(final Div c : children) c.updateAbsSize();
    }
    /**
     * Updates the local size of this element based on the current absolute size, then updates the absolute size of its children, recursively.
     */
    public void updateAbsSizeInverse() {
        updateAbsSizeInverseSelf();
        for(final Div c : children) c.updateAbsSize();
    }


    public void setSize(final @NotNull Vector2f _size) {
        localSize.set(_size);
        updateAbsSize();
    }

    public void setSizeX(final float x) {
        localSize.x = x;
        updateAbsSize();
    }

    public void setSizeY(final float y) {
        localSize.y = y;
        updateAbsSize();
    }

    public void setAbsSize(final @NotNull Vector2f _size) {
        absSize.set(_size);
        updateAbsSizeInverse();
    }

    public void setAbsSizeX(final float x) {
        absSize.x = x;
        updateAbsSizeInverse();
    }

    public void setAbsSizeY(final float y) {
        absSize.y = y;
        updateAbsSizeInverse();
    }

    public void scale(final @NotNull Vector2f _size) {
        localSize.mul(_size);
        updateAbsSize();
    }

    public void scaleX(final float x) {
        localSize.x *= x;
        updateAbsSize();
    }

    public void scaleY(final float y) {
        localSize.y *= y;
        updateAbsSize();
    }




    /**
     * Updates the Z-index value of this element
     * using the parent's Z-index and Z-layer count.
     */
    protected void updateZIndexSelf() {
        zIndex = parent == null ? 0 : parent.zIndex + parent.getLayerCount();
    }

    /**
     * Updates the Z-index value of this element and its children, recursively,
     * using the parent's Z-index and Z-layer count.
     */
    protected void updateZIndex() {
        updateZIndexSelf();
        for(final Div c : children) c.updateZIndex();
    }

    /**
     * Returns the Z-index of this element.
     * @return The Z-index.
     */
    public int getZIndex() {
        return zIndex;
    }

    /**
     * Returns the amount of Z-Layers occupied by this element.
     * @return The amount of Z-Layers.
     */
    public int getLayerCount() {
        return 0;
    }




    /**
     * Updates the absolute position of this element
     * using the parent's absolute position and the element's local position and alignment.
     */
    protected void updateAbsPosSelf() {

        // Calculate unrestricted position
        final Vector2f p = parent == null ? new Vector2f(0, 0) : parent.getAbsPos();
        final Vector2f s = parent == null ? new Vector2f(1, 1) : parent.getAbsSize();

        // Apply horizontal alignment
        final float x = switch(alignmentX) {
            case LEFT   -> p.x - (s.x - absSize.x) / 2;
            case RIGHT  -> p.x + (s.x - absSize.x) / 2;
            case CENTER -> p.x;
            case NONE   -> p.x + localPos.x;
        };

        // Apply vertical alignment
        final float y = switch(alignmentY) {
            case TOP    -> p.y + (s.y - absSize.y);
            case BOTTOM -> p.y;
            case CENTER -> p.y + (s.y - absSize.y) / 2;
            case NONE   -> p.y + localPos.y;
        };

        // Update the value
        absPos.set(x, y);
    }


    /**
     * Updates the absolute position of this element and its children, recursively,
     * using the parent's absolute position and the element's local position and alignment.
     */
    protected void updateAbsPos() {
        updateAbsPosSelf();
        for(Div c : children) c.updateAbsPos();
    }


    public void setPos(final @NotNull Vector2f _pos) {
        localPos.set(_pos);
        updateAbsPos();
    }

    public void setPosX(final float x) {
        localPos.x = x;
        updateAbsPos();
    }

    public void setPosY(final float y) {
        localPos.y = y;
        updateAbsPos();
    }

    public void move(final @NotNull Vector2f _pos) {
        localPos.add(_pos);
        updateAbsPos();
    }

    public void moveX(final float x) {
        localPos.x += x;
        updateAbsPos();
    }

    public void moveY(final float y) {
        localPos.y += y;
        updateAbsPos();
    }




    public void setAlignmentX(final @NotNull AlignmentX _alignmentX) {
        alignmentX = _alignmentX;
        updateAbsPos();
    }

    public void setAlignmentY(final @NotNull AlignmentY _alignmentY) {
        alignmentY = _alignmentY;
        updateAbsPos();
    }

    public void setAlignment(final @NotNull AlignmentX _alignmentX, final @NotNull AlignmentY _alignmentY) {
        alignmentX = _alignmentX;
        alignmentY = _alignmentY;
        updateAbsPos();
    }




    // Getters
    public @NotNull Vector2f   getLocalSize () { return localSize;  }
    public @NotNull Vector2f   getLocalPos  () { return localPos;   }
    public @NotNull Vector2f   getAbsSize   () { return absSize;  }
    public @NotNull Vector2f   getAbsPos    () { return absPos;   }
    public @NotNull AlignmentX getAlignmentX() { return alignmentX; }
    public @NotNull AlignmentY getAlignmentY() { return alignmentY; }
}
