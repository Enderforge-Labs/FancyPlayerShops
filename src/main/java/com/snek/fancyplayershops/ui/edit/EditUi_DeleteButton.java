package com.snek.fancyplayershops.ui.edit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.edit.styles.EditUi_SquareButton_S;
import com.snek.fancyplayershops.ui.misc.ShopButton;
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
import net.minecraft.world.item.Items;








public class EditUi_DeleteButton extends ShopButton {
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
    public boolean onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        final boolean r = super.onClick(player, click);
        if(r) {
            shop.stash();
            shop.delete();


            // Send feedback message
            if(shop.getItem().getItem() == Items.AIR) player.displayClientMessage(new Txt()
                .cat(new Txt("Your empty shop has been deleted.").color(FancyPlayerShops.SHOP_ITEM_NAME_COLOR))
            .get(), false);
            else player.displayClientMessage(new Txt()
                .cat(new Txt("Your shop \"")).color(FancyPlayerShops.SHOP_ITEM_NAME_COLOR)
                .cat(MinecraftUtils.getFancyItemName(shop.getItem()))
                .cat(new Txt("\" has been deleted.").color(FancyPlayerShops.SHOP_ITEM_NAME_COLOR))
            .get(), false);


            // Give the player a default shop item
            final ItemStack defaultShopItem =  FancyPlayerShops.getShopItemCopy();
            if(!MinecraftUtils.attemptGive(player, defaultShopItem)) {
                player.displayClientMessage(new Txt()
                    .cat("1x ").lightGray()
                    .cat(MinecraftUtils.getFancyItemName(defaultShopItem))
                    .cat(new Txt(" has been sent to your stash."))
                .get(), false);
            }
            //FIXME actually stash item
        }
        return r;
    }
}
