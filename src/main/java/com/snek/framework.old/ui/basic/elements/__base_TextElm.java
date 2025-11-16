package com.snek.framework.old.ui.basic.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.old.data_types.animations.Transform;
import com.snek.framework.old.data_types.displays.CustomDisplay;
import com.snek.framework.old.generated.FontSize;
import com.snek.framework.old.ui.Elm;
import com.snek.framework.old.ui.basic.styles.ElmStyle;
import com.snek.framework.old.ui.basic.styles.SimpleTextElmStyle;

import net.minecraft.server.level.ServerLevel;








public class __base_TextElm extends Elm {

    // Entity size cache. This represents the actual size the entity has in the world when using the default transform. Measured in blocks
    private float entitySizeCacheX = 0;
    private float entitySizeCacheY = 0;




    protected __base_TextElm(final @NotNull ServerLevel _world, final @NotNull CustomDisplay _entity, final @NotNull ElmStyle _style) {
        super(_world, _entity, _style);
        updateEntitySizeCacheX();
        updateEntitySizeCacheY();
    }




    public void updateEntitySizeCacheX() {

        // Set cache to 0 if the text is empty.
        final String string = getStyle(SimpleTextElmStyle.class).getText().getString(); //TODO CACHE THIS TOO
        if(string.length() == 0) {
            entitySizeCacheX = 0f;
            return;
        }

        // Retrieve the current text as a string. Set the cache to 0 if the text has no lines
        final String[] lines = string.split("\n");
        if(lines.length == 0) {
            entitySizeCacheX = 0f;
            return;
        }

        // If lines are present, find the longest one and save its length
        float maxWidth = 0;
        for(int i = 0; i < lines.length; ++i) {
            final float w = FontSize.getWidth(lines[i]);
            if(w > maxWidth) maxWidth = w;
        }
        entitySizeCacheX = maxWidth;
    }




    public void updateEntitySizeCacheY() {

        // Set cache to 0 if the text is empty.
        final String string = getStyle(SimpleTextElmStyle.class).getText().getString(); //TODO CACHE THIS TOO
        if(string.length() == 0) {
            entitySizeCacheY = 0f;
            return;
        }

        // Calculate the number of lines. Set the cache to 0 if there are none
        final int lineNum = string.split("\n").length;
        if(lineNum == 0) {
            entitySizeCacheY = 0f;
            return;
        }

        // If lines are present, calculate the height
        entitySizeCacheY = (lineNum == 1 ? 0f : lineNum - 1) * 2 + lineNum * FontSize.getHeight();
    }








    /**
     * Calculates the in-world height of the TextDisplay entity associated with a TextDisplay or FancyTextDisplay.
     * <p> NOTICE: The height can be inaccurate as a lot of assumptions are made to calculate it. The returned value is the best possible approximation.
     * <p> NOTICE: Wrapped lines are counted as one.
     * @return The height in blocks.
     */
    public float calcEntityHeight() {

        // Return 0 if width cache is 0
        if(entitySizeCacheY == 0) {
            return 0;
        }

        // If not, get the transform and calculate the visual height using it
        final Transform t;
        /**/ if(this instanceof SimpleTextElm e) { t =                     e.__calcTransform();  }
        else if(this instanceof FancyTextElm  e) { t = e.__calcTransformFg(e.__calcTransform()); }
        else throw new RuntimeException("calcEntityHeight used on incompatible Elm type: " + this.getClass().getName());
        return entitySizeCacheY * t.getScale().y;
    }




    /**
     * Calculates the in-world width of the TextDisplay entity associated with a TextDisplay or FancyTextDisplay.
     * <p> NOTICE: The width can be inaccurate as a lot of assumptions are made to calculate it. The returned value is the best possible approximation.
     * <p> NOTICE: Wrapped lines are counted as one.
     * @return The width in blocks.
     */
    public float calcEntityWidth() {

        // Return 0 if width cache is 0
        if(entitySizeCacheX == 0) {
            return 0;
        }

        // If not, get the transform and calculate the visual width using it
        final Transform t;
        /**/ if(this instanceof SimpleTextElm e) { t =                     e.__calcTransform();  }
        else if(this instanceof FancyTextElm  e) { t = e.__calcTransformFg(e.__calcTransform()); }
        else throw new RuntimeException("calcEntityWidth used on incompatible Elm type: " + this.getClass().getName());
        return entitySizeCacheX * t.getScale().x;
    }
}
