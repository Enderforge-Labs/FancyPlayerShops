package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ShopTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.Txt;








/**
 * A text display that shows the name of the shop that is currently being bought from.
 */
public class BuyUi_Title extends ShopTextElm {

    /**
     * Creates a new EditUiTitle.
     * @param _shop The target shop.
     */
    public BuyUi_Title(final @NotNull Shop _shop) {
        super(_shop);
        updateDisplay();
    }


    /**
     * Updates the displayed text, reading data from the target shop.
     */
    public void updateDisplay() {
        getStyle(SimpleTextElmStyle.class).setText(new Txt()
            .cat(new Txt("Buying: ").white())
            .cat(MinecraftUtils.getFancyItemName(shop.getItem()))
        .get());
        flushStyle();
    }
}