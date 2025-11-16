package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;

import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.buy.elements.BuyUi_ItemInspector;
import com.snek.fancyplayershops.graphics.ui.edit.EditUi;
import com.snek.framework.old.utils.MinecraftUtils;
import com.snek.framework.old.utils.Txt;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;








/**
 * A button that allows the owner of the shop to change the item sold by it.
 */
public class EditUi_ItemSelector extends BuyUi_ItemInspector {


    /**
     * Creates a new EditUiItemSelector.
     * @param _shop The target shop.
     */
    public EditUi_ItemSelector(final @NotNull Shop _shop) {
        super(_shop, null, "Change item", new EditUiSub_BackButton(_shop));
    }








    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        if(click != ClickAction.SECONDARY) {
            super.onClick(player, click);
            return;
        }


        // Return if item is null or air
        final ItemStack item = player.getItemInHand(InteractionHand.MAIN_HAND);
        if(item == null || item.getItem() == Items.AIR) return;


        // Send a message to the player if item is a shop snapshot, then return
        if(item.hasTag() && item.getTag().contains(ShopManager.SHOP_ITEM_NBT_KEY) && item.getTag().contains(ShopManager.SNAPSHOT_NBT_KEY)) {
            player.displayClientMessage(new Txt("Shop snapshots cannot be sold!").red().bold().get(), true);
            return;
        }


        // Send a message to the player if item contains a shop snapshopt, then return
        if(item.hasTag() && MinecraftUtils.nbtContainsSubstring(item.getTag(), ShopManager.SNAPSHOT_NBT_KEY)) {
            player.displayClientMessage(new Txt("Items containing shop snapshots cannot be sold!").red().bold().get(), true);
            return;
        }


        // Change item if all checks passed
        shop.changeItem(item);
        //FIXME check blacklist before setting the item
        //TODO add item blacklist
        shop.getItemDisplay().updateDisplay();
        ((EditUi_Title)((EditUi)(parent.getParent())).getTitle()).updateDisplay();
        playButtonSound(player);
    }
}