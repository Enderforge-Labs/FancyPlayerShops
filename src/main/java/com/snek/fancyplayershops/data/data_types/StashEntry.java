package com.snek.fancyplayershops.data.data_types;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.debug.Require;

import net.minecraft.world.item.ItemStack;








public class StashEntry {

    // Basic data
    private final @NotNull ItemStack item;
    private int count;

    // Getters
    public int getCount() { return count; }
    public ItemStack getItem() { return item; }


    /**
     * Creates a new StashEntry.
     * @param item The stashed item.
    */
    public StashEntry(final @NotNull ItemStack item) {
        this.item = item;
        count = 0;
    }


    /**
     * Adds the specified amount of items to this entry.
     * @param count The amount of itmes to add.
     */
    public void add(final int count) {
        this.count += count;
    }


    /**
     * Removes the specified amount of items from this entry.
     * @param count The amount of itmes to remove. Must be {@code <= this.getCount()}.
     */
    public void remove(final int count) {
        assert Require.condition(count <= getCount(), "Amount of removed items cannot be greater than the current count"):
        this.count -= count;
    }
}
