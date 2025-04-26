package com.snek.fancyplayershops.implementations.ui.details;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.fancyplayershops.implementations.ui.misc.ShopPanelElm_S;








/**
 * The style of the DetailsUi_OwnerHeadBg UI element.
 */
public class DetailsUi_OwnerHeadBg_S extends ShopPanelElm_S {

    /**
     * Creates a new DetailsUi_OwnerHeadBg_S.
     */
    public DetailsUi_OwnerHeadBg_S(){
        super();
    }


    @Override
    public @NotNull Vector4i getDefaultColor(){
        return new Vector4i(50, 0, 0, 0);
    }
}
