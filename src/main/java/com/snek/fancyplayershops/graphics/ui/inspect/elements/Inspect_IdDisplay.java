package com.snek.fancyplayershops.graphics.ui.inspect.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ShopTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;








/**
 * A UI element that shows the ID of the item.
 */
public class Inspect_IdDisplay extends ShopTextElm {


    /**
     * Creates a new InspectUi_IdDisplay.
     * @param _shop The target shop.
     */
    public Inspect_IdDisplay(@NotNull Shop _shop) {
        super(_shop);
        updateDisplay();
    }




    /**
     * Updates the displayed value.
     */
    public void updateDisplay() {

        getStyle(SimpleTextElmStyle.class).setText(new Txt()
            .cat(new Txt("ID: ").lightGray())
            .cat(new Txt(
                shop.getItem().is(Items.AIR) ? "-" :
                BuiltInRegistries.ITEM.getKey(shop.getItem().getItem()).getPath()
            ).white())
        .get());

        // Flush style
        flushStyle();
    }
}
