package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ProductDIsplay_ToggleableButton;
import com.snek.fancyplayershops.graphics.ui.misc.styles.ProductDisplay_TogglableButton_S;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;






//FIXME change product display settings from click callback

public class Edit_NbtButton extends ProductDIsplay_ToggleableButton {

    public Edit_NbtButton(final @NotNull ProductDisplay display) {
        super(display, null, "Toggle NBT filter", 1, "NBT");
    }



    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        Clickable.playSound(player);
        updateColor(!isActive());
    }
}
