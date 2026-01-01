package com.snek.fancyplayershops.graphics.hud.core.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.graphics.hud.core.styles.HudCanvasBack_S;
import com.snek.fancyplayershops.graphics.hud.core.styles.HudCanvasBackground_S;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.composite.elements.DualInputIndicator;
import com.snek.frameworklib.graphics.composite.elements.InputIndicator;
import com.snek.frameworklib.graphics.core.HudCanvas;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.interfaces.InputIndicatorCanvas;
import com.snek.frameworklib.graphics.layout.Div;

import net.minecraft.server.level.ServerLevel;




public class HudCanvasBase extends HudCanvas implements InputIndicatorCanvas {
    private final @NotNull DualInputIndicator inputIndicator;


    protected HudCanvasBase(final @NotNull HudContext hud, final float height, final float heightTop, final float heightBottom) {
        super(hud, height, heightTop, heightBottom);

        //TODO do something about the gap between the canvas and the right click when left click action is null
        //TODO same issue with the UIs, but the other way around
        // Add input indicators
        final Div e = bg.addChild(new DualInputIndicator(context.getLevel()));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(-FancyPlayerShops.SQUARE_BUTTON_SIZE);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;
    }


    @Override public PanelElm createNewBackElement(@NotNull ServerLevel level) { return new PanelElm(level, new HudCanvasBack_S()); }
    @Override public PanelElm createNewBgElement  (@NotNull ServerLevel level) { return new PanelElm(level, new HudCanvasBackground_S()); }
    @Override public @NotNull InputIndicator getLmbIndicator() { return inputIndicator.getLmbIndicator(); }
    @Override public @NotNull InputIndicator getRmbIndicator() { return inputIndicator.getRmbIndicator(); }
}
