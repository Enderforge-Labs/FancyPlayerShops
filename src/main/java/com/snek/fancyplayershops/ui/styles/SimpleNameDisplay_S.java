package com.snek.fancyplayershops.ui.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.styles.ShopUiBorder_S;
import com.snek.framework.ui.elements.styles.FancyTextElmStyle;








public class SimpleNameDisplay_S extends FancyTextElmStyle {
    protected final @NotNull Shop shop;


    public SimpleNameDisplay_S(final @NotNull Shop _shop) {
        super();
        shop = _shop;
    }


    @Override
    public @NotNull Vector3i getDefaultBgColor(){
        return ShopUiBorder_S.COLOR;
    }


    @Override
    public int getDefaultBgAlpha(){
        return 180;
    }
}
