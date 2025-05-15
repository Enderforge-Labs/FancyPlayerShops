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
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.world.entity.player.Player;








/**
 * A UI that allows the user of a shop to buy items from it.
 */
public class BuyUi extends ShopCanvas implements InputIndicatorCanvas {
    public static final float CONFIRM_BUTTON_Y = 0.25f;
    public static final float CONFIRM_BUTTON_W = 0.5f;
    public static final float BUY_BUTTONS_SPACING = 0.025f;
    public static final float BUY_BUTTONS_W = (CONFIRM_BUTTON_W - BUY_BUTTONS_SPACING * 2f) / 3f;
    private final @NotNull DualInputIndicator inputIndicator;

    // Instance data
    private final @NotNull Shop shop;
    private final @NotNull BuyUi_AmountInputDisplay amountInputDisplay;
    private final @NotNull BuyUi_PriceDisplay priceDisplay;
    private final @NotNull BuyUi_ConfirmButton confirmButton;
    private final @NotNull BuyUi_1xButton buy1xButton;
    private final @NotNull BuyUi_1sButton buy1sButton;
    private final @NotNull BuyUi_1iButton buy1iButton;

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


        // Add amount input and total price display
        e = bg.addChild(new BuyUi_PriceDisplay(_shop, this));
        e.setSize(new Vector2f(1f, ShopFancyTextElm.LINE_H));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setPosY(1f - ShopFancyTextElm.LINE_H * 2);
        priceDisplay = (BuyUi_PriceDisplay)e;

        e = bg.addChild(new BuyUi_AmountInputDisplay(_shop, this));
        e.setSize(new Vector2f(1f, ShopFancyTextElm.LINE_H));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setPosY(1f - ShopFancyTextElm.LINE_H * 3);
        amountInputDisplay = (BuyUi_AmountInputDisplay)e;

        //Add item inspector
        e = bg.addChild(new BuyUi_ItemInspector(_shop, null, null, new BuyUiSub_BackButton(_shop)));
        e.setSize(new Vector2f(EditUi.ITEM_SELECTOR_SIZE));
        e.setPosY(EditUi.ITEM_SELECTOR_Y);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add confirm button
        e = bg.addChild(new BuyUi_ConfirmButton(_shop, this));
        e.setSize(new Vector2f(CONFIRM_BUTTON_W, ShopFancyTextElm.LINE_H));
        e.setPosY(CONFIRM_BUTTON_Y);
        e.setAlignmentX(AlignmentX.CENTER);
        confirmButton = (BuyUi_ConfirmButton)e;


        // Add quick buy buttons
        e = bg.addChild(new BuyUi_1xButton(_shop));
        e.setSize(new Vector2f(BUY_BUTTONS_W, ShopFancyTextElm.LINE_H));
        e.setPos(new Vector2f(-(BUY_BUTTONS_SPACING + BUY_BUTTONS_W), CONFIRM_BUTTON_Y - ShopFancyTextElm.LINE_H - BUY_BUTTONS_SPACING));
        buy1xButton = (BuyUi_1xButton)e;

        e = bg.addChild(new BuyUi_1sButton(_shop));
        e.setSize(new Vector2f(BUY_BUTTONS_W, ShopFancyTextElm.LINE_H));
        e.setPos(new Vector2f(0, CONFIRM_BUTTON_Y - ShopFancyTextElm.LINE_H - BUY_BUTTONS_SPACING));
        buy1sButton = (BuyUi_1sButton)e;

        e = bg.addChild(new BuyUi_1iButton(_shop));
        e.setSize(new Vector2f(BUY_BUTTONS_W, ShopFancyTextElm.LINE_H));
        e.setPos(new Vector2f(+(BUY_BUTTONS_SPACING + BUY_BUTTONS_W), CONFIRM_BUTTON_Y - ShopFancyTextElm.LINE_H - BUY_BUTTONS_SPACING));
        buy1iButton = (BuyUi_1iButton)e;


        // Add input indicators
        e = bg.addChild(new DualInputIndicator(_shop));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(ShopUiBorder.DEFAULT_HEIGHT * 2);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;


        // Set default amount and force button color update
        changeAmount(1);
        onStockChange();
    }




    public void changeAmount(final int newAmount) {
        amount = newAmount;
        priceDisplay.updateDisplay();
        amountInputDisplay.updateDisplay(null);
        confirmButton.updateColor(shop.getStock() >= amount);
    }




    public boolean attemptChangeAmount(final @NotNull Player user, final float _amount) {

        if(_amount < 0.9999) {
            if(user != null) user.displayClientMessage(new Txt("The amount must be at least 1").red().bold().get(), true);
            return false;
        }
        if(_amount > Shop.MAX_STOCK) {
            if(user != null) user.displayClientMessage(new Txt("The amount cannot be greater than " + Utils.formatAmount(Shop.MAX_STOCK, false, true)).red().bold().get(), true);
            return false;
        }
        else changeAmount(Math.round(_amount));
        return true;
    }




    @Override
    public void onStockChange() {
        amountInputDisplay.updateDisplay(null);
        confirmButton.updateColor(shop.getStock() >= amount);
        buy1xButton.updateColor(shop.getStock() >= 1);
        buy1sButton.updateColor(shop.getStock() >= 64);
        buy1iButton.updateColor(shop.getStock() > 0);
    }




    @Override public @NotNull InputIndicator getLmbIndicator() { return inputIndicator.getLmbIndicator(); }
    @Override public @NotNull InputIndicator getRmbIndicator() { return inputIndicator.getRmbIndicator(); }
}