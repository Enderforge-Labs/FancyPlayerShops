package com.snek.fancyplayershops.ui._styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.framework.ui.basic.styles.PanelElmStyle;








/**
 * The style of the ShopUiBorder UI element.
 */
public class UiBorder_S extends PanelElmStyle {
    public static final @NotNull Vector3i COLOR = new Vector3i(33, 33, 35);


    /**
     * Creates a new ShopUiBorderStyle_S.
     */
    public UiBorder_S() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(COLOR);
    }
}
