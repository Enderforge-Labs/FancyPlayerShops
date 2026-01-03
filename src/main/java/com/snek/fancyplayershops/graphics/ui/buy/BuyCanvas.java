package com.snek.fancyplayershops.graphics.ui.buy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.configs.Configs;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.core.elements.ProductCanvasBase;
import com.snek.fancyplayershops.graphics.misc.elements.Misc_BackButton;
import com.snek.fancyplayershops.graphics.ui.buy.elements.Buy_1iButton;
import com.snek.fancyplayershops.graphics.ui.buy.elements.Buy_1sButton;
import com.snek.fancyplayershops.graphics.ui.buy.elements.Buy_1xButton;
import com.snek.fancyplayershops.graphics.ui.buy.elements.Buy_AmountInputDisplay;
import com.snek.fancyplayershops.graphics.ui.buy.elements.Buy_ConfirmButton;
import com.snek.fancyplayershops.graphics.ui.buy.elements.Buy_ItemInspector;
import com.snek.fancyplayershops.graphics.ui.buy.elements.Buy_NbtDisclaimer;
import com.snek.fancyplayershops.graphics.ui.buy.elements.Buy_PriceDisplay;
import com.snek.fancyplayershops.graphics.ui.edit.EditCanvas;
import com.snek.frameworklib.graphics.composite.elements.DualInputIndicator;
import com.snek.frameworklib.graphics.composite.elements.InputIndicator;
import com.snek.frameworklib.graphics.core.elements.CanvasBorder;
import com.snek.frameworklib.graphics.interfaces.InputIndicatorCanvas;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;








/**
 * A UI that allows the user of a shop to buy items from it.
 */
public class BuyCanvas extends ProductCanvasBase implements InputIndicatorCanvas {
    public static final float CONFIRM_BUTTON_Y = 0.3f;
    public static final float CONFIRM_BUTTON_W = 0.5f;
    public static final float BUY_BUTTONS_SPACING = 0.025f;
    public static final float BUY_BUTTONS_W = (CONFIRM_BUTTON_W - BUY_BUTTONS_SPACING * 2f) / 3f;

    // Elements
    private final @Nullable Div nbtDisclaimer;
    private final @NotNull DualInputIndicator inputIndicator;

    // Instance data
    private final @NotNull Buy_AmountInputDisplay amountInputDisplay;
    private final @NotNull Buy_PriceDisplay priceDisplay;
    private final @NotNull Buy_ConfirmButton confirmButton;
    private final @NotNull Buy_1xButton buy1xButton;
    private final @NotNull Buy_1sButton buy1sButton;
    private final @NotNull Buy_1iButton buy1iButton;

    private long amount = 0;
    public long getAmount() { return amount; }








    /**
     * Creates a new EditUi.
     * @param display The target display.
     */
    public BuyCanvas(final @NotNull ProductDisplay display) {

        // Call superconstructor
        super(display, calculateTitle(display), 1, TITLE_H, CanvasBorder.DEFAULT_HEIGHT);
        Div e;


        // Add NBT disclaimer
        if(!display.getNbtFilter()) {
            e = bg.addChild(new Buy_NbtDisclaimer(display.getLevel(),
                new Misc_BackButton(context, () -> {
                    context.changeCanvas(new BuyCanvas(display));
                    ((BuyCanvas)context.getActiveCanvas()).changeAmount(amount);
                })
            ));
            e.setSize(new Vector2f(1f, ProductCanvasBase.DEFAULT_HEIGHT));
            e.setPosY(1f + ProductCanvasBase.DEFAULT_DISTANCE);
            nbtDisclaimer = e;
        }
        else {
            nbtDisclaimer = null;
        }


        // Add amount input and total price display
        e = bg.addChild(new Buy_PriceDisplay(display, this));
        e.setSize(new Vector2f(1f, TITLE_H));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setPosY(1f - TITLE_H * 2);
        priceDisplay = (Buy_PriceDisplay)e;

        e = bg.addChild(new Buy_AmountInputDisplay(display, this));
        e.setSize(new Vector2f(1f, TITLE_H));
        e.setAlignmentX(AlignmentX.CENTER);
        e.setPosY(1f - TITLE_H * 3);
        amountInputDisplay = (Buy_AmountInputDisplay)e;

        //Add item inspector
        e = bg.addChild(new Buy_ItemInspector(
            display, null, null,
            new Misc_BackButton(context, () -> {
                context.changeCanvas(new BuyCanvas(display));
                ((BuyCanvas)context.getActiveCanvas()).changeAmount(amount);
            })
        ));
        e.setSize(new Vector2f(EditCanvas.ITEM_SELECTOR_SIZE));
        e.setPosY(EditCanvas.ITEM_SELECTOR_Y);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add confirm button
        e = bg.addChild(new Buy_ConfirmButton(display, this));
        e.setSize(new Vector2f(CONFIRM_BUTTON_W, TITLE_H));
        e.setPosY(CONFIRM_BUTTON_Y);
        e.setAlignmentX(AlignmentX.CENTER);
        confirmButton = (Buy_ConfirmButton)e;


        // Add quick buy buttons
        e = bg.addChild(new Buy_1xButton(display, this));
        e.setSize(new Vector2f(BUY_BUTTONS_W, TITLE_H));
        e.setPos(new Vector2f(-(BUY_BUTTONS_SPACING + BUY_BUTTONS_W), CONFIRM_BUTTON_Y - TITLE_H - BUY_BUTTONS_SPACING));
        buy1xButton = (Buy_1xButton)e;

        e = bg.addChild(new Buy_1sButton(display, this));
        e.setSize(new Vector2f(BUY_BUTTONS_W, TITLE_H));
        e.setPos(new Vector2f(0, CONFIRM_BUTTON_Y - TITLE_H - BUY_BUTTONS_SPACING));
        buy1sButton = (Buy_1sButton)e;

        e = bg.addChild(new Buy_1iButton(display, this));
        e.setSize(new Vector2f(BUY_BUTTONS_W, TITLE_H));
        e.setPos(new Vector2f(+(BUY_BUTTONS_SPACING + BUY_BUTTONS_W), CONFIRM_BUTTON_Y - TITLE_H - BUY_BUTTONS_SPACING));
        buy1iButton = (Buy_1iButton)e;


        //TODO maybe move input indicators to ProductCanvasBase?
        //TODO issue is, each canvas has its own bottomHeight which defines the position of the input indicator.
        //TODO detailsCanvas has special position too
        // Add input indicators
        e = bg.addChild(new DualInputIndicator(display.getLevel()));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(CanvasBorder.DEFAULT_HEIGHT * 2);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;


        // Set default amount
        changeAmount(1);
    }




    public static @NotNull Component calculateTitle(final @NotNull ProductDisplay display) {
        return new Txt("Buying: " + display.getStandaloneName()).white().get();
    }



    public void changeAmount(final long newAmount) {
        amount = newAmount;
        priceDisplay.updateDisplay();
        amountInputDisplay.updateDisplayedText();
        amountInputDisplay.forceTextUpdate();

        confirmButton.updateColor(display.getStock() >= amount);
        confirmButton.updateDisplay();
    }




    public boolean attemptChangeAmount(final @NotNull Player user, final float _amount) {

        if(_amount < 0.9999) {
            user.displayClientMessage(new Txt("The amount must be at least 1").red().bold().get(), true);
            return false;
        }
        if(_amount > Configs.getDisplay().stock_limit.getMax()) {
            user.displayClientMessage(new Txt("The amount cannot be greater than " + Utils.formatAmount(Configs.getDisplay().stock_limit.getMax(), false, true)).red().bold().get(), true);
            return false;
        }
        else changeAmount(Math.round((double)_amount));
        return true;
    }




    @Override
    public void onStockChange() {
        amountInputDisplay.updateDisplayedText();
        amountInputDisplay.forceTextUpdate();

        confirmButton.updateColor(display.getStock() >= amount);
        buy1xButton.updateColor(display.getStock() >= 1);
        buy1sButton.updateColor(display.getStock() >= 64);
        buy1iButton.updateColor(display.getStock() > 0);
    }




    @Override public @NotNull InputIndicator getLmbIndicator() { return inputIndicator.getLmbIndicator(); }
    @Override public @NotNull InputIndicator getRmbIndicator() { return inputIndicator.getRmbIndicator(); }
    @Override public @Nullable Div getDisclaimerElm() { return nbtDisclaimer; }
}