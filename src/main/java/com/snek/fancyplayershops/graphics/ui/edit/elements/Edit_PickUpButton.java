package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.events.DisplayEvents;
import com.snek.fancyplayershops.events.data.DisplayRemovalReason;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.edit.styles.Edit_SquareButton_S;
import com.snek.frameworklib.graphics.designs.SymbolDesigns;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Edit_PickUpButton extends SimpleButtonElm {
    public Edit_PickUpButton(final @NotNull ProductDisplay display) {
        super(display.getLevel(), null, "Pick up product display", 1,  new Edit_SquareButton_S(display));

        // Create design
        addDesign(display.getLevel(), SymbolDesigns.ArrowHeadsPointingOut);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        final ProductDisplay display = GetDisplay.get(this);


        // Send feedback message to the player
        //! This has to be executed before display.pickUp as the message needs to appear before the "set to stash" message
        player.displayClientMessage(new Txt()
            .cat(new Txt("Your ").lightGray())
            .cat(new Txt(display.getDecoratedName()).white())
            .cat(new Txt(" has been converted into an item").lightGray())
        .get(), false);


        // Pick up and delete product display
        display.pickUp(true);
        display.remove();
        DisplayEvents.DISPLAY_REMOVED.invoker().onDisplayRemove(display, DisplayRemovalReason.PICKED_UP);
    }
}
