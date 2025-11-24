package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.buy.BuyUi;
import com.snek.fancyplayershops.graphics.ui.buy.styles.BuyUi_ConfirmButton_S;
import com.snek.fancyplayershops.graphics.ui.misc.elements.FancyShopButton;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class BuyUi_ConfirmButton extends FancyShopButton {
    private final @NotNull BuyUi menu;
    private boolean active = true;


    public BuyUi_ConfirmButton(final @NotNull Shop _shop, final @NotNull BuyUi _menu) {
        super(_shop, null, "Confirm bulk buy", 10, new BuyUi_ConfirmButton_S(_shop));
        menu = _menu;
        updateDisplay(null);
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        super.onClick(player, click);
        if(active) playButtonSound(player);
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
        getStyle(BuyUi_ConfirmButton_S.class).setText(new Txt("Buy " + Utils.formatAmountShort(menu.getAmount())).white().get());
        flushStyle();
    }

}
