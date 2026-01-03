package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.ui.nbt_disclaimer.NbtDisclaimerCanvas;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.misc.elements.Misc_BackButton;
import com.snek.fancyplayershops.graphics.ui.buy.styles.Buy_NbtDisclaimer_S;
import com.snek.frameworklib.graphics.core.Context;
import com.snek.frameworklib.graphics.functional.elements.FancyButtonElm;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Buy_NbtDisclaimer extends FancyButtonElm {
    final @NotNull Misc_BackButton backButton;


    public Buy_NbtDisclaimer(final @NotNull ServerLevel level, final @NotNull Misc_BackButton backButton) {
        super(level, null, "View details", 1, new Buy_NbtDisclaimer_S());
        this.backButton = backButton;
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);

        // Change canvas
        final ProductDisplay display = GetDisplay.get(this);
        final Context context = canvas.getContext();
        context.changeCanvas(new NbtDisclaimerCanvas(display, backButton));
    }
}
