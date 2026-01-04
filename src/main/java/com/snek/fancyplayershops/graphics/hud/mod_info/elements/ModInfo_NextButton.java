package com.snek.fancyplayershops.graphics.hud.mod_info.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.hud.misc.styles.Hud_SquareButton_S;
import com.snek.fancyplayershops.graphics.hud.mod_info.ModInfoCanvas;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.designs.SymbolDesigns;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;
import com.snek.frameworklib.graphics.interfaces.Clickable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;




public class ModInfo_NextButton extends SimpleButtonElm {
    public ModInfo_NextButton(final @NotNull HudContext context) {
        super(context.getLevel(), null, "Next", 1, new Hud_SquareButton_S());
        addDesign(context.getLevel(), SymbolDesigns.ArrowPointingRight);
    }


    @Override
    public void onClick(@NotNull Player player, @NotNull ClickAction click, @NotNull Vector2f coords) {
        super.onClick(player, click, coords);

        // Go forward 1 page and play button sound (if possible)
        final ModInfoCanvas _canvas = (ModInfoCanvas)canvas;
        final int newPageIndex = _canvas.getActivePageIndex() + 1;
        if(newPageIndex < _canvas.getPageNumber()) {
            _canvas.changePage(newPageIndex);
            Clickable.playSound(player);
        }
    }
}
