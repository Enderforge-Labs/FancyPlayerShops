package com.snek.fancyplayershops.graphics.hud._mainmenu_.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.hud._mainmenu_.styles.MainMenu_LargeButton_S;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.designs.SymbolDesigns;
import com.snek.frameworklib.graphics.functional.elements.FancyButtonElm;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class MainMenu_StatsButton extends FancyButtonElm {
    public MainMenu_StatsButton(final @NotNull HudContext context) {
        super(context.getLevel(), null, "View statistics", 1, new MainMenu_LargeButton_S("View statistics"));

        // Create design
        final Div e = addDesign(SymbolDesigns.GraphUp);
        e.applyAnimation(new Transition().additiveTransform(new Transform().moveY(0.025f)), true, false);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        Clickable.playSound(player);
        player.displayClientMessage(new Txt("statistics").get(), false); //TODO
    }
}
