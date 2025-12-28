package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.edit.EditCanvas;
import com.snek.fancyplayershops.graphics.ui.misc.styles.ProductDisplay_TextInput_S;
import com.snek.frameworklib.graphics.functional.elements.TextInputElm;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;








/**
 * A button that allows the owner of the product display to change its price.
 */
public class Edit_PriceInput extends TextInputElm {

    /**
     * Creates a new EditUiPriceButton.
     * @param display The target product display.
     */
    public Edit_PriceInput(final @NotNull ProductDisplay display) {
        super(
            display.getLevel(),
            null, "Change price",
            new Txt("Send the new price in chat!").color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR).bold().get(),
            new ProductDisplay_TextInput_S(display)
        );
    }


    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        updateDisplayedText();
        super.spawn(pos, animate);
    }


    public void updateDisplayedText() {
        final ProductDisplay display = GetDisplay.get(this);
        setDisplayedText(new Txt()
            .cat(new Txt("Price: ").lightGray())
            .cat(new Txt(Utils.formatPrice(display.getPrice())).white())
        .get());
    }


    @Override
    protected boolean messageCallback(final @NotNull String s) {
        try {

            // Try to set the new price and update the display if it's valid
            final ProductDisplay display = GetDisplay.get(this);
            if(display.setPrice(Double.parseDouble(s))) updateDisplayedText();
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
