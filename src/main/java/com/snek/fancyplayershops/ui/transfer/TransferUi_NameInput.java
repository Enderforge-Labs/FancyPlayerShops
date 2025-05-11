package com.snek.fancyplayershops.ui.transfer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.ShopTextInput;
import com.snek.fancyplayershops.ui.misc.styles.ShopTextInput_S;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;








/**
 * A button that allows the owner of the shop to specify the new owner's name.
 */
public class TransferUi_NameInput extends ShopTextInput {

    /**
     * Creates a new TransferUi_NameInput.
     * @param _shop The target shop.
     */
    public TransferUi_NameInput(final @NotNull Shop _shop) {
        super(_shop, null, "Choose new owner", new Txt("Send the name of the new owner in chat!").color(FancyPlayerShops.SHOP_ITEM_NAME_COLOR).bold().get());
        updateDisplay(null);
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        if(shop.getActiveCanvas() instanceof TransferUi c) {
            getStyle(ShopTextInput_S.class).setText(textOverride != null ? textOverride : new Txt()
                .cat("New owner: ")
                .cat(FancyPlayerShops.getServer().getPlayerList().getPlayer(c.getNewOwnerUUID()).getName().getString())
            .white().get());
        }
        else {
            getStyle(ShopTextInput_S.class).setText(textOverride != null ? textOverride : new Txt()
                .cat("New owner: ")
                .cat(FancyPlayerShops.getServer().getPlayerList().getPlayer(shop.getOwnerUuid()).getName().getString())
            .white().get());
        }
        flushStyle();
    }


    @Override
    protected boolean messageCallback(final @NotNull String s) {

        // Try to set the new owner and update the display if it's valid
        final Player newOwner = FancyPlayerShops.getServer().getPlayerList().getPlayerByName(s);
        if(newOwner == null) {
            shop.getuser().displayClientMessage(new Txt("The specified player is currently offline.").red().bold().get(), true);
        }
        else {
            ((TransferUi)shop.getActiveCanvas()).setNewOwnerUUID(newOwner.getUUID());
        }
        return true;
    }
}
