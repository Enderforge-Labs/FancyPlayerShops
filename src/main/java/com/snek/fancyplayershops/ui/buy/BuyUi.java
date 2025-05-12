package com.snek.fancyplayershops.ui.buy;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.ShopCanvas;
import com.snek.fancyplayershops.ui.edit.EditUi;
import com.snek.fancyplayershops.ui.misc.DualInputIndicator;
import com.snek.fancyplayershops.ui.misc.InputIndicator;
import com.snek.fancyplayershops.ui.misc.ShopFancyTextElm;
import com.snek.fancyplayershops.ui.misc.ShopUiBorder;
import com.snek.fancyplayershops.ui.misc.interfaces.InputIndicatorCanvas;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.ui.elements.styles.TextElmStyle;








//TODO add left click functionality to the item selector that lets you open a 1-slot UI
//TODO to read the item's name, lore and tags as if it were in a normal chest

/**
 * A UI that allows the user of a shop to buy items from it.
 */
public class BuyUi extends ShopCanvas implements InputIndicatorCanvas {
    public static final float CONFIRM_BUTTON_Y = 0.25f;
    private final @NotNull DualInputIndicator inputIndicator;


    private int amount = 1;
    public int getAmount() { return amount; }








    /**
     * Creates a new EditUi.
     * @param _shop The target shop.
     */
    public BuyUi(final @NotNull Shop _shop) {

        // Call superconstructor
        super(_shop, 1, ShopFancyTextElm.LINE_H, ShopUiBorder.DEFAULT_HEIGHT);
        Div e;


        // Add title
        e = bg.addChild(new BuyUi_Title(_shop));
        e.setSize(new Vector2f(1f, ShopFancyTextElm.LINE_H));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);

        //Add item inspector
        e = bg.addChild(new BuyUi_ItemInspector(_shop, null, null, new BuyUiSub_BackButton(_shop)));
        e.setSize(new Vector2f(EditUi.ITEM_SELECTOR_SIZE));
        e.setPosY(EditUi.ITEM_SELECTOR_Y);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add confirm button
        e = bg.addChild(new BuyUi_ConfirmButton(_shop, this));
        e.setSize(new Vector2f(0.5f, ShopFancyTextElm.LINE_H));
        e.setPosY(CONFIRM_BUTTON_Y);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add amount and total price displays
        e = bg.addChild(new BuyUi_AmountDisplay(_shop.getWorld(), this));
        e.setSize(new Vector2f(1f, ShopFancyTextElm.LINE_H));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setPosY(CONFIRM_BUTTON_Y - ShopFancyTextElm.LINE_H * 1);

        e = bg.addChild(new BuyUi_PriceDisplay(_shop, this));
        e.setSize(new Vector2f(1f, ShopFancyTextElm.LINE_H));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setPosY(CONFIRM_BUTTON_Y - ShopFancyTextElm.LINE_H * 2);


        // Add input indicators
        e = bg.addChild(new DualInputIndicator(_shop));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(ShopUiBorder.DEFAULT_HEIGHT * 2);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;
    }




    @Override public @NotNull InputIndicator getLmbIndicator() { return inputIndicator.getLmbIndicator(); }
    @Override public @NotNull InputIndicator getRmbIndicator() { return inputIndicator.getRmbIndicator(); }
}