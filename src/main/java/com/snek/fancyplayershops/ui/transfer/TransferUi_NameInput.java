package com.snek.fancyplayershops.ui.transfer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.ShopTextInput;
import com.snek.fancyplayershops.ui.misc.styles.ShopTextInput_S;
import com.snek.fancyplayershops.ui.transfer.styles.TransferUi_NameInput_S;
import com.snek.framework.utils.Txt;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;








/**
 * A button that allows the owner of the shop to specify the new owner's name.
 */
public class TransferUi_NameInput extends ShopTextInput {
    private final @NotNull TransferUi menu;




    /**
     * Creates a new TransferUi_NameInput.
     * @param _shop The target shop.
     */
    public TransferUi_NameInput(final @NotNull Shop _shop, final @NotNull TransferUi _menu) {
        super(
            _shop,
            null, "Choose new owner",
            new Txt("Send the name of the new owner in chat!").color(FancyPlayerShops.SHOP_ITEM_NAME_COLOR).bold().get(),
            new TransferUi_NameInput_S(_shop)
        );
        menu = _menu;
        updateDisplay(null);
    }




    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        getStyle(ShopTextInput_S.class).setText(textOverride != null ? textOverride : new Txt()
            .cat(
                !menu.getNewOwnerUUID().equals(shop.getOwnerUuid()) ?
                FancyPlayerShops.getServer().getPlayerList().getPlayer(menu.getNewOwnerUUID()).getName().getString() : "-"
            )
        .white().get());
        flushStyle();
    }




    @Override
    protected boolean messageCallback(final @NotNull String s) {

        // Check if the name is not a valid username
        if(!s.matches("^\\w{3,16}$")) {
            shop.getuser().displayClientMessage(new Txt("The specified name is not a valid Minecraft username.").red().bold().get(), true);
        }

        // If it is, try to set the new owner and update the display in case of success
        else {
            final Player newOwner = FancyPlayerShops.getServer().getPlayerList().getPlayerByName(s);
            if(newOwner == null) {
                shop.getuser().displayClientMessage(new Txt("The specified player is currently offline.").red().bold().get(), true);
            }
            else if(newOwner.getUUID().equals(shop.getOwnerUuid())) {
                shop.getuser().displayClientMessage(new Txt("You already own this shop!").red().bold().get(), true);
            }
            else {
                menu.setNewOwnerUUID(newOwner.getUUID());
            }
        }
        return true;
    }
}
