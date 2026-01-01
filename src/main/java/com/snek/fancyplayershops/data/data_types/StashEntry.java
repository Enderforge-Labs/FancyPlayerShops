package com.snek.fancyplayershops.data.data_types;

import org.jetbrains.annotations.NotNull;

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
     * @param _item The stashed item.
    */
    public StashEntry(final @NotNull ItemStack _item) {
        item = _item;
        count = 0;
    }


    /**
     * Adds the specified amount of items to this entry.
     * @param _count The amount of itmes to add
     */
    public void add(final int _count) {
        count += _count;
    }
}
