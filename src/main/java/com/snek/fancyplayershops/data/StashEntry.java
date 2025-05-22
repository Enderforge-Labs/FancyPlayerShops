package com.snek.fancyplayershops.data;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.ItemStack;








public class StashEntry {

    // Basic data
    public final @NotNull ItemStack item;
    private int count;
    public int getCount() { return count; }
    public void add(final int _count) { count += _count; }


    /**
     * Creates a new StashEntry.
     * @param _item The stashed item.
     */
    StashEntry(final @NotNull ItemStack _item) {
        item = _item;
        count = 0;
    }
}
