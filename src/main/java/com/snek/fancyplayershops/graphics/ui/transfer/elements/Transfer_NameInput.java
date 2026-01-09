package com.snek.fancyplayershops.graphics.ui.transfer.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.misc.styles.ProductDisplay_TextInput_S;
import com.snek.fancyplayershops.graphics.ui.transfer.TransferCanvas;
import com.snek.frameworklib.graphics.functional.elements.ChatInputElm;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.Txt;








/**
 * A button that allows the owner of the product display to specify the new owner's name.
 */
public class Transfer_NameInput extends ChatInputElm {
    private final @NotNull TransferCanvas menu;




    /**
     * Creates a new TransferUi_NameInput.
     * @param display The target product display.
     */
    public Transfer_NameInput(final @NotNull ProductDisplay display, final @NotNull TransferCanvas _menu) {
        super(
            display.getLevel(),
            null, "Choose new owner",
            new Txt("Send the name of the new owner in chat!").lightGray().bold().get(),
            new ProductDisplay_TextInput_S(display)
        );
        menu = _menu;
    }


    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        updateDisplayedText();
        super.spawn(pos, animate);
    }




    public void updateDisplayedText() {
        final ProductDisplay display = GetDisplay.get(this);
        setDisplayedText((
            !menu.getNewOwnerUUID().equals(display.getOwnerUuid()) ?
            new Txt(MinecraftUtils.getPlayerByUUID(menu.getNewOwnerUUID()).getName().getString()) :
            new Txt("[Not specified]").lightGray().italic()
        ).white().get());
    }




    @Override
    protected boolean messageCallback(final @NotNull String s) {
        menu.attemptSetNewOwner(s);
        updateDisplayedText();
        return true;
    }
}
