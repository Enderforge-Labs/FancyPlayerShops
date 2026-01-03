package com.snek.fancyplayershops.graphics.hud._mainmenu_.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;




public class MainMenu_Summary_S extends SimpleTextElmStyle {


    public MainMenu_Summary_S() {
        super();
    }


    @Override //TODO replace with font size override
    public @NotNull Transform getDefaultTransform() {
        return super.getDefaultTransform().scale(0.5f);
    }
}
