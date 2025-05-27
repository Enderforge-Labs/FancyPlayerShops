package com.snek.fancyplayershops.hud_ui.stash.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.ui.basic.elements.SimpleTextElm;
import com.snek.framework.ui.basic.styles.SimpleTextElmStyle;
import com.snek.framework.utils.Txt;

import net.minecraft.server.level.ServerLevel;








public class StashHud_Title extends SimpleTextElm {


    public StashHud_Title(final @NotNull ServerLevel _world) {
        super(_world);
        updateDisplay();
    }


    public void updateDisplay() {
        getStyle(SimpleTextElmStyle.class).setText(new Txt("Your stash").white().bold().get());
        flushStyle();
    }
}
