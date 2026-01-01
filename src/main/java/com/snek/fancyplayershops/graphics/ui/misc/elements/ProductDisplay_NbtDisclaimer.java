package com.snek.fancyplayershops.graphics.ui.misc.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.ui.nbt_disclaimer.NbtDisclaimerCanvas;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.details.DetailsCanvas;
import com.snek.fancyplayershops.graphics.ui.misc.styles.ProductDisplay_NbtDisclaimer_S;
import com.snek.frameworklib.graphics.functional.elements.FancyButtonElm;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class ProductDisplay_NbtDisclaimer extends FancyButtonElm {


    public ProductDisplay_NbtDisclaimer(final @NotNull ServerLevel level) {
        super(level, null, "View details", 1, new ProductDisplay_NbtDisclaimer_S());
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);

        // Change canvas
        final ProductDisplay display = GetDisplay.get(this);
        canvas.getContext().changeCanvas(new NbtDisclaimerCanvas(display));
    }


    //! Override checkIntersection to make the element not interactive if in DetailsCanvas
    @Override
    public @Nullable Vector2f checkIntersection(@NotNull Player player, boolean calculateIntersectionCoords) {
        if(canvas.getContext().getActiveCanvas() instanceof DetailsCanvas) return null;
        return super.checkIntersection(player, calculateIntersectionCoords);
    }
}
