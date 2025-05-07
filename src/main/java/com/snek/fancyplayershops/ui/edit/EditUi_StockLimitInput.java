package com.snek.fancyplayershops.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.details.DetailsUi;
import com.snek.fancyplayershops.ui.misc.ShopTextInput;
import com.snek.fancyplayershops.ui.misc.styles.ShopTextInput_S;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.network.chat.Component;








/**
 * A button that allows the owner of the shop to change its stock limit.
 */
public class EditUi_StockLimitInput extends ShopTextInput {

    /**
     * Creates a new EditUi_StockLimitInput.
     * @param _shop The target shop.
     */
    public EditUi_StockLimitInput(final @NotNull Shop _shop) {
        super(_shop, null, "Change stock limit", new Txt("Send the new stock limit in chat!").color(FancyPlayerShops.SHOP_ITEM_NAME_COLOR).get());
        updateDisplay(null);
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        getStyle(ShopTextInput_S.class).setText(textOverride != null ? textOverride : new Txt()
            .cat(new Txt(Utils.formatAmount(shop.getMaxStock(), true, true)).color(DetailsUi.C_HSV_STOCK_HIGH))
        .get());
        flushStyle();
    }




    @Override
    protected boolean messageCallback(final @NotNull String s) {
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
