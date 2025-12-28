package com.snek.fancyplayershops.graphics.ui.core.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.core.styles.ProductCanvasBack_S;
import com.snek.fancyplayershops.graphics.ui.core.styles.ProductCanvasBackground_S;
import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.graphics.core.UiCanvas;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;






//TODO use the same base canvas system for the HUDs.
//TODO add callbacks for stash changes / external display and shop removals / balance changes


/**
 * An abstract canvas class used to create product display menus.
 * <p>
 * It stores a reference to the product display and handles the item display.
 */
public abstract class ProductCanvasBase extends UiCanvas {
    protected final @NotNull ProductDisplay display;


    /**
     * Creates a new ProductDisplayCanvasBase.
     * @param display The target display.
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     */
    protected ProductCanvasBase(final @NotNull ProductDisplay display, final float height, final float heightTop, final float heightBottom) {
        super(display.getUi(), height, heightTop, heightBottom, new ProductCanvasBackground_S(display), new ProductCanvasBack_S());
        this.display = display;
    }


    /**
     * Callback method called any time the current stock changes.
     * This is also called once right before the canvas spawns into the level.
     */
    public abstract void onStockChange();


    @Override
    public void spawn(@NotNull final Vector3d pos, final boolean animate) {
        onStockChange();
        super.spawn(pos, animate);
    }


    @Override
    public boolean forwardClick(final @NotNull Player player, final @NotNull ClickAction clickType) {
        final boolean player_has_permission = display.onClick(player, clickType);
        if(player_has_permission) return super.forwardClick(player, clickType);
        return true;
    }




    @Override
    protected void rotate(final int from, final int to, final boolean animate) {
        updateItemDisplayRot(from, to, animate);
        super.rotate(from, to, animate);
    }


    protected void updateItemDisplayRot(final int from, final int to, final boolean animate) {
        final Animation animation = calcItemDisplayRotationAnimation(from, to);
        display.getItemDisplay().applyAnimation(animation, true, animate);
    }
}