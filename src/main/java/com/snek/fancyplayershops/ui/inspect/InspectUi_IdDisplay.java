package com.snek.fancyplayershops.ui.inspect;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.ShopTextElm;
import com.snek.framework.ui.elements.styles.TextElmStyle;
import com.snek.framework.utils.Txt;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;








/**
 * A UI element that shows the ID of the item.
 */
public class InspectUi_IdDisplay extends ShopTextElm {


    /**
     * Creates a new InspectUi_Values.
     * @param _shop The target shop.
     */
    public InspectUi_IdDisplay(@NotNull Shop _shop) {
        super(_shop);
        updateDisplay();
    }




    /**
     * Updates the displayed value.
     */
    public void updateDisplay() {

        getStyle(TextElmStyle.class).setText(new Txt()
            .cat(new Txt("ID: ").lightGray())
            .cat(new Txt(
                shop.getItem().getItem() == Items.AIR ? "-" :
                BuiltInRegistries.ITEM.getKey(shop.getItem().getItem()).getPath()
            ).white())
        .get());

        // Flush style
        flushStyle();
    }
}
