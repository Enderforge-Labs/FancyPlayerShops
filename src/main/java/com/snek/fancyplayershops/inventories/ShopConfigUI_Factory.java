package com.snek.fancyplayershops.inventories;

import com.snek.framework.utils.Txt;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;




public class ShopConfigUI_Factory implements MenuProvider {
    @Override
    public Component getDisplayName() {
        return new Txt("test ui by snek").get();
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new ShopConfigUI(syncId, playerInventory);
    }
}