package com.snek.fancyplayershops.ui.details;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.details.styles.DetailsUi_OwnerHeadBg_S;
import com.snek.fancyplayershops.ui.misc.ShopPanelElm;








/**
 * An element that displays the background color of the owner's head.
 */
public class DetailsUi_OwnerHeadBg extends ShopPanelElm {

    /**
     * Creates a new DetailsUi_OwnerHeadBg
     */
    public DetailsUi_OwnerHeadBg(Shop _shop) {
        super(_shop, new DetailsUi_OwnerHeadBg_S());
    }
}
