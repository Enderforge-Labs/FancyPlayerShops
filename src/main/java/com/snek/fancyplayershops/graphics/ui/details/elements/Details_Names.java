package com.snek.fancyplayershops.graphics.ui.details.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.graphics.basic.elements.TextElm;
import com.snek.frameworklib.graphics.basic.styles.TextStyle;
import com.snek.frameworklib.utils.Txt;








/**
 * Part of the main display of DetailsUi.
 * <p> It shows the names of informations about the product.
 */
public class Details_Names extends TextElm {


    /**
     * Creates a new DetailsUiDisplayNames.
     * @param display The target product display.
     */
    public Details_Names(final @NotNull ProductDisplay display) {
        super(display.getLevel(), new TextStyle()
            .withText(new Txt()
                .cat("Price:\n")
                .cat("Stock:\n")
                .cat("Owner:")
                .lightGray().get()
            )
            .withTextAlignment(TextAlignment.LEFT)
        );
    }
}
