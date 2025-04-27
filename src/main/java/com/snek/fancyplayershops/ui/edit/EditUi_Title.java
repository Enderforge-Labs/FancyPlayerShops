package com.snek.fancyplayershops.ui.edit;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.ShopTextElm;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;

import net.minecraft.item.Items;








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
        getStyle(TextElmStyle.class).setText(new Txt()
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