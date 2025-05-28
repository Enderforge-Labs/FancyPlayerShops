package com.snek.fancyplayershops.hud_ui.stash.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.hud_ui._elements.Hud;
import com.snek.fancyplayershops.hud_ui._elements.__HudElm;
import com.snek.framework.ui.basic.elements.SimpleTextElm;
import com.snek.framework.ui.basic.styles.SimpleTextElmStyle;
import com.snek.framework.utils.Txt;

import net.minecraft.server.level.ServerLevel;








public class StashHud_EmptyText extends SimpleTextElm implements __HudElm {

    public StashHud_EmptyText(final @NotNull Hud _hud) {
        super((ServerLevel)(_hud.getPlayer().level()));
        updateDisplay();
    }


    public void updateDisplay() {
        getStyle(SimpleTextElmStyle.class).setText(new Txt("[Empty]").white().italic().get());
        flushStyle();
    }
}
