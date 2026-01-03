package com.snek.fancyplayershops.graphics.hud.mainmenu.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;




public class ManageShops_EmptyText_S extends SimpleTextElmStyle {
    public ManageShops_EmptyText_S() {
        super();
    }


    @Override
    public @NotNull Component getDefaultText() {
        return new Txt(
            "You aren't selling any product!\n" +
            "Product displays you place will appear here,\n" +
            "organized by shop.\n" +
            "Click on the Info button to learn more."
        ).white().italic().get();
    }


    @Override //TODO replace with font size override
    public @NotNull Transform getDefaultTransform() {
        return super.getDefaultTransform().scale(0.5f);
    }
}
