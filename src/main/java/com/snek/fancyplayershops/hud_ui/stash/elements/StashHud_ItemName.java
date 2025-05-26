package com.snek.fancyplayershops.hud_ui.stash.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.hud_ui._elements.Hud;
import com.snek.fancyplayershops.hud_ui.stash.styles.StashHud_ItemName_S;
import com.snek.framework.ui.elements.TextElm;
import com.snek.framework.ui.elements.styles.TextElmStyle;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;








public class StashHud_ItemName extends TextElm {
    private final @NotNull ItemStack item;


    public StashHud_ItemName(final @NotNull Hud _hud, final @NotNull ItemStack _item) {
        super((ServerLevel)(_hud.getPlayer().level()), new StashHud_ItemName_S());
        item = _item;
        updateDisplay();
    }


    public void updateDisplay() {
        getStyle(TextElmStyle.class).setText(new Txt(MinecraftUtils.getFancyItemName(item).getString()).white().get());
        flushStyle();
    }
}
