package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.misc.elements.Misc_BackButton;
import com.snek.fancyplayershops.graphics.ui.buy.elements.Buy_ItemInspector;
import com.snek.fancyplayershops.graphics.ui.edit.EditCanvas;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;








/**
 * A button that allows the owner of the product display to change the item sold by it.
 */
public class Edit_ItemSelector extends Buy_ItemInspector {


    /**
     * Creates a new EditUiItemSelector.
     * @param display The target product display.
     * @param _backButton The back button. This defines which menu the player is brought to when going back.
     */
    public Edit_ItemSelector(final @NotNull ProductDisplay display, final @NotNull Misc_BackButton _backButton) {
        super(display, null, "Change item", _backButton);
    }








    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        if(click != ClickAction.SECONDARY) {
            super.onClick(player, click, coords);
            return;
        }


        // Return if item is null or air
        final ItemStack item = player.getItemInHand(InteractionHand.MAIN_HAND);
        if(item == null || item.is(Items.AIR)) return;


        // Send a message to the player if item is a display snapshot, then return
        if(MinecraftUtils.hasTag(item, ProductDisplayManager.SNAPSHOT_NBT_KEY)) {
            player.displayClientMessage(new Txt("Product display snapshots cannot be sold!").red().bold().get(), true);
            return;
        }


        // Send a message to the player if item contains a display snapshot, then return
        if(item.hasTag() && MinecraftUtils.nbtContainsSubstring(item.getTag(), ProductDisplayManager.SNAPSHOT_NBT_KEY)) {
            player.displayClientMessage(new Txt("Items containing product display snapshots cannot be sold!").red().bold().get(), true);
            return;
        }


        // Change item if all checks passed
        final ProductDisplay display = GetDisplay.get(this);
        display.changeItem(item);
        //FIXME check blacklist before setting the item
        //TODO add item blacklist
        display.getItemDisplay().updateDisplay();


        // Update canvas title and play click sound
        final EditCanvas editCanvas = (EditCanvas)canvas;
        editCanvas.updateTitle(EditCanvas.calculateTitle(display));
        Clickable.playSound(player);
    }
}