package com.snek.fancyplayershops.hud_ui.stash.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.data_types.ui.TextAlignment;
import com.snek.framework.ui.elements.styles.TextElmStyle;








public class StashHud_ItemName_S extends TextElmStyle {
    public StashHud_ItemName_S() {
        super();
    }


    @Override
    public @NotNull TextAlignment getDefaultTextAlignment(){
        return TextAlignment.LEFT;
    }
}
