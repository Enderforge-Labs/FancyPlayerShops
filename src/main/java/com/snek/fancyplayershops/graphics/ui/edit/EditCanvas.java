package com.snek.fancyplayershops.graphics.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.configs.Configs;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.misc.elements.TitleElm;
import com.snek.fancyplayershops.graphics.ui.core.elements.ProductCanvasBase;
import com.snek.fancyplayershops.graphics.ui.core.elements.ProductItemDisplayElm;
import com.snek.fancyplayershops.graphics.ui.edit.elements.Edit_ColorSelector;
import com.snek.fancyplayershops.graphics.ui.edit.elements.Edit_DeleteButton;
import com.snek.fancyplayershops.graphics.ui.edit.elements.Edit_ShopInput;
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
import com.snek.frameworklib.graphics.layout.HoverableDiv;








/**
 * A UI that allows the owner of the product display to edit it.
 */
public class EditCanvas extends ProductCanvasBase implements InputIndicatorCanvas {
    private final @NotNull TitleElm title;
    private final @NotNull DualInputIndicator inputIndicator;
    public @NotNull SimpleTextElm getTitle() { return title; }


    // Layout
    public static final float ROTATE_BUTTON_Y            = FancyPlayerShops.SQUARE_BUTTON_SIZE / 2 + ProductItemDisplayElm.EDIT_MOVE.y;
    public static final float ROTATE_BUTTON_CENTER_SHIFT = 0.2f;

    public static final float ITEM_SELECTOR_SIZE         = 0.25f;
    public static final float ITEM_SELECTOR_Y            = ProductItemDisplayElm.EDIT_MOVE.y;

    public static final float COLOR_SELECTOR_W        = 0.2f;
    public static final float COLOR_SELECTOR_HIDDEN_W = 0.1f;

    public static final float INPUT_W = 1f - COLOR_SELECTOR_W * COLOR_SELECTOR_HIDDEN_W;


    // Functionalities
    public static final float ROTATE_BUTTON_AMOUNT = (float)Math.toRadians(45);








    /**
     * Creates a new EditUi.
     * @param display The target product display.
     */
    public EditCanvas(final @NotNull ProductDisplay display) {

        // Call superconstructor
        super(display, 1f, FancyPlayerShops.LINE_H, FancyPlayerShops.SQUARE_BUTTON_SIZE);
        Div e;


        // Add title
        e = bg.addChild(new TitleElm(display.getLevel(), recalculateTitle()));
        e.setSize(new Vector2f(TitleElm.DEFAULT_W, FancyPlayerShops.LINE_H));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);
        title = (TitleElm)e;
        updateTitle();


        // Add price button
        e = bg.addChild(new Edit_PriceInput(display));
        e.setSize(new Vector2f(INPUT_W, FancyPlayerShops.LINE_H));
        e.setPosY(1f - FancyPlayerShops.LINE_H * 2f);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add stock limit button
        e = bg.addChild(new Edit_StockLimitInput(display));
        e.setSize(new Vector2f(INPUT_W, FancyPlayerShops.LINE_H));
        e.setPosY(1f - FancyPlayerShops.LINE_H * 3f);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add rotation buttons
        e = bg.addChild(new Edit_RotateButton(display, -ROTATE_BUTTON_AMOUNT));
        e.setSize(new Vector2f(FancyPlayerShops.SQUARE_BUTTON_SIZE));
        e.setPos(new Vector2f(-ROTATE_BUTTON_CENTER_SHIFT, ROTATE_BUTTON_Y));

        e = bg.addChild(new Edit_RotateButton(display, +ROTATE_BUTTON_AMOUNT));
        e.setSize(new Vector2f(FancyPlayerShops.SQUARE_BUTTON_SIZE));
        e.setPos(new Vector2f(+ROTATE_BUTTON_CENTER_SHIFT, ROTATE_BUTTON_Y));


        // Add item selector
        e = bg.addChild(new Edit_ItemSelector(display));
        e.setSize(new Vector2f(ITEM_SELECTOR_SIZE));
        e.setPosY(ITEM_SELECTOR_Y);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add shop input
        e = bg.addChild(new Edit_ShopInput(display));
        e.setSize(new Vector2f(INPUT_W, FancyPlayerShops.LINE_H));
        e.setPosY(FancyPlayerShops.SQUARE_BUTTON_SIZE + FancyPlayerShops.LINE_H * 1f);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add input indicators
        e = bg.addChild(new DualInputIndicator(display.getLevel()));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(FancyPlayerShops.SQUARE_BUTTON_SIZE + CanvasBorder.DEFAULT_HEIGHT);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;


        // Add buttons
        final Div[] buttons = new Div[] {
            new Edit_MoveButton(display),
            new Edit_StatsButton(display),
            new Edit_OpenBuyMenuButton(display),
            new Edit_TransferButton(display),
            new Edit_DeleteButton(display),
        };
        for(int i = 0; i < buttons.length; ++i) {
            e = bg.addChild(buttons[i]);
            e.setSize(new Vector2f(FancyPlayerShops.SQUARE_BUTTON_SIZE));
            e.setPosX(FancyPlayerShops.BOTTOM_ROW_SHIFT * (i - (int)(buttons.length / 2f + 0.0001f)));
            e.setAlignmentY(AlignmentY.BOTTOM);
        }


        // Add color selector container
        final float cslHeight = 1f - FancyPlayerShops.LINE_H - FancyPlayerShops.SQUARE_BUTTON_SIZE;
        final Div csl = bg.addChild(new HoverableDiv());
        csl.setSize(new Vector2f(COLOR_SELECTOR_W, cslHeight));
        csl.setAlignmentX(AlignmentX.RIGHT);
        csl.setPosY(FancyPlayerShops.SQUARE_BUTTON_SIZE);

        // Add color selectors
        final Float[] hues = Configs.getDisplay().theme_hues.getValue();
        for(int i = 0; i < hues.length; ++i) {
            final float h = 1f / hues.length;
            e = csl.addChild(new Edit_ColorSelector(display, hues[i], this));
            e.setSize(new Vector2f(1, h));
            e.setPosY(h * i * cslHeight);
        }
    }




    public @NotNull Component recalculateTitle() {
        if(display.getItem().is(Items.AIR)) {
            return new Txt()
                .cat(new Txt("Editing an empty product display").white())
            .get();
        }
        else {
            return new Txt()
                .cat(new Txt("Editing: ").white())
                .cat(display.getStandaloneName())
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
