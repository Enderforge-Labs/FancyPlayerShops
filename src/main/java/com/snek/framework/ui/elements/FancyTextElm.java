package com.snek.framework.ui.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.InterpolatedData;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.data_types.displays.CustomDisplay;
import com.snek.framework.data_types.displays.CustomTextDisplay;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.FancyTextElmStyle;
import com.snek.framework.ui.styles.PanelElmStyle;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.utils.Txt;

import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity.TextAlignment;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;








/**
 * A TextElm that also has a configurable, animatable background color.
 */
public class FancyTextElm extends Elm {

    // In-world data
    private @NotNull CustomDisplay text;
    public CustomTextDisplay getFgEntity() { return (CustomTextDisplay)text; }
    public CustomTextDisplay getBgEntity() { return (CustomTextDisplay)getEntity(); }
    private FancyTextElmStyle getStyle() { return (FancyTextElmStyle)style; }








    /**
     * Creates a new FancyTextElm using a custom style.
     * @param _world The world in which to place the element.
     * @param _style The custom style.
     */
    protected FancyTextElm(@NotNull ServerWorld _world, @NotNull ElmStyle _style) {

        // Create element and background element
        super(_world, new CustomTextDisplay(_world), _style);
        text = new CustomTextDisplay(_world);

        // Initialize permanent entity values
        getBgEntity().setText(new Txt().get());
        getFgEntity().setBackground(new Vector4i(0, 0, 0, 0));
        getFgEntity().setLineWidth(Integer.MAX_VALUE);
    }


    /**
     * Creates a new FancyTextElm using the default style.
     * @param _world The world in which to place the element.
     */
    public FancyTextElm(@NotNull ServerWorld _world) {
        this(_world, new FancyTextElmStyle());
    }







    /**
     * Helper function. Calculates the final transformation that is applied to the foreground entity.
     * @param initialTransform The value to start from.
     *     This is usually the transform shared between background and foreground.
     *     The shared transform is returned by __calcTransform().
     * @return The final transformation.
     */
    public Transform __calcTransformFg(@NotNull Transform initialTransform) {
        return
            initialTransform.copy()
            .apply(getStyle().getTransformFg())
            .moveZ((getZIndex() + 1) * 0.001f) //TODO move Z layer spacing to config file
            .scale(TextElmStyle.DEFAULT_TEXT_SCALE)
        ;
    }

    /**
     * Helper function. Calculates the final transformation that is applied to the background entity.
     * @param initialTransform The value to start from.
     *     This is usually the transform shared between background and foreground.
     *     The shared transform is returned by __calcTransform().
     * @return The final transformation.
     */
    public Transform __calcTransformBg(@NotNull Transform initialTransform) {
        return
            initialTransform.copy()
            .apply(getStyle().getTransformBg())
            .scaleX(PanelElm.ENTITY_BLOCK_RATIO_X * getAbsSize().x)
            .scaleY(PanelElm.ENTITY_BLOCK_RATIO_Y * getAbsSize().y)
            .move(new Vector3f(PanelElm.ENTITY_SHIFT_X * getAbsSize().x, 0, 0).rotate(initialTransform.getRot()))
        ;
    }








    @Override
    public void flushStyle() {

        // Alias entities
        CustomTextDisplay fg = getFgEntity();
        CustomTextDisplay bg = getBgEntity();


        // Handle transforms
        {
            Flagged<Transform> f = style.getFlaggedTransform();
            Flagged<Transform> fFg = getStyle().getFlaggedTransformFg();
            Flagged<Transform> fBg = getStyle().getFlaggedTransformBg();
            final boolean fgNeedsUpdate = f.isFlagged() || fFg.isFlagged() || getStyle().getFlaggedTextAlignment().isFlagged() || getStyle().getFlaggedText().isFlagged() || fBg.isFlagged();
            final boolean bgNeedsUpdate = f.isFlagged() || fBg.isFlagged();
            if(f.isFlagged()) f.unflag();


            // Calculate superclass transform if needed
            Transform t = null;
            if(fgNeedsUpdate || bgNeedsUpdate) {
                t = __calcTransform();

                // Update foreground transform if necessary
                if(fgNeedsUpdate) {
                    final Transform tFg = __calcTransformFg(t);
                    if(getStyle().getTextAlignment() == TextAlignment.LEFT ) tFg.moveX(-(getAbsSize().x - TextElm.calcWidth(this)) / 2f);
                    if(getStyle().getTextAlignment() == TextAlignment.RIGHT) tFg.moveX(+(getAbsSize().x - TextElm.calcWidth(this)) / 2f);
                    fg.setTransformation(tFg.toMinecraftTransform());
                    fFg.unflag();
                }

                // Update background transform if necessary
                if(bgNeedsUpdate) {
                    bg.setTransformation(__calcTransformBg(t).toMinecraftTransform());
                    fBg.unflag();
                }
            }
        }


        // Handle the other Elm values normally, applying them to both entities
        {Flagged<Float> f = style.getFlaggedViewRange();
        if(f.isFlagged()) {
            fg.setViewRange(f.get());
            bg.setViewRange(f.get());
            f.unflag();
        }}
        {Flagged<BillboardMode> f = style.getFlaggedBillboardMode();
        if(f.isFlagged()) {
            fg.setBillboardMode(f.get());
            bg.setBillboardMode(f.get());
            f.unflag();
        }}


        // Handle TextElm values
        { Flagged<Text>          f = getStyle().getFlaggedText();          if(f.isFlagged()) { fg.setText         (f.get()); f.unflag(); }}
        { Flagged<Integer>       f = getStyle().getFlaggedTextOpacity();   if(f.isFlagged()) { fg.setTextOpacity  (f.get()); f.unflag(); }}
        { Flagged<TextAlignment> f = getStyle().getFlaggedTextAlignment(); if(f.isFlagged()) { fg.setTextAlignment(f.get()); f.unflag(); }}
        { Flagged<Vector4i>      f = getStyle().getFlaggedBackground();    if(f.isFlagged()) { bg.setBackground   (f.get()); f.unflag(); }}


        // Transform, view range and billboard mode are already unflagged
        super.flushStyle();
    }




    @Override
    public int getLayerCount() {
        return 2;
    }




    @Override
    protected void __applyTransitionStep(@NotNull InterpolatedData d) {
        super.__applyTransitionStep(d);
        if(d.hasOpacity    ()) getStyle().setTextOpacity(d.getOpacity    ());
        if(d.hasBackground ()) getStyle().setBackground (d.getBackground ());
        if(d.hasTransformFg()) getStyle().setTransformFg(d.getTransformFg());
        if(d.hasTransformBg()) getStyle().setTransformBg(d.getTransformBg());
    }




    @Override
    protected InterpolatedData __generateInterpolatedData() {
        return new InterpolatedData(
            getStyle().getTransform().copy(),
            new Vector4i(getStyle().getBackground()),
            getStyle().getTextOpacity(),
            getStyle().getTransformFg().copy(),
            getStyle().getTransformBg().copy()
        );
    }
    @Override
    protected InterpolatedData __generateInterpolatedData(int index) {
        return new InterpolatedData(
            futureDataQueue.get(index).getTransform().copy(),
            new Vector4i(futureDataQueue.get(index).getBackground()),
            futureDataQueue.get(index).getOpacity(),
            futureDataQueue.get(index).getTransformFg().copy(),
            futureDataQueue.get(index).getTransformBg().copy()
        );
    }




    @Override
    public void spawn(Vector3d pos) {

        flushStyle();
        getFgEntity().spawn(world, pos);

        // Set tracking custom name
        getFgEntity().setCustomNameVisible(false);
        getFgEntity().setCustomName(new Txt(Elm.ENTITY_CUSTOM_NAME).get());

        // Call superclass spawn
        super.spawn(pos);
    }




    @Override
    public void despawn() {
        super.despawn();
    }


    @Override
    public void despawnNow() {
        super.despawnNow();
        getFgEntity().despawn();
    }




    @Override
    public boolean stepTransition() {
        boolean r = super.stepTransition();
        getFgEntity().setInterpolationDuration(TRANSITION_REFRESH_TIME);
        getFgEntity().setStartInterpolation();
        getFgEntity().tick();
        return r;
    }
}