package com.snek.fancyplayershops.ui.transfer.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.styles.ShopButton_S;
import com.snek.framework.utils.Txt;

import net.minecraft.network.chat.Component;








public class TransferUi_ConfirmButton_S extends ShopButton_S {


    public TransferUi_ConfirmButton_S(final @NotNull Shop _shop){
        super(_shop);
    }

    @Override
    public @NotNull Component getDefaultText() {
        return new Txt("Confirm").get();
    }
}
