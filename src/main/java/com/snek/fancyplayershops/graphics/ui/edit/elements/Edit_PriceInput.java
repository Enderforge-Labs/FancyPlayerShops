package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.edit.EditCanvas;
import com.snek.fancyplayershops.graphics.ui.edit.styles.Edit_Input_S;
import com.snek.fancyplayershops.graphics.ui.misc.styles.ShopTextInput_S;
import com.snek.frameworklib.graphics.functional.elements.TextInputElm;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.network.chat.Component;








/**
 * A button that allows the owner of the shop to change its price.
 */
public class Edit_PriceInput extends TextInputElm {

    /**
     * Creates a new EditUiPriceButton.
     * @param _shop The target shop.
     */
    public Edit_PriceInput(final @NotNull ProductDisplay _shop) {
        super(_shop.getLevel(), null, "Change price", new Txt("Send the new price in chat!").color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR).bold().get(), new Edit_Input_S(_shop));
    }


    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        updateDisplay(null);
        super.spawn(pos, animate);
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        final ProductDisplay shop = GetDisplay.get(this);
        getStyle(ShopTextInput_S.class).setText(textOverride != null ? textOverride : new Txt()
            .cat(new Txt("Price: ").lightGray())
            .cat(new Txt(Utils.formatPrice(shop.getPrice())).white())
        .get());
        flushStyle();
    }


    @Override
    protected boolean messageCallback(final @NotNull String s) {
        try {

            // Try to set the new price and update the display if it's valid
            final ProductDisplay shop = GetDisplay.get(this);
            if(shop.setPrice(Double.parseDouble(s))) updateDisplay(null);
            return true;

        } catch(NumberFormatException e) {
            return false;
        }
    }


    @Override
    public float getInteractionSizeRight() {
        return super.getInteractionSizeRight() - EditCanvas.COLOR_SELECTOR_W;
    }
}
