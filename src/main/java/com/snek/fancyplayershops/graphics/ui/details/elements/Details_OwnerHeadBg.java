package com.snek.fancyplayershops.graphics.ui.details.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.details.styles.Details_OwnerHeadBg_S;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ShopPanelElm;








/**
 * A UI element used for the background color of the owner's head.
 */
public class Details_OwnerHeadBg extends ShopPanelElm {

    /**
     * Creates a new DetailsUi_OwnerHeadBg
     */
    public Details_OwnerHeadBg(final @NotNull Shop _shop) {
        super(_shop, new Details_OwnerHeadBg_S());
    }
}
