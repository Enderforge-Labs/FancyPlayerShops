package com.snek.fancyplayershops.graphics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.interfaces.Scrollable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

// FIXME move to framework lib

import com.snek.frameworklib.input.ScrollReceiver;
import com.snek.frameworklib.input.HoverReceiver;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;
import com.snek.frameworklib.graphics.core.Canvas;
import com.snek.frameworklib.graphics.core.HudCanvas;
import com.snek.frameworklib.graphics.core.UiCanvas;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.core.styles.ElmStyle;






//TODO add axis methods and member
//TODO add horizontal alignment
/**
 * A Div that aligns its children on the specified axis by changing their position.
 * It also allows users to scroll through them.
 * <p>
 * Notice: In child elements, the alignment options on the axis perpendicular to the alignment axis are ignored.
 * <p>
 * Notice: The position of child elements on the alignment axis is also ignored, as it's recaculated automatically every time the list is updated.
*/
public class ScrollableList extends PanelElm implements Scrollable {

    // List data
    private final float elmSize;
    private final List<Div> listChildren = new ArrayList<>();
    // private int centralElmIndex = 0;
    // private float centralElmProgress = 0;
    private float scroll = 0;



    public ScrollableList(final @NotNull ServerLevel world, final @NotNull ElmStyle style, final float elmSize) {
        super(world, style);
        this.elmSize = elmSize;
    }
    public ScrollableList(final @NotNull ServerLevel world, final float elmSize) {
        this(world, new PanelElmStyle(), elmSize);
    }




    public Div addElm(final @NotNull Div elm) {
        return addElmAt(elm, listChildren.size());
    }
    public Div addElmAt(final @NotNull Div elm, final int index) {
        listChildren.add(index, elm);
        // elm.setPosY(getAbsSize().y - (elmSize * (index + 1)));
        elm.setSize(new Vector2f(1f, elmSize));
        refreshViewSides();
        return elm;
    }




    public Div removeElm(final @NotNull Div elm) {
        //TODO unify code?
        listChildren.remove(elm);
        refreshViewSides();
        return elm;
    }
    public Div removeElmAt(final int index) {
        //TODO unify code?
        final Div elm = listChildren.remove(index);
        refreshViewSides();
        return elm;
    }




    /**
     * Calculates the position of the element at {@code elmIndex} relative to the current scroll amount.
     * @param elmIndex The index of the element.
     * @return -1 if the element is at the left of the center of the view, 1 if it's at the right, or 0 if it's centered.
     */
    private int getElmSide(final int elmIndex) {
        //FIXME calculate the center of the screen instead of scroll.
        //FIXME scroll can be less than the center when the list scroll maxes out
        /**/ if( elmIndex      * elmSize < scroll) return -1;
        else if((elmIndex + 1) * elmSize > scroll) return  1;
        else                                       return  0;
    }




    /**
     * Recalculates the visible elements and, if needed, spawns/despawns elements near the sides of the list.
     */
    public void refreshViewSides() {
        //FIXME actually change them dynamically instead of replacing the whole thing
        if(isSpawned) {
            for(final Div elm : children) elm.despawn(false);
            clearChildren();

            final float clampedScroll = getClampedScroll();
            final int firstVisible = Math.max(0, (int)Math.floor((clampedScroll - 0.5f) / elmSize));
            final int lastVisible = Math.min(listChildren.size() - 1, (int)Math.ceil((clampedScroll + 0.5f) / elmSize));
            for(int i = firstVisible; i <= lastVisible; i++) {
                // final Div elm = super.addChild(listChildren.get(i));
                final Div elm = addChild(listChildren.get(i));
                elm.setPosY(getAbsSize().y - (elmSize * (i - firstVisible + 1)));
                elm.spawn(canvas.getContext().getSpawnPos());

                //FIXME move to frameworklib:
                //FIXME     calculate shift if spawned in huds
                //FIXME     calculate rotation everywhere
                // if(canvas instanceof HudCanvas hud) {
                //     elm.applyAnimationNowRecursive(new Transition().additiveTransform(new Transform().move(hud.__calcVisualShift())));
                // }
                elm.applyAnimationNowRecursive(Canvas.calcCanvasRotationAnimation(0, canvas.getRotation()));
            }


            // // Update child and return
            // elm.updateAbsPos();
        }
    }




    //TODO this function might need a more complex logic once refreshViewSides() gets updated
    @Override
    public void onScroll(final @NotNull Player player, final float amount) {
        scroll += amount;
        scroll = getClampedScroll();
        System.out.println(scroll);
        refreshViewSides();
    }


    public float getClampedScroll() {
        final float halfHeight = getAbsSize().y / 2;
        return Math.max(halfHeight, Math.min(scroll, listChildren.size() * elmSize - halfHeight));
    }


    @Override
    public void spawn(final @NotNull Vector3d pos) {
        if(!isSpawned) {
            super.spawn(pos);
            scroll = getClampedScroll();
            refreshViewSides();
        }
    }
}
