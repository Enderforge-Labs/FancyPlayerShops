package com.snek.fancyplayershops.hud_ui.stash.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.ui.elements.TextElm;
import com.snek.framework.ui.elements.styles.TextElmStyle;
import com.snek.framework.utils.Txt;

import net.minecraft.server.level.ServerLevel;








public class StashHud_Title extends TextElm {


    public StashHud_Title(final @NotNull ServerLevel _world) {
        super(_world);
        updateDisplay();
    }


    public void updateDisplay() {
        getStyle(TextElmStyle.class).setText(new Txt("Your stash").white().bold().get());
        flushStyle();
    }
}
