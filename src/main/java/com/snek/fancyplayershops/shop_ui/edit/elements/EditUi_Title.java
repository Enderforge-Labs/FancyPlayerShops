package com.snek.fancyplayershops.shop_ui.edit.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.shop_ui.misc.elements.ShopTextElm;
import com.snek.framework.ui.basic.styles.TextElmStyle;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;

import net.minecraft.world.item.Items;








/**
 * A text display that shows the name of the shop that is currently being edited.
 */
public class EditUi_Title extends ShopTextElm {

    /**
     * Creates a new EditUiTitle.
     * @param _shop The target shop.
     */
    public EditUi_Title(@NotNull Shop _shop) {
        super(_shop);
        updateDisplay();
    }


    /**
     * Updates the displayed text, reading data from the target shop.
     */
    public void updateDisplay() {
        if(shop.getItem().getItem() == Items.AIR) getStyle(TextElmStyle.class).setText(new Txt()
            .cat(new Txt("Editing an empty shop").white())
        .get());
        else getStyle(TextElmStyle.class).setText(new Txt()
            .cat(new Txt("Editing: ").white())
            .cat(MinecraftUtils.getFancyItemName(shop.getItem()))
        .get());
        flushStyle();
    }
}