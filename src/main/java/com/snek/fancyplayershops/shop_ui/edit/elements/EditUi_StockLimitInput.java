package com.snek.fancyplayershops.shop_ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.shop_ui.edit.EditUi;
import com.snek.fancyplayershops.shop_ui.edit.styles.EditUi_Input_S;
import com.snek.fancyplayershops.shop_ui.misc.elements.ShopTextInput;
import com.snek.fancyplayershops.shop_ui.misc.styles.ShopTextInput_S;
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
        super(_shop, null, "Change stock limit", new Txt("Send the new stock limit in chat!").color(ShopManager.SHOP_ITEM_NAME_COLOR).bold().get(), new EditUi_Input_S(_shop));
        updateDisplay(null);
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        getStyle(ShopTextInput_S.class).setText(textOverride != null ? textOverride : new Txt()
            .cat(new Txt("Stock limit: ").lightGray())
            .cat(new Txt(Utils.formatAmount(shop.getMaxStock(), true, true)).white())
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




    @Override
    public float getInteractionSizeRight() {
        return getAbsSize().x / 2f - EditUi.COLOR_SELECTOR_W;
    }
}
