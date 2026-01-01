package com.snek.fancyplayershops.graphics.hud._mainmenu_.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.hud._mainmenu_.styles._MainMenu_LargeButton_S;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.designs.SymbolDesigns;
import com.snek.frameworklib.graphics.functional.elements.FancyButtonElm;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class _MainMenu_PreferencesButton extends FancyButtonElm {
    public _MainMenu_PreferencesButton(final @NotNull HudContext context) {
        super(context.getLevel(), null, "Preferences", 1, new _MainMenu_LargeButton_S("Preferences"));

        // Create design
        final Div e = addChild(new PolylineSetElm(level, SymbolDesigns.Settings));
        e.applyAnimation(new Transition().additiveTransform(new Transform().moveY(0.025f)), true, false);
        e.setSize(new Vector2f(0.4f));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        Clickable.playSound(player);
        player.displayClientMessage(new Txt("Preferences").get(), false); //TODO
    }
}
