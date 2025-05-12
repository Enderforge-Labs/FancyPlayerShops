package com.snek.fancyplayershops.ui.transfer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.ShopButton;
import com.snek.fancyplayershops.ui.transfer.styles.TransferUi_ConfirmButton_S;
import com.snek.framework.ui.elements.styles.FancyTextElmStyle;
import com.snek.framework.utils.Utils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class TransferUi_ConfirmButton extends ShopButton {
    private final @NotNull TransferUi menu;


    protected TransferUi_ConfirmButton(final @NotNull Shop _shop, final @NotNull TransferUi _menu) {
        super(_shop, null, "Confirm ownership transfer", 1, new TransferUi_ConfirmButton_S(_shop));
        menu = _menu;
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        shop.changeOwner(FancyPlayerShops.getServer().getPlayerList().getPlayer(menu.getNewOwnerUUID()));
    }


    public void updateColor(final boolean active) {
        final FancyTextElmStyle s = getStyle(FancyTextElmStyle.class);
        if(active) {
            s.resetBgColor();
        }
        else {
            s.setBgColor(Utils.toBW(s.getDefaultBgColor()));
        }
        flushStyle();
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        // Empty
    }

}
