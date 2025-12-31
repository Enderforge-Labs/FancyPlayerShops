package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.edit.styles.Edit_SquareButton_S;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.designs.SymbolDesigns;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Edit_MoveButton extends SimpleButtonElm {
    public Edit_MoveButton(final @NotNull ProductDisplay display) {
        super(display.getLevel(), null, "Move product display", 1,  new Edit_SquareButton_S(display));

        // Create design
        final Div e = addChild(new PolylineSetElm(display.getLevel(), SymbolDesigns.ArrowHeadsPointingOut));
        e.setSize(new Vector2f(FancyPlayerShops.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        final ProductDisplay display = GetDisplay.get(this);


        // Send feedback message to the player
        //! This has to be executed before display.pickUp as the message needs to appear before the "set to stash" message
        player.displayClientMessage(new Txt()
            .cat(new Txt("Your " + display.getDecoratedName() + " has been converted into an item"))
            .color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR)
        .get(), false);


        // Pick up and delete product display
        display.pickUp(true);
        display.delete();
    }
}
