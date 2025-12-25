package com.snek.fancyplayershops.graphics.ui.details.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.graphics.ui.details.DetailsCanvas;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.graphics.basic.styles.ItemElmStyle;








/**
 * The style for the DetailsUi_OwnerHead UI element.
 */
public class Details_OwnerHead_S extends ItemElmStyle {
    public static final float Z_SCALE = 0.001f;


    /**
     * Creates a new DetailsUi_OwnerHead_S.
     */
    public Details_OwnerHead_S() {
        super();
    }


    @Override
    public @NotNull Transform getDefaultTransform() {
        return super.getDefaultTransform().scale(DetailsCanvas.HEAD_SIZE).scaleZ(Z_SCALE);
    }
}
