package com.snek.fancyplayershops.graphics.hud.mainmenu.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;




public class MainMenu_EmptyText_S extends SimpleTextElmStyle {
    public MainMenu_EmptyText_S() {
        super();
    }


    @Override
    public @NotNull Component getDefaultText() {
        return new Txt("You don't own any shop! Click on the Info button to learn more.").white().italic().get();
    }

    @Override
    public @NotNull TextOverflowBehaviour getDefaultTextOverflowBehaviour() {
        return TextOverflowBehaviour.SCROLL;
    }
}
