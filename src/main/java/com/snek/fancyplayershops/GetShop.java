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

        if(_this.getCanvas() == null) {
            FancyPlayerShops.LOGGER.error("GetShop.get() used orphan element", new RuntimeException());
            return null;
        }

        final Context context = _this.getCanvas().getContext();
        if(!(context instanceof ShopContext)) {
            FancyPlayerShops.LOGGER.error("GetShop.get() used from non-shop graphic element", new RuntimeException());
            return null;
        }

        return ((ShopContext)context).getShop();
    }
}
