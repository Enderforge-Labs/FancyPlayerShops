package com.snek.fancyplayershops.graphics.ui.details.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.graphics.ui.details.DetailsCanvas;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.world.item.Items;








/**
 * Part of the main display of DetailsUi.
 * <p> It shows the values of informations about the product.
 */
public class Details_Values extends SimpleTextElm {
    final @NotNull String ownerName;


    /**
     * Creates a new DetailsUiDisplayValues.
     * @param display The target product display.
     */
    public Details_Values(@NotNull final ProductDisplay display) {
        super(display.getLevel());
        ownerName = MinecraftUtils.getOfflinePlayerName(display.getOwnerUuid());
    }


    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        updateDisplay();
        super.spawn(pos, animate);
    }




    /**
     * Updates the displayed values using the current item name, price and stock.
     */
    public void updateDisplay() {
        final ProductDisplay display = GetDisplay.get(this);

        // Calculate the color of the stock amount and retrieve the owner's name
        final double factor = Math.min(1.0f, (double)display.getStock() / display.getMaxStock());
        final Vector3i col = Utils.interpolateRGB(DetailsCanvas.C_HSV_STOCK_LOW, DetailsCanvas.C_HSV_STOCK_HIGH, (float)Easings.quadOut.compute(factor));

        // Empty product display case
        if(display.getItem().is(Items.AIR)) {
            getStyle(SimpleTextElmStyle.class).setText(new Txt()
                .cat(new Txt("-").lightGray())
                .cat(new Txt("\n-").lightGray())
                .cat("\n" + ownerName)
            .get());
        }

        // Configured product display case
        else {
            final long price = display.getPrice();
            final String priceStr = price == 0 ? "Free" : (price >= 100_000_00 ? Utils.formatPriceShort(price) : Utils.formatPrice(price));
            getStyle(SimpleTextElmStyle.class).setText(new Txt()
                .cat(new Txt(priceStr).color(DetailsCanvas.C_RGB_PRICE))
                .cat("\n").cat(new Txt(Utils.formatAmount(display.getStock())).color(col))
                .cat("\n" + ownerName)
            .get());
        }

        // Flush style
        flushStyle();
    }
}
