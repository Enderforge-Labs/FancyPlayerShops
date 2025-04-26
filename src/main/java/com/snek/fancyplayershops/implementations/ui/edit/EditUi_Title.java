package com.snek.fancyplayershops.implementations.ui.edit;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.edit.styles.EditUi_Title_S;
import com.snek.fancyplayershops.implementations.ui.misc.ShopFancyTextElm;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;

import net.minecraft.item.Items;








/**
 * A text display that shows the name of the shop that is currently being edited.
 */
public class EditUi_Title extends ShopFancyTextElm {

    /**
     * Creates a new EditUiTitle.
     * @param _shop The target shop.
     */
    public EditUi_Title(@NotNull Shop _shop) {
        super(_shop, 1f, ShopFancyTextElm.LINE_H, new EditUi_Title_S());
        updateDisplay();
    }


    /**
     * Updates the displayed text, reading data from the target shop.
     */
    public void updateDisplay() {
        ((TextElmStyle)style).setText(new Txt()
            .cat(new Txt("Editing: ").white())
            .cat(new Txt(
                shop.getItem().getItem() == Items.AIR ?
                new Txt("an empty shop").white().get() :
                new Txt(MinecraftUtils.getFancyItemName(shop.getItem())).get()
            ))
        .get());
        flushStyle();
    }
}