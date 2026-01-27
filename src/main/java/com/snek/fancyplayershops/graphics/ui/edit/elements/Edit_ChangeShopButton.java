package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.graphics.ui.change_shop.ChangeShopCanvas;
import com.snek.fancyplayershops.graphics.ui.edit.styles.Edit_SquareButton_S;
import com.snek.frameworklib.graphics.designs.SymbolDesigns;
import com.snek.frameworklib.graphics.functional.elements.ButtonElm;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Edit_ChangeShopButton extends ButtonElm {
    public Edit_ChangeShopButton(final @NotNull ProductDisplay display) {
        super(display.getLevel(), null, "Change shop", 1,  new Edit_SquareButton_S(display));

        // Create design
        addDesign(display.getLevel(), SymbolDesigns.ArrowsPointingLeftRight);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);

        // Change canvas
        final ProductDisplay display = GetDisplay.get(this);
        display.changeCanvas(new ChangeShopCanvas(display));
    }
}
