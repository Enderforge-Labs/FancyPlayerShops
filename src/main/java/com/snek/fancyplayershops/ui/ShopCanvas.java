package com.snek.fancyplayershops.ui;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.hud.Canvas;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.styles.UiCanvasBack_S;
import com.snek.fancyplayershops.ui.misc.styles.UiCanvasBackground_S;








/**
 * An abstract canvas class used to create shop menus.
 * <p> It creates and manages a background panel, a back side panel and a top and bottom borders,
 * <p> which are kept spawned and are inherited by future canvases until the targeted shop stops being focused.
 */
public abstract class ShopCanvas extends Canvas {


    /**
     * Creates a new ShopCanvas.
     * @param _shop The target shop.
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     */
    protected ShopCanvas(final @NotNull Shop _shop, final float height, final float heightTop, final float heightBottom) {
        super(_shop.getActiveCanvas(), _shop.getWorld(), height, heightTop, heightBottom, new UiCanvasBackground_S(_shop), new UiCanvasBack_S());
    }


    public abstract void onStockChange();
}