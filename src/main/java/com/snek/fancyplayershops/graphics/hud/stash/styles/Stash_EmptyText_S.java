package com.snek.fancyplayershops.graphics.hud.stash.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;




public class Stash_EmptyText_S extends SimpleTextElmStyle {
    public Stash_EmptyText_S() {
        super();
    }


    @Override
    public @NotNull Component getDefaultText() {
        return new Txt("Your stash is empty :3").white().italic().get();
    }
}
