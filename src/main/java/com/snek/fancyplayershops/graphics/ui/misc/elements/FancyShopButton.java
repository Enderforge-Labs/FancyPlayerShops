package com.snek.fancyplayershops.graphics.ui.misc.elements;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.misc.interfaces.Any_InputIndicatorCanvas;
import com.snek.fancyplayershops.graphics.ui.misc.styles.FancyShopButton_S;
import com.snek.frameworklib.graphics.functional.elements.FancyButtonElm;








/**
 * A generic button class with clicking and hovering capabilities and a configurable cooldown time.
 * <p> It also specifies action names for the input indicators to display.
 */
public abstract class FancyShopButton extends FancyButtonElm {
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
    protected FancyShopButton(final @NotNull Shop _shop, final @Nullable String _lmbActionName, final @Nullable String _rmbActionName, final int _clickCooldown, final FancyShopButton_S _style) {
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
    protected FancyShopButton(final @NotNull Shop _shop, final @Nullable String _lmbActionName, final @Nullable String _rmbActionName, final int _clickCooldown) {
        this(_shop, _lmbActionName, _rmbActionName, _clickCooldown, new FancyShopButton_S(_shop));
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
        if(canvas != null && canvas instanceof Any_InputIndicatorCanvas c) {
            c.getLmbIndicator().updateDisplay(lmbActionName);
            c.getRmbIndicator().updateDisplay(rmbActionName);
        }
    }




    @Override
    public void onHoverExit(final @Nullable Player player) {
        if(player != shop.getuser()) return;
        super.onHoverExit(player);

        // Update input displays if present
        if(canvas != null && canvas instanceof Any_InputIndicatorCanvas c) {
            c.getLmbIndicator().updateDisplay(null);
            c.getRmbIndicator().updateDisplay(null);
        }
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        super.onClick(player, click);
    }
}
