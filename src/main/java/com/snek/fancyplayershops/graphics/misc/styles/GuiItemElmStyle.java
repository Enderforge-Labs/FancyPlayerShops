package com.snek.fancyplayershops.graphics.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.frameworklib.data_types.animations.Transform;

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
}
