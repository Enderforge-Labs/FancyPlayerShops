package com.snek.fancyplayershops.shop_ui.buy.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.framework.utils.Txt;

import net.minecraft.network.chat.Component;








public class BuyUi_BuyButton_S extends BuyUi_ConfirmButton_S {


    public BuyUi_BuyButton_S(final @NotNull Shop _shop){
        super(_shop);
    }


    @Override
    public @NotNull Component getDefaultText() {
        return new Txt("").get();
    }
}
