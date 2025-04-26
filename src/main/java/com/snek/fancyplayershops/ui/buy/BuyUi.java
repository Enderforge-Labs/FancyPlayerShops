package com.snek.fancyplayershops.ui.buy;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.ShopCanvas;
import com.snek.fancyplayershops.ui.misc.ShopFancyTextElm;
import com.snek.fancyplayershops.ui.misc.ShopUiBorder;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.Elm;







//TODO add left click functionality to the item selector that lets you open a 1-slot UI
//TODO to read the item's name, lore and tags as if it were in a normal chest

//TODO add small text elements in a corner of the UIs that tell you what each mouse button does when clicked.
//TODO they change based on the player's currently hovered element.

/**
 * A UI that allows the user of a shop to buy items from it.
 */
public class BuyUi extends ShopCanvas {
    private final @NotNull Elm title;
    public @NotNull Elm getTitle() { return title; }








    /**
     * Creates a new EditUi.
     * @param _shop The target shop.
     */
    public BuyUi(Shop _shop) {

        // Call superconstructor
        super(_shop, 1, ShopFancyTextElm.LINE_H, ShopUiBorder.DEFAULT_HEIGHT);
        Div e;


        // Add title
        e = bg.addChild(new BuyUi_Title(_shop));
        e.moveY(1f - ShopFancyTextElm.LINE_H * 1f);
        e.setSizeY(ShopFancyTextElm.LINE_H);
        e.setAlignmentX(AlignmentX.CENTER);
        title = (Elm)e;

        // Add item selector //FIXME replace with an "item inspector" element. make the selector it's subclass
        // e = bg.addChild(new EditUiItemSelector(_shop));
        // e.moveY(ITEM_SELECTOR_Y);
    }
}
