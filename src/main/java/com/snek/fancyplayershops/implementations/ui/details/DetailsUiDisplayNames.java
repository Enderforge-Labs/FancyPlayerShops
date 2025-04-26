package com.snek.fancyplayershops.implementations.ui.details;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.misc.ShopTextElm;
import com.snek.framework.ui.elements.TextElm;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;








/**
 * The main display of DetailsUi.
 * It shows the names of informations about the shop.
 */
public class DetailsUiDisplayNames extends ShopTextElm {




    /**
     * Creates a new DetailsUiDisplayNames.
     * @param _shop The target shop.
     */
    public DetailsUiDisplayNames(@NotNull Shop _shop){
        super(_shop, 1, DetailsUi.BACKGROUND_HEIGHT);
        updateDisplay();
    }




    /**
     * Updates the displayed values.
     */
    public void updateDisplay(){

        ((TextElmStyle)style).setText(new Txt()
            .cat(new Txt("Price:").lightGray())
            .cat(new Txt("\nStock:").lightGray())
            .cat(new Txt("\nOwner:").lightGray())
        .get());

        // Flush style
        flushStyle();
    }
}
