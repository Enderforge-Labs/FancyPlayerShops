package com.snek.fancyplayershops.ui.misc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.ChatInput;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.styles.ShopTextInput_S;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.scheduler.Scheduler;
import com.snek.framework.utils.scheduler.TaskHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;








/**
 * A generic text input class that allows the user to enter a chat input after clicking the element.
 */
public abstract class ShopTextInput extends ShopButton {
    public static final int CURSOR_TOGGLE_DELAY = 10;

    private final @Nullable Text        clickFeedbackMessage;
    private       @Nullable TaskHandler inputStateHandler = null;
    private                 boolean     cursorToggleState = true;
    private                 boolean     inputState        = false;


    /**
     * Creates a new ShopTextInput.
     * @param _shop The target shop.
     * @param w The width of the button, expressed in blocks.
     * @param h The height of the button, expressed in blocks.
     * @param _clickFeedbackMessage The message to show to the player when they click the element.
     */
    protected ShopTextInput(final @NotNull Shop _shop, final @Nullable String _lmbActionName, final @Nullable String _rmbActionName, final @Nullable Text _clickFeedbackMessage) {
        super(_shop, _lmbActionName, _rmbActionName, 1, new ShopTextInput_S());
        clickFeedbackMessage = _clickFeedbackMessage;
    }




    @Override
    public boolean onClick(final @NotNull PlayerEntity player, final @NotNull ClickType click) {
        final boolean r = super.onClick(player, click);
        if(r) {
            if(!inputState) {
                enterInputState();
                playButtonSound(player);
            }
            if(clickFeedbackMessage != null) player.sendMessage(clickFeedbackMessage, true);
            ChatInput.setCallback(player, this::__internal_messageCallback);
        }
        return r;
    }


    /**
     * Enters the input state.
     * <p> This hides the text to show a blinking cursor.
     */
    protected void enterInputState() {
        if(!inputState) {
            inputState = true;
            cursorToggleState = true;
            inputStateHandler = Scheduler.loop(0, CURSOR_TOGGLE_DELAY, () -> {
                updateDisplay(new Txt(cursorToggleState ? "|" : " ").get());
                cursorToggleState = !cursorToggleState;
            });
        }
    }


    /**
     * Exists the input state.
     * <p> This stops the blinking animation, hides the cursor and shows the text.
     */
    protected void exitInputState() {
        if(inputState) {
            inputState = false;
            if(inputStateHandler != null) inputStateHandler.cancel();
            updateDisplay(null);
        }
    }


    /**
     * A wrapper for the message callback.
     * @param s The input string.
     * @return True if the string s is recognized as an input and should not be broadcasted to the chat, false otherwise.
     */
    protected boolean __internal_messageCallback(final @NotNull String s) {
        final boolean r = messageCallback(s);
        if(r) exitInputState();
        return r;
    }


    /**
     * The callback function called when a chat input is received.
     * @param s The input string.
     * @return True if the string s is recognized as an input and should not be broadcasted to the chat, false otherwise.
     */
    protected abstract boolean messageCallback(@NotNull String s);




    @Override
    public void onHoverExit(final @Nullable PlayerEntity player) {
        if(player != shop.getuser()) return;
        super.onHoverExit(player);
        exitInputState();
        if(player != null) ChatInput.removeCallback(player);
    }
}
