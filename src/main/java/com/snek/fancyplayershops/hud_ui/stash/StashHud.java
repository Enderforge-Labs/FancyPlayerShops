package com.snek.fancyplayershops.hud_ui.stash;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.hud_ui._elements.Hud;
import com.snek.fancyplayershops.hud_ui._elements.HudCanvas;
import com.snek.fancyplayershops.shop_ui.misc.elements.ShopFancyTextElm;
import com.snek.fancyplayershops.ui._elements.UiBorder;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;

import net.minecraft.server.level.ServerLevel;








public class StashHud extends HudCanvas {

    public StashHud(final @NotNull Hud _hud){
        super(_hud, 1f, ShopFancyTextElm.LINE_H, UiBorder.DEFAULT_HEIGHT);
        Div e;


        // Add title
        e = bg.addChild(new StashHud_Title((ServerLevel)(hud.getPlayer().level())));
        e.setSize(new Vector2f(1f, ShopFancyTextElm.LINE_H));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);
    }
}
