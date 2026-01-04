package com.snek.fancyplayershops.graphics.hud.mod_info.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;




public class ModInfo_1_TextB_S extends SimpleTextElmStyle {
    public ModInfo_1_TextB_S() {
        super();
    }


    @Override
    public @NotNull Component getDefaultText() {
        return new Txt()
            .cat(new Txt("Basic").color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR)).cat(" product displays can be crafted\n")
            .cat("using Vanilla materials.\n")
            .cat("They have a maximum stock of " + 123456 + " items.") //FIXME actually retrieve the max stock from configs
        .white().get();
    }


    @Override
    public int getDefaultFontSize() {
        return 6;
    }
}
