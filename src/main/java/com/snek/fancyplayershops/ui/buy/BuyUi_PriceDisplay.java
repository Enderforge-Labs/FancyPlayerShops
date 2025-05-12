package com.snek.fancyplayershops.ui.buy;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.ShopTextElm;
import com.snek.framework.ui.elements.styles.TextElmStyle;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;








public class BuyUi_PriceDisplay extends ShopTextElm {


    public BuyUi_PriceDisplay(final @NotNull Shop _shop) {
        super(_shop);
        updateDisplay(1);
    }


    public void updateDisplay(final int amount) {
        final Txt priceTxt = new Txt(Utils.formatPrice(shop.getPrice() * amount));

        getStyle(TextElmStyle.class).setText(new Txt()
            .cat(new Txt("Total: ").lightGray())
            .cat(shop.getuser().getUUID().equals(shop.getOwnerUuid()) ? priceTxt.lightGray().strikethrough() : priceTxt.white() )
        .get());
    }
}
