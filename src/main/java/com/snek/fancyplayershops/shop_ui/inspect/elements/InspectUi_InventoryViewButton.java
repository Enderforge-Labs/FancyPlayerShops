package com.snek.fancyplayershops.shop_ui.inspect.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.inventories.ItemInspectorInventory_Factory;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.shop_ui.buy.styles.BuyUi_ItemInspector_S;
import com.snek.fancyplayershops.shop_ui.misc.elements.ShopButton;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.Items;







//TODO add "open in inventory" button
//TODO use the "external link" icon design

/**
 * A button that allows the user of the shop to view the item as a stack in a vanilla inventory.
 */
public class InspectUi_InventoryViewButton extends ShopButton {
    private final @NotNull ItemInspectorInventory_Factory inventoryViewFactory;


    /**
     * Creates a new InspectUi_InventoryView.
     * @param _shop The target shop.
     */
    public InspectUi_InventoryViewButton(final @NotNull Shop _shop) {
        super(_shop, null, "View item in inventory", 0, new BuyUi_ItemInspector_S(_shop));
        inventoryViewFactory = new ItemInspectorInventory_Factory(_shop);
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        // Empty
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        if(shop.getItem().getItem() != Items.AIR) {
            player.openMenu(inventoryViewFactory);
            playButtonSound(player);
        }
    }
}