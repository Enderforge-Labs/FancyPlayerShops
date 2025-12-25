package com.snek.fancyplayershops.inventories;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;




public class ItemInspectorInventory_Factory implements MenuProvider {
    private final @NotNull ProductDisplay display;


    public ItemInspectorInventory_Factory(final @NotNull ProductDisplay display) {
        this.display = display;
    }


    @Override
    public Component getDisplayName() {
        return new Txt()
            .cat(new Txt(display.getStandaloneName()).gray().noBold().noItalic())
        .get();
    }


    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new ItemInspectorInventory(syncId, playerInventory, display);
    }
}