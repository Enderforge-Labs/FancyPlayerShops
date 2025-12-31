package com.snek.fancyplayershops.graphics.ui.buy.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.frameworklib.graphics.functional.styles.FancyButtonElmStyle;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;




public class Buy_nbt_disclaimer_S extends FancyButtonElmStyle {
    public Buy_nbt_disclaimer_S() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultBgColor() {
        return new Vector3i(255, 0, 0);
    }


    @Override
    public int getDefaultBgAlpha() {
        return 96;
    }


    @Override
    public @NotNull Component getDefaultText() {
        return new Txt("MIXED NBTs").red().get();
    }
}
