package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.graphics.ui.edit.styles.Edit_SquareButton_S;
import com.snek.frameworklib.graphics.designs.ItemDesigns;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Edit_OpenBuyMenuButton extends SimpleButtonElm {
    public Edit_OpenBuyMenuButton(final @NotNull ProductDisplay display) {
        super(display.getLevel(), null, "Open buy menu", 1, new Edit_SquareButton_S(display));

        // Create design
        addDesign(ItemDesigns.PriceTag);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);

        // Open UI
        final ProductDisplay display = GetDisplay.get(this);
        display.openBuyUi(player, false);
    }
}
