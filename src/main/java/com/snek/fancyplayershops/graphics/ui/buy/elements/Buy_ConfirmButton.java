package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.graphics.ui.buy.BuyCanvas;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ProductDIsplay_ToggleableButton;
import com.snek.fancyplayershops.graphics.ui.misc.styles.ProductDisplay_TogglableButton_S;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Buy_ConfirmButton extends ProductDIsplay_ToggleableButton {
    private final @NotNull BuyCanvas menu;


    public Buy_ConfirmButton(final @NotNull ProductDisplay display, final @NotNull BuyCanvas _menu) {
        super(display, null, "Confirm bulk buy", 10, "");
        menu = _menu;
    }


    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        updateDisplay();
        super.spawn(pos, animate);
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);

        // Play sound and buy items
        final ProductDisplay display = GetDisplay.get(this);
        if(isActive()) Clickable.playSound(player);
        if(player.getUUID().equals(display.getOwnerUuid())) {
            display.retrieveItem(player, menu.getAmount(), true);
        }
        else {
            display.buyItem(player, menu.getAmount());
        }
    }


    public void updateDisplay() {
        getStyle(ProductDisplay_TogglableButton_S.class).setText(new Txt("Buy " + Utils.formatAmountShort(menu.getAmount())).white().get());
        flushStyle();
    }

}
