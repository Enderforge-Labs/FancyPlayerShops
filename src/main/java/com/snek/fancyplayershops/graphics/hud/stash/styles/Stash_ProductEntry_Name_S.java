package com.snek.fancyplayershops.graphics.hud.stash.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;








public class Stash_ProductEntry_Name_S extends SimpleTextElmStyle {
    public Stash_ProductEntry_Name_S() {
        super();
    }


    @Override
    public @NotNull TextAlignment getDefaultTextAlignment() {
        return TextAlignment.LEFT;
    }


    @Override
    public int getDefaultFontSize() {
        return 6;
    }


    @Override
    public @NotNull TextOverflowBehaviour getDefaultTextOverflowBehaviour() {
        return TextOverflowBehaviour.SCROLL;
    }
}
