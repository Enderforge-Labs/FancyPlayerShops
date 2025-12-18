package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.GetShop;
import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.data.StashManager;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.edit.styles.Edit_SquareButton_S;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.designs.SymbolDesigns;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;








public class Edit_DeleteButton extends SimpleButtonElm {
    public Edit_DeleteButton(final @NotNull Shop _shop) {
        super(_shop.getLevel(), null, "Delete shop", 1,  new Edit_SquareButton_S(_shop));

        // Create design
        final Div e = addChild(new PolylineSetElm(_shop.getLevel(), SymbolDesigns.DiagonalCross));
        e.setSize(new Vector2f(FancyPlayerShops.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        super.onClick(player, click);
        final Shop shop = GetShop.get(this);


        // Send feedback message
        player.displayClientMessage(new Txt()
            .cat("Your " + shop.getDecoratedName() + " has been deleted.")
            .color(ShopManager.SHOP_ITEM_NAME_COLOR)
        .get(), false);


        // Give the player a default shop item
        final ItemStack defaultShopItem =  ShopManager.getShopItemCopy();
        if(!MinecraftUtils.attemptGive(player, defaultShopItem)) {
            StashManager.stashItem(shop.getOwnerUuid(), defaultShopItem, 1);
            //! ^ saveStash() call is done by shop.stash()
        }


        // Stash and delete the shop
        shop.stash();
        shop.delete();
    }
}
