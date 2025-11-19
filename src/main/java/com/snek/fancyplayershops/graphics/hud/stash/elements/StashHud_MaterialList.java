package com.snek.fancyplayershops.graphics.hud.stash.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.graphics.core.Div;
import com.snek.frameworklib.graphics.interfaces.Scrollable;

import net.minecraft.world.entity.player.Player;








public class StashHud_MaterialList extends Div implements Scrollable {

    public StashHud_MaterialList() {
        super();
    }


    @Override
    public void onScroll(final @NotNull Player player, final int amount) {
        System.out.println("SCROLLED " + amount);
    }
}
