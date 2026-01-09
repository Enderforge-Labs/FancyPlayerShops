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








public class Buy_1iButton extends ProductDIsplay_ToggleableButton {
    private final @NotNull BuyCanvas menu;


    public Buy_1iButton(final @NotNull ProductDisplay display, final @NotNull BuyCanvas _menu) {
        super(display, "Buy a full inventory now", "Set amount to a full inventory", 1, "");
        menu = _menu;

        // Create design
        addDesign(display.getLevel(), ItemDesigns.MinecraftChest);
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        final ProductDisplay display = GetDisplay.get(this);
        final long amount = Math.min(display.getStock(), display.getItem().getMaxStackSize() * 9l * 4l);


        // Play sound and buy items (left click)
        if(click == ClickAction.PRIMARY) {
            final long oldStock = display.getStock();
            if(player.getUUID().equals(display.getOwnerUuid())) {
                display.retrieveItem(player, amount, false);
            }
            else {
                display.buyItem(player, amount);
            }
            if(display.getStock() != oldStock) Clickable.playSound(player);
        }


        // Change amount (right click)
        else {
            menu.changeAmount(amount);
        }
    }
}
