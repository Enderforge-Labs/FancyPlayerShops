package com.snek.fancyplayershops.ui.transfer;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.ShopCanvas;
import com.snek.fancyplayershops.ui.edit.EditUiSub_BackButton;
import com.snek.fancyplayershops.ui.misc.DualInputIndicator;
import com.snek.fancyplayershops.ui.misc.InputIndicator;
import com.snek.fancyplayershops.ui.misc.ShopFancyTextElm;
import com.snek.fancyplayershops.ui.misc.ShopUiBorder;
import com.snek.fancyplayershops.ui.misc.interfaces.InputIndicatorCanvas;
import com.snek.fancyplayershops.ui.transfer.styles.TransferUi_NameInput_S;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.data_types.ui.TextAlignment;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.ui.elements.TextElm;
import com.snek.framework.ui.elements.styles.TextElmStyle;
import com.snek.framework.utils.Txt;

import net.minecraft.world.entity.player.Player;








/**
 * A UI that allows the owner of the shop to edit it.
 */
public class TransferUi extends ShopCanvas implements InputIndicatorCanvas {
    public static final float CONFIRM_BUTTON_Y = 0.25f;
    private final @NotNull DualInputIndicator inputIndicator;


    // Instance data
    private final @NotNull TransferUi_ConfirmButton confirmButton;
    private final @NotNull Shop shop;
    private @NotNull UUID newOwnerUUID;
    public @NotNull UUID getNewOwnerUUID() { return newOwnerUUID; }







    /**
     * Creates a new TransferUi.
     * @param _shop The target shop.
     */
    public TransferUi(final @NotNull Shop _shop) {

        // Call superconstructor
        super(_shop, 1f, ShopFancyTextElm.LINE_H, SQUARE_BUTTON_SIZE);
        shop = _shop;
        newOwnerUUID = _shop.getOwnerUuid();
        Div e;


        // Add title
        e = bg.addChild(new TransferUi_Title(_shop));
        e.setPosY(1f - ShopFancyTextElm.LINE_H * 1f);
        e.setSizeY(ShopFancyTextElm.LINE_H);
        e.setAlignmentX(AlignmentX.CENTER);


        // Add player name input
        e = bg.addChild(new TextElm(_shop.getWorld()));
        e.setSize(new Vector2f(0.75f, ShopFancyTextElm.LINE_H));
        e.setPosY(1f - ShopFancyTextElm.LINE_H * 2f);
        e.setAlignmentX(AlignmentX.CENTER);
        ((Elm)e).getStyle(TextElmStyle.class).setTextAlignment(TextAlignment.CENTER);
        ((Elm)e).getStyle(TextElmStyle.class).setText(new Txt("New owner:").get());

        e = bg.addChild(new TransferUi_NameInput(_shop, this));
        e.setSize(new Vector2f(0.75f, ShopFancyTextElm.LINE_H));
        e.setPosY(1f - ShopFancyTextElm.LINE_H * 3f);
        e.setAlignmentX(AlignmentX.CENTER);
        ((Elm)e).getStyle(TransferUi_NameInput_S.class).setTextAlignment(TextAlignment.CENTER);


        // Add confirm button
        e = bg.addChild(new TransferUi_ConfirmButton(_shop, this));
        e.setSize(new Vector2f(0.5f, ShopFancyTextElm.LINE_H));
        e.setPosY(CONFIRM_BUTTON_Y);
        e.setAlignmentX(AlignmentX.CENTER);
        confirmButton = (TransferUi_ConfirmButton)e;


        // Add input indicators
        e = bg.addChild(new DualInputIndicator(_shop));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(SQUARE_BUTTON_SIZE + ShopUiBorder.DEFAULT_HEIGHT);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;


        // Add back button
        e = bg.addChild(new EditUiSub_BackButton(_shop));
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
            final Player newOwner = FancyPlayerShops.getServer().getPlayerList().getPlayerByName(s);
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




    @Override public @NotNull InputIndicator getLmbIndicator() { return inputIndicator.getLmbIndicator(); }
    @Override public @NotNull InputIndicator getRmbIndicator() { return inputIndicator.getRmbIndicator(); }
}
