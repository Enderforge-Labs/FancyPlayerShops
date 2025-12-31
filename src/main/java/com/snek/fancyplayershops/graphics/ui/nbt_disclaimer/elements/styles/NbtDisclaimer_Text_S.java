package com.snek.fancyplayershops.graphics.ui.nbt_disclaimer.elements.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;




public class NbtDisclaimer_Text_S extends SimpleTextElmStyle {
    public NbtDisclaimer_Text_S() {
        super();
    }


    @Override
    public @NotNull Component getDefaultText() {
        return new Txt(
            "This display contains products with mixed NBT data.\n" +
            "\n" +
            "You might receive any variation of the item shown.\n" +
            "This includes energy level, upgrades, enchantments, \n" +
            "and other data.\n" +
            "Buy at your own risk!"
        ).white().italic().get();
    }


    @Override //TODO replace with font size override
    public @NotNull Transform getDefaultTransform() {
        return super.getDefaultTransform().scale(0.5f);
    }
}
