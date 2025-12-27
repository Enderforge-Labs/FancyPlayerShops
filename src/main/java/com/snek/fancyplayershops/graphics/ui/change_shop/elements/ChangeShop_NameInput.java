package com.snek.fancyplayershops.graphics.ui.change_shop.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.change_shop.ChangeShopCanvas;
import com.snek.fancyplayershops.graphics.ui.misc.styles.ProductDisplay_TextInput_S;
import com.snek.frameworklib.graphics.functional.elements.TextInputElm;
import com.snek.frameworklib.utils.Txt;








/**
 * A button that allows the owner of the product display to specify the new shop's display name.
 */
public class ChangeShop_NameInput extends TextInputElm {
    private final @NotNull ChangeShopCanvas menu;




    /**
     * Creates a new ChangeShop_NameInput.
     * @param display The target product display.
     */
    public ChangeShop_NameInput(final @NotNull ProductDisplay display, final @NotNull ChangeShopCanvas _menu) {
        super(
            display.getLevel(),
            null, "Choose new shop",
            new Txt("Send the name of the new shop in chat!").color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR).bold().get(),
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
            !menu.getNewShopName().equals(display.getShop().getDisplayName()) ? //FIXME strip color codes
            new Txt(menu.getNewShopName()) :
            new Txt("[Not specified]").lightGray().italic()
        ).white().get());
    }




    @Override
    protected boolean messageCallback(final @NotNull String s) {
        menu.attemptSetNewShop(s);
        updateDisplayedText();
        return true;
    }
}
