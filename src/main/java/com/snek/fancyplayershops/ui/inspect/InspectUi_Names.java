package com.snek.fancyplayershops.ui.inspect;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.ShopTextElm;
import com.snek.framework.ui.elements.styles.TextElmStyle;
import com.snek.framework.utils.Txt;








/**
 * Part of the main display of InspectUi.
 * <p> It shows the names of informations about the item.
 */
public class InspectUi_Names extends ShopTextElm {


    /**
     * Creates a new InspectUi_Names.
     * @param _shop The target shop.
     */
    public InspectUi_Names(final @NotNull Shop _shop) {
        super(_shop);
        updateDisplay();
    }




    /**
     * Updates the displayed values.
     */
    public void updateDisplay() {

        getStyle(TextElmStyle.class).setText(new Txt()
            .cat("ID:")
            .cat("\nSource:")
        .lightGray().get());

        // Flush style
        flushStyle();
    }
}
