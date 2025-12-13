package com.snek.fancyplayershops;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.graphics.ui.ShopContext;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.frameworklib.graphics.core.Context;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.UtilityClassBase;




public final class GetShop extends UtilityClassBase {
    public static @NotNull Shop get(final @NotNull Div _this) {
        final Context context = _this.getCanvas().getContext();
        if(context instanceof ShopContext) {
            return ((ShopContext)context).getShop();
        }
        else {
            FancyPlayerShops.LOGGER.error("GetShop.get() used from non-shop graphic element", new RuntimeException());
            return null;
        }
    }
}
