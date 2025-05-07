package com.snek.framework.ui.elements.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.containers.Flagged;
import com.snek.framework.data_types.ui.TextAlignment;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.Txt;

import net.minecraft.network.chat.Component;








public class TextElmStyle extends ElmStyle {
    public static final float DEFAULT_TEXT_SCALE = 0.3f;

    private @NotNull Flagged<@NotNull Component>     text          = null;
    private @NotNull Flagged<@NotNull TextAlignment> textAlignment = null;
    private @NotNull Flagged<@NotNull Integer>       textOpacity   = null;




    @Override
    public @NotNull Transform getDefaultTransform () {
        return new Transform().scale(DEFAULT_TEXT_SCALE);
    }




    /**
     * Creates a new default TextElmStyle.
     */
    public TextElmStyle() {
        super();
    }


    @Override
    public void resetAll() {
        resetText();
        resetTextAlignment();
        resetTextOpacity();
        super.resetAll();
    }




    @Override
    public @NotNull Animation getDefaultPrimerAnimation() {
        return new Animation(
            new Transition()
            .targetOpacity(0)
        );
    }


    @Override
    public @NotNull Animation getDefaultSpawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.S_TIME, Easings.sineOut)
            .targetOpacity(255)
        );
    }


    @Override
    public @NotNull Animation getDefaultDespawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.D_TIME, Easings.sineOut)
            .targetOpacity(0)
        );
    }




    // Default value providers
    public @NotNull Component     getDefaultText         () { return new Txt().get()     ; }
    public @NotNull TextAlignment getDefaultTextAlignment() { return TextAlignment.CENTER; }
    public          int           getDefaultTextOpacity  () { return 255                 ; }


    // Reset functions
    public void resetText         () { text          = Flagged.from(getDefaultText()         ); }
    public void resetTextAlignment() { textAlignment = Flagged.from(getDefaultTextAlignment()); }
    public void resetTextOpacity  () { textOpacity   = Flagged.from(getDefaultTextOpacity()  ); }


    // Setters
    public void setText         (final @NotNull Component     _text         ) { text         .set(_text         ); }
    public void setTextAlignment(final @NotNull TextAlignment _textAlignment) { textAlignment.set(_textAlignment); }
    public void setTextOpacity  (final          int           _textOpacity  ) { textOpacity  .set(_textOpacity  ); }


    // Flagged getters
    public @NotNull Flagged<@NotNull Component>     getFlaggedText         () { return text;          }
    public @NotNull Flagged<@NotNull TextAlignment> getFlaggedTextAlignment() { return textAlignment; }
    public @NotNull Flagged<@NotNull Integer>       getFlaggedTextOpacity  () { return textOpacity;   }


    // Getters
    public @NotNull Component     getText         () { return text         .get(); }
    public @NotNull TextAlignment getTextAlignment() { return textAlignment.get(); }
    public          int           getTextOpacity  () { return textOpacity  .get(); }


    // Edit getters
    public @NotNull Component editText          () { return text         .edit(); }
    //!                       editTextAlignment Primitive types cannot be edited
    //!                       editTextOpacity   Primitive types cannot be edited
}
