package com.snek.fancyplayershops.implementations.ui.details;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.misc.ShopTextElm;
import com.snek.framework.ui.elements.TextElm;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;








/**
 * The main display of DetailsUi.
 * It shows the values of informations about the shop.
 */
public class DetailsUiDisplayValues extends ShopTextElm {




    /**
     * Creates a new DetailsUiDisplayValues.
     * @param _shop The target shop.
     */
    public DetailsUiDisplayValues(@NotNull Shop _shop){
        super(_shop, 1, DetailsUi.BACKGROUND_HEIGHT);
        updateDisplay();
    }




    /**
     * Updates the displayed values using the current item name, price and stock.
     */
    public void updateDisplay(){

        // Calculate the color of the stock amount and retrieve the owner's name
        float factor = 1.0f - shop.getStock() / 1000f;
        Vector3i col = Utils.HSVtoRGB(new Vector3f(DetailsUi.C_HSV_STOCK_LOW).add(new Vector3f(DetailsUi.C_HSV_STOCK_HIGH).sub(DetailsUi.C_HSV_STOCK_LOW).mul(1.0f - (factor * factor))));
        String ownerName = MinecraftUtils.getOfflinePlayerName(shop.getOwnerUuid(), shop.getWorld().getServer());


        // Empty shop case
        if(shop.getItem().getItem() == Items.AIR) {
            ((TextElmStyle)style).setText(new Txt()
                .cat(new Txt("-").lightGray())
                .cat(new Txt("\n-").lightGray())
                .cat("\n" + ownerName)
            .get());
        }

        // Configured shop case
        else {
            double price = shop.getPrice();
            ((TextElmStyle)style).setText(new Txt()
                .cat(new Txt(price < 0.005 ? "Free" : Utils.formatPrice(price)).bold().color(DetailsUi.C_RGB_PRICE))
                .cat("\n").cat(new Txt(Utils.formatAmount(shop.getStock())).bold().color(col))
                .cat("\n" + ownerName)
            .get());
        }

        // Flush style
        flushStyle();
    }
}
