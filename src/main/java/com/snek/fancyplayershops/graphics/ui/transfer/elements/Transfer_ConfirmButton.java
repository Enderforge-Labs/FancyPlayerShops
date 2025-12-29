package com.snek.fancyplayershops.graphics.ui.transfer.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ProductDIsplay_ToggleableButton;
import com.snek.fancyplayershops.graphics.ui.transfer.TransferCanvas;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.utils.MinecraftUtils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Transfer_ConfirmButton extends ProductDIsplay_ToggleableButton {
    private final @NotNull TransferCanvas menu;


    public Transfer_ConfirmButton(final @NotNull ProductDisplay display, final @NotNull TransferCanvas _menu) {
        super(display, null, "Confirm ownership transfer", 1, "Confirm");
        menu = _menu;
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);

        // Change owner
        final ProductDisplay display = GetDisplay.get(this);
        display.changeOwner(MinecraftUtils.getPlayerByUUID(menu.getNewOwnerUUID()));
        if(isActive()) Clickable.playSound(player);
    }
}
