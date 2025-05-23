package com.snek.framework.data_types.displays;

import java.lang.reflect.Method;

import org.jetbrains.annotations.NotNull;

import com.snek.framework.utils.Utils;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Display.ItemDisplay;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;







/**
 * A wrapper for Minecraft's ItemDisplayEntity.
 * <p> This class allows for better customization and more readable code.
 */
public class CustomItemDisplay extends CustomDisplay {
    public @NotNull ItemDisplay getRawDisplay() { return (ItemDisplay)heldEntity; }


    // Private methods
    private static @NotNull Method method_setItemStack;
    private static @NotNull Method method_setDisplayType;
    private static @NotNull Method method_getDisplayType;
    static {
        try {
            method_setItemStack   = ItemDisplay.class.getDeclaredMethod("setItemStack",              ItemStack.class);
            method_setDisplayType = ItemDisplay.class.getDeclaredMethod("setItemTransform", ItemDisplayContext.class);
            method_getDisplayType = ItemDisplay.class.getDeclaredMethod("getItemTransform");
        } catch(final NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        method_setItemStack.setAccessible(true);
        method_setDisplayType.setAccessible(true);
        method_getDisplayType.setAccessible(true);
    }




    /**
     * Creates a new CustomItemDisplay using an existing ItemDisplayEntity.
     * @param _rawDisplay The display entity.
     */
    public CustomItemDisplay(final @NotNull ItemDisplay _rawDisplay) {
        super(_rawDisplay);
    }

    /**
     * Creates a new CustomItemDisplay in the specified world.
     * @param _world The world.
     */
    public CustomItemDisplay(final @NotNull Level _world) {
        super(new ItemDisplay(EntityType.ITEM_DISPLAY, _world));
    }




    /**
     * Sets a new item stack value to the entity.
     * <p> This is equivalent to changing the entity's "item" NBT.
     * @param itemStack The new value.
     */
    public void setItemStack(final @NotNull ItemStack itemStack) {
        Utils.invokeSafe(method_setItemStack, getRawDisplay(), itemStack);
    }


    /**
     * Sets a new display type value to the entity.
     * <p> This is equivalent to changing the entity's "item_display" NBT.
     * @param displayType The new value.
     */
    public void setDisplayType(final @NotNull ItemDisplayContext displayType) {
        Utils.invokeSafe(method_setDisplayType, getRawDisplay(), displayType);
    }


    /**
     * Retrieves the entity's display type value.
     * @return The current display type.
     */
    public @NotNull ItemDisplayContext getDisplayType() {
        return (ItemDisplayContext)Utils.invokeSafe(method_getDisplayType, heldEntity);
    }
}
