package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.ui.buy.styles.Buy_nbt_disclaimer_S;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.frameworklib.graphics.functional.elements.FancyButtonElm;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Buy_nbt_disclaimer extends FancyButtonElm {
    public static final float DEFAULT_HEIGHT = FancyPlayerShops.LINE_H * 0.75f;
    public static final float DEFAULT_DISTANCE = 0.02f;


    public Buy_nbt_disclaimer(final @NotNull ServerLevel level) {
        super(level, null, "View details", 1, new Buy_nbt_disclaimer_S());
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        player.displayClientMessage(Component.literal("test messge"), false);
        //TODO open Mixed nbt disclaimer
    }
}
