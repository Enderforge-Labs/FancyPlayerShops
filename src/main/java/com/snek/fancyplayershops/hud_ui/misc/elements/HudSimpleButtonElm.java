package com.snek.fancyplayershops.hud_ui.misc.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import com.snek.fancyplayershops.hud_ui._elements.Hud;
import com.snek.fancyplayershops.main.Configs;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.ui.functional.elements.SimpleButtonElm;

import net.minecraft.server.level.ServerLevel;








public abstract class HudSimpleButtonElm extends SimpleButtonElm {
    final Hud hud;


    protected HudSimpleButtonElm(@NotNull Hud _hud, int _clickCooldown) {
        super((ServerLevel)(_hud.getPlayer().level()), _clickCooldown);
        hud = _hud;
    }


    @Override
    public @NotNull Vector3f __calcEntityVisualOrigin(final @NotNull Transform _transform){
        return
            new Vector3f(getAbsPos().x, getAbsPos().y, getZIndex() * Configs.ui.z_layer_spacing.getValue())
            .add(hud.getActiveCanvas().__calcVisualShift())
            .add(getEntity().getPosCopy())
        ;
    }
}
