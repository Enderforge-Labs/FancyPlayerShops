package com.snek.fancyplayershops.graphics.ui.inspect.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.inventories.ItemInspectorInventory_Factory;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.graphics.functional.elements.ButtonElm;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.graphics.ui.buy.styles.Buy_ItemInspector_S;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.Items;







/**
 * A button that allows the user of the product displat to view the item as a stack in a vanilla inventory.
 */
public class Inspect_InventoryViewButton extends ButtonElm {
    private final @NotNull ItemInspectorInventory_Factory inventoryViewFactory;


    /**
     * Creates a new InspectUi_InventoryView.
     * @param display The target product display.
     */
    public Inspect_InventoryViewButton(final @NotNull ProductDisplay display) {
        super(display.getLevel(), null, "View item in inventory", 0, new Buy_ItemInspector_S(display));
        inventoryViewFactory = new ItemInspectorInventory_Factory(display);
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);

        // Open menu if the product display is configured
        final ProductDisplay display = GetDisplay.get(this);
        if(!display.getItem().is(Items.AIR)) {
            player.openMenu(inventoryViewFactory);
            Clickable.playSound(player);
        }
    }
}