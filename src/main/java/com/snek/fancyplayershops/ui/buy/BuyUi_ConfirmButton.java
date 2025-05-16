package com.snek.fancyplayershops.ui.buy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.buy.styles.BuyUi_ConfirmButton_S;
import com.snek.fancyplayershops.ui.misc.ShopButton;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class BuyUi_ConfirmButton extends ShopButton {
    private final @NotNull BuyUi menu;
    private boolean active = true;


    protected BuyUi_ConfirmButton(final @NotNull Shop _shop, final @NotNull BuyUi _menu) {
        super(_shop, null, "Confirm bulk buy", 10, new BuyUi_ConfirmButton_S(_shop));
        menu = _menu;
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        if(player.getUUID().equals(shop.getOwnerUuid())) {
            shop.retrieveItem(player, menu.getAmount(), true);
        }
        else {
            shop.buyItem(player, menu.getAmount(), true);
        }
    }


    public void updateColor(final boolean _active) {
        if(active == _active) return;
        active = _active;
        final BuyUi_ConfirmButton_S s = getStyle(BuyUi_ConfirmButton_S.class);
        s.setDefaultColor(active ? BuyUi_ConfirmButton_S.BASE_COLOR : BuyUi_ConfirmButton_S.BASE_COLOR_INACTIVE);
        applyAnimation(new Transition(4, Easings.expOut).targetBgColor(s.getDefaultBgColor()));
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        // Empty
    }

}
