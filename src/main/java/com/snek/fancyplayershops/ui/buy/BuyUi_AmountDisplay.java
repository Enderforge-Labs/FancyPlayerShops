package com.snek.fancyplayershops.ui.buy;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.ui.elements.TextElm;
import com.snek.framework.ui.elements.styles.TextElmStyle;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.server.level.ServerLevel;








public class BuyUi_AmountDisplay extends TextElm {


    public BuyUi_AmountDisplay(final @NotNull ServerLevel _world) {
        super(_world);
        updateDisplay(1);
    }


    public void updateDisplay(final int amount) {
        getStyle(TextElmStyle.class).setText(new Txt()
            .cat(new Txt("Amount: ").lightGray())
            .cat(new Txt(Utils.formatAmount(amount)).white())
        .get());
    }
}
