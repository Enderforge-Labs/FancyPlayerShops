package com.snek.fancyplayershops.graphics.ui.buy;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.configs.Configs;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.core.elements.ShopCanvasBase;
import com.snek.fancyplayershops.graphics.misc.elements.TitleElm;
import com.snek.fancyplayershops.graphics.ui.buy.elements.Buy_Sub_BackButton;
import com.snek.fancyplayershops.graphics.ui.buy.elements.Buy_1iButton;
import com.snek.fancyplayershops.graphics.ui.buy.elements.Buy_1sButton;
import com.snek.fancyplayershops.graphics.ui.buy.elements.Buy_1xButton;
import com.snek.fancyplayershops.graphics.ui.buy.elements.Buy_AmountInputDisplay;
import com.snek.fancyplayershops.graphics.ui.buy.elements.Buy_ConfirmButton;
import com.snek.fancyplayershops.graphics.ui.buy.elements.Buy_ItemInspector;
import com.snek.fancyplayershops.graphics.ui.buy.elements.Buy_PriceDisplay;
import com.snek.fancyplayershops.graphics.ui.edit.EditCanvas;
import com.snek.frameworklib.graphics.composite.elements.DualInputIndicator;
import com.snek.frameworklib.graphics.composite.elements.InputIndicator;
import com.snek.frameworklib.graphics.core.elements.CanvasBorder;
import com.snek.frameworklib.graphics.interfaces.InputIndicatorCanvas;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;








/**
 * A UI that allows the user of a shop to buy items from it.
 */
public class BuyCanvas extends ShopCanvasBase implements InputIndicatorCanvas {
    public static final float CONFIRM_BUTTON_Y = 0.25f;
    public static final float CONFIRM_BUTTON_W = 0.5f;
    public static final float BUY_BUTTONS_SPACING = 0.025f;
    public static final float BUY_BUTTONS_W = (CONFIRM_BUTTON_W - BUY_BUTTONS_SPACING * 2f) / 3f;
    private final @NotNull DualInputIndicator inputIndicator;

    // Instance data
    private final @NotNull Buy_AmountInputDisplay amountInputDisplay;
    private final @NotNull Buy_PriceDisplay priceDisplay;
    private final @NotNull Buy_ConfirmButton confirmButton;
    private final @NotNull Buy_1xButton buy1xButton;
    private final @NotNull Buy_1sButton buy1sButton;
    private final @NotNull Buy_1iButton buy1iButton;

    private int amount = 0;
    public int getAmount() { return amount; }








    /**
     * Creates a new EditUi.
     * @param _shop The target shop.
     */
    public BuyCanvas(final @NotNull Shop _shop) {

        // Call superconstructor
        super(_shop, 1, FancyPlayerShops.LINE_H, CanvasBorder.DEFAULT_HEIGHT);
        Div e;


        // Add title
        e = bg.addChild(new TitleElm(_shop.getLevel(), recalculateTitle()));
        e.setSize(new Vector2f(TitleElm.DEFAULT_W, FancyPlayerShops.LINE_H));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);


        // Add amount input and total price display
        e = bg.addChild(new Buy_PriceDisplay(_shop, this));
        e.setSize(new Vector2f(1f, FancyPlayerShops.LINE_H));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setPosY(1f - FancyPlayerShops.LINE_H * 2);
        priceDisplay = (Buy_PriceDisplay)e;

        e = bg.addChild(new Buy_AmountInputDisplay(_shop, this));
        e.setSize(new Vector2f(1f, FancyPlayerShops.LINE_H));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setPosY(1f - FancyPlayerShops.LINE_H * 3);
        amountInputDisplay = (Buy_AmountInputDisplay)e;

        //Add item inspector
        e = bg.addChild(new Buy_ItemInspector(_shop, null, null, new Buy_Sub_BackButton(_shop)));
        e.setSize(new Vector2f(EditCanvas.ITEM_SELECTOR_SIZE));
        e.setPosY(EditCanvas.ITEM_SELECTOR_Y);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add confirm button
        e = bg.addChild(new Buy_ConfirmButton(_shop, this));
        e.setSize(new Vector2f(CONFIRM_BUTTON_W, FancyPlayerShops.LINE_H));
        e.setPosY(CONFIRM_BUTTON_Y);
        e.setAlignmentX(AlignmentX.CENTER);
        confirmButton = (Buy_ConfirmButton)e;


        // Add quick buy buttons
        e = bg.addChild(new Buy_1xButton(_shop));
        e.setSize(new Vector2f(BUY_BUTTONS_W, FancyPlayerShops.LINE_H));
        e.setPos(new Vector2f(-(BUY_BUTTONS_SPACING + BUY_BUTTONS_W), CONFIRM_BUTTON_Y - FancyPlayerShops.LINE_H - BUY_BUTTONS_SPACING));
        buy1xButton = (Buy_1xButton)e;

        e = bg.addChild(new Buy_1sButton(_shop));
        e.setSize(new Vector2f(BUY_BUTTONS_W, FancyPlayerShops.LINE_H));
        e.setPos(new Vector2f(0, CONFIRM_BUTTON_Y - FancyPlayerShops.LINE_H - BUY_BUTTONS_SPACING));
        buy1sButton = (Buy_1sButton)e;

        e = bg.addChild(new Buy_1iButton(_shop));
        e.setSize(new Vector2f(BUY_BUTTONS_W, FancyPlayerShops.LINE_H));
        e.setPos(new Vector2f(+(BUY_BUTTONS_SPACING + BUY_BUTTONS_W), CONFIRM_BUTTON_Y - FancyPlayerShops.LINE_H - BUY_BUTTONS_SPACING));
        buy1iButton = (Buy_1iButton)e;


        // Add input indicators
        e = bg.addChild(new DualInputIndicator(_shop.getLevel()));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(CanvasBorder.DEFAULT_HEIGHT * 2);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;


        // Set default amount and force button color update
        changeAmount(1);
        onStockChange();
    }




    public @NotNull Component recalculateTitle() {
        return new Txt("Buying: " + shop.getStandaloneName()).white().get();
    }



    public void changeAmount(final int newAmount) {
        amount = newAmount;
        priceDisplay.updateDisplay();
        amountInputDisplay.updateDisplay(null);
        confirmButton.updateColor(shop.getStock() >= amount);
        confirmButton.updateDisplay(null);
    }




    public boolean attemptChangeAmount(final @NotNull Player user, final float _amount) {

        if(_amount < 0.9999) {
            user.displayClientMessage(new Txt("The amount must be at least 1").red().bold().get(), true);
            return false;
        }
        if(_amount > Configs.getShop().stock_limit.getMax()) {
            user.displayClientMessage(new Txt("The amount cannot be greater than " + Utils.formatAmount(Configs.getShop().stock_limit.getMax(), false, true)).red().bold().get(), true);
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