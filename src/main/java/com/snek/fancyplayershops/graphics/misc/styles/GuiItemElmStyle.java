package com.snek.fancyplayershops.graphics.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.graphics.core.styles.ElmStyle;
import com.snek.frameworklib.utils.Easings;

import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class GuiItemElmStyle extends ScalableItemElmStyle {

    /**
     * Creates a new GuiItemElmStyle.
     */
    public GuiItemElmStyle(final @NotNull ItemStack item, final float size) {
        super(item, size);
    }

    /**
     * Creates a new GuiItemElmStyle.
     */
    public GuiItemElmStyle(final @NotNull ItemStack item, final float size, final float shiftZ) {
        super(item, size, shiftZ);
    }

    /**
     * Creates a new GuiItemElmStyle.
     */
    public GuiItemElmStyle(final @NotNull ItemStack item, final float size, final float shiftZ, final @NotNull Vector2f shift) {
        super(item, size, shiftZ, shift);
    }


    @Override
    public @NotNull ItemDisplayContext getDefaultDisplayContext() {
        return ItemDisplayContext.GUI;
    }


    @Override
    public @NotNull Transform getDefaultTransform() {
        return super.getDefaultTransform().rotY((float)Math.PI);
    }



    @Override
    public @Nullable Animation getDefaultPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransform(new Transform().scale(PRIMER_ANIMATION_SCALE, PRIMER_ANIMATION_SCALE, 1))
        );
    }


    @Override
    public @Nullable Animation getDefaultSpawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.S_TIME, Easings.sineOut)
            .additiveTransform(new Transform().scale(1f / PRIMER_ANIMATION_SCALE, 1f / PRIMER_ANIMATION_SCALE, 1))
        );
    }
}
