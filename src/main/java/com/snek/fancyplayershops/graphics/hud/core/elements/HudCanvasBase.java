package com.snek.fancyplayershops.graphics.hud.core.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.graphics.hud.core.styles.HudCanvasBack_S;
import com.snek.fancyplayershops.graphics.hud.core.styles.HudCanvasBackground_S;
import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.core.HudCanvas;
import com.snek.frameworklib.graphics.core.HudContext;

import net.minecraft.server.level.ServerLevel;




public class HudCanvasBase extends HudCanvas {

    protected HudCanvasBase(final @NotNull HudContext hud, final float height, final float heightTop, final float heightBottom) {
        super(hud, height, heightTop, heightBottom);
    }

    @Override public PanelElm createNewBackElement(@NotNull ServerLevel level) { return new PanelElm(level, new HudCanvasBack_S()); }
    @Override public PanelElm createNewBgElement  (@NotNull ServerLevel level) { return new PanelElm(level, new HudCanvasBackground_S()); }
}
