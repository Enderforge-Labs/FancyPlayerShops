package com.snek.fancyplayershops.graphics.ui.transfer.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.misc.styles.ProductDisplay_TextInput_S;
import com.snek.fancyplayershops.graphics.ui.transfer.TransferCanvas;
import com.snek.fancyplayershops.graphics.ui.transfer.styles.Transfer_Input_S;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.graphics.functional.elements.TextInputElm;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;








/**
 * A button that allows the owner of the product display to specify the new owner's name.
 */
public class Transfer_NameInput extends TextInputElm {
    private final @NotNull TransferCanvas menu;




    /**
     * Creates a new TransferUi_NameInput.
     * @param display The target product display.
     */
    public Transfer_NameInput(final @NotNull ProductDisplay display, final @NotNull TransferCanvas _menu) {
        super(
            display.getLevel(),
            null, "Choose new owner",
            new Txt("Send the name of the new owner in chat!").color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR).bold().get(),
            new Transfer_Input_S(display)
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
        final ProductDisplay display = GetDisplay.get(this);
        getStyle(ProductDisplay_TextInput_S.class).setText(textOverride != null ? textOverride : new Txt()
            .cat(
                !menu.getNewOwnerUUID().equals(display.getOwnerUuid()) ?
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
