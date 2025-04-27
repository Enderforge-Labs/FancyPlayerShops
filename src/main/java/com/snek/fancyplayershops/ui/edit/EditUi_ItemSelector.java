package com.snek.fancyplayershops.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.edit.styles.EditUi_ItemSelector_S;
import com.snek.fancyplayershops.ui.misc.ShopButton;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;








/**
 * A button that allows the owner of the shop to change the item sold by it.
 */
public class EditUi_ItemSelector extends ShopButton {


    /**
     * Creates a new EditUiItemSelector.
     * @param _shop The target shop.
     */
    public EditUi_ItemSelector(Shop _shop) {
        super(
            _shop,
            "TODO inspect item",
            "Change item",
            0,
            new EditUi_ItemSelector_S()
        );
    }


    @Override
    public void updateDisplay(@Nullable Text textOverride) {
        // Empty
    }


    @Override
    public boolean onClick(@NotNull PlayerEntity player, @NotNull ClickType click) {
        boolean r = super.onClick(player, click);
        if(r && player == shop.user) {

            ItemStack item = player.getMainHandStack();
            if(item != null && item.getItem() != Items.AIR) {
                shop.changeItem(item);
                //FIXME check blacklist before setting the item
                //TODO add item blacklist
                shop.getItemDisplay().updateDisplay();
                ((EditUi_Title)((EditUi)(parent.getParent())).getTitle()).updateDisplay();
                playButtonSound(player);
            }
        }
        return r;
    }
}
