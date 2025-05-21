package com.snek.fancyplayershops.shop_ui.misc.elements;

import com.snek.framework.ui.functional.ButtonElm;

import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.shop_ui.misc.interfaces.InputIndicatorCanvas;
import com.snek.fancyplayershops.shop_ui.misc.styles.ShopButton_S;








/**
 * A generic button class with clicking and hovering capabilities and a configurable cooldown time.
 * <p> It also specifies action names for the input indicators to display.
 */
public abstract class ShopButton extends ButtonElm {
    protected final @NotNull Shop shop;
    private   final @Nullable String lmbActionName;
    private   final @Nullable String rmbActionName;




    /**
     * Creates a new ShopButton using a custom style.
     * @param _shop The target shop.
     * @param _lmbActionName The name of the action associated with left clicks.
     * @param _rmbActionName The name of the action associated with right clicks.
     * @param clickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     * @param _style The custom style.
     */
    protected ShopButton(final @NotNull Shop _shop, final @Nullable String _lmbActionName, final @Nullable String _rmbActionName, final int _clickCooldown, final ShopButton_S _style) {
        super(_shop.getWorld(), _clickCooldown, _style);
        shop = _shop;
        lmbActionName = _lmbActionName;
        rmbActionName = _rmbActionName;
    }


    /**
     * Creates a new ShopButton using the default style.
     * @param _shop The target shop.
     * @param _lmbActionName The name of the action associated with left clicks.
     * @param _rmbActionName The name of the action associated with right clicks.
     * @param clickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     */
    protected ShopButton(final @NotNull Shop _shop, final @Nullable String _lmbActionName, final @Nullable String _rmbActionName, final int _clickCooldown) {
        this(_shop, _lmbActionName, _rmbActionName, _clickCooldown, new ShopButton_S(_shop));
    }




    @Override
    public void onHoverEnter(final @NotNull Player player) {
        if(player != shop.getuser()) return;
        super.onHoverEnter(player);
    }




    @Override
    public void onHoverTick(final @NotNull Player player) {
        super.onHoverTick(player);

        // Update input displays if present
        if(shop.getActiveCanvas() != null && shop.getActiveCanvas() instanceof InputIndicatorCanvas c) {
            c.getLmbIndicator().updateDisplay(lmbActionName);
            c.getRmbIndicator().updateDisplay(rmbActionName);
        }
    }




    @Override
    public void onHoverExit(final @Nullable Player player) {
        if(player != shop.getuser()) return;
        super.onHoverExit(player);

        // Update input displays if present
        if(shop.getActiveCanvas() != null && shop.getActiveCanvas() instanceof InputIndicatorCanvas c) {
            c.getLmbIndicator().updateDisplay(null);
            c.getRmbIndicator().updateDisplay(null);
        }
    }
}
