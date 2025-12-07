package com.snek.fancyplayershops.graphics.hud.stash.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.fancyplayershops.graphics.hud.stash.styles.StashHud_ItemNameCount_S;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;







//FIXME make not clickable. this was only used for testing
public class StashHud_ItemNameCount extends SimpleTextElm implements Clickable {
    private final @NotNull ItemStack item;
    private int count;


    public StashHud_ItemNameCount(final @NotNull HudContext _hud, final @NotNull ItemStack _item, final int _count) {
        super((ServerLevel)(_hud.getPlayer().level()), new StashHud_ItemNameCount_S());
        item = _item;
        count = _count;
        updateDisplay();
    }


    public void updateDisplay() {
        getStyle(SimpleTextElmStyle.class).setText(new Txt()
            .cat(new Txt(MinecraftUtils.getFancyItemName(item).getString()).white())
            .cat(new Txt("\n" + Utils.formatAmount(count)).lightGray())
        .get());
        flushStyle(false);
    }


    @Override
    public boolean attemptClick(@NotNull Player player, @NotNull ClickAction click) {
        return true;
    }


    @Override
    public void onClick(@NotNull Player player, @NotNull ClickAction click) {
    }
}
