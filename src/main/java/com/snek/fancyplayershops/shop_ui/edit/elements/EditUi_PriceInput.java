package com.snek.fancyplayershops.shop_ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.shop_ui.edit.EditUi;
import com.snek.fancyplayershops.shop_ui.edit.styles.EditUi_Input_S;
import com.snek.fancyplayershops.shop_ui.misc.elements.ShopTextInput;
import com.snek.fancyplayershops.shop_ui.misc.styles.ShopTextInput_S;
import com.snek.framework.old.utils.Txt;
import com.snek.framework.old.utils.Utils;

import net.minecraft.network.chat.Component;








/**
 * A button that allows the owner of the shop to change its price.
 */
public class EditUi_PriceInput extends ShopTextInput {

    /**
     * Creates a new EditUiPriceButton.
     * @param _shop The target shop.
     */
    public EditUi_PriceInput(final @NotNull Shop _shop) {
        super(_shop, null, "Change price", new Txt("Send the new price in chat!").color(ShopManager.SHOP_ITEM_NAME_COLOR).bold().get(), new EditUi_Input_S(_shop));
        updateDisplay(null);
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        getStyle(ShopTextInput_S.class).setText(textOverride != null ? textOverride : new Txt()
            .cat(new Txt("Price: ").lightGray())
            .cat(new Txt(Utils.formatPrice(shop.getPrice())).white())
        .get());
        flushStyle();
    }


    @Override
    protected boolean messageCallback(final @NotNull String s) {
        try {

            // Try to set the new price and update the display if it's valid
            if(shop.setPrice(Double.parseDouble(s))) updateDisplay(null);
            return true;

        } catch(NumberFormatException e) {
            return false;
        }
    }


    @Override
    public float getInteractionSizeRight() {
        return super.getInteractionSizeRight() - EditUi.COLOR_SELECTOR_W;
    }
}
