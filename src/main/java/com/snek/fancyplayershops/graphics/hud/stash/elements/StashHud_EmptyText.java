package com.snek.fancyplayershops.graphics.hud.stash.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.server.level.ServerLevel;








public class StashHud_EmptyText extends SimpleTextElm {

    public StashHud_EmptyText(final @NotNull HudContext _hud) {
        super((ServerLevel)(_hud.getPlayer().level()));
        updateDisplay();
    }


    public void updateDisplay() {
        getStyle(SimpleTextElmStyle.class).setText(new Txt("[Empty]").white().italic().get());
        flushStyle(false);
    }
}
