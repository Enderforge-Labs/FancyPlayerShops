package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ProductDIsplay_ToggleableButton;
import com.snek.frameworklib.graphics.interfaces.Clickable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;






public class Edit_NbtButton extends ProductDIsplay_ToggleableButton {

    public Edit_NbtButton(final @NotNull ProductDisplay display) {
        super(display, null, "Toggle NBT filter", 1, "NBT");
    }



    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        final ProductDisplay display = GetDisplay.get(this);

        Clickable.playSound(player);
        updateColor(!isActive());
        display.changeNbtFilterSetting(isActive());
    }
}


//TODO item view with nbts off should show a default item with custom description that says the shop has mixed nbts

//TODO save a list of items instead of one single item in the display