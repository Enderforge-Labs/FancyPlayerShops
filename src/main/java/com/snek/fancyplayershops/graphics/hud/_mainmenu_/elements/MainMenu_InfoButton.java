package com.snek.fancyplayershops.graphics.hud._mainmenu_.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.hud.misc.styles.Hud_SquareButton_S;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.designs.SymbolDesigns;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;
import com.snek.frameworklib.graphics.interfaces.Clickable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class MainMenu_InfoButton extends SimpleButtonElm {
    public MainMenu_InfoButton(final @NotNull HudContext context) {
        super(context.getLevel(), null, "Info", 1, new Hud_SquareButton_S());

        // Create design
        addDesign(context.getLevel(), SymbolDesigns.Info);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        Clickable.playSound(player);
        //TODO
    }
}
