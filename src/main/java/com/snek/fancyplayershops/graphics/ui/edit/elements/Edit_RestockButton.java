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






//FIXME restock from click callback

public class Edit_RestockButton extends ProductDIsplay_ToggleableButton {

    public Edit_RestockButton(final @NotNull ProductDisplay display) {
        super(display, null, "Manually restock display", 1, "Restock");
    }



    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        Clickable.playSound(player);
        //TODO restock
    }
}
