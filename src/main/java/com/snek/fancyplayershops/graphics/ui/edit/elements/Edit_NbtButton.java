package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ProductDIsplay_ToggleableButton;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;






public class Edit_NbtButton extends ProductDIsplay_ToggleableButton {

    public Edit_NbtButton(final @NotNull ProductDisplay display) {
        super(display, null, "Toggle NBT filter", 1, "NBT");
    }


    @Override
    public void spawn(@NotNull Vector3d pos, boolean animate) {
        final ProductDisplay display = GetDisplay.get(this);
        updateColor(display.getNbtFilter());
        super.spawn(pos, animate);
    }



    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        final ProductDisplay display = GetDisplay.get(this);

        // Play sound and toggle the button
        Clickable.playSound(player);
        updateColor(!isActive());

        // Actually change the NBT filter setting
        display.changeNbtFilterSetting(isActive());

        // Send feedback message
        player.displayClientMessage(new Txt()
            .cat(new Txt("NBT Filter set to: ").lightGray())
            .cat(isActive() ? new Txt("On").green().bold() : new Txt("Off").red().bold())
        .get(), true);
    }
}


//TODO item view with nbts off should show a default item with custom description that says the shop has mixed nbts