package com.snek.fancyplayershops.hud_ui.stash.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.ui.TextAlignment;
import com.snek.framework.ui.elements.styles.TextElmStyle;








public class StashHud_ItemCount_S extends TextElmStyle {
    public StashHud_ItemCount_S() {
        super();
    }


    @Override
    public @NotNull TextAlignment getDefaultTextAlignment(){
        return TextAlignment.LEFT;
    }

    @Override
    public @NotNull Transform getDefaultTransform(){
        return super.getDefaultTransform().scale(0.5f);
    }
}
