package com.snek.fancyplayershops.graphics.hud.stash.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.graphics.basic.styles.ItemElmStyle;








public class Stash_ItemDisplay_S extends ItemElmStyle {
    public static final float SCALE   = 0.075f;
    public static final float Z_SCALE = 0.2f;
    public static final float Z_SHIFT = 0.01f;


    /**
     * Creates a new StashHud_ItemDisplay_S.
     */
    public Stash_ItemDisplay_S() {
        super();
    }


    @Override
    public @NotNull Transform getDefaultTransform() {
        return super.getDefaultTransform().scale(SCALE).scaleZ(Z_SCALE).moveZ(Z_SHIFT);
    }
}

