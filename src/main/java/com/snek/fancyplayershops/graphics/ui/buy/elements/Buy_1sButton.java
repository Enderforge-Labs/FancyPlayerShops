package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.graphics.ui.buy.BuyCanvas;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ProductDIsplay_ToggleableButton;
import com.snek.frameworklib.graphics.designs.ItemDesigns;
import com.snek.frameworklib.graphics.interfaces.Clickable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Buy_1sButton extends ProductDIsplay_ToggleableButton {
    private final @NotNull BuyCanvas menu;


    public Buy_1sButton(final @NotNull ProductDisplay display, final @NotNull BuyCanvas _menu) {
        super(display, "Buy one stack now", "Set amount to 1 stack", 1, "");
        menu = _menu;

        // Create design
        addDesign(display.getLevel(), ItemDesigns.CoinPile);
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        final ProductDisplay display = GetDisplay.get(this);
        final long amount = display.getItem().getMaxStackSize();


        // Play sound and buy items (left click)
        if(click == ClickAction.PRIMARY) {
            if(isActive()) Clickable.playSound(player);
            if(player.getUUID().equals(display.getOwnerUuid())) {
                display.retrieveItem(player, amount, true);
            }
            else {
                display.buyItem(player, amount);
            }
        }


        // Change amount (right click)
        else {
            menu.changeAmount(amount);
        }
    }
}
