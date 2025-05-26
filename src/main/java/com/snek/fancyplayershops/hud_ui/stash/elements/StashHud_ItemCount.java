package com.snek.fancyplayershops.hud_ui.stash.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.hud_ui._elements.Hud;
import com.snek.fancyplayershops.hud_ui.stash.styles.StashHud_ItemCount_S;
import com.snek.framework.ui.elements.TextElm;
import com.snek.framework.ui.elements.styles.TextElmStyle;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.server.level.ServerLevel;








public class StashHud_ItemCount extends TextElm {
    private final int count;


    public StashHud_ItemCount(final @NotNull Hud _hud, final int _count) {
        super((ServerLevel)(_hud.getPlayer().level()), new StashHud_ItemCount_S());
        count = _count;
        updateDisplay();
    }


    public void updateDisplay() {
        getStyle(TextElmStyle.class).setText(new Txt(Utils.formatAmount(count)).lightGray().get());
        flushStyle();
    }
}

