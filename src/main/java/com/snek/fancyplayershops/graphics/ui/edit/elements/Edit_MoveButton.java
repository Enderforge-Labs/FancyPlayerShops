package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.GetShop;
import com.snek.fancyplayershops.data.ShopManager;
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
    public Edit_MoveButton(final @NotNull ProductDisplay _shop) {
        super(_shop.getLevel(), null, "Move shop", 1,  new Edit_SquareButton_S(_shop));

        // Create design
        final Div e = addChild(new PolylineSetElm(_shop.getLevel(), SymbolDesigns.ArrowHeadsPointingOut));
        e.setSize(new Vector2f(FancyPlayerShops.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        final ProductDisplay shop = GetShop.get(this);


        // Send feedback message to the player
        //! This has to be executed before shop.pickUp as the message needs to appear before the "set to stash" message
        player.displayClientMessage(new Txt()
            .cat(new Txt("Your " + shop.getDecoratedName() + " has been converted into an item."))
            .color(ShopManager.SHOP_ITEM_NAME_COLOR)
        .get(), false);


        // Pick up and delete shop
        shop.pickUp(true);
        shop.delete();
    }
}
