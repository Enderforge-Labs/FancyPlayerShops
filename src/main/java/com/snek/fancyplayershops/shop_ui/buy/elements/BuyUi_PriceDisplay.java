package com.snek.fancyplayershops.shop_ui.buy.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.shop_ui.buy.BuyUi;
import com.snek.fancyplayershops.shop_ui.misc.elements.ShopTextElm;
import com.snek.framework.ui.elements.styles.TextElmStyle;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;








public class BuyUi_PriceDisplay extends ShopTextElm {
    private final @NotNull BuyUi menu;


    public BuyUi_PriceDisplay(final @NotNull Shop _shop, final @NotNull BuyUi _menu) {
        super(_shop);
        menu = _menu;
        updateDisplay();
    }


    public void updateDisplay() {
        final Txt priceTxt = new Txt(Utils.formatPrice(shop.getPrice() * menu.getAmount()));

        getStyle(TextElmStyle.class).setText(new Txt()
            .cat(new Txt("Total: ").lightGray())
            .cat(shop.getuser().getUUID().equals(shop.getOwnerUuid()) ? priceTxt.lightGray().strikethrough().cat(new Txt(" 0").white().noStrikethrough()) : priceTxt.white() )
        .get());
        flushStyle();
    }
}
