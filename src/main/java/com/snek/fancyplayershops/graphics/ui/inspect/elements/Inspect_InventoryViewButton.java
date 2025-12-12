package com.snek.fancyplayershops.graphics.ui.inspect.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.inventories.ItemInspectorInventory_Factory;
import com.snek.fancyplayershops.main.Shop;
import com.snek.frameworklib.graphics.functional.elements.__base_ButtonElm;
import com.snek.fancyplayershops.graphics.ui.buy.styles.Buy_ItemInspector_S;
import com.snek.fancyplayershops.graphics.ui.misc.elements.SimpleShopButton;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.Items;







/**
 * A button that allows the user of the shop to view the item as a stack in a vanilla inventory.
 */
public class Inspect_InventoryViewButton extends SimpleShopButton {
    private final @NotNull ItemInspectorInventory_Factory inventoryViewFactory;


    /**
     * Creates a new InspectUi_InventoryView.
     * @param _shop The target shop.
     */
    public Inspect_InventoryViewButton(final @NotNull Shop _shop) {
        super(_shop, null, "View item in inventory", 0, new Buy_ItemInspector_S(_shop));
        inventoryViewFactory = new ItemInspectorInventory_Factory(_shop);
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        super.onClick(player, click);
        if(!shop.getItem().is(Items.AIR)) {
            player.openMenu(inventoryViewFactory);
            __base_ButtonElm.playButtonSound(player);
        }
    }
}