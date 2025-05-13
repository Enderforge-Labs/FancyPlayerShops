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








/**
 * A UI that allows the user of a shop to buy items from it.
 */
public class BuyUi extends ShopCanvas implements InputIndicatorCanvas {
    public static final float CONFIRM_BUTTON_Y = 0.25f;
    private final @NotNull DualInputIndicator inputIndicator;

    // Instance data
    private final @NotNull Shop shop;
    private final @NotNull BuyUi_ConfirmButton confirmButton;
    private final @NotNull BuyUi_AmountDisplay amountDisplay;
    private final @NotNull BuyUi_PriceDisplay priceDisplay;

    private int amount = 0;
    public int getAmount() { return amount; }








    /**
     * Creates a new EditUi.
     * @param _shop The target shop.
     */
    public BuyUi(final @NotNull Shop _shop) {

        // Call superconstructor
        super(_shop, 1, ShopFancyTextElm.LINE_H, ShopUiBorder.DEFAULT_HEIGHT);
        shop = _shop;
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
        confirmButton = (BuyUi_ConfirmButton)e;


        // Add amount and total price displays
        e = bg.addChild(new BuyUi_AmountDisplay(_shop.getWorld(), this));
        e.setSize(new Vector2f(1f, ShopFancyTextElm.LINE_H));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setPosY(CONFIRM_BUTTON_Y - ShopFancyTextElm.LINE_H * 1);
        amountDisplay = (BuyUi_AmountDisplay)e;

        e = bg.addChild(new BuyUi_PriceDisplay(_shop, this));
        e.setSize(new Vector2f(1f, ShopFancyTextElm.LINE_H));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setPosY(CONFIRM_BUTTON_Y - ShopFancyTextElm.LINE_H * 2);
        priceDisplay = (BuyUi_PriceDisplay)e;


        // Add input indicators
        e = bg.addChild(new DualInputIndicator(_shop));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(ShopUiBorder.DEFAULT_HEIGHT * 2);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;


        // Set default amount and force button color update
        changeAmount(1);
    }




    public void changeAmount(final int newAmount) {
        amount = newAmount;
        amountDisplay.updateDisplay();
        priceDisplay.updateDisplay();
        confirmButton.updateColor(shop.getStock() >= amount);
    }




    @Override
    public void onStockChange() {
        confirmButton.updateColor(shop.getStock() >= amount);
    }




    @Override public @NotNull InputIndicator getLmbIndicator() { return inputIndicator.getLmbIndicator(); }
    @Override public @NotNull InputIndicator getRmbIndicator() { return inputIndicator.getRmbIndicator(); }
}