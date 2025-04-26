package com.snek.fancyplayershops.implementations.ui.details;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.details.styles.DetailsUi_OwnerHeadBg_S;
import com.snek.fancyplayershops.implementations.ui.misc.ShopPanelElm;








/**
 * An element that displays the background color of the owner's head.
 */
public class DetailsUi_OwnerHeadBg extends ShopPanelElm {

    /**
     * Creates a new DetailsUi_OwnerHeadBg
     */
    public DetailsUi_OwnerHeadBg(Shop _shop){
        super(_shop, new DetailsUi_OwnerHeadBg_S());
    }
}
