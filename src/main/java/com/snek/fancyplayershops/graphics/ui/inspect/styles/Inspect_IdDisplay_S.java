package com.snek.fancyplayershops.graphics.ui.inspect.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;




public class Inspect_IdDisplay_S extends SimpleTextElmStyle {
    public Inspect_IdDisplay_S() {
        super();
    }


    @Override
    public @NotNull TextOverflowBehaviour getDefaultTextOverflowBehaviour() {
        return TextOverflowBehaviour.SCROLL;
    }
}
