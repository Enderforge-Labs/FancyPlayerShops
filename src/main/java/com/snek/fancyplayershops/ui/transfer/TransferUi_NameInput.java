package com.snek.fancyplayershops.ui.transfer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.misc.ShopTextInput;
import com.snek.fancyplayershops.ui.misc.styles.ShopTextInput_S;
import com.snek.fancyplayershops.ui.transfer.styles.TransferUi_Input_S;
import com.snek.framework.utils.Txt;

import net.minecraft.network.chat.Component;








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
            new Txt("Send the name of the new owner in chat!").color(ShopManager.SHOP_ITEM_NAME_COLOR).bold().get(),
            new TransferUi_Input_S(_shop)
        );
        menu = _menu;
        updateDisplay(null);
    }




    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        getStyle(ShopTextInput_S.class).setText(textOverride != null ? textOverride : new Txt()
            .cat(
                !menu.getNewOwnerUUID().equals(shop.getOwnerUuid()) ?
                new Txt(FancyPlayerShops.getServer().getPlayerList().getPlayer(menu.getNewOwnerUUID()).getName().getString()) :
                new Txt("[Not specified]").lightGray().italic()
            )
        .white().get());
        flushStyle();
    }




    @Override
    protected boolean messageCallback(final @NotNull String s) {
        menu.attemptSetNewOwner(s);
        return true;
    }
}
