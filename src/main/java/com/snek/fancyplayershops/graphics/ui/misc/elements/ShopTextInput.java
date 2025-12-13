package com.snek.fancyplayershops.graphics.ui.misc.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.misc.styles.ShopTextInput_S;
import com.snek.frameworklib.graphics.functional.elements.TextInputElm;

import net.minecraft.network.chat.Component;








/**
 * A generic text input class that allows the user to enter a chat input after clicking the element.
 */
public abstract class ShopTextInput extends TextInputElm {
    public static final int CURSOR_TOGGLE_DELAY = 10;
    protected final @NotNull Shop shop;




    /**
     * Creates a new ShopTextInput using a custom style.
     * @param _shop The target shop.
     * @param _lmbActionName The name of the action associated with left clicks.
     * @param _rmbActionName The name of the action associated with right clicks.
     * @param _clickFeedbackMessage The message to show to the player when they click the element.
     * @param _style The custom style.
     */
    protected ShopTextInput(final @NotNull Shop _shop, final @Nullable String _lmbActionName, final @Nullable String _rmbActionName, final @Nullable Component _clickFeedbackMessage, final @NotNull ShopTextInput_S _style) {
        super(_shop.getWorld(), _lmbActionName, _rmbActionName, _clickFeedbackMessage, _style);
        shop = _shop;
    }

    /**
     * Creates a new ShopTextInput using the default style.
     * @param _shop The target shop.
     * @param _lmbActionName The name of the action associated with left clicks.
     * @param _rmbActionName The name of the action associated with right clicks.
     * @param _clickFeedbackMessage The message to show to the player when they click the element.
     */
    protected ShopTextInput(final @NotNull Shop _shop, final @Nullable String _lmbActionName, final @Nullable String _rmbActionName, final @Nullable Component _clickFeedbackMessage) {
        this(_shop, _lmbActionName, _rmbActionName, _clickFeedbackMessage, new ShopTextInput_S(_shop));
    }
}
