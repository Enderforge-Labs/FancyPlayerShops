package com.snek.fancyplayershops.data;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.ItemStack;








public class StashEntry {
    public final @NotNull ItemStack item;
    public final int count;

    StashEntry(final @NotNull ItemStack _item, final int _count) {
        item = _item;
        count = _count;
    }
}
