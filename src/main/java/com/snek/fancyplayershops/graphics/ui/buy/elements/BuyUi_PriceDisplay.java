package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.buy.BuyUi;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ShopTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;








public class BuyUi_PriceDisplay extends ShopTextElm {
    private final @NotNull BuyUi menu;


    public BuyUi_PriceDisplay(final @NotNull Shop _shop, final @NotNull BuyUi _menu) {
        super(_shop);
        menu = _menu;
        updateDisplay();
    }


    public void updateDisplay() {
        final Txt priceTxt = new Txt(Utils.formatPrice(shop.getPrice() * menu.getAmount()));

        getStyle(SimpleTextElmStyle.class).setText(new Txt()
            .cat(new Txt("Total: ").lightGray())
            .cat(shop.getuser().getUUID().equals(shop.getOwnerUuid()) ? priceTxt.lightGray().strikethrough().cat(new Txt(" 0").white().noStrikethrough()) : priceTxt.white() )
        .get());
        flushStyle(false);
    }
}
