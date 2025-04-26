package com.snek.fancyplayershops.implementations.ui.details;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.ui.styles.ItemElmStyle;








/**
 * The style for the DetailsUi_OwnerHead UI element.
 */
public class DetailsUi_OwnerHead_S extends ItemElmStyle {

    /**
     * Creates a new DetailsUi_OwnerHead.
     */
    public DetailsUi_OwnerHead_S() {
        super();
    }


    @Override
    public @NotNull Transform getDefaultTransform() {
        return super.getDefaultTransform().scale(DetailsUi.HEAD_SIZE).scaleZ(0.001f).rotY((float)Math.PI);
    }
}
