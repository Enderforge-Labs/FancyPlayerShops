package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.data.StashManager;
import com.snek.fancyplayershops.events.DisplayEvents;
import com.snek.fancyplayershops.events.data.DisplayRemovalReason;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.edit.styles.Edit_SquareButton_S;
import com.snek.frameworklib.graphics.designs.SymbolDesigns;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;








public class Edit_DeleteButton extends SimpleButtonElm {
    public Edit_DeleteButton(final @NotNull ProductDisplay display) {
        super(display.getLevel(), null, "Delete product display", 1,  new Edit_SquareButton_S(display));

        // Create design
        addDesign(display.getLevel(), SymbolDesigns.DiagonalCross);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        final ProductDisplay display = GetDisplay.get(this);


        // Send feedback message
        player.displayClientMessage(new Txt()
            .cat(new Txt("Your ").lightGray())
            .cat(new Txt(display.getDecoratedName()).white())
            .cat(new Txt(" has been deleted").lightGray())
        .get(), false);


        // Give the player a default product display item
        final ItemStack defaultProductDisplayItem =  ProductDisplayManager.getProductDisplayItemCopy();
        StashManager.giveItem(player.getUUID(), defaultProductDisplayItem, 1, true);


        // Stash, claim and delete the display
        display.stash(true);
        display.claimBalance();
        display.remove();
        DisplayEvents.DISPLAY_REMOVED.invoker().onDisplayRemove(display, DisplayRemovalReason.DELETED);
    }
}
