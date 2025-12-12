package com.snek.fancyplayershops.graphics.ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.frameworklib.utils.Txt;








/**
 * The style of the MouseButtonDown UI element.
 */
public class Any_InputIndicator_MouseButtonDown_S extends ShopPanelElm_S {

    /**
     * Creates a new MouseButtonDown_S.
     */
    public Any_InputIndicator_MouseButtonDown_S() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(Txt.COLOR_LIGHTGRAY);
    }
}
