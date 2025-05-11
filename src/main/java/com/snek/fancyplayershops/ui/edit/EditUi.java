package com.snek.fancyplayershops.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.ShopCanvas;
import com.snek.fancyplayershops.ui.ShopItemDisplay;
import com.snek.fancyplayershops.ui.misc.DualInputIndicator;
import com.snek.fancyplayershops.ui.misc.InputIndicator;
import com.snek.fancyplayershops.ui.misc.ShopFancyTextElm;
import com.snek.fancyplayershops.ui.misc.ShopUiBorder;
import com.snek.fancyplayershops.ui.misc.interfaces.InputIndicatorCanvas;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.Elm;







//TODO add left click functionality to the item selector that lets you open a 1-slot UI
//TODO to read the item's name, lore and tags as if it were in a normal chest

/**
 * A UI that allows the owner of the shop to edit it.
 */
public class EditUi extends ShopCanvas implements InputIndicatorCanvas {
    private final @NotNull Elm title;
    private final @NotNull DualInputIndicator inputIndicator;
    public @NotNull Elm getTitle() { return title; }


    // Colors
    public static final @NotNull Vector3i TOOLBAR_FG_COLOR = new Vector3i(255, 255, 255);
    public static final          int      TOOLBAR_FG_ALPHA = 255;


    // Layout
    public static final float ROTATE_BUTTON_Y            = 0.45f - SQUARE_BUTTON_SIZE / 2 + ShopItemDisplay.FOCUS_HEIGHT;
    public static final float ROTATE_BUTTON_CENTER_SHIFT = 0.2f;

    public static final float ITEM_SELECTOR_SIZE         = 0.25f;
    public static final float ITEM_SELECTOR_Y            = 0.45f - ITEM_SELECTOR_SIZE / 2 + ShopItemDisplay.FOCUS_HEIGHT;

    public static final float BOTTOM_ROW_SPACING         = 0.04f;
    public static final float BOTTOM_ROW_SHIFT           = SQUARE_BUTTON_SIZE + BOTTOM_ROW_SPACING;
    public static final float BOTTOM_ROW_CONTENT_SIZE    = 0.6f;
    public static final float TOOLBAR_FG_WIDTH           = 0.15f;

    public static final float COLOR_SELECTOR_W        = 0.2f;
    public static final float COLOR_SELECTOR_HIDDEN_W = 0.1f;


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
        e = bg.addChild(new EditUi_Title(_shop));
        e.setPosY(1f - ShopFancyTextElm.LINE_H * 1f);
        e.setSizeY(ShopFancyTextElm.LINE_H);
        e.setAlignmentX(AlignmentX.CENTER);
        title = (Elm)e;


        // Add price button
        e = bg.addChild(new EditUi_PriceInput(_shop));
        e.setSize(new Vector2f(0.75f, ShopFancyTextElm.LINE_H));
        e.setPosY(1f - ShopFancyTextElm.LINE_H * 2f);
        e.setAlignmentX(AlignmentX.LEFT);


        // Add stock limit button
        e = bg.addChild(new EditUi_StockLimitInput(_shop));
        e.setSize(new Vector2f(0.75f, ShopFancyTextElm.LINE_H));
        e.setPosY(1f - ShopFancyTextElm.LINE_H * 3f);
        e.setAlignmentX(AlignmentX.LEFT);


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
        e.setPosY(SQUARE_BUTTON_SIZE + ShopUiBorder.DEFAULT_HEIGHT);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;


        // Add buttons
        e = bg.addChild(new EditUi_MoveButton(_shop));
        e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
        e.setPos(new Vector2f(BOTTOM_ROW_SHIFT * -2f, 0));

        e = bg.addChild(new EditUi_GraphButton(_shop));
        e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
        e.setPos(new Vector2f(BOTTOM_ROW_SHIFT * -1f, 0));

        e = bg.addChild(new EditUi_OpenBuyMenuButton(_shop));
        e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
        e.setPos(new Vector2f(BOTTOM_ROW_SHIFT * 0f, 0));

        e = bg.addChild(new EditUi_TransferButton(_shop));
        e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
        e.setPos(new Vector2f(BOTTOM_ROW_SHIFT * 1f, 0));

        e = bg.addChild(new EditUi_DeleteButton(_shop));
        e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
        e.setPos(new Vector2f(BOTTOM_ROW_SHIFT * 2f, 0));


        // Add color selectors
        final int[] hues = new int[] { 0, 25, 55, 95, 135, 180, 220, 260, 300 };
        for(int i = 0; i < hues.length; ++i) {
            final float h = (1f - ShopFancyTextElm.LINE_H - SQUARE_BUTTON_SIZE) / hues.length;
            e = bg.addChild(new EditUi_ColorSelector(_shop, hues[i]));
            e.setSize(new Vector2f(COLOR_SELECTOR_W, h));
            e.setAlignmentX(AlignmentX.RIGHT);
            e.setPosY(1f - ShopFancyTextElm.LINE_H - h * (i + 1));
        }
    }




    @Override public @NotNull InputIndicator getLmbIndicator() { return inputIndicator.getLmbIndicator(); }
    @Override public @NotNull InputIndicator getRmbIndicator() { return inputIndicator.getRmbIndicator(); }
}
