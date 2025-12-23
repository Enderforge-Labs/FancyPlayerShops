package com.snek.fancyplayershops.graphics.ui.core.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.core.styles.ShopCanvasBack_S;
import com.snek.fancyplayershops.graphics.ui.core.styles.ShopCanvasBackground_S;
import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.graphics.core.UiCanvas;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








/**
 * An abstract canvas class used to create shop menus.
 * <p>
 * It stores a reference to the shop and handles the item display.
 */
public abstract class ShopCanvasBase extends UiCanvas {
    protected final @NotNull ProductDisplay shop;


    /**
     * Creates a new ShopCanvas.
     * @param shop The target shop.
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     */
    protected ShopCanvasBase(final @NotNull ProductDisplay shop, final float height, final float heightTop, final float heightBottom) {
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
    protected void rotate(final int from, final int to, final boolean instant) {
        updateItemDisplayRot(from, to, instant);
        super.rotate(from, to, instant);
    }


    protected void updateItemDisplayRot(final int from, final int to, final boolean instant) {
        final Animation animation = calcItemDisplayRotationAnimation(from, to);
        if(instant) shop.getItemDisplay().applyAnimationNowRecursive(animation);
        else        shop.getItemDisplay().applyAnimationRecursive(animation);
    }
}