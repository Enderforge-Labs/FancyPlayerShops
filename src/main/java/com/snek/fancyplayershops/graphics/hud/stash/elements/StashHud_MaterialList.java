package com.snek.fancyplayershops.graphics.hud.stash.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.graphics.hud._elements.__HudElm;
import com.snek.frameworklib.graphics.Div;
import com.snek.frameworklib.graphics.interfaces.Scrollable;

import net.minecraft.world.entity.player.Player;








public class StashHud_MaterialList extends Div implements Scrollable, __HudElm {

    public StashHud_MaterialList() {
        super();
    }


    @Override
    public void onScroll(final @NotNull Player player, final int amount) {
        // Scroll handling - implementation pending
    }
}
