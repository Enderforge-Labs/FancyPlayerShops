package com.snek.fancyplayershops.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.edit.styles.EditUi_ItemSelector_S;
import com.snek.fancyplayershops.ui.misc.ShopButton;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;








/**
 * A button that allows the owner of the shop to change the item sold by it.
 */
public class EditUi_ItemSelector extends ShopButton {


    /**
     * Creates a new EditUiItemSelector.
     * @param _shop The target shop.
     */
    public EditUi_ItemSelector(final @NotNull Shop _shop) {
        super(_shop, "Inspect item", "Change item", 0, new EditUi_ItemSelector_S(_shop));
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        // Empty
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {

        final ItemStack item = player.getItemInHand(InteractionHand.MAIN_HAND);
        if(item != null && item.getItem() != Items.AIR) {
            shop.changeItem(item);
            //FIXME check blacklist before setting the item
            //TODO add item blacklist
            shop.getItemDisplay().updateDisplay();
            ((EditUi_Title)((EditUi)(parent.getParent())).getTitle()).updateDisplay();
            playButtonSound(player);
        }
    }
}
//TODO make this a subclass of item inspector
//TODO     item inspectors show the price graph, the mod the item comes from, the name, the description
//TODO     it also lets you open an inventory to see it as a real item