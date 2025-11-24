package com.snek.fancyplayershops.graphics.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.configs.Configs;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.misc.elements.TitleElm;
import com.snek.fancyplayershops.graphics.ui.core.elements.ShopCanvas;
import com.snek.fancyplayershops.graphics.ui.core.elements.ShopItemDisplay;
import com.snek.fancyplayershops.graphics.ui.edit.elements.EditUi_ColorSelector;
import com.snek.fancyplayershops.graphics.ui.edit.elements.EditUi_DeleteButton;
import com.snek.fancyplayershops.graphics.ui.edit.elements.EditUi_ItemSelector;
import com.snek.fancyplayershops.graphics.ui.edit.elements.EditUi_MoveButton;
import com.snek.fancyplayershops.graphics.ui.edit.elements.EditUi_OpenBuyMenuButton;
import com.snek.fancyplayershops.graphics.ui.edit.elements.EditUi_PriceInput;
import com.snek.fancyplayershops.graphics.ui.edit.elements.EditUi_RotateButton;
import com.snek.fancyplayershops.graphics.ui.edit.elements.EditUi_StatsButton;
import com.snek.fancyplayershops.graphics.ui.edit.elements.EditUi_StockLimitInput;
import com.snek.fancyplayershops.graphics.ui.edit.elements.EditUi_TransferButton;
import com.snek.fancyplayershops.graphics.ui.misc.elements.DualInputIndicator;
import com.snek.fancyplayershops.graphics.ui.misc.elements.InputIndicator;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ShopFancyTextElm;
import com.snek.fancyplayershops.graphics.ui.misc.interfaces.InputIndicatorCanvas;
import com.snek.frameworklib.graphics.core.elements.CanvasBorder;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

import com.snek.frameworklib.data_types.ui.AlignmentX;
import com.snek.frameworklib.data_types.ui.AlignmentY;
import com.snek.frameworklib.graphics.core.Div;








/**
 * A UI that allows the owner of the shop to edit it.
 */
public class EditUi extends ShopCanvas implements InputIndicatorCanvas {
    private final @NotNull TitleElm title;
    private final @NotNull DualInputIndicator inputIndicator;
    public @NotNull TitleElm getTitle() { return title; }


    // Layout
    public static final float ROTATE_BUTTON_Y            = 0.45f - SQUARE_BUTTON_SIZE / 2 + ShopItemDisplay.FOCUS_HEIGHT;
    public static final float ROTATE_BUTTON_CENTER_SHIFT = 0.2f;

    public static final float ITEM_SELECTOR_SIZE         = 0.25f;
    public static final float ITEM_SELECTOR_Y            = 0.45f - ITEM_SELECTOR_SIZE / 2 + ShopItemDisplay.FOCUS_HEIGHT;

    public static final float COLOR_SELECTOR_W        = 0.2f;
    public static final float COLOR_SELECTOR_HIDDEN_W = 0.1f;

    public static final float INPUT_W = 1f - COLOR_SELECTOR_W * COLOR_SELECTOR_HIDDEN_W;


    // Functionalities
    public static final float ROTATE_BUTTON_AMOUNT = (float)Math.toRadians(45);








    /**
     * Creates a new EditUi.
     * @param _shop The target shop.
     */
    public EditUi(final @NotNull Shop _shop) {

        // Call superconstructor
        super(_shop, 1f, ShopFancyTextElm.LINE_H, SQUARE_BUTTON_SIZE);
        Div e;


        // Add title
        e = bg.addChild(new TitleElm(_shop.getWorld(), recalculateTitle()));
        e.setSize(new Vector2f(TitleElm.DEFAULT_W, ShopFancyTextElm.LINE_H));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);
        title = (TitleElm)e;


        // Add price button
        e = bg.addChild(new EditUi_PriceInput(_shop));
        e.setSize(new Vector2f(INPUT_W, ShopFancyTextElm.LINE_H));
        e.setPosY(1f - ShopFancyTextElm.LINE_H * 2f);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add stock limit button
        e = bg.addChild(new EditUi_StockLimitInput(_shop));
        e.setSize(new Vector2f(INPUT_W, ShopFancyTextElm.LINE_H));
        e.setPosY(1f - ShopFancyTextElm.LINE_H * 3f);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add rotation buttons
        e = bg.addChild(new EditUi_RotateButton(_shop, -ROTATE_BUTTON_AMOUNT));
        e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
        e.setPos(new Vector2f(-ROTATE_BUTTON_CENTER_SHIFT, ROTATE_BUTTON_Y));

        e = bg.addChild(new EditUi_RotateButton(_shop, +ROTATE_BUTTON_AMOUNT));
        e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
        e.setPos(new Vector2f(+ROTATE_BUTTON_CENTER_SHIFT, ROTATE_BUTTON_Y));


        // Add item selector
        e = bg.addChild(new EditUi_ItemSelector(_shop));
        e.setSize(new Vector2f(ITEM_SELECTOR_SIZE));
        e.setPosY(ITEM_SELECTOR_Y);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add input indicators
        e = bg.addChild(new DualInputIndicator(_shop));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(SQUARE_BUTTON_SIZE + CanvasBorder.DEFAULT_HEIGHT);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;


        // Add buttons
        final Div[] buttons = new Div[] {
            new EditUi_MoveButton(_shop),
            new EditUi_StatsButton(_shop),
            new EditUi_OpenBuyMenuButton(_shop),
            new EditUi_TransferButton(_shop),
            new EditUi_DeleteButton(_shop),
        };
        for(int i = 0; i < buttons.length; ++i) {
            e = bg.addChild(buttons[i]);
            e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
            e.setPosX(BOTTOM_ROW_SHIFT * (i - (int)(buttons.length / 2f + 0.0001f)));
            e.setAlignmentY(AlignmentY.BOTTOM);
        }


        // Add color selectors
        final Float[] hues = Configs.getShop().theme_hues.getValue();
        for(int i = 0; i < hues.length; ++i) {
            final float h = (1f - ShopFancyTextElm.LINE_H - SQUARE_BUTTON_SIZE) / hues.length;
            e = bg.addChild(new EditUi_ColorSelector(_shop, hues[i], this));
            e.setSize(new Vector2f(COLOR_SELECTOR_W, h));
            e.setAlignmentX(AlignmentX.RIGHT);
            e.setPosY(1f - ShopFancyTextElm.LINE_H - h * (i + 1));
        }
    }


    public @NotNull Component recalculateTitle() {
        if(shop.getItem().getItem() == Items.AIR) {
            return new Txt()
                .cat(new Txt("Editing an empty shop").white())
            .get();
        }
        else {
            return new Txt()
                .cat(new Txt("Editing: ").white())
                .cat(shop.getStandaloneName())
            .get();
        }
    }
    public void updateTitle() {
        title.updateDisplay(recalculateTitle());
    }




    @Override
    public void onStockChange() {
        // Empty
    }




    @Override public @NotNull InputIndicator getLmbIndicator() { return inputIndicator.getLmbIndicator(); }
    @Override public @NotNull InputIndicator getRmbIndicator() { return inputIndicator.getRmbIndicator(); }
}
