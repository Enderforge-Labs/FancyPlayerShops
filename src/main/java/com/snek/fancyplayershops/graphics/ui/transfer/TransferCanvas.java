package com.snek.fancyplayershops.graphics.ui.transfer;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.misc.elements.Misc_BackButton;
import com.snek.fancyplayershops.graphics.ui.core.elements.ProductCanvasBase;
import com.snek.fancyplayershops.graphics.ui.edit.EditCanvas;
import com.snek.fancyplayershops.graphics.ui.transfer.elements.Transfer_ConfirmButton;
import com.snek.fancyplayershops.graphics.ui.transfer.elements.Transfer_NameInput;
import com.snek.frameworklib.graphics.core.elements.CanvasBorder;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.interfaces.InputIndicatorCanvas;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.graphics.composite.elements.DualInputIndicator;
import com.snek.frameworklib.graphics.composite.elements.InputIndicator;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;








/**
 * A UI that allows the owner of the product display to transfer it to another player.
 */
public class TransferCanvas extends ProductCanvasBase implements InputIndicatorCanvas {
    public static final float CONFIRM_BUTTON_Y = 0.25f;
    private final @NotNull DualInputIndicator inputIndicator;


    // Instance data
    private final @NotNull Transfer_ConfirmButton confirmButton;
    private @NotNull UUID newOwnerUUID;
    public @NotNull UUID getNewOwnerUUID() { return newOwnerUUID; }







    /**
     * Creates a new TransferUi.
     * @param display The target product display.
     */
    public TransferCanvas(final @NotNull ProductDisplay display) {

        // Call superconstructor
        super(display, calculateTitle(display), 1f, TITLE_H, TOOLBAR_H);
        newOwnerUUID = display.getOwnerUuid();
        Div e;


        // Add player name input
        e = bg.addChild(new SimpleTextElm(display.getLevel(), new SimpleTextElmStyle().withText(new Txt("New owner:").get())));
        e.setSize(new Vector2f(1f, TITLE_H));
        e.setPosY(1f - TITLE_H * 2f);
        e.setAlignmentX(AlignmentX.CENTER);

        e = bg.addChild(new Transfer_NameInput(display, this));
        e.setSize(new Vector2f(1, TITLE_H));
        e.setPosY(1f - TITLE_H * 3f);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add confirm button
        e = bg.addChild(new Transfer_ConfirmButton(display, this));
        e.setSize(new Vector2f(0.5f, TITLE_H));
        e.setPosY(CONFIRM_BUTTON_Y);
        e.setAlignmentX(AlignmentX.CENTER);
        confirmButton = (Transfer_ConfirmButton)e;


        // Add input indicators
        e = bg.addChild(new DualInputIndicator(display.getLevel()));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(TOOLBAR_H + CanvasBorder.DEFAULT_HEIGHT);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;


        //FIXME add item inspector element


        // Add buttons
        setToolbarButtons(new Div[] {
            new Misc_BackButton(context, () -> context.changeCanvas(new EditCanvas(display)))
        });


        // Force button color change
        confirmButton.updateColor(false);
    }




    /**
     * Tries to change the new owner.
     * <p> Updates the confirmation button's color and sends feedback messages to the player.
     * @param s The name of the new owner.
     */
    public void attemptSetNewOwner(final @NotNull String s) {

        // Check if the name is not a valid username. In that case, send an error message to the player and update the input dipslay
        if(!s.matches("^\\w{3,16}$")) {
            display.getuser().displayClientMessage(new Txt("The specified name is not a valid Minecraft username").red().bold().get(), true);
            failNameValidation();
        }

        // If it is, try to set the new owner and update the display in case of success
        else {
            final Player newOwner = MinecraftUtils.getPlayerByName(s);
            if(newOwner == null) {
                display.getuser().displayClientMessage(new Txt("The specified player is currently offline").red().bold().get(), true);
                failNameValidation();
            }
            else if(newOwner.getUUID().equals(display.getOwnerUuid())) {
                display.getuser().displayClientMessage(new Txt("You already own this product display!").red().bold().get(), true);
                failNameValidation();
            }
            else {
                newOwnerUUID = newOwner.getUUID();
                confirmButton.updateColor(true);
            }
        }
    }
    private final void failNameValidation() {
        confirmButton.updateColor(false);
        newOwnerUUID = display.getOwnerUuid();
    }




    @Override
    public void onStockChange() {
        // Empty
    }



    public static @NotNull Component calculateTitle(final @NotNull ProductDisplay display) {
        if(display.getItem().is(Items.AIR)) {
            return new Txt("Transferring an empty product display").white().get();
        }
        else {
            return new Txt()
                .cat(new Txt("Transferring: ").white())
                .cat(display.getStandaloneName())
            .get();
        }
    }




    @Override public @NotNull InputIndicator getLmbIndicator() { return inputIndicator.getLmbIndicator(); }
    @Override public @NotNull InputIndicator getRmbIndicator() { return inputIndicator.getRmbIndicator(); }
    @Override public @Nullable Div getDisclaimerElm() { return null; }
}
