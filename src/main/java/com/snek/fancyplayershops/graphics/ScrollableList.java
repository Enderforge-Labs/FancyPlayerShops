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

import net.minecraft.world.entity.player.Player;

// FIXME move to framework lib

import com.snek.frameworklib.input.ScrollReceiver;
import com.snek.frameworklib.input.HoverReceiver;
import com.snek.frameworklib.graphics.core.HudCanvas;






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
public class ScrollableList extends Div implements Scrollable {

    // List data
    private final float elmSize;
    private final List<Div> listChildren = new ArrayList<>();
    // private int centralElmIndex = 0;
    // private float centralElmProgress = 0;
    private float scroll = 0;



    public ScrollableList(final float elmSize) {
        super();
        this.elmSize = elmSize;
    }




    @Override
    public Div addChild(final @NotNull Div elm) {
        return addChildAt(elm, listChildren.size());
    }
    public Div addChildAt(final @NotNull Div elm, final int index) {
        listChildren.add(index, elm);
        elm.setPosY(elmSize * index); //FIXME y pos starts from the bottom? i think?
        elm.setSize(new Vector2f(1f, elmSize));
        refreshViewSides();
        return elm;
    }




    @Override
    public Div removeChild(final @NotNull Div elm) {
        //TODO unify code?
        listChildren.remove(elm);
        refreshViewSides();
        return elm;
    }
    public Div removeChildAt(final int index) {
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

            final int firstVisible = Math.max(0, (int)Math.floor((scroll - 0.5f) / elmSize));
            final int lastVisible = Math.min(listChildren.size() - 1, (int)Math.ceil((scroll + 0.5f) / elmSize));
            for(int i = firstVisible; i <= lastVisible; i++) {
                final Div elm = super.addChild(listChildren.get(i));
                elm.spawn(canvas.getContext().getSpawnPos());
            }


            // // Update child and return
            // elm.updateAbsPos();
        }
    }




    //TODO this function might need a more complex logic once refreshViewSides() gets updated
    @Override
    public void onScroll(final @NotNull Player player, final float amount) {
        scroll += amount;
        refreshViewSides();
    }


    @Override
    public void spawn(final @NotNull Vector3d pos) {
        super.spawn(pos);
        refreshViewSides();
    }
}
