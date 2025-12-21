package com.snek.fancyplayershops.graphics.ui.details.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.fancyplayershops.main.Shop;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Txt;








/**
 * Part of the main display of DetailsUi.
 * <p> It shows the names of informations about the shop.
 */
public class Details_Names extends SimpleTextElm {


    /**
     * Creates a new DetailsUiDisplayNames.
     * @param _shop The target shop.
     */
    public Details_Names(final @NotNull Shop _shop) {
        super(_shop.getLevel());
    }


    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        updateDisplay();
        super.spawn(pos, animate);
    }




    /**
     * Updates the displayed values.
     */
    public void updateDisplay() {

        getStyle(SimpleTextElmStyle.class).setText(new Txt()
            .cat("Price:")
            .cat("\nStock:")
            .cat("\nOwner:")
        .lightGray().get());

        // Flush style
        flushStyle();
    }
}
