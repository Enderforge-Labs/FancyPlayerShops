package com.snek.fancyplayershops.ui.buy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.buy.styles.BuyUi_ConfirmButton_S;
import com.snek.fancyplayershops.ui.misc.ShopButton;
import com.snek.fancyplayershops.ui.transfer.styles.TransferUi_ConfirmButton_S;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class BuyUi_ConfirmButton extends ShopButton {
    private final @NotNull BuyUi menu;


    protected BuyUi_ConfirmButton(final @NotNull Shop _shop, final @NotNull BuyUi _menu) {
        super(_shop, null, "Confirm bulk buy", 10, new BuyUi_ConfirmButton_S(_shop));
        menu = _menu;
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        if(player.getUUID().equals(shop.getOwnerUuid())) {
            shop.retrieveItem(player, menu.getAmount());
        }
        else {
            shop.buyItem(player, menu.getAmount());
        }
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        // Empty
    }

}
