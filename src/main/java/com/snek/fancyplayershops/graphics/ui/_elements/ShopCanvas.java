package com.snek.fancyplayershops.graphics.ui._elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.ShopUI;
import com.snek.fancyplayershops.graphics.ui._styles.ShopCanvasBack_S;
import com.snek.fancyplayershops.graphics.ui._styles.ShopCanvasBackground_S;
import com.snek.frameworklib.graphics.ui._elements.UiCanvas;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;


//TODO move to ui/misc/elements





/**
 * An abstract canvas class used to create shop menus.
 * <p> It creates and manages a background panel, a back side panel and a top and bottom borders,
 * <p> which are kept spawned and are inherited by future canvases until the targeted shop stops being focused.
 */
public abstract class ShopCanvas extends UiCanvas {
    private final @NotNull Shop shop;


    /**
     * Creates a new ShopCanvas.
     * @param _shop The target shop.
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     */
    protected ShopCanvas(final @NotNull Shop _shop, final float height, final float heightTop, final float heightBottom) {
        super(_shop.getUi(), (UiCanvas)_shop.getUi().getActiveCanvas(), _shop.getWorld(), height, heightTop, heightBottom, new ShopCanvasBackground_S(_shop), new ShopCanvasBack_S());
        shop = _shop;
    }


    public abstract void onStockChange();



    @Override
    public boolean forwardClick(final @NotNull Player player, final @NotNull ClickAction clickType) {
        final boolean player_has_permission = shop.onClick(player, clickType); //TODO check if this is correct
        if(player_has_permission) return super.forwardClick(player, clickType); //TODO check if this is correct
        return true; //TODO check if this is correct
    }
}