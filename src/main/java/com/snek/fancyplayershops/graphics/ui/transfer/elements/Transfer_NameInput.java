package com.snek.fancyplayershops.graphics.ui.transfer.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.fancyplayershops.GetShop;
import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.misc.styles.ShopTextInput_S;
import com.snek.fancyplayershops.graphics.ui.transfer.TransferCanvas;
import com.snek.fancyplayershops.graphics.ui.transfer.styles.Transfer_Input_S;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.graphics.functional.elements.TextInputElm;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;








/**
 * A button that allows the owner of the shop to specify the new owner's name.
 */
public class Transfer_NameInput extends TextInputElm {
    private final @NotNull TransferCanvas menu;




    /**
     * Creates a new TransferUi_NameInput.
     * @param _shop The target shop.
     */
    public Transfer_NameInput(final @NotNull ProductDisplay _shop, final @NotNull TransferCanvas _menu) {
        super(
            _shop.getLevel(),
            null, "Choose new owner",
            new Txt("Send the name of the new owner in chat!").color(ShopManager.SHOP_ITEM_NAME_COLOR).bold().get(),
            new Transfer_Input_S(_shop)
        );
        menu = _menu;
    }


    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        updateDisplay(null);
        super.spawn(pos, animate);
    }




    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        final ProductDisplay shop = GetShop.get(this);
        getStyle(ShopTextInput_S.class).setText(textOverride != null ? textOverride : new Txt()
            .cat(
                !menu.getNewOwnerUUID().equals(shop.getOwnerUuid()) ?
                new Txt(FrameworkLib.getServer().getPlayerList().getPlayer(menu.getNewOwnerUUID()).getName().getString()) :
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
