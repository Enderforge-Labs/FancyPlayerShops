package com.snek.framework.old.ui.composite.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import com.snek.framework.old.ui.basic.styles.PanelElmStyle;
import com.snek.framework.old.utils.Txt;








public class LinePanelStyle extends PanelElmStyle {
    public LinePanelStyle() {
        super();
    }


    @Override
    public @NotNull Vector3i getDefaultColor() {
        return new Vector3i(Txt.COLOR_WHITE);
    }
}
