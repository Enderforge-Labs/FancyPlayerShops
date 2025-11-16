package com.snek.fancyplayershops.graphics.hud.stash.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.old.data_types.animations.Transform;
import com.snek.framework.old.data_types.ui.TextAlignment;
import com.snek.framework.old.ui.basic.styles.SimpleTextElmStyle;








public class StashHud_ItemNameCount_S extends SimpleTextElmStyle {
    public StashHud_ItemNameCount_S() {
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
