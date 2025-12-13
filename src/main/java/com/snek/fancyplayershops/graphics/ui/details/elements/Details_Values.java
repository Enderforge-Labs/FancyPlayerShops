package com.snek.fancyplayershops.graphics.ui.details.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.details.DetailsCanvas;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ShopTextElm;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.world.item.Items;








/**
 * Part of the main display of DetailsUi.
 * <p> It shows the values of informations about the shop.
 */
public class Details_Values extends ShopTextElm {
    final @NotNull String ownerName;


    /**
     * Creates a new DetailsUiDisplayValues.
     * @param _shop The target shop.
     */
    public Details_Values(@NotNull Shop _shop) {
        super(_shop);
        ownerName = MinecraftUtils.getOfflinePlayerName(shop.getOwnerUuid(), FrameworkLib.getServer());
    }


    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        updateDisplay();
        super.spawn(pos, animate);
    }




    /**
     * Updates the displayed values using the current item name, price and stock.
     */
    public void updateDisplay() {

        // Calculate the color of the stock amount and retrieve the owner's name
        final float factor = Math.min(1.0f, (float)shop.getStock() / shop.getMaxStock());
        final Vector3i col = Utils.interpolateRGB(DetailsCanvas.C_HSV_STOCK_LOW, DetailsCanvas.C_HSV_STOCK_HIGH, (float)Easings.quadOut.compute(factor));

        // Empty shop case
        if(shop.getItem().is(Items.AIR)) {
            getStyle(SimpleTextElmStyle.class).setText(new Txt()
                .cat(new Txt("-").lightGray())
                .cat(new Txt("\n-").lightGray())
                .cat("\n" + ownerName)
            .get());
        }

        // Configured shop case
        else {
            long price = shop.getPrice();
            getStyle(SimpleTextElmStyle.class).setText(new Txt()
                .cat(new Txt(price == 0 ? "Free" : Utils.formatPrice(price)).color(DetailsCanvas.C_RGB_PRICE))
                .cat("\n").cat(new Txt(Utils.formatAmount(shop.getStock())).color(col))
                .cat("\n" + ownerName)
            .get());
        }

        // Flush style
        flushStyle();
    }
}
