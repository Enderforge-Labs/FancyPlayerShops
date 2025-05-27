package com.snek.fancyplayershops.shop_ui.inspect.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.shop_ui.misc.elements.ShopTextElm;
import com.snek.framework.ui.basic.styles.TextElmStyle;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;

import net.minecraft.world.item.Items;








/**
 * A text display that shows the name of the item that is currently being inspected.
 */
public class InspectUi_Title extends ShopTextElm {

    /**
     * Creates a new InspectUi_Title.
     * @param _shop The target shop.
     */
    public InspectUi_Title(final @NotNull Shop _shop) {
        super(_shop);
        updateDisplay();
    }


    /**
     * Updates the displayed text, reading data from the target shop.
     */
    public void updateDisplay() {
        getStyle(TextElmStyle.class).setText(new Txt()
            .cat(
                shop.getItem().getItem() == Items.AIR ? new Txt("Empty shop").get() :
                MinecraftUtils.getFancyItemName(shop.getItem())
            ).get()
        );
        flushStyle();
    }
}