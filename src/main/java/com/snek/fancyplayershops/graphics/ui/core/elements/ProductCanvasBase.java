package com.snek.fancyplayershops.graphics.ui.core.elements;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.core.styles.ProductCanvasBack_S;
import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.core.UiCanvas;
import com.snek.frameworklib.graphics.core.UiContext;
import com.snek.frameworklib.graphics.layout.Div;

import net.minecraft.server.level.ServerLevel;
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
    public static final float DEFAULT_HEIGHT = FancyPlayerShops.LINE_H * 0.75f;
    public static final float DEFAULT_DISTANCE = 0.02f;


    // Temporary product display reference
    //! This is used to initialize the default canvas elements
    //! the overridden provider method is called before the local reference can be initialized by the subclass
    private static ProductDisplay __tmp_display_ref;


    protected final @NotNull ProductDisplay display;
    public abstract @Nullable Div getDisclaimerElm(); //TODO REMOVE


    /**
     * Creates a new ProductDisplayCanvasBase.
     * @param display The target display.
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     */
    protected ProductCanvasBase(final @NotNull ProductDisplay display, final float height, final float heightTop, final float heightBottom) {
        super(((Supplier<UiContext>)()->{ __tmp_display_ref = display; return display.getUi(); }).get(), height, heightTop, heightBottom);
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

        // Click the shop first. This allows non-users to retrieve items by left clicking
        final boolean player_has_permission = display.onClick(player, clickType);

        // If the player is the user, click the canvas (which is now EditCanvas or BuyCanvas)
        if(player_has_permission) {

            // // Attempt to click the disclaimer. If that fails, click the actual canvas //TODO remove
            // //! This is done to allow the disclaimer element to be clickable even though it's not inside of the canvas's hitbox //TODO remove
            // if(!display.attemptDisclaimerClick(player, clickType)) { //TODO remove
                return super.forwardClick(player, clickType);
            // }
            // else return true;
        }

        // If the player is not the user, return true (click was consumed)
        else {
            return true;
        }
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


    @Override
    public float getInteractionSizeTop() {
        return super.getInteractionSizeTop() + DEFAULT_DISTANCE + DEFAULT_HEIGHT;
    }



    @Override public PanelElm createNewBgElement  (final @NotNull ServerLevel level) { return new ProductCanvasBackground(__tmp_display_ref); }
    @Override public PanelElm createNewBackElement(final @NotNull ServerLevel level) { return new PanelElm(level, new ProductCanvasBack_S()); }
}