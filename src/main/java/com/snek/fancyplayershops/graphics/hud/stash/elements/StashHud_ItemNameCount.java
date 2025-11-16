package com.snek.fancyplayershops.graphics.hud.stash.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.graphics.hud._elements.old.Hud;
import com.snek.fancyplayershops.graphics.hud._elements.old.__HudElm;
import com.snek.fancyplayershops.graphics.hud.stash.styles.StashHud_ItemNameCount_S;
import com.snek.framework.old.ui.basic.elements.SimpleTextElm;
import com.snek.framework.old.ui.basic.styles.SimpleTextElmStyle;
import com.snek.framework.old.utils.MinecraftUtils;
import com.snek.framework.old.utils.Txt;
import com.snek.framework.old.utils.Utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;








public class StashHud_ItemNameCount extends SimpleTextElm implements __HudElm {
    private final @NotNull ItemStack item;
    private int count;


    public StashHud_ItemNameCount(final @NotNull Hud _hud, final @NotNull ItemStack _item, final int _count) {
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
        flushStyle();
    }
}
