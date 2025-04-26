package com.snek.fancyplayershops.implementations.ui.edit;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.misc.ShopTextInput;
import com.snek.fancyplayershops.implementations.ui.misc.ShopButton_S;
import com.snek.fancyplayershops.implementations.ui.misc.ShopFancyTextElm;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.text.Text;








/**
 * A button that allows the owner of the shop to change its stock limit.
 */
public class EditUi_StockLimitButton extends ShopTextInput {

    public EditUi_StockLimitButton(@NotNull Shop _shop) {
        super(_shop, 0.75f, ShopFancyTextElm.LINE_H, new Txt("Send the new stock limit in chat!").green().get());
        updateDisplay(null);
    }


    @Override
    public void updateDisplay(Text textOverride) {
        ((ShopButton_S)style).setText(textOverride != null ? textOverride : new Txt()
            .cat(new Txt(Utils.formatAmount(shop.getMaxStock(), true, true)).color(EditUi.RGB_STOCK_COLOR))
        .get());
        flushStyle();
    }




    @Override
    protected boolean messageCallback(String s) {
        try {

            // Try to set the new stock limit, update the display if it's valid
            if(shop.setStockLimit(Integer.parseInt(s))) updateDisplay(null);
            return true;

        } catch(NumberFormatException e) {
            try {

                // Try to set the new stock limit, update the display if it's valid
                if(shop.setStockLimit(Float.parseFloat(s))) updateDisplay(null);
                return true;

            } catch(NumberFormatException e2) {
                return false;
            }
        }
    }
}
