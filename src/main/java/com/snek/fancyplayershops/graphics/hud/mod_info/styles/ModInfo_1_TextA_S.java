package com.snek.fancyplayershops.graphics.hud.mod_info.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;




public class ModInfo_1_TextA_S extends SimpleTextElmStyle {
    public ModInfo_1_TextA_S() {
        super();
    }


    @Override
    public @NotNull Component getDefaultText() {
        return new Txt()
            .cat("They can be ").cat(new Txt("upgraded").color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR)).cat(" using various items\n")
            .cat("to increase their storage, let them restock\n")
            .cat("automatically and retrieve items wirelessly.")
        .white().get();
    }


    @Override
    public int getDefaultFontSize() {
        return 6;
    }
}
