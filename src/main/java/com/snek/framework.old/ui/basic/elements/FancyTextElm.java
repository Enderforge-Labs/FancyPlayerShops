package com.snek.framework.old.ui.basic.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4i;

import com.snek.fancyplayershops.main.Configs;
import com.snek.framework.old.data_types.animations.InterpolatedData;
import com.snek.framework.old.data_types.animations.Transform;
import com.snek.framework.old.data_types.containers.Flagged;
import com.snek.framework.old.data_types.displays.CustomDisplay;
import com.snek.framework.old.data_types.displays.CustomTextDisplay;
import com.snek.framework.old.data_types.ui.TextAlignment;
import com.snek.framework.old.ui.basic.styles.ElmStyle;
import com.snek.framework.old.ui.basic.styles.FancyTextElmStyle;
import com.snek.framework.old.ui.basic.styles.SimpleTextElmStyle;
import com.snek.framework.old.utils.Txt;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display.BillboardConstraints;








/**
 * A text element that also has a configurable, animatable background color.
 */
public class FancyTextElm extends __base_TextElm {

    // In-world data
    private final @NotNull CustomDisplay text;
    public  @NotNull CustomTextDisplay getFgEntity()  { return (CustomTextDisplay)text; }
    public  @NotNull CustomTextDisplay getBgEntity()  { return getEntity(CustomTextDisplay.class); }
    private @NotNull FancyTextElmStyle getThisStyle() { return getStyle(FancyTextElmStyle.class); }








    /**
     * Creates a new FancyTextElm using a custom style.
     * @param _world The world in which to place the element.
     * @param _style The custom style.
     */
    public FancyTextElm(final @NotNull ServerLevel _world, final @NotNull ElmStyle _style) {

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
    public FancyTextElm(final @NotNull ServerLevel _world) {
        this(_world, new FancyTextElmStyle());
    }








    /**
     * Helper function. Calculates the final transformation that is applied to the foreground entity.
     * @param initialTransform The value to start from.
     * <p> This is usually the transform shared between background and foreground.
     * <p> The shared transform is returned by __calcTransform().
     * @return The final transformation.
     */
    public @NotNull Transform __calcTransformFg(final @NotNull Transform initialTransform) {
        return
            initialTransform.copy()
            .apply(getThisStyle().getTransformFg())
            .moveZ((getZIndex() + 1) * Configs.ui.z_layer_spacing.getValue())
            .scale(SimpleTextElmStyle.DEFAULT_TEXT_SCALE)
        ;
    }

    /**
     * Helper function. Calculates the final transformation that is applied to the background entity.
     * @param initialTransform The value to start from.
     * <p> This is usually the transform shared between background and foreground.
     * <p> The shared transform is returned by __calcTransform().
     * @return The final transformation.
     */
    public @NotNull Transform __calcTransformBg(final @NotNull Transform initialTransform) {
        return
            initialTransform.copy()
            .apply(getThisStyle().getTransformBg())
            .scaleX(PanelElm.ENTITY_BLOCK_RATIO_X * getAbsSize().x)
            .scaleY(PanelElm.ENTITY_BLOCK_RATIO_Y * getAbsSize().y)
            .move(new Vector3f(PanelElm.ENTITY_SHIFT_X * getAbsSize().x, 0, 0).rotate(initialTransform.getRot()))
        ;
    }








    @Override
    public void flushStyle() {

        // Alias entities
        final @NotNull CustomTextDisplay fg = getFgEntity();
        final @NotNull CustomTextDisplay bg = getBgEntity();


        // Handle text first (transform depends on it)
        { final Flagged<Component> f = getThisStyle().getFlaggedText();
        if(f.isFlagged()) {
            fg.setText(f.get());
            updateEntitySizeCacheX();
            updateEntitySizeCacheY();
            f.unflag();
        }}


        // Handle transforms
        {
            final Flagged<Transform> f   = getThisStyle().getFlaggedTransform();
            final Flagged<Transform> fFg = getThisStyle().getFlaggedTransformFg();
            final Flagged<Transform> fBg = getThisStyle().getFlaggedTransformBg();
            final boolean fgNeedsUpdate = f.isFlagged() || fFg.isFlagged() || getThisStyle().getFlaggedTextAlignment().isFlagged() || getThisStyle().getFlaggedText().isFlagged();
            final boolean bgNeedsUpdate = f.isFlagged() || fBg.isFlagged();
            if(f.isFlagged()) f.unflag();


            // Calculate superclass transform if needed
            if(fgNeedsUpdate || bgNeedsUpdate) {
                final Transform t = __calcTransform();

                // Update foreground transform if necessary
                if(fgNeedsUpdate) {
                    final Transform tFg = __calcTransformFg(t);
                    if(getThisStyle().getTextAlignment() == TextAlignment.LEFT ) tFg.moveX(-(getAbsSize().x - calcEntityWidth()) / 2f);
                    if(getThisStyle().getTextAlignment() == TextAlignment.RIGHT) tFg.moveX(+(getAbsSize().x - calcEntityWidth()) / 2f);
                    fg.setTransformation(tFg.moveZ(EPSILON * epsilonPolarity).toMinecraftTransform());
                    fFg.unflag();
                }

                // Update background transform if necessary
                if(bgNeedsUpdate) {
                    bg.setTransformation(__calcTransformBg(t).moveZ(EPSILON * epsilonPolarity).toMinecraftTransform());
                    fBg.unflag();
                }
            }
        }


        // Handle the other Elm values normally, applying them to both entities
        { final Flagged<Float> f = getThisStyle().getFlaggedViewRange();
        if(f.isFlagged()) {
            fg.setViewRange(f.get());
            bg.setViewRange(f.get());
            f.unflag();
        }}
        { final Flagged<BillboardConstraints> f = getThisStyle().getFlaggedBillboardMode();
        if(f.isFlagged()) {
            fg.setBillboardMode(f.get());
            bg.setBillboardMode(f.get());
            f.unflag();
        }}


        // Handle TextElm values
        { final Flagged<Integer> f = getThisStyle().getFlaggedTextOpacity();
            //! Opacity is updated every time to keep the opacity cache moving
            fg.setTextOpacity(f.get());
            f.unflag();
        }
        { final Flagged<TextAlignment> f = getThisStyle().getFlaggedTextAlignment();
        if(f.isFlagged()) {
            fg.setTextAlignment(f.get());
            f.unflag();
        }}
        {
            final Flagged<Vector3i> fc = getThisStyle().getFlaggedBgColor();
            final Flagged<Integer>  fa = getThisStyle().getFlaggedBgAlpha();
            if(fc.isFlagged() || fa.isFlagged()) {
                final Vector3i color = fc.get();
                bg.setBackground(new Vector4i(fa.get(), color.x, color.y, color.z));
                fa.unflag();
                fc.unflag();
            }
        }


        // Transform, view range and billboard mode are already unflagged
        super.flushStyle();
    }




    @Override
    public int getLayerCount() {
        return 2;
    }




    @Override
    protected void __applyTransitionStep(final @NotNull InterpolatedData d) {
        super.__applyTransitionStep(d);
        if(d.hasOpacity    ()) { getThisStyle().setTextOpacity(d.getOpacity    ()); }
        if(d.hasBgAlpha    ()) { getThisStyle().setBgAlpha    (d.getBgAlpha    ()); }
        if(d.hasBgColor    ()) { getThisStyle().setBgColor    (d.getBgColor    ()); }
        if(d.hasTransformFg()) { getThisStyle().setTransformFg(d.getTransformFg()); }
        if(d.hasTransformBg()) { getThisStyle().setTransformBg(d.getTransformBg()); }
    }




    @Override
    protected @NotNull InterpolatedData __generateInterpolatedData() {
        return new InterpolatedData(
            getThisStyle().getTransform().copy(),
            new Vector3i(getThisStyle().getBgColor()),
            getThisStyle().getBgAlpha(),
            getThisStyle().getTextOpacity(),
            getThisStyle().getTransformFg().copy(),
            getThisStyle().getTransformBg().copy()
        );
    }
    @Override
    protected @NotNull InterpolatedData __generateInterpolatedData(int index) {
        final InterpolatedData fd = futureDataQueue.get(index);
        return new InterpolatedData(
            fd.getTransform().copy(),
            new Vector3i(fd.getBgColor()),
            fd.getBgAlpha(),
            fd.getOpacity(),
            fd.getTransformFg().copy(),
            fd.getTransformBg().copy()
        );
    }




    @Override
    public void spawn(final @NotNull Vector3d pos) {

        flushStyle();
        getFgEntity().spawn(world, pos);

        // Set tracking custom name
        getFgEntity().setCustomNameVisible(false);
        getFgEntity().setCustomName(new Txt(SimpleTextElm.ENTITY_CUSTOM_NAME).get());

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
        final boolean r = super.stepTransition();
        getThisStyle().editTransform();
        getFgEntity().setInterpolationDuration(Configs.perf.animation_refresh_time.getValue());
        getFgEntity().setStartInterpolation();
        getFgEntity().tick();
        return r;
    }
}