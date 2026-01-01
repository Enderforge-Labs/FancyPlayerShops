package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ProductDIsplay_ToggleableButton;
import com.snek.frameworklib.graphics.interfaces.Clickable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;






public class Edit_RestockButton extends ProductDIsplay_ToggleableButton {

    public Edit_RestockButton(final @NotNull ProductDisplay display) {
        super(display, null, "Manually restock display", 1, "Restock");
    }



    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);

        // Play sound and attempt restock
        Clickable.playSound(player);
        final ProductDisplay display = GetDisplay.get(this);
        display.attemptRestock();
    }
}
