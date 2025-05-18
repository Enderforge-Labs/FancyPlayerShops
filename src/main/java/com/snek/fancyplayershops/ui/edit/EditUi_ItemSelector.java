package com.snek.fancyplayershops.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.buy.BuyUi_ItemInspector;

import net.minecraft.network.chat.Component;
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
    public void updateDisplay(final @Nullable Component textOverride) {
        // Empty
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        if(click != ClickAction.SECONDARY) {
            super.onClick(player, click);
            return;
        }

        final ItemStack item = player.getItemInHand(InteractionHand.MAIN_HAND);
        if(item != null && item.getItem() != Items.AIR && !(item.getTag().contains(ShopManager.SHOP_ITEM_NBT_KEY) && item.getTag().contains("snapshot"))) {
            shop.changeItem(item);
            //FIXME check blacklist before setting the item
            //TODO add item blacklist
            shop.getItemDisplay().updateDisplay();
            ((EditUi_Title)((EditUi)(parent.getParent())).getTitle()).updateDisplay();
            playButtonSound(player);
        }
    }
}