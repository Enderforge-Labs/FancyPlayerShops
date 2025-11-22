package com.snek.fancyplayershops.graphics.misc.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.graphics.misc.styles.TitleElm_S;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;








public class TitleElm extends SimpleTextElm {


    /**
     * Creates a new TitleElm.
     * @param _world The world to create this element in.
     * @param defaultText The initial text to display when the element is created.
     */
    public TitleElm(final @NotNull ServerLevel _world, final @NotNull Component defaultText) {
        super(_world, new TitleElm_S());
        updateDisplay(defaultText);
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
