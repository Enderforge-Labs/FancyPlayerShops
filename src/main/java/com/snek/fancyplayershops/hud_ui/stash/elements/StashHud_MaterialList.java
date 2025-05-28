package com.snek.fancyplayershops.hud_ui.stash.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.hud_ui._elements.__HudElm;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.interfaces.Scrollable;

import net.minecraft.world.entity.player.Player;








public class StashHud_MaterialList extends Div implements Scrollable, __HudElm {

    public StashHud_MaterialList() {
        super();
    }


    @Override
    public void onScroll(final @NotNull Player player, final int amount) {
        System.out.println("SCROLLED " + amount);
    }
}
