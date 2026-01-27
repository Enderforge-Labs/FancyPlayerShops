package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.graphics.ui.buy.BuyCanvas;
import com.snek.frameworklib.graphics.basic.elements.TextElm;
import com.snek.frameworklib.graphics.basic.styles.TextStyle;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;








public class Buy_PriceDisplay extends TextElm {
    private final @NotNull BuyCanvas menu;


    public Buy_PriceDisplay(final @NotNull ProductDisplay display, final @NotNull BuyCanvas _menu) {
        super(display.getLevel());
        menu = _menu;
    }


    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        updateDisplay();
        super.spawn(pos, animate);
    }


    public void updateDisplay() {
        final ProductDisplay display = GetDisplay.get(this);
        final Txt priceTxt = new Txt(Utils.formatPrice(display.getPrice() * menu.getAmount()));

        getStyle(TextStyle.class).setText(new Txt()
            .cat(new Txt("Total: ").lightGray())
            .cat(display.getuser().getUUID().equals(display.getOwnerUuid()) ? priceTxt.lightGray().strikethrough().cat(new Txt(" 0").white().noStrikethrough()) : priceTxt.white() )
        .get());
        flushStyle();
    }
}
