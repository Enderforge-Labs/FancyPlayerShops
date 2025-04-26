package com.snek.fancyplayershops.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.ShopCanvas;
import com.snek.fancyplayershops.ui.ShopItemDisplay;
import com.snek.fancyplayershops.ui.details.DetailsUi;
import com.snek.fancyplayershops.ui.misc.ShopFancyTextElm;
import com.snek.fancyplayershops.ui.misc.ShopUiBorder;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;







//TODO add left click functionality to the item selector that lets you open a 1-slot UI
//TODO to read the item's name, lore and tags as if it were in a normal chest

//TODO add small text elements in a corner of the UIs that tell you what each mouse button does when clicked.
//TODO they change based on the player's currently hovered element.

/**
 * A UI that allows the owner of the shop to edit it.
 */
public class EditUi extends ShopCanvas {
    private final @NotNull Elm title;
    public @NotNull Elm getTitle() { return title; }


    // Colors
    public static final Vector3i RGB_STOCK_COLOR = Utils.HSVtoRGB(DetailsUi.C_HSV_STOCK_HIGH);


    // Layout
    public static final float ROTATE_BUTTON_Y            = 0.4f - ShopCanvas.SQUARE_BUTTON_SIZE / 2 + ShopItemDisplay.FOCUS_HEIGHT;
    public static final float ROTATE_BUTTON_CENTER_SHIFT = 0.3f;

    public static final float ITEM_SELECTOR_SIZE         = 0.25f;
    public static final float ITEM_SELECTOR_Y            = 0.4f - ITEM_SELECTOR_SIZE / 2 + ShopItemDisplay.FOCUS_HEIGHT;


    // Functionalities
    public static final float ROTATE_BUTTON_AMOUNT = (float)Math.toRadians(45);








    /**
     * Creates a new EditUi.
     * @param _shop The target shop.
     */
    public EditUi(Shop _shop) {

        // Call superconstructor
        super(_shop, 1f, ShopFancyTextElm.LINE_H, ShopUiBorder.DEFAULT_HEIGHT);
        Div e;


        // Add title
        e = bg.addChild(new EditUi_Title(_shop));
        e.moveY(1f - ShopFancyTextElm.LINE_H * 1f);
        e.setSizeY(ShopFancyTextElm.LINE_H);
        e.setAlignmentX(AlignmentX.CENTER);
        title = (Elm)e;


        // Add price button
        e = bg.addChild(new EditUi_PriceButton(_shop));
        e.moveY(1f - ShopFancyTextElm.LINE_H * 2f);
        e.setAlignmentX(AlignmentX.LEFT);


        // Add stock limit button
        e = bg.addChild(new EditUi_StockLimitButton(_shop));
        e.moveY(1f - ShopFancyTextElm.LINE_H * 3f);
        e.setAlignmentX(AlignmentX.LEFT);


        // Add rotation buttons
        e = bg.addChild(new EditUi_RotateButton(_shop, -ROTATE_BUTTON_AMOUNT, new Txt("◀").get()));
        e.move(new Vector2f(-ROTATE_BUTTON_CENTER_SHIFT, ROTATE_BUTTON_Y));
        e = bg.addChild(new EditUi_RotateButton(_shop, +ROTATE_BUTTON_AMOUNT, new Txt("▶").get()));
        e.move(new Vector2f(+ROTATE_BUTTON_CENTER_SHIFT, ROTATE_BUTTON_Y));


        // Add item selector
        e = bg.addChild(new EditUi_ItemSelector(_shop));
        e.moveY(ITEM_SELECTOR_Y);
    }
}
