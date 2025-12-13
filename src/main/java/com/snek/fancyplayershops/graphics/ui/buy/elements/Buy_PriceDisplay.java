package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.buy.BuyCanvas;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ShopTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;








public class Buy_PriceDisplay extends ShopTextElm {
    private final @NotNull BuyCanvas menu;


    public Buy_PriceDisplay(final @NotNull Shop _shop, final @NotNull BuyCanvas _menu) {
        super(_shop);
        menu = _menu;
    }


    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        updateDisplay();
        super.spawn(pos, animate);
    }


    public void updateDisplay() {
        final Txt priceTxt = new Txt(Utils.formatPrice(shop.getPrice() * menu.getAmount()));

        getStyle(SimpleTextElmStyle.class).setText(new Txt()
            .cat(new Txt("Total: ").lightGray())
            .cat(shop.getuser().getUUID().equals(shop.getOwnerUuid()) ? priceTxt.lightGray().strikethrough().cat(new Txt(" 0").white().noStrikethrough()) : priceTxt.white() )
        .get());
        flushStyle();
    }
}
