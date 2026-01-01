package com.snek.fancyplayershops.graphics.hud._mainmenu_.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.hud.misc.styles.Hud_SquareButton_S;
import com.snek.fancyplayershops.graphics.hud.stash.StashCanvas;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.designs.ItemDesigns;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.graphics.layout.Div;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class _MainMenu_OpenStashButton extends SimpleButtonElm {
    public _MainMenu_OpenStashButton(final @NotNull HudContext context) {
        super(context.getLevel(), null, "Open stash", 1, new Hud_SquareButton_S());

        // Create design
        final Div e = addChild(new PolylineSetElm(level, ItemDesigns.Drawer));
        e.setSize(new Vector2f(FancyPlayerShops.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        Clickable.playSound(player);

        final @NotNull HudContext context = (HudContext)canvas.getContext();
        context.changeCanvas(new StashCanvas(context));
    }
}
