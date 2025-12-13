package com.snek.fancyplayershops.graphics.ui.transfer;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.misc.elements.TitleElm;
import com.snek.fancyplayershops.graphics.ui.core.elements.ShopCanvasBase;
import com.snek.fancyplayershops.graphics.ui.edit.elements.Edit_Sub_BackButton;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ShopFancyTextElm;
import com.snek.fancyplayershops.graphics.ui.transfer.elements.Transfer_ConfirmButton;
import com.snek.fancyplayershops.graphics.ui.transfer.elements.Transfer_NameInput;
import com.snek.fancyplayershops.graphics.ui.transfer.styles.Transfer_Input_S;
import com.snek.frameworklib.graphics.core.elements.CanvasBorder;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.interfaces.InputIndicatorCanvas;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.graphics.composite.elements.DualInputIndicator;
import com.snek.frameworklib.graphics.composite.elements.InputIndicator;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;








/**
 * A UI that allows the owner of the shop to edit it.
 */
public class TransferCanvas extends ShopCanvasBase implements InputIndicatorCanvas {
    public static final float CONFIRM_BUTTON_Y = 0.25f;
    private final @NotNull DualInputIndicator inputIndicator;


    // Instance data
    private final @NotNull Transfer_ConfirmButton confirmButton;
    private @NotNull UUID newOwnerUUID;
    public @NotNull UUID getNewOwnerUUID() { return newOwnerUUID; }







    /**
     * Creates a new TransferUi.
     * @param _shop The target shop.
     */
    public TransferCanvas(final @NotNull Shop _shop) {

        // Call superconstructor
        super(_shop, 1f, ShopFancyTextElm.LINE_H, SQUARE_BUTTON_SIZE);
        newOwnerUUID = _shop.getOwnerUuid();
        Div e;


        // Add title
        e = bg.addChild(new TitleElm(_shop.getWorld(), recalculateTitle()));
        e.setPosY(1f - ShopFancyTextElm.LINE_H * 1f);
        e.setSize(new Vector2f(TitleElm.DEFAULT_W, ShopFancyTextElm.LINE_H));
        e.setAlignmentX(AlignmentX.CENTER);


        // Add player name input
        e = bg.addChild(new SimpleTextElm(_shop.getWorld()));
        e.setSize(new Vector2f(1f, ShopFancyTextElm.LINE_H));
        e.setPosY(1f - ShopFancyTextElm.LINE_H * 2f);
        e.setAlignmentX(AlignmentX.CENTER);
        ((Elm)e).getStyle(SimpleTextElmStyle.class).setText(new Txt("New owner:").get());

        e = bg.addChild(new Transfer_NameInput(_shop, this));
        e.setSize(new Vector2f(1, ShopFancyTextElm.LINE_H));
        e.setPosY(1f - ShopFancyTextElm.LINE_H * 3f);
        e.setAlignmentX(AlignmentX.CENTER);
        ((Elm)e).getStyle(Transfer_Input_S.class).setTextAlignment(TextAlignment.CENTER);


        // Add confirm button
        e = bg.addChild(new Transfer_ConfirmButton(_shop, this));
        e.setSize(new Vector2f(0.5f, ShopFancyTextElm.LINE_H));
        e.setPosY(CONFIRM_BUTTON_Y);
        e.setAlignmentX(AlignmentX.CENTER);
        confirmButton = (Transfer_ConfirmButton)e;


        // Add input indicators
        e = bg.addChild(new DualInputIndicator(_shop.getWorld()));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(SQUARE_BUTTON_SIZE + CanvasBorder.DEFAULT_HEIGHT);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;


        // Add back button
        e = bg.addChild(new Edit_Sub_BackButton(_shop));
        e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.BOTTOM);


        // Force button color change
        confirmButton.updateColor(false);
    }




    /**
     * Tries to change the new owner.
     * <p> Updates the confirmation button's color and sends feedback messages to the player.
     * @param s The name of the new owner.
     */
    public void attemptSetNewOwner(final @NotNull String s) {

        // Check if the name is not a valid username
        if(!s.matches("^\\w{3,16}$")) {
            shop.getuser().displayClientMessage(new Txt("The specified name is not a valid Minecraft username.").red().bold().get(), true);
            confirmButton.updateColor(false);
        }

        // If it is, try to set the new owner and update the display in case of success
        else {
            final Player newOwner = FrameworkLib.getServer().getPlayerList().getPlayerByName(s);
            if(newOwner == null) {
                shop.getuser().displayClientMessage(new Txt("The specified player is currently offline.").red().bold().get(), true);
                confirmButton.updateColor(false);
            }
            else if(newOwner.getUUID().equals(shop.getOwnerUuid())) {
                shop.getuser().displayClientMessage(new Txt("You already own this shop!").red().bold().get(), true);
                confirmButton.updateColor(false);
            }
            else {
                newOwnerUUID = newOwner.getUUID();
                confirmButton.updateColor(true);
            }
        }
    }




    @Override
    public void onStockChange() {
        // Empty
    }



    public @NotNull Component recalculateTitle() {
        if(shop.getItem().is(Items.AIR)) {
            return new Txt("Transferring an empty shop").white().get();
        }
        else {
            return new Txt()
                .cat(new Txt("Transferring: ").white())
                .cat(shop.getStandaloneName())
            .get();
        }
    }




    @Override public @NotNull InputIndicator getLmbIndicator() { return inputIndicator.getLmbIndicator(); }
    @Override public @NotNull InputIndicator getRmbIndicator() { return inputIndicator.getRmbIndicator(); }
}
