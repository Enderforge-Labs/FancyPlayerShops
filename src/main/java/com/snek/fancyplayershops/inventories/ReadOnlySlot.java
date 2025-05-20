package com.snek.fancyplayershops.inventories;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;








/**
 * A custom slot that prevents players from interacting with it in any way.
 * Creative mode players can still pick the item with middle clicks.
 */
public class ReadOnlySlot extends Slot {

    public ReadOnlySlot(Container container, int index, int x, int y) {
        super(container, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public boolean mayPickup(Player player) {
        return false;
    }
}
