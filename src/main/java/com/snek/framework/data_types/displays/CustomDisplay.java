package com.snek.framework.data_types.displays;

import java.lang.reflect.Method;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.mojang.math.Transformation;
import com.snek.framework.utils.Utils;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Brightness;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Display.BillboardConstraints;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.level.Level;







/**
 * An abstract wrapper for Minecraft's Display Entities.
 * <p> This class allows for better customization and more readable code.
 */
public abstract class CustomDisplay {
    protected @NotNull Display heldEntity;




    // Private methods
    private static @NotNull Method method_setTransformation;
    private static @NotNull Method method_setInterpolationDuration;
    private static @NotNull Method method_setStartInterpolation;
    private static @NotNull Method method_setBillboardMode;
    private static @NotNull Method method_getBillboardMode;
    private static @NotNull Method method_setViewRange;
    private static @NotNull Method method_getViewRange;
    private static @NotNull Method method_setBrightness;
    private static @NotNull Method method_getBrightness;
    private static @NotNull Method method_getMaxRenderWidth;
    private static @NotNull Method method_getMaxRenderHeight;
    private static @NotNull Method method_setMaxRenderWidth;
    private static @NotNull Method method_setMaxRenderHeight;
    static {
        try {
            method_setTransformation        = Display.class.getDeclaredMethod("setTransformation",             Transformation.class);
            method_setInterpolationDuration = Display.class.getDeclaredMethod("setInterpolationDuration",                 int.class);
            method_setStartInterpolation    = Display.class.getDeclaredMethod("setInterpolationDelay",                    int.class);
            method_setBillboardMode         = Display.class.getDeclaredMethod("setBillboardConstraints", BillboardConstraints.class);
            method_getBillboardMode         = Display.class.getDeclaredMethod("getBillboardConstraints");
            method_setViewRange             = Display.class.getDeclaredMethod("setViewRange",                           float.class);
            method_getViewRange             = Display.class.getDeclaredMethod("getViewRange");
            method_setBrightness            = Display.class.getDeclaredMethod("setBrightnessOverride",             Brightness.class);
            method_getBrightness            = Display.class.getDeclaredMethod("getPackedBrightnessOverride");
            method_setMaxRenderWidth        = Display.class.getDeclaredMethod("setWidth",                               float.class);
            method_getMaxRenderWidth        = Display.class.getDeclaredMethod("getWidth");
            method_setMaxRenderHeight       = Display.class.getDeclaredMethod("setHeight",                              float.class);
            method_getMaxRenderHeight       = Display.class.getDeclaredMethod("getHeight");
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        method_setTransformation.setAccessible(true);
        method_setInterpolationDuration.setAccessible(true);
        method_setStartInterpolation.setAccessible(true);
        method_setBillboardMode.setAccessible(true);
        method_getBillboardMode.setAccessible(true);
        method_setViewRange.setAccessible(true);
        method_getViewRange.setAccessible(true);
        method_setBrightness.setAccessible(true);
        method_getBrightness.setAccessible(true);
        method_setMaxRenderWidth.setAccessible(true);
        method_getMaxRenderWidth.setAccessible(true);
        method_setMaxRenderHeight.setAccessible(true);
        method_getMaxRenderHeight.setAccessible(true);
    }




    /**
     * Creates a new CustomDisplay using an existing DisplayEntity as in-world entity.
     * @param _heldEntity The display entity.
     */
    protected CustomDisplay(final @NotNull Display _heldEntity) {
        heldEntity = _heldEntity;
        setBrightness(new Brightness(15, 15));
    }




    /**
     * Returns the UUID of the raw display entity.
     * @return The UUID.
     */
    public UUID getUuid() {
        return heldEntity.getUUID();
    }




    /**
     * Spawns the entity into the world.
     * @param world The world to spawn the entity in.
     * @param pos The position of the spawned entity.
     */
    public void spawn(final @NotNull Level world, final @NotNull Vector3d pos) {
        heldEntity.setPos(pos.x, pos.y, pos.z);
        world.addFreshEntity(heldEntity);
    }


    /**
     * Removes the entity from the world.
     */
    public void despawn() {
        heldEntity.remove(RemovalReason.KILLED);
    }








    /**
     * Sets a new transformation value to the entity.
     * <p> This is equivalent to changing the entity's "transformation" NBT.
     * @param transformation The new value.
     */
    public void setTransformation(final @NotNull Transformation transformation) {
        Utils.invokeSafe(method_setTransformation, heldEntity, transformation);
    }


    /**
     * Sets a new interpolation duration value to the entity.
     * <p> This is equivalent to changing the entity's "interpolation_duration" NBT.
     * @param duration The new value, measured in ticks
     */
    public void setInterpolationDuration(final int duration) {
        Utils.invokeSafe(method_setInterpolationDuration, heldEntity, duration);
    }


    /**
     * Starts the interpolation at the end of the current tick.
     * <p> This is equivalent to setting the entity's "start_interpolation" NBT to 0.
     */
    public void setStartInterpolation() {
        Utils.invokeSafe(method_setStartInterpolation, heldEntity, 0);
    }


    /**
     * Sets a new billboard mode value to the entity.
     * <p> This is equivalent to changing the entity's "billboard" NBT.
     * @param billboardMode The new value.
     */
    public void setBillboardMode(final @NotNull BillboardConstraints billboardMode) {
        Utils.invokeSafe(method_setBillboardMode, heldEntity, billboardMode);
    }


    /**
     * Retrieves the entity's billboard mode.
     * @return The current billboard mode.
     */
    public @NotNull BillboardConstraints getBillboardMode() {
        return (BillboardConstraints)Utils.invokeSafe(method_getBillboardMode, heldEntity);
    }


    /**
     * Sets a new view range value to the entity.
     * <p> This is equivalent to changing the entity's "view_range" NBT.
     * <p> The maximum distance the entity is visible at is (view_range * 64) blocks.
     * @param viewRange The new value.
     */
    public void setViewRange(final float viewRange) {
        Utils.invokeSafe(method_setViewRange, heldEntity, viewRange);
    }


    /**
     * Retrieves the entity's view range.
     * <p> The maximum distance the entity is visible at is (view_range * 64) blocks.
     * @return The current view range.
     */
    public float getViewRange() {
        return (float)Utils.invokeSafe(method_getViewRange, heldEntity);
    }


    /**
     * Sets a new brightness value to the entity.
     * <p> This is equivalent to changing the entity's "brightness" NBT.
     * @param brightness
     */
    public void setBrightness(final @NotNull Brightness brightness) {
        Utils.invokeSafe(method_setBrightness, heldEntity, brightness);
    }


    /**
     * Retrieves the entity's brightness.
     * @return The current brightness.
     */
    public @NotNull Brightness getBrightness() {
        return (Brightness)Utils.invokeSafe(method_getBrightness, heldEntity);
    }


    /**
     * Sets a new custom name value to the entity.
     * <p> This is equivalent to changing the entity's "custom_name" NBT.
     * @param name The new value.
     */
    public void setCustomName(final @NotNull Component name) {
        heldEntity.setCustomName(name);
    }


    /**
     * Sets a new custom name visible value to the entity.
     * <p> This is equivalent to changing the entity's "custom_name_visible" NBT.
     * @param name The new value.
     */
    public void setCustomNameVisible(final boolean nameVisible) {
        heldEntity.setCustomNameVisible(nameVisible);
    }


    /**
     * Sets a new glowing value to the entity.
     * @param name The new value. True makes the entity glow. False makes it stop glowing.
     */
    public void setGlowing(final boolean glowing) {
        heldEntity.setGlowingTag(glowing);
    }


    /**
     * Sets a new position to the entity.
     * @param pos The new position.
     */
    public void setPos(final @NotNull Vector3d pos) {
        heldEntity.setPos(pos.x, pos.y, pos.z);
    }


    /**
     * Retrieves the entity's position and returns a copy of it.
     * @return A copy of the current position.
     */
    public @NotNull Vector3f getPosCopy() {
        return heldEntity.getPosition(1f).toVector3f();
    }




    /**
     * Sets a new maximum render width to the entity.
     * <p> This is equivalent to changing the entity's "width" NBT.
     * <p> NOTICE: This property seems to be very buggy in 1.20.1 and often has no effect.
     * @param n The new value.
     */
    public void setMaxRenderWidth(final float n) {
        Utils.invokeSafe(method_setMaxRenderWidth, heldEntity, n);
    }


    /**
     * Retrieves the entity's maximum render width.
     * @return The current maximum render width.
     */
    public float getMaxRenderWidth() {
        return (float)Utils.invokeSafe(method_getMaxRenderWidth, heldEntity);
    }


    /**
     * Sets a new maximum render height to the entity.
     * <p> This is equivalent to changing the entity's "height" NBT.
     * <p> NOTICE: This property seems to be very buggy in 1.20.1 and often has no effect.
     * @param n The new value.
     */
    public void setMaxRenderHeight(final float n) {
        Utils.invokeSafe(method_setMaxRenderHeight, heldEntity, n);
    }


    /**
     * Retrieves the entity's maximum render height.
     * @return The current maximum render height.
     */
    public float getMaxRenderHeight() {
        return (float)Utils.invokeSafe(method_getMaxRenderHeight, heldEntity);
    }
}
