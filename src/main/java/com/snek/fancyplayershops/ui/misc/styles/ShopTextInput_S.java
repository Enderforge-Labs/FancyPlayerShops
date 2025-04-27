package com.snek.fancyplayershops.ui.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;

import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity.TextAlignment;








public class ShopTextInput_S extends ShopButton_S {

    public ShopTextInput_S(){
        super();
    }


    @Override
    public @Nullable Animation getDefaultHoverPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransformBg(new Transform().scaleX(UNHOVERED_W))
            .additiveTransformFg(new Transform().moveX(UNHOVERED_W * 2))
        );
    }


    @Override
    public @NotNull TextAlignment getDefaultTextAlignment(){
        return TextAlignment.LEFT;
    }
}
