package com.snek.fancyplayershops.graphics.hud.mod_info.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;




public class ModInfo_0_TextA_S extends SimpleTextElmStyle {
    public ModInfo_0_TextA_S() {
        super();
    }


    @Override
    public @NotNull Component getDefaultText() {
        return new Txt()
            .cat(new Txt("Shops").color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR)).cat(" are a great way to sell your items.\n")
            .cat("Other players can purchase products remotely\n")
            .cat("or visit your shops for bulk options and orders.\n")
        .white().get();
    }


    @Override
    public int getDefaultFontSize() {
        return 6;
    }
}
