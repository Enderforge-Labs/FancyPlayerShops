package com.snek.framework.ui.functional.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.input.MessageReceiver;
import com.snek.framework.ui.functional.styles.TextInputElmStyle;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.scheduler.Scheduler;
import com.snek.framework.utils.scheduler.TaskHandler;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








/**
 * A generic text input class that allows the user to enter a chat input after clicking the element.
 */
public abstract class TextInputElm extends FancyButtonElm {
    public static final int CURSOR_TOGGLE_DELAY = 10;

    private final @Nullable Component        clickFeedbackMessage;
    private       @Nullable TaskHandler inputStateHandler = null;
    private                 boolean     cursorToggleState = true;
    private                 boolean     inputState        = false;




    /**
     * Creates a new TextInputElm using a custom style.
     * @param _world The world in which to place the element.
     * @param _clickFeedbackMessage The message to show to the player when they click the element.
     * @param _style The custom style.
     */
    protected TextInputElm(final @NotNull ServerLevel _world, final @Nullable Component _clickFeedbackMessage, final @NotNull TextInputElmStyle _style) {
        super(_world, 1, _style);
        clickFeedbackMessage = _clickFeedbackMessage;
    }


    /**
     * Creates a new TextInputElm using the default style.
     * @param _world The world in which to place the element.
     * @param _clickFeedbackMessage The message to show to the player when they click the element.
     */
    protected TextInputElm(final @NotNull ServerLevel _world, final @Nullable Component _clickFeedbackMessage) {
        this(_world, _clickFeedbackMessage, new TextInputElmStyle());
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        if(!inputState) {
            enterInputState();
            playButtonSound(player);
        }
        if(clickFeedbackMessage != null) player.displayClientMessage(clickFeedbackMessage, true);
        MessageReceiver.setCallback(player, this::__internal_messageCallback);
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
    public void onHoverExit(final @Nullable Player player) {
        super.onHoverExit(player);
        exitInputState();
        if(player != null) MessageReceiver.removeCallback(player);
    }
}
