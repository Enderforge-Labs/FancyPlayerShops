package com.snek.fancyplayershops.graphics.hud.mod_info.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;




public class ModInfo_0_TextB_S extends SimpleTextElmStyle {
    public ModInfo_0_TextB_S() {
        super();
    }


    @Override
    public @NotNull Component getDefaultText() {
        return new Txt()
            .cat("To get started, craft a ").cat(new Txt("Product Display").color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR)).cat(".\n")
            .cat("Once placed, you will be able to\n")
            .cat("select the item to sell, set a price, and\n")
            .cat("adjust other settings to your liking.\n")
            .cat(new Txt("Enjoy :3").color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR))
        .white().get();
    }


    @Override
    public int getDefaultFontSize() {
        return 6;
    }
}
