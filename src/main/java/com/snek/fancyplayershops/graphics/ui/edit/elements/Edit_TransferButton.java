package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.graphics.ui.edit.styles.Edit_SquareButton_S;
import com.snek.fancyplayershops.graphics.ui.transfer.TransferCanvas;
import com.snek.frameworklib.graphics.designs.ItemDesigns;
import com.snek.frameworklib.graphics.functional.elements.ButtonElm;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Edit_TransferButton extends ButtonElm {
    public Edit_TransferButton(final @NotNull ProductDisplay display) {
        super(display.getLevel(), null, "Transfer ownership", 1,  new Edit_SquareButton_S(display));

        // Create design
        addDesign(display.getLevel(), ItemDesigns.UserIcon);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);

        // Change canvas
        final ProductDisplay display = GetDisplay.get(this);
        display.changeCanvas(new TransferCanvas(display));
    }
}
