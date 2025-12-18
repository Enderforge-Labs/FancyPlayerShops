package com.snek.fancyplayershops.graphics.ui.inspect.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;




public class Inspect_ModDisplay_S extends SimpleTextElmStyle {
    public Inspect_ModDisplay_S() {
        super();
    }


    @Override
    public @NotNull TextOverflowBehaviour getDefaultTextOverflowBehaviour() {
        return TextOverflowBehaviour.SCROLL;
    }
}