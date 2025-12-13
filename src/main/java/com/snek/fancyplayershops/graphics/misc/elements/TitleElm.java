package com.snek.fancyplayershops.graphics.misc.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;








public class TitleElm extends SimpleTextElm {
    public static final float DEFAULT_W = 0.9f;


    /**
     * Creates a new TitleElm.
     * @param _world The world to create this element in.
     * @param defaultText The initial text to display when the element is created.
     */
    public TitleElm(final @NotNull ServerLevel _world, final @NotNull Component defaultText) {
        super(_world, defaultText, TextAlignment.CENTER, TextOverflowBehaviour.SCROLL);
    }


    /**
     * Creates a new TitleElm.
     * @param _world The world to create this element in.
     * @param defaultText The initial text to display when the element is created.
     */
    public TitleElm(final @NotNull ServerLevel _world, final @NotNull String defaultText) {
        this(_world, Component.literal(defaultText));
    }


    /**
     * Updates the displayed text.
     * @param text The text to display.
     */
    public void updateDisplay(final @NotNull Component text) {
        getStyle(SimpleTextElmStyle.class).setText(text);
        flushStyle();
    }
}
