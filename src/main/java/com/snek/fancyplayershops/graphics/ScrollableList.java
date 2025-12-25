package com.snek.fancyplayershops.graphics;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.graphics.interfaces.Scrollable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;

import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.basic.styles.PanelElmStyle;



//TODO don't use an entity instead of making it invisible. To improve performance







// FIXME move to framework lib


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
    public @NotNull ScrollableListStyle getThisStyle() {
        return getStyle(ScrollableListStyle.class);
    }

    // Bar elements
    public static final float THUMB_MIN_HEIGHT = 0.05f;
    private final @NotNull PanelElm barThumb;
    private final @NotNull PanelElm barTrack;

    // List data
    private final @NotNull Div elmContainer;
    private final float elmSize;
    private final List<Div> elmList = new ArrayList<>();
    private float scroll = 0;



    final float tmp_bar_width = 0.01f; //TODO
    public ScrollableList(final @NotNull ServerLevel level, final @NotNull ScrollableListStyle style, final float elmSize) {
        super(level, style);
        this.elmSize = elmSize;

        // Add element container
        elmContainer = addChild(new Div());
        elmContainer.setSize(new Vector2f(1 - tmp_bar_width, 1));
        elmContainer.setAlignment(AlignmentX.LEFT, AlignmentY.BOTTOM);

        // Add bar track
        barTrack = (PanelElm)addChild(new PanelElm(level, getThisStyle().getTrackStyleReference()));
        barTrack.setSize(new Vector2f(tmp_bar_width, 1)); //FIXME make width customizable
        barTrack.setAlignment(AlignmentX.RIGHT, AlignmentY.BOTTOM);

        // Add bar thumb
        barThumb = (PanelElm)barTrack.addChild(new PanelElm(level, getThisStyle().getThumbStyleReference()));
        barThumb.setSizeX(3);
        barThumb.setSizeY(0.05f);   //! Actual height is set dynamically
        barThumb.setPosY(0);        //! Actual y position is set dynamically
        barThumb.setAlignmentX(AlignmentX.CENTER);

    }
    public ScrollableList(final @NotNull ServerLevel level, final float elmSize) {
        this(level, new ScrollableListStyle(), elmSize);
    }




    public Div storeElm(final @NotNull Div elm) {
        return addElmAt(elm, elmList.size());
    }
    public Div addElmAt(final @NotNull Div elm, final int index) {
        elmList.add(index, elm);
        refreshViewSides(false);
        return elm;
    }




    public Div removeElm(final @NotNull Div elm) {
        //TODO unify code?
        elmList.remove(elm);
        refreshViewSides(false);
        return elm;
    }
    public Div removeElmAt(final int index) {
        //TODO unify code?
        final Div elm = elmList.remove(index);
        refreshViewSides(false);
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
    public void refreshViewSides(final boolean instant) {

        //FIXME actually change them dynamically instead of replacing the whole thing

        if(isSpawned) {

            // Despawn current elements
            for(final Div elm : elmContainer.getChildren()) elm.despawn(false);
            elmContainer.clearChildren();


            // If the stash is not empty
            if(!elmList.isEmpty()) { //FIXME remove this if we are removing elements dynamically. not needed if we aren't respawning everything

                // Calculate clamped scroll value and element range
                final float clampedScroll = getClampedScroll(scroll);
                final int firstVisible = Math.max(0, (int)Math.floor((clampedScroll - 0.5f) / elmSize)); //FIXME cache and compare, only despawned needed
                final int lastVisible = Math.min(elmList.size() - 1, firstVisible + Math.round(1 / elmSize) - 1); //FIXME cache and compare, only despawned needed
                //FIXME                                        account for elements added or removed within the ranges   ^^^


                // Spawn visible elements
                for(int i = firstVisible; i <= lastVisible; i++) {
                    final Div elm = elmContainer.addChild(elmList.get(i));
                    elm.setSize(new Vector2f(1, elmSize));
                    elm.setPosY((1 - (elmSize * (i - firstVisible + 1))) * getAbsSize().y);
                    elm.spawn(canvas.getContext().getSpawnPos(), false);
                }


                // Update scrollbar
                if(lastVisible - firstVisible != elmList.size() - 1) {
                    final float elmAmount = (float)elmList.size();
                    final float trueHeight = (lastVisible - firstVisible + 1f) / elmAmount;
                    final float finalHeight = Math.max(trueHeight, THUMB_MIN_HEIGHT);
                    final float adjustY = finalHeight - trueHeight;
                    final float truePosY = (elmAmount - lastVisible + 1f) / elmAmount;  //! 0 to 1.0f
                    final float finalPosY = (truePosY - adjustY) * getAbsSize().y;      //! 0 to list height
                    barThumb.setSizeY(finalHeight);
                    barThumb.setPosY(finalPosY);
                    barThumb.flushStyle();
                    //TODO maybe animate this? idk. setSize and setPos and similar might need an "animate" parameter.
                    barTrack.spawn(canvas.getContext().getSpawnPos(), !instant);
                }
                else {
                    barTrack.despawn(!instant);
                }
            }

            // If it's empty, hide the scrollbar
            else {
                barTrack.despawn(!instant);
            }
        }
    }




    //TODO this function might need a more complex logic once refreshViewSides() gets updated
    @Override
    public void onScroll(final @NotNull Player player, final float amount) {

        // Add the received scroll to the current value, then clamp it
        scroll = getClampedScroll(scroll + amount);

        // Refresh view and play sound
        refreshViewSides(false);
        Scrollable.playSound(player);
    }


    /**
     * Calculates the scroll value and clamps it so it always stays between 0.5 and the total length of the list minus 0.5.
     * <p>
     * This allows the returned value to be used as a position directly.
     * @param rawScroll The unconstrianed scroll value.
     * @return The clamped scroll value.
     */
    public float getClampedScroll(final float rawScroll) {
        return Math.max(0.5f, Math.min(rawScroll, elmList.size() * elmSize - 0.5f));
    }


    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        if(!isSpawned) {
            super.spawn(pos, animate);
            scroll = getClampedScroll(scroll);
            refreshViewSides(true);
        }
    }




    @Override
    public void flushStyle() {
        super.flushStyle();


        // Set bar styles
        { final Flagged<PanelElmStyle> f = getThisStyle().getFlaggedThumbStyle();
        if(f.isFlagged()) {
            barThumb.setStyle(f.get());
            barThumb.getStyle().flagAll();
            f.unflag();
        }}
        { final Flagged<PanelElmStyle> f = getThisStyle().getFlaggedTrackStyle();
        if(f.isFlagged()) {
            barTrack.setStyle(f.get());
            barTrack.getStyle().flagAll();
            f.unflag();
        }}


        // Flush bar styles
        barThumb.flushStyle();
        barTrack.flushStyle();
    }
}
