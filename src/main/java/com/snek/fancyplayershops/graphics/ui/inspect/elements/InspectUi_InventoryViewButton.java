package com.snek.fancyplayershops.graphics.ui.inspect.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.inventories.ItemInspectorInventory_Factory;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.buy.styles.BuyUi_ItemInspector_S;
import com.snek.fancyplayershops.graphics.ui.misc.elements.SimpleShopButton;
import com.snek.frameworklib.data_types.ui.AlignmentX;
import com.snek.frameworklib.data_types.ui.AlignmentY;
import com.snek.frameworklib.data_types.ui.PolylineData;
import com.snek.frameworklib.graphics.Canvas;
import com.snek.frameworklib.graphics.Div;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.Items;



/**
 * A button that allows the user of the shop to view the item as a stack in a vanilla inventory.
 */
public class InspectUi_InventoryViewButton extends SimpleShopButton {
    private static final @NotNull PolylineData[] design = new PolylineData[] {
        // Box (bottom and left sides)
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.03f,
            new Vector2f(0.2f, 0.2f),
            new Vector2f(0.2f, 0.8f),
            new Vector2f(0.8f, 0.8f),
            new Vector2f(0.8f, 0.5f)
        ),
        // Arrow shaft (diagonal line pointing to top-right)
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.03f,
            new Vector2f(0.5f, 0.5f),
            new Vector2f(0.9f, 0.1f)
        ),
        // Arrow head (right side)
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.03f,
            new Vector2f(0.9f, 0.1f),
            new Vector2f(0.9f, 0.35f)
        ),
        // Arrow head (top side)
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.03f,
            new Vector2f(0.9f, 0.1f),
            new Vector2f(0.65f, 0.1f)
        )
    };

    private final @NotNull ItemInspectorInventory_Factory inventoryViewFactory;


    /**
     * Creates a new InspectUi_InventoryView.
     * @param _shop The target shop.
     */
    public InspectUi_InventoryViewButton(final @NotNull Shop _shop) {
        super(_shop, null, "View item in inventory", 0, new BuyUi_ItemInspector_S(_shop));
        inventoryViewFactory = new ItemInspectorInventory_Factory(_shop);

        // Create design
        final Div e = addChild(new PolylineSetElm(_shop.getWorld(), design));
        e.setSize(new Vector2f(Canvas.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        if(shop.getItem().getItem() != Items.AIR) {
            player.openMenu(inventoryViewFactory);
            playButtonSound(player);
        }
    }
}