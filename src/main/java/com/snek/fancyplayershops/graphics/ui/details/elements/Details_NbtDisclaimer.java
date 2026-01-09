package com.snek.fancyplayershops.graphics.ui.details.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.graphics.ui.buy.styles.Buy_NbtDisclaimer_S;
import com.snek.frameworklib.graphics.basic.elements.PanelTextElm;

import net.minecraft.server.level.ServerLevel;








public class Details_NbtDisclaimer extends PanelTextElm {


    public Details_NbtDisclaimer(final @NotNull ServerLevel level) {
        super(level, new Buy_NbtDisclaimer_S());
        //! ^ Use the button's style directly, as it is compatible. FancyButtonStyle is a subclass of PanelTextStyle
    }
}
