package com.snek.fancyplayershops.inventories;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.ProductDisplay;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;








public class ItemInspectorInventory extends AbstractContainerMenu {
    private final @NotNull SimpleContainer inventory;




    public ItemInspectorInventory(final int containerId, final @NotNull Inventory playerInventory, final @NotNull ProductDisplay display) {
        super(MenuType.HOPPER, containerId);
        inventory = new SimpleContainer(5);

        // Add 5 storage slots
        for(int i = 0; i < 5; i++) {
            this.addSlot(new ReadOnlySlot(inventory, i, i, 0));
        }
        // Copy display item to central slot
        this.setItem(2, this.incrementStateId(), display.getItem().copy());


        // Add player inventory (27 slots)
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
            }
        }

        // Add hotbar (9 slots)
        for(int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 198));
        }
    }




    @Override
    public boolean stillValid(final Player player) {
        return true;
    }


    @Override
    public ItemStack quickMoveStack(final Player player, final int slot) {
        // Disable shift click interactions
        return ItemStack.EMPTY;
    }
}