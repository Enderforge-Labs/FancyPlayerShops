package com.snek.fancyplayershops.graphics.ui.change_shop.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.graphics.ui.change_shop.ChangeShopCanvas;
import com.snek.fancyplayershops.graphics.ui.edit.EditCanvas;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ProductDIsplay_ToggleableButton;
import com.snek.frameworklib.graphics.interfaces.Clickable;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class ChangeShop_ConfirmButton extends ProductDIsplay_ToggleableButton {
    private final @NotNull ChangeShopCanvas menu;


    public ChangeShop_ConfirmButton(final @NotNull ProductDisplay display, final @NotNull ChangeShopCanvas _menu) {
        super(display, null, "Confirm shop change", 1, "Confirm");
        menu = _menu;
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);

        // Change shop
        final ProductDisplay display = GetDisplay.get(this);
        display.changeShop(menu.getNewShopName(), (ServerPlayer)menu.getContext().getPlayer());
        if(isActive()) Clickable.playSound(player);

        // Change canvas
        display.changeCanvas(new EditCanvas(display));
    }
}
