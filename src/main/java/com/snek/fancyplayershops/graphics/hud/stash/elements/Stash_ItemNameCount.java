package com.snek.fancyplayershops.graphics.hud.stash.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.fancyplayershops.graphics.hud.stash.styles.Stash_ItemEntry_Name_S;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.world.item.ItemStack;







public class Stash_ItemNameCount extends SimpleTextElm {
    private final @NotNull ItemStack item;
    private int count;


    public Stash_ItemNameCount(final @NotNull HudContext _hud, final @NotNull ItemStack _item, final int _count) {
        super(_hud.getLevel(), new Stash_ItemEntry_Name_S());
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
