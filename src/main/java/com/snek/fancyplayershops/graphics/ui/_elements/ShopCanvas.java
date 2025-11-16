package com.snek.fancyplayershops.graphics.ui._elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui._styles.ShopCanvasBack_S;
import com.snek.fancyplayershops.graphics.ui._styles.ShopCanvasBackground_S;
import com.snek.fancyplayershops.ui.old._elements.UiCanvas;


//TODO move to ui/misc/elements





/**
 * An abstract canvas class used to create shop menus.
 * <p> It creates and manages a background panel, a back side panel and a top and bottom borders,
 * <p> which are kept spawned and are inherited by future canvases until the targeted shop stops being focused.
 */
public abstract class ShopCanvas extends UiCanvas {


    /**
     * Creates a new ShopCanvas.
     * @param _shop The target shop.
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     */
    protected ShopCanvas(final @NotNull Shop _shop, final float height, final float heightTop, final float heightBottom) {
        super(_shop.getActiveCanvas(), _shop.getWorld(), height, heightTop, heightBottom, new ShopCanvasBackground_S(_shop), new ShopCanvasBack_S());
    }


    public abstract void onStockChange();
}