package com.snek.fancyplayershops.graphics.ui.details.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ShopTextElm;
import com.snek.framework.old.ui.basic.styles.SimpleTextElmStyle;
import com.snek.framework.old.utils.MinecraftUtils;
import com.snek.framework.old.utils.Txt;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;








/**
 * The title display of DetailsUi.
 * <p> It shows the name of the shop.
 */
public class DetailsUi_Title extends ShopTextElm {


    /**
     * Creates a new DetailsUiTitle.
     * @param _shop The target shop.
     */
    public DetailsUi_Title(final @NotNull Shop _shop) {
        super(_shop);
        updateDisplay();
    }




    /**
     * Updates the displayed name.
     */
    public void updateDisplay() {

        // Empty shop case
        final ItemStack _item = shop.getItem();
        if(_item.getItem() == Items.AIR) {
            getStyle(SimpleTextElmStyle.class).setText(new Txt(Shop.EMPTY_SHOP_NAME).get());
        }

        // Configured shop case
        else {
            getStyle(SimpleTextElmStyle.class).setText(new Txt(MinecraftUtils.getFancyItemName(_item)).bold().get());
        }

        // Flush style
        flushStyle();
    }
}
