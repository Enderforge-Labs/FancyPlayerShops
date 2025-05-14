package com.snek.fancyplayershops.ui.details;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.ShopTextElm;
import com.snek.framework.ui.elements.styles.TextElmStyle;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.world.item.Items;








/**
 * Part of the main display of DetailsUi.
 * <p> It shows the values of informations about the shop.
 */
public class DetailsUi_Values extends ShopTextElm {
    final @NotNull String ownerName;


    /**
     * Creates a new DetailsUiDisplayValues.
     * @param _shop The target shop.
     */
    public DetailsUi_Values(@NotNull Shop _shop) {
        super(_shop);
        ownerName = MinecraftUtils.getOfflinePlayerName(shop.getOwnerUuid(), FancyPlayerShops.getServer());
        updateDisplay();
    }




    /**
     * Updates the displayed values using the current item name, price and stock.
     */
    public void updateDisplay() {

        // Calculate the color of the stock amount and retrieve the owner's name
        final float factor = Math.min(1.0f, (float)shop.getStock() / shop.getMaxStock());
        final Vector3i col = Utils.interpolateRGB(DetailsUi.C_HSV_STOCK_LOW, DetailsUi.C_HSV_STOCK_HIGH, (float)Easings.quadOut.compute(factor));

        // Empty shop case
        if(shop.getItem().getItem() == Items.AIR) {
            getStyle(TextElmStyle.class).setText(new Txt()
                .cat(new Txt("-").lightGray())
                .cat(new Txt("\n-").lightGray())
                .cat("\n" + ownerName)
            .get());
        }

        // Configured shop case
        else {
            long price = shop.getPrice();
            getStyle(TextElmStyle.class).setText(new Txt()
                .cat(new Txt(price == 0 ? "Free" : Utils.formatPrice(price)).color(DetailsUi.C_RGB_PRICE))
                .cat("\n").cat(new Txt(Utils.formatAmount(shop.getStock())).color(col))
                .cat("\n" + ownerName)
            .get());
        }

        // Flush style
        flushStyle();
    }
}
