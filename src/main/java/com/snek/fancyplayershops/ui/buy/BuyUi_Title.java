package com.snek.fancyplayershops.ui.buy;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.ShopTextElm;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;








/**
 * A text display that shows the name of the shop that is currently being bought from.
 */
public class BuyUi_Title extends ShopTextElm {

    /**
     * Creates a new EditUiTitle.
     * @param _shop The target shop.
     */
    public BuyUi_Title(@NotNull Shop _shop) {
        super(_shop);
        updateDisplay();
    }


    /**
     * Updates the displayed text, reading data from the target shop.
     */
    public void updateDisplay() {
        ((TextElmStyle)style).setText(new Txt()
            .cat(new Txt("Buying: ").white())
            .cat(MinecraftUtils.getFancyItemName(shop.getItem()))
        .get());
        flushStyle();
    }
}