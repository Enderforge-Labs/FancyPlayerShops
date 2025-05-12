package com.snek.fancyplayershops.ui.buy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.buy.styles.BuyUi_ConfirmButton_S;
import com.snek.fancyplayershops.ui.misc.ShopButton;
import com.snek.framework.ui.elements.styles.FancyTextElmStyle;
import com.snek.framework.utils.Utils;

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
