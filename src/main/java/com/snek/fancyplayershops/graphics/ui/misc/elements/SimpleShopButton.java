package com.snek.fancyplayershops.graphics.ui.misc.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.misc.styles.SimpleShopButton_S;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;








/**
 * A generic button class with clicking and hovering capabilities and a configurable cooldown time.
 * <p> It also specifies action names for the input indicators to display.
 */
public abstract class SimpleShopButton extends SimpleButtonElm {
    protected final @NotNull Shop shop;




    /**
     * Creates a new ShopButton using a custom style.
     * @param _shop The target shop.
     * @param _lmbActionName The name of the action associated with left clicks.
     * @param _rmbActionName The name of the action associated with right clicks.
     * @param clickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     * @param _style The custom style.
     */
    protected SimpleShopButton(final @NotNull Shop _shop, final @Nullable String _lmbActionName, final @Nullable String _rmbActionName, final int _clickCooldown, final SimpleShopButton_S _style) {
        super(_shop.getWorld(), _lmbActionName, _rmbActionName, _clickCooldown, _style);
        shop = _shop;
    }


    /**
     * Creates a new ShopButton using the default style.
     * @param _shop The target shop.
     * @param _lmbActionName The name of the action associated with left clicks.
     * @param _rmbActionName The name of the action associated with right clicks.
     * @param clickCooldown The amount of ticks before the button becomes clickable again after being clicked.
     */
    protected SimpleShopButton(final @NotNull Shop _shop, final @Nullable String _lmbActionName, final @Nullable String _rmbActionName, final int _clickCooldown) {
        this(_shop, _lmbActionName, _rmbActionName, _clickCooldown, new SimpleShopButton_S(_shop));
    }
}
