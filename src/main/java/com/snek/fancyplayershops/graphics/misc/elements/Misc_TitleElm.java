package com.snek.fancyplayershops.graphics.misc.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;








public class Misc_TitleElm extends SimpleTextElm {
    public static final float DEFAULT_W = 0.9f;


    /**
     * Creates a new TitleElm.
     * @param level The level to create this element in.
     * @param defaultText The initial text to display when the element is created.
     */
    public Misc_TitleElm(final @NotNull ServerLevel level, final @NotNull Component defaultText) {
        super(level, defaultText, TextAlignment.CENTER, TextOverflowBehaviour.SCROLL);
    }


    /**
     * Creates a new TitleElm.
     * @param level The level to create this element in.
     * @param defaultText The initial text to display when the element is created.
     */
    public Misc_TitleElm(final @NotNull ServerLevel level, final @NotNull String defaultText) {
        this(level, Component.literal(defaultText));
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
