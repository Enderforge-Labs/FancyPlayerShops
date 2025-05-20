package com.snek.fancyplayershops.hud;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.hud.misc.styles.HudCanvasBack_S;
import com.snek.fancyplayershops.hud.misc.styles.HudCanvasBackground_S;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;








public class HudCanvas extends Canvas {
    protected final @NotNull Player player;




    public HudCanvas(final @NotNull Player _player, final @Nullable Canvas prevCanvas, final float height, final float heightTop, final float heightBottom) {
        super(prevCanvas, (ServerLevel)_player.level(), height, heightTop, heightBottom, new HudCanvasBackground_S(), new HudCanvasBack_S());
        player = _player;
    }
}
