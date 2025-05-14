package com.snek.framework.ui.composite.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.framework.ui.elements.styles.PanelElmStyle;
import com.snek.framework.utils.Txt;








public class LinePanelStyle extends PanelElmStyle {
    public LinePanelStyle() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultColor(){
        return new Vector3i(Txt.COLOR_WHITE);
    }
}
