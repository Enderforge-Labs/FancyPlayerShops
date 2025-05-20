package com.snek.fancyplayershops.hud.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.framework.ui.elements.styles.PanelElmStyle;








/**
 * The style of the ShopUiBorder UI element.
 */
public class CanvasBorder_S extends PanelElmStyle {
    public static final @NotNull Vector3i COLOR = new Vector3i(33, 33, 35);


    /**
     * Creates a new ShopUiBorderStyle_S.
     */
    public CanvasBorder_S() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(COLOR);
    }
}
