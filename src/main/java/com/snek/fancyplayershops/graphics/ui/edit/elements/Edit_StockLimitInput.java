package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.edit.EditCanvas;
import com.snek.fancyplayershops.graphics.ui.misc.styles.ProductDisplay_TextInput_S;
import com.snek.frameworklib.graphics.functional.elements.ChatInputElm;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;








/**
 * A button that allows the owner of the product display to change its stock limit.
 */
public class Edit_StockLimitInput extends ChatInputElm {

    /**
     * Creates a new EditUi_StockLimitInput.
     * @param display The target product display.
     */
    public Edit_StockLimitInput(final @NotNull ProductDisplay display) {
        super(
            display.getLevel(),
            null, "Change stock limit", new Txt("Send the new stock limit in chat!").lightGray().bold().get(),
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
            .cat(new Txt("Stock limit: ").lightGray())
            .cat(new Txt(Utils.formatAmount(display.getMaxStock(), true, true)).white())
        .get());
    }




    @Override
    protected boolean messageCallback(final @NotNull String s) {
        final ProductDisplay display = GetDisplay.get(this);
        try {

            // Try to set the new stock limit, update the display if it's valid
            if(display.setStockLimit(Integer.parseInt(s))) updateDisplayedText();
            return true;

        } catch(final NumberFormatException e) {
            try {

                // Try to set the new stock limit, update the display if it's valid
                if(display.setStockLimit(Float.parseFloat(s))) updateDisplayedText();
                return true;

            } catch(final NumberFormatException e2) {
                return false;
            }
        }
    }




    @Override
    public float getInteractionSizeRight() {
        return super.getInteractionSizeRight() - EditCanvas.COLOR_SELECTOR_W;
    }
}
