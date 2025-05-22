package com.snek.fancyplayershops.ui._elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.ui.elements.PanelElm;
import com.snek.framework.ui.elements.styles.PanelElmStyle;
import com.snek.framework.utils.Easings;

import net.minecraft.server.level.ServerLevel;

















public abstract class UiCanvas extends Div {

    // Layout
    public static final int   SPAWN_SIZE_TIME    = 8;
    public static final float SQUARE_BUTTON_SIZE = 0.12f;




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








    /**
     * Creates a new Canvas.
     * @param prevCanvas The previous canvas. Used to inherit elements.
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     */
    protected UiCanvas(
        final @Nullable UiCanvas prevCanvas, final @NotNull ServerLevel _world, final float height, final float heightTop, final float heightBottom,
        final @Nullable PanelElmStyle bgStyle, final @Nullable PanelElmStyle backStyle
    ) {
        setSize(new Vector2f(1f, 1f));

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
        if(prevCanvas == null) {

            // Create the elements
            bg     = (Elm)addChild(new PanelElm(_world, bgStyle   == null ? new PanelElmStyle() : bgStyle));
            back   = (Elm)addChild(new PanelElm(_world, backStyle == null ? new PanelElmStyle() : backStyle));
            top    = (Elm)addChild(new UiBorder(_world));
            bottom = (Elm)addChild(new UiBorder(_world));


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
            final Transform transformBack   = new Transform().scaleY(newHeightBack  ).moveY(newPosBack  ).rotY((float)Math.PI).moveX(1f);
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
            for(final Div c : prevCanvas.getBg().getChildren()) c.despawnNow();
            prevCanvas.getBg().clearChildren();

            // Inherit the elements
            bg     = (Elm)addChild(prevCanvas.getBg());
            back   = (Elm)addChild(prevCanvas.getBack());
            top    = (Elm)addChild(prevCanvas.getTop());
            bottom = (Elm)addChild(prevCanvas.getBottom());

            // Animate them to match the specified visual dimensions
            final Transform transformBg     = new Transform().scaleY(newHeightBg     / prevCanvas.newHeightBg    ).moveY(newPosBg     - prevCanvas.newPosBg    );
            final Transform transformBack   = new Transform().scaleY(newHeightBack   / prevCanvas.newHeightBack  ).moveY(newPosBack   - prevCanvas.newPosBack  );
            final Transform transformTop    = new Transform().scaleY(newHeightTop    / prevCanvas.newHeightTop   ).moveY(newPosTop    - prevCanvas.newPosTop   );
            final Transform transformBottom = new Transform().scaleY(newHeightBottom / prevCanvas.newHeightBottom).moveY(newPosBottom - prevCanvas.newPosBottom);
            bg    .applyAnimation(new Transition(SPAWN_SIZE_TIME, Easings.expOut).additiveTransform(transformBg));
            back  .applyAnimation(new Transition(SPAWN_SIZE_TIME, Easings.expOut).additiveTransform(transformBack));
            top   .applyAnimation(new Transition(SPAWN_SIZE_TIME, Easings.expOut).additiveTransform(transformTop));
            bottom.applyAnimation(new Transition(SPAWN_SIZE_TIME, Easings.expOut).additiveTransform(transformBottom));
        }
    }








    @Override
    public void spawn(final @NotNull Vector3d pos) {

        // If the background is already spawned, only spawn its children
        if(bg.isSpawned()) for(final Div c : bg.getChildren()) {
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
