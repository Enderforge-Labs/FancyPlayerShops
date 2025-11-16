package com.snek.fancyplayershops.graphics.hud.stash.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.graphics.hud._elements.__HudElm;
import com.snek.framework.old.ui.basic.elements.SimpleTextElm;
import com.snek.framework.old.ui.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.server.level.ServerLevel;








public class StashHud_Title extends SimpleTextElm implements __HudElm {


    public StashHud_Title(final @NotNull ServerLevel _world) {
        super(_world);
        updateDisplay();
    }


    public void updateDisplay() {
        getStyle(SimpleTextElmStyle.class).setText(new Txt("Your stash").white().bold().get());
        flushStyle();
    }
}
