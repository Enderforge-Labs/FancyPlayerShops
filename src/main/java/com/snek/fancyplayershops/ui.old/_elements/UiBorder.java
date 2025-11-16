package com.snek.fancyplayershops.ui.old._elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.ui.old._styles.UiBorder_S;
import com.snek.framework.old.ui.basic.elements.PanelElm;

import net.minecraft.server.level.ServerLevel;








/**
 * An element that can display a full-width, horizontally centered line of configurable color.
 */
public class UiBorder extends PanelElm {
    public static final float DEFAULT_HEIGHT = 0.02f;


    /**
     * Creates a new HudBorder using a custom style.
     * @param _shop The target shop.
     * @param _style The style.
     */
    public UiBorder(final @NotNull ServerLevel _world, final @NotNull UiBorder_S _style) {
        super(_world, _style);
    }


    /**
     * Creates a new HudBorder using the default style.
     * @param _shop The target shop.
     */
    public UiBorder(final @NotNull ServerLevel _world) {
        this(_world, new UiBorder_S());
    }
}
