package com.snek.fancyplayershops.shop_ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.data.StashManager;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.shop_ui.edit.EditUi;
import com.snek.fancyplayershops.shop_ui.edit.styles.EditUi_SquareButton_S;
import com.snek.fancyplayershops.shop_ui.misc.elements.ShopButton;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.composite.PolylineData;
import com.snek.framework.ui.composite.PolylineSetElm;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;








public class EditUi_DeleteButton extends ShopButton  {
    private static final @NotNull PolylineData[] design = new PolylineData[] {
        new PolylineData(
            EditUi.TOOLBAR_FG_COLOR, EditUi.TOOLBAR_FG_ALPHA,
            EditUi.TOOLBAR_FG_WIDTH, 0.05f,
            new Vector2f(0.1f, 0.1f),
            new Vector2f(0.9f, 0.9f)
        ),
        new PolylineData(
            EditUi.TOOLBAR_FG_COLOR, EditUi.TOOLBAR_FG_ALPHA,
            EditUi.TOOLBAR_FG_WIDTH, 0.05f,
            new Vector2f(0.1f, 0.9f),
            new Vector2f(0.9f, 0.1f)
        )
    };




    public EditUi_DeleteButton(final @NotNull Shop _shop){
        super(_shop, null, "Delete shop", 1,  new EditUi_SquareButton_S(_shop));

        // Create design
        final Div e = addChild(new PolylineSetElm(_shop.getWorld(), design));
        e.setSize(new Vector2f(EditUi.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        // Empty
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {

        // Send feedback message
        player.displayClientMessage(new Txt()
            .cat("Your " + shop.getDecoratedName() + " has been deleted.")
        .color(ShopManager.SHOP_ITEM_NAME_COLOR).get(), false);


        // Give the player a default shop item
        final ItemStack defaultShopItem =  ShopManager.getShopItemCopy();
        if(!MinecraftUtils.attemptGive(player, defaultShopItem)) {
            StashManager.stashItem(shop.getOwnerUuid(), defaultShopItem, 1);
            //! ^ saveStash() call is done by shop.stash()
            player.displayClientMessage(new Txt()
                .cat("1x ")
                .cat(MinecraftUtils.getFancyItemName(defaultShopItem).getString())
                .cat(" has been sent to your stash.")
            .lightGray().get(), false);
        }


        // Stash and delete the shop
        shop.stash();
        shop.delete();
    }
}
