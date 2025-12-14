package com.snek.fancyplayershops.graphics.ui.inspect.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.GetShop;
import com.snek.fancyplayershops.main.Shop;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;








/**
 * A UI element that shows the mod of the item.
 */
public class Inspect_ModDisplay extends SimpleTextElm {


    /**
     * Creates a new InspectUi_ModDisplay.
     * @param _shop The target shop.
     */
    public Inspect_ModDisplay(@NotNull Shop _shop) {
        super(_shop.getLevel());
        updateDisplay();
    }




    /**
     * Updates the displayed value.
     */
    public void updateDisplay() {

        final Shop shop = GetShop.get(this);
        getStyle(SimpleTextElmStyle.class).setText(new Txt()
            .cat(new Txt("Mod: ").lightGray())
            .cat(new Txt(
                shop.getItem().is(Items.AIR) ? "-" :
                BuiltInRegistries.ITEM.getKey(shop.getItem().getItem()).getNamespace()
            ).white())
        .get());

        // Flush style
        flushStyle();
    }
}
