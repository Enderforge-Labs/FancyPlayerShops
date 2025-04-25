package com.snek.fancyplayershops.implementations.ui;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.implementations.ui.misc.ShopPanelElm;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;








/**
 * The background panel of shop canvases.
 */
public class CanvasBackground extends ShopPanelElm {

    /**
     * Creates a new CanvasBackground.
     * @param _shop The target shop.
     */
    public CanvasBackground(@NotNull Shop _shop) {
        super(_shop);
    }
}