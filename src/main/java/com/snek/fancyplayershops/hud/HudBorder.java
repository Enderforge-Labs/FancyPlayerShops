package com.snek.fancyplayershops.hud;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.hud.styles.CanvasBorder_S;
import com.snek.framework.ui.elements.PanelElm;

import net.minecraft.server.level.ServerLevel;








/**
 * An element that can display a full-width, horizontally centered line of configurable color.
 */
public class HudBorder extends PanelElm {
    public static final float DEFAULT_HEIGHT = 0.02f;


    /**
     * Creates a new HudBorder of the specified height.
     * @param _shop The target shop.
     */
    public HudBorder(final @NotNull ServerLevel _world, final float h) {
        super(_world, new CanvasBorder_S());
    }


    /**
     * Creates a new ShopUiBorder of default height.
     * @param _shop The target shop.
     * @param h The height of the line.
     */
    public HudBorder(final @NotNull ServerLevel _world) {
        this(_world, DEFAULT_HEIGHT);
    }
}
