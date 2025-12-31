package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.ui.buy.styles.Buy_nbt_disclaimer_S;
import com.snek.fancyplayershops.graphics.ui.nbt_disclaimer.NbtDisclaimerCanvas;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.graphics.functional.elements.FancyButtonElm;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Buy_nbt_disclaimer extends FancyButtonElm {


    public Buy_nbt_disclaimer(final @NotNull ServerLevel level) {
        super(level, null, "View details", 1, new Buy_nbt_disclaimer_S());
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);

        // Change canvas
        final ProductDisplay display = GetDisplay.get(this);
        canvas.getContext().changeCanvas(new NbtDisclaimerCanvas(display));
    }
}
