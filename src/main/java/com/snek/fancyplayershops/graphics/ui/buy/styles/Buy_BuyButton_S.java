package com.snek.fancyplayershops.graphics.ui.buy.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;








public class Buy_BuyButton_S extends Buy_ConfirmButton_S {


    public Buy_BuyButton_S(final @NotNull ProductDisplay display) {
        super(display);
    }


    @Override
    public @NotNull Component getDefaultText() {
        return new Txt("").get();
    }
}
