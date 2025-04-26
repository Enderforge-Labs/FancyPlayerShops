package com.snek.fancyplayershops.ui;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.buy.BuyUi;
import com.snek.fancyplayershops.ui.details.DetailsUi;
import com.snek.fancyplayershops.ui.misc.ShopUiBorder;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.utils.Easings;








//TODO add a "line" element that draws a single line using two 2d points

//TODO add a "polyline" element that draws the line using a list of 2d points
//TODO put it in the "composite" elements directory
//TODO composite elements automatically spawn their children and have special behaviour

/**
 * A generic canvas class used to create shop menus.
 *     It creates and manages a background panel, a back side panel and a top and bottom borders,
 *     which are kept spawned and are inherited by future canvases until the targeted shop stops being focused.
 */
public class ShopCanvas extends Div {

    // Inherited elements
    protected final @NotNull Elm bg;
    protected final @NotNull Elm back;
    protected final @NotNull Elm top;
    protected final @NotNull Elm bottom;

    // Getters
    public @NotNull Elm getBg    () { return bg;     }
    public @NotNull Elm getBack  () { return back;   }
    public @NotNull Elm getTop   () { return top;    }
    public @NotNull Elm getBottom() { return bottom; }

    // Height cache
    private final float newHeightBg;
    private final float newHeightBack;
    private final float newHeightTop;
    private final float newHeightBottom;

    // Position cache
    private final float newPosBg;
    private final float newPosBack;
    private final float newPosTop;
    private final float newPosBottom;


    // // Temporary children list used to distinguish between inherited elements and new ones
    // private List<Div> tmpChildren = new ArrayList<>();
    // @Override
    // public Div addChild(Div elm) {
    //     tmpChildren.add(elm);
    //     return super.addChild(elm);
    // }
    // @Override
    // public void clearChildren() {
    //     tmpChildren.clear();
    //     super.clearChildren();
    // }
    // @Override
    // public Div removeChild(Div elm) {
    //     tmpChildren.remove(elm);
    //     return super.removeChild(elm);
    // }


    // Layout
    public static final int   SPAWN_SIZE_TIME    = 8;
    public static final float SQUARE_BUTTON_SIZE = 0.12f;








    /**
     * Creates a new ShopCanvas.
     * @param _shop The target shop.
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     */
    public ShopCanvas(@NotNull Shop _shop, float height, float heightTop, float heightBottom) {
        super();
        setSize(new Vector2f(1f, 1f));
        ShopCanvas activeCanvas = _shop.getActiveCanvas();

        // Calculate new heights
        newHeightBg     = height - heightTop - heightBottom;
        newHeightBack   = height;
        newHeightTop    = heightTop;
        newHeightBottom = heightBottom;

        // Calculate new positions
        newPosBg     = 1 - height + heightBottom;
        newPosBack   = 1 - height;
        newPosTop    = 1 - heightTop;
        newPosBottom = 1 - height;




        // If the elements don't exist yet
        if(activeCanvas == null) {

            // Create the elements
            bg     = (Elm)addChild(new CanvasBackground(_shop));
            back   = (Elm)addChild(new CanvasBackground(_shop));
            top    = (Elm)addChild(new ShopUiBorder(_shop));
            bottom = (Elm)addChild(new ShopUiBorder(_shop));

            // Set their size, position and alignments
            bg    .setSize(new Vector2f(1f, 1f));
            bg    .setAlignmentX(AlignmentX.CENTER);
            bg    .setAlignmentY(AlignmentY.BOTTOM);
            back  .setSize(new Vector2f(1f, 1f));
            back  .setAlignmentX(AlignmentX.CENTER);
            back  .setAlignmentY(AlignmentY.BOTTOM);
            top   .setSize(new Vector2f(1f, 1f));
            top   .setAlignmentX(AlignmentX.CENTER);
            top   .setAlignmentY(AlignmentY.BOTTOM);
            bottom.setSize(new Vector2f(1f, 1f));
            bottom.setAlignmentX(AlignmentX.CENTER);
            bottom.setAlignmentY(AlignmentY.TOP);

            // Set visual dimensions (and rotation for the back side)
            final Transform transformBg     = new Transform().scaleY(newHeightBg    ).moveY(newPosBg    );
            final Transform transformBack   = new Transform().scaleY(newHeightBack  ).moveY(newPosBack  ).rotY((float)Math.PI);
            final Transform transformTop    = new Transform().scaleY(newHeightTop   ).moveY(newPosTop   );
            final Transform transformBottom = new Transform().scaleY(newHeightBottom).moveY(newPosBottom);
            bg    .applyAnimationNow(new Transition().additiveTransform(transformBg));
            back  .applyAnimationNow(new Transition().additiveTransform(transformBack));
            top   .applyAnimationNow(new Transition().additiveTransform(transformTop));
            bottom.applyAnimationNow(new Transition().additiveTransform(transformBottom));
        }




        // If the elements already exist
        else {

            // Instantly despawn and remove previous children of the background element
            for(Div c : activeCanvas.getBg().getChildren()) c.despawnNow();
            activeCanvas.getBg().clearChildren();

            // Inherit the elements
            bg     = (Elm)addChild(activeCanvas.getBg());
            back   = (Elm)addChild(activeCanvas.getBack());
            top    = (Elm)addChild(activeCanvas.getTop());
            bottom = (Elm)addChild(activeCanvas.getBottom());

            // Animate them to match the specified visual dimensions
            final Transform transformBg     = new Transform().scaleY(newHeightBg     / activeCanvas.newHeightBg    ).moveY(newPosBg     - activeCanvas.newPosBg    );
            final Transform transformBack   = new Transform().scaleY(newHeightBack   / activeCanvas.newHeightBack  ).moveY(newPosBack   - activeCanvas.newPosBack  );
            final Transform transformTop    = new Transform().scaleY(newHeightTop    / activeCanvas.newHeightTop   ).moveY(newPosTop    - activeCanvas.newPosTop   );
            final Transform transformBottom = new Transform().scaleY(newHeightBottom / activeCanvas.newHeightBottom).moveY(newPosBottom - activeCanvas.newPosBottom);
            bg    .applyAnimation(new Transition(SPAWN_SIZE_TIME, Easings.expOut).additiveTransform(transformBg));
            back  .applyAnimation(new Transition(SPAWN_SIZE_TIME, Easings.expOut).additiveTransform(transformBack));
            top   .applyAnimation(new Transition(SPAWN_SIZE_TIME, Easings.expOut).additiveTransform(transformTop));
            bottom.applyAnimation(new Transition(SPAWN_SIZE_TIME, Easings.expOut).additiveTransform(transformBottom));
        }

if(activeCanvas != null) {
        System.out.println("newHeightBg :     " + (newHeightBg     / activeCanvas.newHeightBg));
        System.out.println("newHeightBack :   " + (newHeightBack   / activeCanvas.newHeightBack));
        System.out.println("newHeightTop :    " + (newHeightTop    / activeCanvas.newHeightTop));
        System.out.println("newHeightBottom : " + (newHeightBottom / activeCanvas.newHeightBottom));}

        // super(new CanvasBackground(_shop), new CanvasBackground(_shop));
        // bg.setPosY(1 - DetailsUi.BACKGROUND_HEIGHT);
        // bg.setSize(new Vector2f(1, BACKGROUND_HEIGHT));
        // back.setPosY(1 - DetailsUi.BACKGROUND_HEIGHT);
        // back.setSize(new Vector2f(1, BACKGROUND_HEIGHT));
        // back.applyAnimationNow(new Transition().additiveTransform(new Transform().rotY((float)Math.PI)));
        // Div e;

        // // Add borders
        // e = bg.addChild(new ShopUiBorder(_shop));
        // e.setAlignmentY(AlignmentY.BOTTOM);
        // e = bg.addChild(new ShopUiBorder(_shop));
        // e.setAlignmentY(AlignmentY.TOP);

        // // Call superconstructor and add background
        // super(_shop.getActiveCanvas().getBackground(), _shop.getActiveCanvas().getBack());
        // Div e;

        // // Instantly despawn and remove previous children
        // for(Div c : bg.getChildren()) c.despawnNow();
        // bg.clearChildren();

        // // Reset size and position, visually simulate the previous values using an instant animation
        // bg.setSizeY(1);
        // bg.setPosY(0);
        // bg.applyAnimationNow(
        //     new Transition()
        //     .additiveTransform(new Transform().scaleY(DetailsUi.BACKGROUND_HEIGHT).moveY(1 - DetailsUi.BACKGROUND_HEIGHT))
        // );
        // back.setSizeY(1);
        // back.setPosY(0);
        // back.applyAnimationNow(
        //     new Transition()
        //     .additiveTransform(new Transform().scaleY(DetailsUi.BACKGROUND_HEIGHT).moveY(1 - DetailsUi.BACKGROUND_HEIGHT))
        // );
        // //TODO MOVE TO SHOP CANVAS. It takes the borders, the background and the back side and changes their sizes on spawn
    }
    // public ShopCanvas(@NotNull Elm _bg, @NotNull Elm _back) {
    //     super();

    //     // Add front
    //     addChild(_bg);
    //     bg = _bg;

    //     // Add back
    //     addChild(_back);
    //     back = _back;

    // }








    @Override
    public void spawn(Vector3d pos) {

        // If the background is already spawned, only spawn its children
        if(bg.isSpawned()) for(Div c : bg.getChildren()) {
            c.spawn(pos);
        }

        // If not, spawn everything
        else {
            super.spawn(pos);
        }
    }








    @Override
    public void despawn() {
        super.despawn();
    }
}