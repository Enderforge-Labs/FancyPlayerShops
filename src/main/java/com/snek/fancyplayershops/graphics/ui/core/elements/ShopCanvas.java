package com.snek.fancyplayershops.graphics.ui.core.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.core.styles.ShopCanvasBack_S;
import com.snek.fancyplayershops.graphics.ui.core.styles.ShopCanvasBackground_S;
import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.graphics.core.UiCanvas;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








/**
 * An abstract canvas class used to create shop menus.
 * <p> It creates and manages a background panel, a back side panel and a top and bottom borders,
 * <p> which are kept spawned and are inherited by future canvases until the targeted shop stops being focused.
 */
public abstract class ShopCanvas extends UiCanvas {
    protected final @NotNull Shop shop;


    /**
     * Creates a new ShopCanvas.
     * @param shop The target shop.
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     */
    protected ShopCanvas(final @NotNull Shop shop, final float height, final float heightTop, final float heightBottom) {
        super(shop.getUi(), height, heightTop, heightBottom, new ShopCanvasBackground_S(shop), new ShopCanvasBack_S());
        this.shop = shop;
    }


    public abstract void onStockChange();


    @Override
    public boolean forwardClick(final @NotNull Player player, final @NotNull ClickAction clickType) {
        final boolean player_has_permission = shop.onClick(player, clickType); //TODO check if this is correct
        if(player_has_permission) return super.forwardClick(player, clickType); //TODO check if this is correct
        return true; //TODO check if this is correct
    }




    @Override
    protected void updateRot(final boolean instant) {
        updateItemDisplayRot(lastRotation, instant);
        super.updateRot(instant);
    }


    protected void updateItemDisplayRot(final int initialRotation, final boolean instant) {
        final int newRot = calcRot();
        if(initialRotation != newRot) {
            final Animation animation = calcItemDisplayRotationAnimation(initialRotation, newRot);
            if(instant) shop.getItemDisplay().applyAnimationNowRecursive(animation);
            else        shop.getItemDisplay().applyAnimationRecursive(animation);
        }
    }
}