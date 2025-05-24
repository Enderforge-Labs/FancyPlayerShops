package com.snek.framework.ui.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector4i;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.framework.data_types.animations.InterpolatedData;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.data_types.displays.CustomDisplay;
import com.snek.framework.data_types.displays.CustomTextDisplay;
import com.snek.framework.data_types.ui.TextAlignment;
import com.snek.framework.generated.FontSize;
import com.snek.framework.ui.elements.styles.ElmStyle;
import com.snek.framework.ui.elements.styles.FancyTextElmStyle;
import com.snek.framework.ui.elements.styles.TextElmStyle;
import com.snek.framework.utils.Txt;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;








/**
 * An element that can display text.
 * This class has transparent background. For a text element with background color, use FancyTextElm.
 */
public class TextElm extends Elm {
    public static final @NotNull String ENTITY_CUSTOM_NAME = FancyPlayerShops.MOD_ID + ".ui.displayentity";
    private @NotNull CustomTextDisplay getThisEntity() { return getEntity(CustomTextDisplay.class); }
    private @NotNull TextElmStyle      getThisStyle () { return getStyle (TextElmStyle     .class); }




    /**
     * Creates a new TextElm using an existing entity and a custom style.
     * @param _world The world in which to place the element.
     * @param _entity The display entity.
     * @param _style The custom style.
     */
    protected TextElm(final @NotNull ServerLevel _world, final @NotNull CustomDisplay _entity, final @NotNull ElmStyle _style) {
        super(_world, _entity, _style);
        getThisEntity().setBackground(new Vector4i(0, 0, 0, 0));
        getThisEntity().setLineWidth(Integer.MAX_VALUE);
    }


    /**
     * Creates a new TextElm using a custom style.
     * @param _world The world in which to place the element.
     * @param _style The custom style.
     */
    protected TextElm(final @NotNull ServerLevel _world, final @NotNull ElmStyle _style) {
        this(_world, new CustomTextDisplay(_world), _style);
    }


    /**
     * Creates a new TextElm using the default style.
     * @param _world The world in which to place the element.
     */
    public TextElm(final @NotNull ServerLevel _world) {
        this(_world, new CustomTextDisplay(_world), new TextElmStyle());
    }




    @Override
    public void flushStyle() {

        // Handle transform calculations separately
        {
            final Flagged<Transform> f = getThisStyle().getFlaggedTransform();
            if(f.isFlagged() || getThisStyle().getFlaggedTextAlignment().isFlagged() || getThisStyle().getFlaggedText().isFlagged()) {
                final Transform t = __calcTransform();
                if(getThisStyle().getTextAlignment() == TextAlignment.LEFT ) t.moveX(-(getAbsSize().x - calcWidth(this)) / 2f);
                if(getThisStyle().getTextAlignment() == TextAlignment.RIGHT) t.moveX(+(getAbsSize().x - calcWidth(this)) / 2f);
                getThisEntity().setTransformation(t.moveZ(EPSILON * epsilonPolarity).toMinecraftTransform());
                f.unflag();
            }
        }

        // Call superconstructor (transform is already unflagged) and handle the other values normally
        super.flushStyle();
        { final Flagged<Component> f = getThisStyle().getFlaggedText();
        if(f.isFlagged()) {
            getThisEntity().setText(f.get());
            f.unflag();
        }}
        { final Flagged<Integer> f = getThisStyle().getFlaggedTextOpacity();
        if(f.isFlagged()) {
            getThisEntity().setTextOpacity(f.get());
            f.unflag();
        }}
        { final Flagged<TextAlignment> f = getThisStyle().getFlaggedTextAlignment();
        if(f.isFlagged()) {
            getThisEntity().setTextAlignment(f.get());
            f.unflag();
        }}
    }




    @Override
    protected void __applyTransitionStep(final @NotNull InterpolatedData d) {
        super.__applyTransitionStep(d);
        if(d.hasOpacity()) getThisStyle().setTextOpacity(d.getOpacity());
    }




    @Override
    protected @NotNull InterpolatedData __generateInterpolatedData() {
        return new InterpolatedData(
            getThisStyle().getTransform().copy(),
            null,
            null,
            getThisStyle().getTextOpacity()
        );
    }
    @Override
    protected @NotNull InterpolatedData __generateInterpolatedData(int index) {
        final InterpolatedData fd = futureDataQueue.get(index);
        return new InterpolatedData(
            fd.getTransform().copy(),
            null,
            null,
            fd.getOpacity()
        );
    }




    @Override
    public void spawn(final @NotNull Vector3d pos) {
        super.spawn(pos);

        // Set tracking custom name
        getThisEntity().setCustomNameVisible(false);
        getThisEntity().setCustomName(new Txt(ENTITY_CUSTOM_NAME).get());
    }


    @Override
    public void despawn() {
        super.despawn();
    }


    @Override
    public boolean stepTransition() {
        final boolean r = super.stepTransition();
        getThisEntity().tick();
        return r;
    }




    //TODO cache width and update it when flushing the style.
    //TODO check transforms and everything else that could change it
    /**
     * Calculates the in-world height of the TextDisplay entity associated with a TextDisplay or FancyTextDisplay.
     * <p> NOTICE: The height can be inaccurate as a lot of assumptions are made to calculate it. The returned value is the best possible approximation.
     * <p> NOTICE: This operation is fairly expensive. The result should be cached whenever possible.
     * <p> NOTICE: Wrapped lines are counted as one.
     * @return The height in blocks.
     */
    public static float calcHeight(final @NotNull Elm elm) {
        final Component text;
        final Transform t;
        /**/ if(elm instanceof TextElm      e) { text = e.getThisStyle()                   .getText(); t =                     e.__calcTransform();  }
        else if(elm instanceof FancyTextElm e) { text = e.getStyle(FancyTextElmStyle.class).getText(); t = e.__calcTransformFg(e.__calcTransform()); }
        else throw new RuntimeException("calcHeight used on incompatible Elm type: " + elm.getClass().getName());

        // Retrieve the current text as a string and count the number of lines
        final int lineNum = text.getString().split("\n").length;
        if(lineNum == 0) return 0;

        // Calculate their height and return it
        return ((lineNum == 1 ? 0f : lineNum - 1) * 2 + lineNum * FontSize.getHeight()) * t.getScale().y;
    }




    //TODO cache width and update it when flushing the style.
    //TODO check transforms and everything else that could change it
    /**
     * Calculates the in-world width of the TextDisplay entity associated with a TextDisplay or FancyTextDisplay.
     * <p> NOTICE: The width can be inaccurate as a lot of assumptions are made to calculate it. The returned value is the best possible approximation.
     * <p> NOTICE: This operation is fairly expensive. The result should be cached whenever possible.
     * <p> NOTICE: Wrapped lines are counted as one.
     * @return The width in blocks.
     */
    public static float calcWidth(final @NotNull Elm elm) {
        final Component text;
        final Transform t;
        /**/ if(elm instanceof TextElm      e) { text = e.getThisStyle()                   .getText(); t =                     e.__calcTransform();  }
        else if(elm instanceof FancyTextElm e) { text = e.getStyle(FancyTextElmStyle.class).getText(); t = e.__calcTransformFg(e.__calcTransform()); }
        else throw new RuntimeException("calcWidth used on incompatible Elm type: " + elm.getClass().getName());

        // Retrieve the current text as a string
        final String[] lines = text.getString().split("\n");
        if(lines.length == 0) {
            return 0;
        }

        // Find the longest line and save its length in pixels
        float maxWidth = 0;
        for(int i = 0; i < lines.length; ++i) {
            final float w = FontSize.getWidth(lines[i]);
            if(w > maxWidth) maxWidth = w;
        }

        // Calculate its length in blocks and return it
        return maxWidth * t.getScale().x;
    }




    /**
     * Checks for stray displays and purges them.
     * <p> Must be called on entity load event.
     * @param entity The entity.
     */
    public static void onEntityLoad(final @NotNull Entity entity) {
        if(entity instanceof Display) {
            if(
                entity.level() != null &&
                entity.hasCustomName() &&
                entity.getCustomName().getString().equals(ENTITY_CUSTOM_NAME)
            ) {
                entity.remove(RemovalReason.KILLED);
            }
        }
    }
}
