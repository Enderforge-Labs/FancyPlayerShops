package com.snek.fancyplayershops.graphics.ui.transfer.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.misc.elements.FancyShopButton;
import com.snek.fancyplayershops.graphics.ui.transfer.TransferUi;
import com.snek.fancyplayershops.graphics.ui.transfer.styles.TransferUi_ConfirmButton_S;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.functional.elements.__base_ButtonElm;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class TransferUi_ConfirmButton extends FancyShopButton {
    private final @NotNull TransferUi menu;
    private boolean active = true;


    public TransferUi_ConfirmButton(final @NotNull Shop _shop, final @NotNull TransferUi _menu) {
        super(_shop, null, "Confirm ownership transfer", 1, new TransferUi_ConfirmButton_S(_shop));
        menu = _menu;
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        super.onClick(player, click);
        shop.changeOwner(FrameworkLib.getServer().getPlayerList().getPlayer(menu.getNewOwnerUUID()));
        if(active) __base_ButtonElm.playButtonSound(player);
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
