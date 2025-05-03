package com.snek.framework.ui.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.InterpolatedData;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.data_types.displays.CustomDisplay;
import com.snek.framework.data_types.displays.CustomTextDisplay;
import com.snek.framework.ui.elements.styles.ElmStyle;
import com.snek.framework.ui.elements.styles.PanelElmStyle;
import com.snek.framework.utils.Txt;

import net.minecraft.server.world.ServerWorld;








/**
 * A simple UI element with a background color and animations.
 * <p> Panels default to a 1x1 blocks size.
 */
public class PanelElm extends Elm {
    private @NotNull CustomTextDisplay getThisEntity() { return getEntity(CustomTextDisplay.class); }
    private @NotNull PanelElmStyle     getThisStyle () { return getStyle (PanelElmStyle    .class); }

    // The amount of panel elements of default size would be needed to cover the width of a block
    public static final float ENTITY_BLOCK_RATIO_X = 40f;
    public static final float ENTITY_BLOCK_RATIO_Y = 40f;

    // The translation on the X axis needed to align the panel entity with the element's bounding box
    public static final float ENTITY_SHIFT_X = -0.5f;






    /**
     * Creates a new PanelElm using an existing entity and a custom style.
     * @param _world The world in which to place the element.
     * @param _entity The display entity.
     * @param _style The custom style.
     */
    protected PanelElm(final @NotNull ServerWorld _world, final @NotNull CustomDisplay _entity, final @NotNull ElmStyle _style) {
        super(_world, _entity, _style);
        getThisEntity().setText(new Txt().get());
    }


    /**
     * Creates a new PanelElm using a custom style.
     * @param _world The world in which to place the element.
     * @param _style The custom style.
     */
    protected PanelElm(final @NotNull ServerWorld _world, final @NotNull ElmStyle _style) {
        this(_world, new CustomTextDisplay(_world), _style);
    }


    /**
     * Creates a new PanelElm using the default style.
     * @param _world The world in which to place the element.
     */
    public PanelElm(final @NotNull ServerWorld _world) {
        this(_world, new CustomTextDisplay(_world), new PanelElmStyle());
    }




    @Override
    public void flushStyle() {
        super.flushStyle();


        // Apply color
        {
            final Flagged<Vector3i> fc = getThisStyle().getFlaggedColor();
            final Flagged<Integer>  fa = getThisStyle().getFlaggedAlpha();
            if(fc.isFlagged() || fa.isFlagged()) {
                final Vector3i color = fc.get();
                getThisEntity().setBackground(new Vector4i(fa.get(), color.x, color.y, color.z));
                fa.unflag();
                fc.unflag();
            }
        }
    }




    @Override
    protected void __applyTransitionStep(final @NotNull InterpolatedData d) {
        super.__applyTransitionStep(d);
        if(d.hasBgColor()) getThisStyle().setColor(d.getBgColor());
        if(d.hasBgAlpha()) getThisStyle().setAlpha(d.getBgAlpha());
    }




    @Override
    protected @NotNull InterpolatedData __generateInterpolatedData() {
        return new InterpolatedData(
            getThisStyle().getTransform().copy(),
            new Vector3i(getThisStyle().getColor()),
            getThisStyle().getAlpha(),
            null
        );
    }
    @Override
    protected @NotNull InterpolatedData __generateInterpolatedData(final int index) {
        final InterpolatedData fd = futureDataQueue.get(index);
        return new InterpolatedData(
            fd.getTransform().copy(),
            new Vector3i(fd.getBgColor()),
            fd.getBgAlpha(),
            null
        );
    }




    @Override
    public boolean stepTransition() {
        return super.stepTransition();
    }




    @Override
    protected @NotNull Transform __calcTransform() {
        final Transform t = super.__calcTransform();
        return t.copy()
            .scaleX(ENTITY_BLOCK_RATIO_X * getAbsSize().x)
            .scaleY(ENTITY_BLOCK_RATIO_Y * getAbsSize().y)
            .moveX(ENTITY_SHIFT_X * getAbsSize().x)
        ;
    }
}
