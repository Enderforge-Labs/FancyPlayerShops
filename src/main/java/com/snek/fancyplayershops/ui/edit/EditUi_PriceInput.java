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

import net.minecraft.text.Text;








/**
 * A button that allows the owner of the shop to change its price.
 */
public class EditUi_PriceInput extends ShopTextInput {

    /**
     * Creates a new EditUiPriceButton.
     * @param _shop The target shop.
     */
    public EditUi_PriceInput(final @NotNull Shop _shop) {
        super(_shop, null, "Change price", new Txt("Send the new price in chat!").color(FancyPlayerShops.SHOP_ITEM_NAME_COLOR).get());
        updateDisplay(null);
    }


    @Override
    public void updateDisplay(final @Nullable Text textOverride) {
        getStyle(ShopTextInput_S.class).setText(textOverride != null ? textOverride : new Txt()
            .cat(new Txt(Utils.formatPrice(shop.getPrice())).color(DetailsUi.C_RGB_PRICE))
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
}
