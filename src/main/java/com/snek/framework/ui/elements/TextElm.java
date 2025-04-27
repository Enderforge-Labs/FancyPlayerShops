package com.snek.framework.ui.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector4i;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.InterpolatedData;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.data_types.containers.Pair;
import com.snek.framework.data_types.displays.CustomDisplay;
import com.snek.framework.data_types.displays.CustomTextDisplay;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.generated.FontSize;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.ui.styles.FancyTextElmStyle;
import com.snek.framework.ui.styles.PanelElmStyle;
import com.snek.framework.ui.styles.TextElmStyle;

import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity.TextAlignment;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;








/**
 * An element that can display text.
 * This class has transparent background. For a text element with background color, use FancyTextElm.
 */
public class TextElm extends Elm {
    private CustomTextDisplay getThisEntity() { return getEntity(CustomTextDisplay.class); }
    private TextElmStyle      getThisStyle () { return getStyle (TextElmStyle     .class); }

    // This value identifies the amount of rendered text pixels that fit in a minecraft block
    public static final int TEXT_PIXEL_BLOCK_RATIO = 40;




    /**
     * Creates a new TextElm using an existing entity and a custom style.
     * @param _world The world in which to place the element.
     * @param _entity The display entity.
     * @param _style The custom style.
     */
    protected TextElm(@NotNull ServerWorld _world, @NotNull CustomDisplay _entity, @NotNull ElmStyle _style) {
        super(_world, _entity, _style);
        getThisEntity().setBackground(new Vector4i(0, 0, 0, 0));
        getThisEntity().setLineWidth(Integer.MAX_VALUE);
    }


    /**
     * Creates a new TextElm using a custom style.
     * @param _world The world in which to place the element.
     * @param _style The custom style.
     */
    protected TextElm(@NotNull ServerWorld _world, @NotNull ElmStyle _style) {
        this(_world, new CustomTextDisplay(_world), _style);
    }


    /**
     * Creates a new TextElm using the default style.
     * @param _world The world in which to place the element.
     */
    public TextElm(@NotNull ServerWorld _world) {
        this(_world, new CustomTextDisplay(_world), new TextElmStyle());
    }




    @Override
    public void flushStyle() {

        // Handle transform calculations separately
        {
            Flagged<Transform> f = getThisStyle().getFlaggedTransform();
            if(f.isFlagged() || getThisStyle().getFlaggedTextAlignment().isFlagged() || getThisStyle().getFlaggedText().isFlagged()) {
                final Transform t = __calcTransform();
                if(getThisStyle().getTextAlignment() == TextAlignment.LEFT ) t.moveX(-(getAbsSize().x - calcWidth(this)) / 2f);
                if(getThisStyle().getTextAlignment() == TextAlignment.RIGHT) t.moveX(+(getAbsSize().x - calcWidth(this)) / 2f);
                getThisEntity().setTransformation(t.toMinecraftTransform());
                f.unflag();
            }
        }

        // Call superconstructor (transform is already unflagged) and handle the other values normally
        super.flushStyle();
        { Flagged<Text>          f = getThisStyle().getFlaggedText();          if(f.isFlagged()) { getThisEntity().setText         (f.get()); f.unflag(); }}
        { Flagged<Integer>       f = getThisStyle().getFlaggedTextOpacity();   if(f.isFlagged()) { getThisEntity().setTextOpacity  (f.get()); f.unflag(); }}
        { Flagged<TextAlignment> f = getThisStyle().getFlaggedTextAlignment(); if(f.isFlagged()) { getThisEntity().setTextAlignment(f.get()); f.unflag(); }}
    }




    @Override
    protected void __applyTransitionStep(@NotNull InterpolatedData d) {
        super.__applyTransitionStep(d);
        if(d.hasOpacity()) getThisStyle().setTextOpacity(d.getOpacity());
    }




    @Override
    protected InterpolatedData __generateInterpolatedData() {
        return new InterpolatedData(
            getThisStyle().getTransform().copy(),
            null,
            getThisStyle().getTextOpacity()
        );
    }
    @Override
    protected InterpolatedData __generateInterpolatedData(int index) {
        return new InterpolatedData(
            futureDataQueue.get(index).getTransform().copy(),
            null,
            futureDataQueue.get(index).getOpacity()
        );
    }




    @Override
    public void spawn(Vector3d pos) {
        super.spawn(pos);
    }


    @Override
    public void despawn() {
        super.despawn();
    }


    @Override
    public boolean stepTransition() {
        boolean r = super.stepTransition();
        getThisEntity().tick();
        return r;
    }




    //TODO cache width and update it when flushing the style.
    //TODO check transforms and everything else that could change it
    /**
     * Calculates the in-world height of the TextDisplay entity associated with a TextDisplay or FancyTextDisplay.
     * NOTICE: The height can be inaccurate as a lot of assumptions are made to calculate it.
     *     The returned value is the best possible approximation.
     * NOTICE: This operation is fairly expensive. The result should be cached whenever possible.
     * NOTICE: Wrapped lines are counted as one.
     * @return The height in blocks.
     */
    public static float calcHeight(Elm elm) {
        final Text text;
        final Transform t;
        /**/ if(elm instanceof TextElm      e) { text = e.getThisStyle()                   .getText(); t =                     e.__calcTransform();  }
        else if(elm instanceof FancyTextElm e) { text = e.getStyle(FancyTextElmStyle.class).getText(); t = e.__calcTransformFg(e.__calcTransform()); }
        else throw new RuntimeException("calcHeight used on incompatible Elm type: " + elm.getClass().getName());

        // Retrieve the current text as a string and count the number of lines
        final int lineNum = text.getString().split("\n").length;
        if(lineNum == 0) return 0;

        // Calculate their height and return it
        return ((lineNum == 1 ? 0f : lineNum - 1) * 2 + lineNum * FontSize.getHeight()) / TEXT_PIXEL_BLOCK_RATIO * t.getScale().y;
    }




    //TODO cache width and update it when flushing the style.
    //TODO check transforms and everything else that could change it
    /**
     * Calculates the in-world width of the TextDisplay entity associated with a TextDisplay or FancyTextDisplay.
     * NOTICE: The width can be inaccurate as a lot of assumptions are made to calculate it.
     *     The returned value is the best possible approximation.
     * NOTICE: This operation is fairly expensive. The result should be cached whenever possible.
     * NOTICE: Wrapped lines are counted as one.
     * @return The width in blocks.
     */
    public static float calcWidth(Elm elm) {
        final Text text;
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
        int maxWidth = 0;
        for(int i = 0; i < lines.length; ++i) {
            int w = FontSize.getWidth(lines[i]);
            if(w > maxWidth) maxWidth = w;
        }

        // final Pair<String, Integer> line = Pair.from(lines[0], lines[0].length());
        // if(lines.length > 1) for(int i = 0; i < lines.length; ++i) {
        //     final int len = lines[i].length();
        //     if(len > line.second) {
        //         line.first = lines[i];
        //         line.second = len;
        //     }
        // }

        // Calculate its length in blocks and return it
        System.out.println("    pixels:  " + maxWidth);
        System.out.println("    t scale: " + t.getScale().x);
        return (float)maxWidth / TEXT_PIXEL_BLOCK_RATIO * t.getScale().x;
    }
}
