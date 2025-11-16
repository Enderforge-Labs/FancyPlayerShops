package com.snek.fancyplayershops.graphics.hud.misc.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import com.snek.frameworklib.graphics.hud._elements.Hud;
import com.snek.fancyplayershops.configs.Configs;
import com.snek.framework.old.data_types.animations.Transform;
import com.snek.framework.old.ui.functional.elements.SimpleButtonElm;
import com.snek.framework.old.ui.functional.styles.SimpleButtonElmStyle;

import net.minecraft.server.level.ServerLevel;








public abstract class HudSimpleButtonElm extends SimpleButtonElm {
    final Hud hud;


    protected HudSimpleButtonElm(@NotNull Hud _hud, int _clickCooldown, final @NotNull SimpleButtonElmStyle _style) {
        super((ServerLevel)(_hud.getPlayer().level()), _clickCooldown, _style);
        hud = _hud;
    }


    protected HudSimpleButtonElm(@NotNull Hud _hud, int _clickCooldown) {
        this(_hud, _clickCooldown, new SimpleButtonElmStyle());
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
