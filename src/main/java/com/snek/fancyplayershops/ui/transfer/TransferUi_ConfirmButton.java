package com.snek.fancyplayershops.ui.transfer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.ShopButton;
import com.snek.fancyplayershops.ui.transfer.styles.TransferUi_ConfirmButton_S;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.Utils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class TransferUi_ConfirmButton extends ShopButton {
    private final @NotNull TransferUi menu;
    private boolean active = true;


    protected TransferUi_ConfirmButton(final @NotNull Shop _shop, final @NotNull TransferUi _menu) {
        super(_shop, null, "Confirm ownership transfer", 1, new TransferUi_ConfirmButton_S(_shop));
        menu = _menu;
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        shop.changeOwner(FancyPlayerShops.getServer().getPlayerList().getPlayer(menu.getNewOwnerUUID()));
        if(active) playButtonSound(player);
    }


    public void updateColor(final boolean _active) {
        if(active == _active) return;
        active = _active;
        final TransferUi_ConfirmButton_S s = getStyle(TransferUi_ConfirmButton_S.class);
        s.setDefaultColor(active ? TransferUi_ConfirmButton_S.BASE_COLOR : Utils.toBW(TransferUi_ConfirmButton_S.BASE_COLOR));
        applyAnimation(new Transition(4, Easings.expOut).targetBgColor(s.getDefaultBgColor()));
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        // Empty
    }

}
