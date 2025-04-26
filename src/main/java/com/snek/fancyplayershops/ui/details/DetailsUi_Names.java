package com.snek.fancyplayershops.ui.details;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.ShopTextElm;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.utils.Txt;








/**
 * The main display of DetailsUi.
 * It shows the names of informations about the shop.
 */
public class DetailsUi_Names extends ShopTextElm {




    /**
     * Creates a new DetailsUiDisplayNames.
     * @param _shop The target shop.
     */
    public DetailsUi_Names(@NotNull Shop _shop){
        super(_shop, 1, DetailsUi.BACKGROUND_HEIGHT);
        updateDisplay();
    }




    /**
     * Updates the displayed values.
     */
    public void updateDisplay(){

        ((TextElmStyle)style).setText(new Txt()
            .cat(new Txt("Price:").lightGray())
            .cat(new Txt("\nStock:").lightGray())
            .cat(new Txt("\nOwner:").lightGray())
        .get());

        // Flush style
        flushStyle();
    }
}
