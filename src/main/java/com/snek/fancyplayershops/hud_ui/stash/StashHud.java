package com.snek.fancyplayershops.hud_ui.stash;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.hud_ui._elements.HudCanvas;
import com.snek.fancyplayershops.shop_ui.misc.elements.ShopFancyTextElm;
import com.snek.fancyplayershops.ui._elements.UiBorder;
import com.snek.fancyplayershops.ui._elements.UiCanvas;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;








public class StashHud extends HudCanvas {

    public StashHud(final @NotNull Player _player, final @Nullable UiCanvas prevCanvas){
        super(_player, prevCanvas, 1f, ShopFancyTextElm.LINE_H, UiBorder.DEFAULT_HEIGHT);
        Div e;


        // Add title
        e = bg.addChild(new StashHud_Title((ServerLevel)_player.level()));
        e.setSize(new Vector2f(1f, ShopFancyTextElm.LINE_H));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);
    }
}
