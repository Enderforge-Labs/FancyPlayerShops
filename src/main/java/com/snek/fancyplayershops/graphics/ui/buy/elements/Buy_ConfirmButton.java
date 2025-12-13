package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.GetShop;
import com.snek.fancyplayershops.graphics.ui.buy.BuyCanvas;
import com.snek.fancyplayershops.graphics.ui.buy.styles.Buy_ConfirmButton_S;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.functional.elements.FancyButtonElm;
import com.snek.frameworklib.graphics.functional.elements.__base_ButtonElm;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Buy_ConfirmButton extends FancyButtonElm {
    private final @NotNull BuyCanvas menu;
    private boolean active = true;


    public Buy_ConfirmButton(final @NotNull Shop _shop, final @NotNull BuyCanvas _menu) {
        super(_shop.getWorld(), null, "Confirm bulk buy", 10, new Buy_ConfirmButton_S(_shop));
        menu = _menu;
    }


    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        updateDisplay(null);
        super.spawn(pos, animate);
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        super.onClick(player, click);

        // Play sound and buy items
        final Shop shop = GetShop.get(this);
        if(active) __base_ButtonElm.playButtonSound(player);
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
        final Buy_ConfirmButton_S s = getStyle(Buy_ConfirmButton_S.class);
        s.setDefaultColor(active ? Buy_ConfirmButton_S.BASE_COLOR : Buy_ConfirmButton_S.BASE_COLOR_INACTIVE);
        applyAnimation(new Transition(4, Easings.expOut).targetBgColor(s.getDefaultBgColor()));
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        getStyle(Buy_ConfirmButton_S.class).setText(new Txt("Buy " + Utils.formatAmountShort(menu.getAmount())).white().get());
        flushStyle();
    }

}
