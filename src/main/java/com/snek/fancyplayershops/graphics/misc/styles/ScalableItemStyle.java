package com.snek.fancyplayershops.graphics.misc.styles;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.graphics.basic.styles.ItemStyle;

import net.minecraft.world.item.ItemStack;


//TODO idk if overriding get methods is fine, this needs to be checked

public class ScalableItemStyle extends ItemStyle {
    public static final float Z_SCALE = 0.001f;

    private final float size;
    private final ItemStack item;
    private final Vector3f shift;


    /**
     * Creates a new ScalableItemStyle.
     */
    public ScalableItemStyle(final @NotNull ItemStack item, final float size) {
        this(item, size, 0);
    }

    /**
     * Creates a new ScalableItemStyle.
     */
    public ScalableItemStyle(final @NotNull ItemStack item, final float size, final float shiftZ) {
        this(item, size, shiftZ, new Vector2f());
    }

    /**
     * Creates a new ScalableItemStyle.
     */
    public ScalableItemStyle(final @NotNull ItemStack item, final float size, final float shiftZ, final @NotNull Vector2f shift) {
        super(false);
        this.size = size;
        this.item = item;
        this.shift = new Vector3f(shift.x, shift.y, shiftZ);
        resetAll();
    }




    @Override
    public @NotNull ItemStack getDefaultItem() {
        return item;
    }


    @Override
    public @NotNull Transform getDefaultTransform() {
        return super.getDefaultTransform().scale(size).scaleZ(Z_SCALE).move(shift);
    }
}

