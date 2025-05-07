package com.snek.fancyplayershops.inventories;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;





public class ShopConfigUI extends AbstractContainerMenu {
    private final SimpleContainer inventory;




    public ShopConfigUI(int syncId, Inventory playerInventory) {
        super(MenuType.GENERIC_9x6, syncId);
        this.inventory = new SimpleContainer(54);

        // Add 54 storage slots
        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inventory, j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }

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




    //FIXME
    @Override
    public boolean stillValid(Player player) { //TODO check if this is correct
        return true;
    }




    //FIXME
    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        throw new UnsupportedOperationException("Unimplemented method 'quickMove'");
    }
}