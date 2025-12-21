package com.snek.fancyplayershops.graphics.hud.misc.styles;


import org.joml.Vector3i;

import com.snek.frameworklib.graphics.functional.styles.SimpleButtonElmStyle;








/**
 * The style of a generic HUD button.
 */
public class Hud_SimpleButton_S extends SimpleButtonElmStyle {

    /**
     * Creates a new HudSimpleButton_S.
     */
    public Hud_SimpleButton_S() {
        super();
    }


    @Override
    public Vector3i getDefaultColor() {
        return new Vector3i(128, 128, 128);
    }
}