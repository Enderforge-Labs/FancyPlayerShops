package com.snek.fancyplayershops.ui.transfer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.ShopButton;
import com.snek.fancyplayershops.ui.transfer.styles.TransferUi_ConfirmButton_S;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class TransferUi_ConfirmButton extends ShopButton {


    protected TransferUi_ConfirmButton(final @NotNull Shop _shop) {
        super(_shop, null, "Confirm ownership transfer", 1, new TransferUi_ConfirmButton_S(_shop));
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        shop.changeOwner(FancyPlayerShops.getServer().getPlayerList().getPlayer(((TransferUi)shop.getActiveCanvas()).getNewOwnerUUID()));
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        // Empty
    }

}
