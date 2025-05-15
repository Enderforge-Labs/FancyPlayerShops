package com.snek.fancyplayershops.ui.buy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.buy.styles.BuyUi_Input_S;
import com.snek.fancyplayershops.ui.misc.ShopTextInput;
import com.snek.framework.ui.elements.styles.TextElmStyle;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.network.chat.Component;








public class BuyUi_AmountInputDisplay extends ShopTextInput {
    private final @NotNull BuyUi menu;


    public BuyUi_AmountInputDisplay(final @NotNull Shop _shop, final @NotNull BuyUi _menu) {
        super(_shop, null, "Specify buy amount", new Txt("Send the amount in chat!").color(FancyPlayerShops.SHOP_ITEM_NAME_COLOR).get(), new BuyUi_Input_S(_shop));
        menu = _menu;
        updateDisplay(null);
    }


    @Override
    protected boolean messageCallback(final @NotNull String s) {
        try {

            // Try to set the new amount
            return menu.attemptChangeAmount(shop.getuser(), Integer.parseInt(s));

        } catch(NumberFormatException e) {
            try {

                // Try to set the new amount
                return menu.attemptChangeAmount(shop.getuser(), Float.parseFloat(s));

            } catch(NumberFormatException e2) {
                return false;
            }
        }
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        getStyle(TextElmStyle.class).setText(textOverride != null ? textOverride : new Txt()
            .cat(new Txt("Amount: ").lightGray())
            .cat(new Txt(Utils.formatAmount(menu.getAmount())).white())
            .cat(new Txt(" / " + shop.getStock()).lightGray())
        .get());
        flushStyle();
    }
}
