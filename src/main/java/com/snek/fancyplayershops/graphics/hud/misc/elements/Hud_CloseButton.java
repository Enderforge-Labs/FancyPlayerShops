package com.snek.fancyplayershops.graphics.hud.misc.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.designs.SymbolDesigns;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;
import com.snek.fancyplayershops.graphics.hud.misc.styles.Hud_SquareButton_S;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Hud_CloseButton extends SimpleButtonElm {
    public Hud_CloseButton(final @NotNull HudContext _hud) {
        super(_hud.getLevel(), null, "Close", 1, new Hud_SquareButton_S());

        // Create design
        addDesign(SymbolDesigns.DiagonalCross);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        canvas.getContext().despawn(true);
    }
}
