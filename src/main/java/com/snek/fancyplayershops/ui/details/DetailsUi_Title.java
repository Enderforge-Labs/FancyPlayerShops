package com.snek.fancyplayershops.ui.details;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.ShopTextElm;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;








/**
 * The title display of DetailsUi.
 * It shows the name of the shop.
 */
public class DetailsUi_Title extends ShopTextElm {




    /**
     * Creates a new DetailsUiTitle.
     * @param _shop The target shop.
     */
    public DetailsUi_Title(@NotNull Shop _shop){
        super(_shop, 1, DetailsUi.BACKGROUND_HEIGHT);
        updateDisplay();
    }




    /**
     * Updates the displayed name.
     */
    public void updateDisplay(){

        // Empty shop case
        final ItemStack _item = shop.getItem();
        if(_item.getItem() == Items.AIR) {
            ((TextElmStyle)style).setText(new Txt(Shop.EMPTY_SHOP_NAME).get());
        }

        // Configured shop case
        else {
            ((TextElmStyle)style).setText(new Txt(MinecraftUtils.getFancyItemName(_item)).bold().get());
        }

        // Flush style
        flushStyle();
    }
}
