package com.snek.fancyplayershops.graphics.hud.core.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.graphics.hud.core.styles.HudCanvasBack_S;
import com.snek.fancyplayershops.graphics.hud.core.styles.HudCanvasBackground_S;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.composite.elements.DualInputIndicator;
import com.snek.frameworklib.graphics.composite.elements.InputIndicator;
import com.snek.frameworklib.graphics.core.HudCanvas;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.interfaces.InputIndicatorCanvas;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;




public class HudCanvasBase extends HudCanvas implements InputIndicatorCanvas {
    private final @NotNull DualInputIndicator inputIndicator;


    /**
     * Creates a new HudCanvasBase.
     * @param hud The parent context.
     * @param defaultTitle The text to display in the title element.
     *     If null, no title element is created.
     *     This value can be later changed using {@link #updateTitle(Component)}
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     */
    protected HudCanvasBase(final @NotNull HudContext hud, final @Nullable Component defaultTitle, final float height, final float heightTop, final float heightBottom) {
        super(hud, defaultTitle, height, heightTop, heightBottom);

        //TODO do something about the gap between the canvas and the right click when left click action is null
        //TODO same issue with the UIs, but the other way around
        // Add input indicators
        final Div e = bg.addChild(new DualInputIndicator(context.getLevel()));
        e.setSize(DualInputIndicator.DEFAULT_DUAL_INDICATOR_SIZE);
        e.setPosY(-TOOLBAR_H);
        e.setAlignmentX(AlignmentX.CENTER);
        inputIndicator = (DualInputIndicator)e;
    }

    /**
     * Creates a new HudCanvasBase.
     * @param hud The parent context.
     * @param defaultTitle The text to display in the title element.
     *     If null, no title element is created.
     *     This value can be later changed using {@link #updateTitle(Component)}
     * @param height The total height of the canvas.
     * @param heightTop The height of the top border.
     * @param heightBottom The height of the bottom border.
     */
    protected HudCanvasBase(final @NotNull HudContext hud, final @Nullable String defaultTitle, final float height, final float heightTop, final float heightBottom) {
        this(hud, defaultTitle == null ? null : new Txt(defaultTitle).white().bold().get(), height, heightTop, heightBottom);
    }




    @Override public PanelElm createNewBackElement(@NotNull ServerLevel level) { return new PanelElm(level, new HudCanvasBack_S()); }
    @Override public PanelElm createNewBgElement  (@NotNull ServerLevel level) { return new PanelElm(level, new HudCanvasBackground_S()); }
    @Override public @NotNull InputIndicator getLmbIndicator() { return inputIndicator.getLmbIndicator(); }
    @Override public @NotNull InputIndicator getRmbIndicator() { return inputIndicator.getRmbIndicator(); }
}
