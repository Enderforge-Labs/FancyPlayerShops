package com.snek.fancyplayershops.graphics.hud.main_menu.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.hud.misc.styles.Hud_SquareButton_S;
import com.snek.fancyplayershops.graphics.hud.stash.StashCanvas;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.designs.ItemDesigns;
import com.snek.frameworklib.graphics.functional.elements.ButtonElm;
import com.snek.frameworklib.graphics.interfaces.Clickable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class MainMenu_OpenStashButton extends ButtonElm {
    public MainMenu_OpenStashButton(final @NotNull HudContext context) {
        super(context.getLevel(), null, "Open stash", 1, new Hud_SquareButton_S());

        // Create design
        addDesign(context.getLevel(), ItemDesigns.Drawer);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        Clickable.playSound(player);

        final @NotNull HudContext context = (HudContext)canvas.getContext();
        context.changeCanvas(new StashCanvas(context));
    }
}
