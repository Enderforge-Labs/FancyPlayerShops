package com.snek.fancyplayershops.graphics.ui.inspect.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ShopTextElm;
import com.snek.framework.old.ui.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;








/**
 * A UI element that shows the mod of the item.
 */
public class InspectUi_ModDisplay extends ShopTextElm {


    /**
     * Creates a new InspectUi_ModDisplay.
     * @param _shop The target shop.
     */
    public InspectUi_ModDisplay(@NotNull Shop _shop) {
        super(_shop);
        updateDisplay();
    }




    /**
     * Updates the displayed value.
     */
    public void updateDisplay() {

        getStyle(SimpleTextElmStyle.class).setText(new Txt()
            .cat(new Txt("Mod: ").lightGray())
            .cat(new Txt(
                shop.getItem().getItem() == Items.AIR ? "-" :
                BuiltInRegistries.ITEM.getKey(shop.getItem().getItem()).getNamespace()
            ).white())
        .get());

        // Flush style
        flushStyle();
    }
}
