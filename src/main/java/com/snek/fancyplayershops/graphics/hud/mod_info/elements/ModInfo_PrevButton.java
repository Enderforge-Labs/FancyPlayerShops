package com.snek.fancyplayershops.graphics.hud.mod_info.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.hud.misc.styles.Hud_SquareButton_S;
import com.snek.fancyplayershops.graphics.hud.mod_info.ModInfoCanvas;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.designs.SymbolDesigns;
import com.snek.frameworklib.graphics.functional.elements.ButtonElm;
import com.snek.frameworklib.graphics.interfaces.Clickable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;




public class ModInfo_PrevButton extends ButtonElm {
    public ModInfo_PrevButton(final @NotNull HudContext context) {
        super(context.getLevel(), null, "Previous", 1, new Hud_SquareButton_S());
        addDesign(context.getLevel(), SymbolDesigns.ArrowPointingLeft);
    }


    @Override
    public void onClick(@NotNull Player player, @NotNull ClickAction click, @NotNull Vector2f coords) {
        super.onClick(player, click, coords);

        // Go back 1 page and play button sound (if possible)
        final ModInfoCanvas _canvas = (ModInfoCanvas)canvas;
        final int newPageIndex = _canvas.getActivePageIndex() - 1;
        if(newPageIndex >= 0) {
            _canvas.changePage(newPageIndex);
            Clickable.playSound(player);
        }
    }
}
