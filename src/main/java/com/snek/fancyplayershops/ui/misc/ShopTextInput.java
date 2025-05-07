package com.snek.fancyplayershops.ui.misc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.interfaces.InputIndicatorCanvas;
import com.snek.fancyplayershops.ui.misc.styles.ShopTextInput_S;
import com.snek.framework.ui.functional.TextInputElm;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;








/**
 * A generic text input class that allows the user to enter a chat input after clicking the element.
 */
public abstract class ShopTextInput extends TextInputElm {
    public static final int CURSOR_TOGGLE_DELAY = 10;


    protected final @NotNull Shop shop;
    private   final @Nullable String lmbActionName;
    private   final @Nullable String rmbActionName;




    /**
     * Creates a new ShopTextInput.
     * @param _shop The target shop.
     * @param _lmbActionName The name of the action associated with left clicks.
     * @param _rmbActionName The name of the action associated with right clicks.
     * @param _clickFeedbackMessage The message to show to the player when they click the element.
     */
    protected ShopTextInput(final @NotNull Shop _shop, final @Nullable String _lmbActionName, final @Nullable String _rmbActionName, final @Nullable Component _clickFeedbackMessage) {
        super(_shop.getWorld(), _clickFeedbackMessage, new ShopTextInput_S());
        shop = _shop;
        lmbActionName = _lmbActionName;
        rmbActionName = _rmbActionName;
    }




    @Override
    public void onHoverEnter(final @Nullable Player player) {
        if(player != shop.getuser()) return;
        super.onHoverEnter(player);
    }




    @Override
    public void onCheckTick(final @NotNull Player player) {
        super.onCheckTick(player);

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
