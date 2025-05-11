package com.snek.fancyplayershops.ui.transfer;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.ShopTextElm;
import com.snek.framework.ui.elements.styles.TextElmStyle;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;

import net.minecraft.world.item.Items;








/**
 * A text display that shows the name of the shop that is currently being edited.
 */
public class TransferUi_Title extends ShopTextElm {

    /**
     * Creates a new TransferUi_Title.
     * @param _shop The target shop.
     */
    public TransferUi_Title(@NotNull Shop _shop) {
        super(_shop);
        updateDisplay();
    }


    /**
     * Updates the displayed text, reading data from the target shop.
     */
    public void updateDisplay() {
        if(shop.getItem().getItem() == Items.AIR) getStyle(TextElmStyle.class).setText(new Txt()
            .cat(new Txt("Transferring an empty shop").white())
        .get());
        else getStyle(TextElmStyle.class).setText(new Txt()
            .cat(new Txt("Transferring: ").white())
            .cat(MinecraftUtils.getFancyItemName(shop.getItem()))
        .get());
        flushStyle();
    }
}