package com.snek.fancyplayershops.ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;
import com.snek.framework.ui.functional.styles.ButtonElmStyle;








/**
 * The style of the generic ShopButton UI element.
 */
public class ShopButton_S extends ButtonElmStyle {
    final protected Shop shop;


    /**
     * Creates a new ShopButton_S.
     */
    public ShopButton_S(final @NotNull Shop _shop){
        super();
        shop = _shop;
    }


    @Override
    public Vector3i getDefaultBgColor(){
        return shop.getThemeColor1();
    }
}
