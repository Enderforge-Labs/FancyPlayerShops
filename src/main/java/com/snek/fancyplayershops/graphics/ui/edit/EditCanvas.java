package com.snek.fancyplayershops.graphics.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.configs.Configs;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.misc.elements.TitleElm;
import com.snek.fancyplayershops.graphics.ui.core.elements.ShopCanvasBase;
import com.snek.fancyplayershops.graphics.ui.core.elements.ShopItemDisplayElm;
import com.snek.fancyplayershops.graphics.ui.edit.elements.Edit_ColorSelector;
import com.snek.fancyplayershops.graphics.ui.edit.elements.Edit_DeleteButton;
import com.snek.fancyplayershops.graphics.ui.edit.elements.Edit_ItemSelector;
import com.snek.fancyplayershops.graphics.ui.edit.elements.Edit_MoveButton;
import com.snek.fancyplayershops.graphics.ui.edit.elements.Edit_OpenBuyMenuButton;
import com.snek.fancyplayershops.graphics.ui.edit.elements.Edit_PriceInput;
import com.snek.fancyplayershops.graphics.ui.edit.elements.Edit_RotateButton;
import com.snek.fancyplayershops.graphics.ui.edit.elements.Edit_StatsButton;
import com.snek.fancyplayershops.graphics.ui.edit.elements.Edit_StockLimitInput;
import com.snek.fancyplayershops.graphics.ui.edit.elements.Edit_TransferButton;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.composite.elements.DualInputIndicator;
import com.snek.frameworklib.graphics.composite.elements.InputIndicator;
import com.snek.frameworklib.graphics.core.elements.CanvasBorder;
import com.snek.frameworklib.graphics.interfaces.InputIndicatorCanvas;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.layout.Div;








/**
 * A UI that allows the owner of the shop to edit it.
 */
public class EditCanvas extends ShopCanvasBase implements InputIndicatorCanvas {
    private final @NotNull TitleElm title;
    private final @NotNull DualInputIndicator inputIndicator;
    public @NotNull SimpleTextElm getTitle() { return title; }


    // Layout
    public static final float ROTATE_BUTTON_Y            = FancyPlayerShops.SQUARE_BUTTON_SIZE / 2 + ShopItemDisplayElm.EDIT_MOVE.y;
    public static final float ROTATE_BUTTON_CENTER_SHIFT = 0.2f;

    public static final float ITEM_SELECTOR_SIZE         = 0.25f;
    public static final float ITEM_SELECTOR_Y            = ShopItemDisplayElm.EDIT_MOVE.y;

    public static final float COLOR_SELECTOR_W        = 0.2f;
    public static final float COLOR_SELECTOR_HIDDEN_W = 0.1f;

    public static final float INPUT_W = 1f - COLOR_SELECTOR_W * COLOR_SELECTOR_HIDDEN_W;


    // Functionalities
    public static final float ROTATE_BUTTON_AMOUNT = (float)Math.toRadians(45);








    /**
     * Creates a new EditUi.
     * @param _shop The target shop.
     */
    public EditCanvas(final @NotNull Shop _shop) {

        // Call superconstructor
        super(_shop, 1f, FancyPlayerShops.LINE_H, FancyPlayerShops.SQUARE_BUTTON_SIZE);
        Div e;


        // Add title
        e = bg.addChild(new TitleElm(_shop.getLevel(), recalculateTitle()));
        e.setSize(new Vector2f(TitleElm.DEFAULT_W, FancyPlayerShops.LINE_H));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);
        title = (TitleElm)e;
        updateTitle();


        // Add price button
        e = bg.addChild(new Edit_PriceInput(_shop));
        e.setSize(new Vector2f(INPUT_W, FancyPlayerShops.LINE_H));
        e.setPosY(1f - FancyPlayerShops.LINE_H * 2f);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add stock limit button
        e = bg.addChild(new Edit_StockLimitInput(_shop));
        e.setSize(new Vector2f(INPUT_W, FancyPlayerShops.LINE_H));
        e.setPosY(1f - FancyPlayerShops.LINE_H * 3f);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add rotation buttons
        e = bg.addChild(new Edit_RotateButton(_shop, -ROTATE_BUTTON_AMOUNT));
        e.setSize(new Vector2f(FancyPlayerShops.SQUARE_BUTTON_SIZE));
        e.setPos(new Vector2f(-ROTATE_BUTTON_CENTER_SHIFT, ROTATE_BUTTON_Y));

        e = bg.addChild(new Edit_RotateButton(_shop, +ROTATE_BUTTON_AMOUNT));
        e.setSize(new Vector2f(FancyPlayerShops.SQUARE_BUTTON_SIZE));
        e.setPos(new Vector2f(+ROTATE_BUTTON_CENTER_SHIFT, ROTATE_BUTTON_Y));


        // Add item selector
        e = bg.addChild(new Edit_ItemSelector(_shop));
        e.setSize(new Vector2f(ITEM_SELECTOR_SIZE));
        e.setPosY(ITEM_SELECTOR_Y);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add input indicators
        e = bg.addChild(new DualInputIndicator(_shop.getLevel()));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(FancyPlayerShops.SQUARE_BUTTON_SIZE + CanvasBorder.DEFAULT_HEIGHT);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;


        // Add buttons
        final Div[] buttons = new Div[] {
            new Edit_MoveButton(_shop),
            new Edit_StatsButton(_shop),
            new Edit_OpenBuyMenuButton(_shop),
            new Edit_TransferButton(_shop),
            new Edit_DeleteButton(_shop),
        };
        for(int i = 0; i < buttons.length; ++i) {
            e = bg.addChild(buttons[i]);
            e.setSize(new Vector2f(FancyPlayerShops.SQUARE_BUTTON_SIZE));
            e.setPosX(FancyPlayerShops.BOTTOM_ROW_SHIFT * (i - (int)(buttons.length / 2f + 0.0001f)));
            e.setAlignmentY(AlignmentY.BOTTOM);
        }


        // Add color selectors
        final Float[] hues = Configs.getShop().theme_hues.getValue();
        for(int i = 0; i < hues.length; ++i) {
            final float h = (1f - FancyPlayerShops.LINE_H - FancyPlayerShops.SQUARE_BUTTON_SIZE) / hues.length;
            e = bg.addChild(new Edit_ColorSelector(_shop, hues[i], this));
            e.setSize(new Vector2f(COLOR_SELECTOR_W, h));
            e.setAlignmentX(AlignmentX.RIGHT);
            e.setPosY(1f - FancyPlayerShops.LINE_H - h * (i + 1));
        }
    }


    public @NotNull Component recalculateTitle() {
        if(shop.getItem().is(Items.AIR)) {
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
        if(title != null) title.updateDisplay(recalculateTitle());
    }




    @Override
    public void onStockChange() {
        // Empty
    }




    @Override public @NotNull InputIndicator getLmbIndicator() { return inputIndicator.getLmbIndicator(); }
    @Override public @NotNull InputIndicator getRmbIndicator() { return inputIndicator.getRmbIndicator(); }
}
