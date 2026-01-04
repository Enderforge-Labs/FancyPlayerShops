package com.snek.fancyplayershops.graphics.hud.mod_info.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.graphics.basic.styles.ItemElmStyle;








public class ModInfo_0_DisplayItem_S extends ItemElmStyle {
    public static final float SCALE   = 0.05f;
    public static final float Z_SCALE = 0.5f;
    public static final float Z_SHIFT = 0.01f;


    /**
     * Creates a new ModInfo_0_DisplayItem_S.
     */
    public ModInfo_0_DisplayItem_S() {
        super();
    }


    @Override
    public @NotNull Transform getDefaultTransform() {
        return super.getDefaultTransform().scale(SCALE).scaleZ(Z_SCALE).moveZ(Z_SHIFT);
    }
}

