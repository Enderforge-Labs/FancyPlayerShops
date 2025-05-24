package com.snek.fancyplayershops.hud_ui.stash.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.hud_ui._elements.Hud;
import com.snek.framework.ui.elements.TextElm;
import com.snek.framework.ui.elements.styles.TextElmStyle;
import com.snek.framework.utils.Txt;

import net.minecraft.server.level.ServerLevel;








public class StashHud_EmptyText extends TextElm {

    public StashHud_EmptyText(final @NotNull Hud _hud) {
        super((ServerLevel)(_hud.getPlayer().level()));
        updateDisplay();
    }


    public void updateDisplay() {
        getStyle(TextElmStyle.class).setText(new Txt("[Empty]").white().italic().get());
        flushStyle();
    }
}
