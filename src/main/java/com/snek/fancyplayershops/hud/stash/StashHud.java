package com.snek.fancyplayershops.hud.stash;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.hud.Canvas;
import com.snek.fancyplayershops.hud.HudBorder;
import com.snek.fancyplayershops.hud.HudCanvas;
import com.snek.fancyplayershops.ui.misc.ShopFancyTextElm;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;








public class StashHud extends HudCanvas {

    public StashHud(final @NotNull Player _player, final @Nullable Canvas prevCanvas){
        super(_player, prevCanvas, 1f, ShopFancyTextElm.LINE_H, HudBorder.DEFAULT_HEIGHT);
        Div e;


        // Add title
        e = bg.addChild(new StashHud_Title((ServerLevel)_player.level()));
        e.setSize(new Vector2f(1f, ShopFancyTextElm.LINE_H));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);
    }
}
