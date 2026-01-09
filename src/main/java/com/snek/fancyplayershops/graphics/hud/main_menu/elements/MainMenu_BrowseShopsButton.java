package com.snek.fancyplayershops.graphics.hud.main_menu.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.hud.main_menu.MainMenuCanvas;
import com.snek.fancyplayershops.graphics.hud.main_menu.styles.MainMenu_LargeButton_S;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.designs.ItemDesigns;
import com.snek.frameworklib.graphics.functional.elements.TextButtonElm;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class MainMenu_BrowseShopsButton extends TextButtonElm {
    public MainMenu_BrowseShopsButton(final @NotNull HudContext context) {
        super(context.getLevel(), null, "Open shop browser", 1, new MainMenu_LargeButton_S("Browse shops"));

        // Create design
        final Div e = addDesign(context.getLevel(), ItemDesigns.MagnifyingGlass, MainMenuCanvas.MAIN_BUTTON_DEISNG_SIZE);
        e.applyAnimation(new Transition().additiveTransform(new Transform().moveY(0.025f)), true, false);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        Clickable.playSound(player);
        player.displayClientMessage(new Txt("Shop browser ").get(), false); //TODO
    }
}
