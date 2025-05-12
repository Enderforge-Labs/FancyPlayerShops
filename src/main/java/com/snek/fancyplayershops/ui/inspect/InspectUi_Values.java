package com.snek.fancyplayershops.ui.inspect;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.ShopTextElm;
import com.snek.framework.ui.elements.styles.TextElmStyle;
import com.snek.framework.utils.Txt;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;








/**
 * Part of the main display of InspectUi.
 * <p> It shows the values of informations about the item.
 */
public class InspectUi_Values extends ShopTextElm {


    /**
     * Creates a new InspectUi_Values.
     * @param _shop The target shop.
     */
    public InspectUi_Values(@NotNull Shop _shop) {
        super(_shop);
        updateDisplay();
    }




    /**
     * Updates the displayed values using the current item name, price and stock.
     */
    public void updateDisplay() {

        // Empty shop case
        if(shop.getItem().getItem() == Items.AIR) {
            getStyle(TextElmStyle.class).setText(new Txt()
                .cat("-")
                .cat("\n-")
            .lightGray().get());
        }

        // Configured shop case
        else {
            final ResourceLocation id = BuiltInRegistries.ITEM.getKey(shop.getItem().getItem());
            getStyle(TextElmStyle.class).setText(new Txt()
                .cat(id.getPath())
                .cat("\n" + id.getNamespace())
            .white().get());
        }

        // Flush style
        flushStyle();
    }
}
