package com.snek.fancyplayershops.ui.buy.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.main.Shop;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.network.chat.Component;








public class BuyUi_BuyButton_S extends BuyUi_ConfirmButton_S {


    public BuyUi_BuyButton_S(final @NotNull Shop _shop){
        super(_shop);
    }


    @Override
    public @NotNull Component getDefaultText() {
        return new Txt("").get();
    }
}
