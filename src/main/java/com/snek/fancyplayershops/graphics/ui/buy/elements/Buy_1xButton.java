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








public class Buy_1xButton extends ProductDIsplay_ToggleableButton {
    private final @NotNull BuyCanvas menu;


    public Buy_1xButton(final @NotNull ProductDisplay display, final @NotNull BuyCanvas _menu) {
        super(display, "Buy 1 item now", "Set amount to 1", 1, "");
        menu = _menu;

        // Create design
        addDesign(ItemDesigns.Coin);
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);


        // Play sound and buy items (left click)
        if(click == ClickAction.PRIMARY) {
            final ProductDisplay display = GetDisplay.get(this);
            if(isActive()) Clickable.playSound(player);
            if(player.getUUID().equals(display.getOwnerUuid())) {
                display.retrieveItem(player, 1l, true);
            }
            else {
                display.buyItem(player, 1l);
            }
        }


        // Change amount (right click)
        else {
            menu.changeAmount(1l);
        }
    }
}
