package com.snek.fancyplayershops.shop_ui.details.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.shop_ui.misc.elements.ShopTextElm;
import com.snek.framework.ui.basic.styles.TextElmStyle;
import com.snek.framework.utils.Txt;








/**
 * Part of the main display of DetailsUi.
 * <p> It shows the names of informations about the shop.
 */
public class DetailsUi_Names extends ShopTextElm {


    /**
     * Creates a new DetailsUiDisplayNames.
     * @param _shop The target shop.
     */
    public DetailsUi_Names(final @NotNull Shop _shop) {
        super(_shop);
        updateDisplay();
    }




    /**
     * Updates the displayed values.
     */
    public void updateDisplay() {

        getStyle(TextElmStyle.class).setText(new Txt()
            .cat("Price:")
            .cat("\nStock:")
            .cat("\nOwner:")
        .lightGray().get());

        // Flush style
        flushStyle();
    }
}
