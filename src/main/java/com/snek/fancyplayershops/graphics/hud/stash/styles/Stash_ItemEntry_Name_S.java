package com.snek.fancyplayershops.graphics.hud.stash.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;








public class Stash_ItemEntry_Name_S extends SimpleTextElmStyle {
    public Stash_ItemEntry_Name_S() {
        super();
    }


    @Override
    public @NotNull TextAlignment getDefaultTextAlignment() {
        return TextAlignment.LEFT;
    }


    @Override
    public @NotNull Transform getDefaultTransform() {
        return super.getDefaultTransform().scale(0.5f); //FIXME move to confiurable style's font size or something
    }


    @Override
    public @NotNull TextOverflowBehaviour getDefaultTextOverflowBehaviour() {
        return TextOverflowBehaviour.SCROLL;
    }
}
