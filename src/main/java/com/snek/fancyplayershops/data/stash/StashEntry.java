package com.snek.fancyplayershops.data.stash;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.debug.Require;

import net.minecraft.world.item.ItemStack;








public class StashEntry {

    // Basic data
    private final @NotNull ItemStack item;
    private long count;

    // Getters
    public long getCount() { return count; }
    public ItemStack getItem() { return item; }


    /**
     * Creates a new StashEntry containing the specified item with &lt;count&gt; count.
     * @param item The stashed item.
     * @param count The item count.
    */
    public StashEntry(final @NotNull ItemStack item, final long count) {
        this.item = item;
        this.count = count;
    }


    /**
     * Creates a new StashEntry containing the specified item with count 0.
     * @param item The stashed item.
    */
    public StashEntry(final @NotNull ItemStack item) {
        this(item, 0);
    }


    /**
     * Adds the specified amount of items to this entry.
     * <p>
     * This method fires a stash item change event. //FIXME actually do that, then detect it from uis?
     * @param count The amount of itmes to add.
     */
    public void add(final long count) {
        this.count += count;
        //FIXME fire event
    }


    /**
     * Removes the specified amount of items from this entry.
     * <p>
     * This method fires a stash item change event. //FIXME actually do that, then detect it from uis?
     * @param count The amount of itmes to remove. Must be {@code <= this.getCount()}.
     */
    public void remove(final long count) {
        assert Require.condition(count <= getCount(), "Amount of removed items cannot be greater than the current count");
        this.count -= count;
        //FIXME fire event
    }
}
