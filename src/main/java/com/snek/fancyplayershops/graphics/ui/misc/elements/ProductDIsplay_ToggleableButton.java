package com.snek.fancyplayershops.graphics.ui.misc.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.graphics.ui.misc.styles.ProductDisplay_TogglableButton_S;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.functional.elements.FancyButtonElm;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.Txt;








public class ProductDIsplay_ToggleableButton extends FancyButtonElm {
    private boolean active = true;
    public boolean isActive() { return active; }


    public ProductDIsplay_ToggleableButton(
        final @NotNull ProductDisplay display,
        final @Nullable String lmbActionName,
        final @Nullable String rmbActionName,
        final int clickCooldown,
        final ProductDisplay_TogglableButton_S style
    ) {
        super(display.getLevel(), lmbActionName, rmbActionName, clickCooldown, style);
    }


    public ProductDIsplay_ToggleableButton(
        final @NotNull ProductDisplay display,
        final @Nullable String lmbActionName,
        final @Nullable String rmbActionName,
        final int clickCooldown,
        final @Nullable String textOverride
    ) {
        this(display, lmbActionName, rmbActionName, clickCooldown, new ProductDisplay_TogglableButton_S());
        if(textOverride != null) {
           getStyle(ProductDisplay_TogglableButton_S.class).setText(new Txt(textOverride).get());
        }
    }


    public void updateColor(final boolean _active) {
        if(active != _active) {
            active = _active;
            final ProductDisplay_TogglableButton_S s = getStyle(ProductDisplay_TogglableButton_S.class);
            s.setColor(active ? ProductDisplay_TogglableButton_S.BASE_COLOR : ProductDisplay_TogglableButton_S.BASE_COLOR_INACTIVE);
            applyAnimation(new Transition(4, Easings.expOut).targetBgColor(s.getColor()), false, true);
        }
    }
}

