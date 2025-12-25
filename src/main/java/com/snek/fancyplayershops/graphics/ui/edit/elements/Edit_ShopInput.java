package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.edit.EditCanvas;
import com.snek.fancyplayershops.graphics.ui.edit.styles.Edit_Input_S;
import com.snek.frameworklib.graphics.functional.elements.TextInputElm;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.server.level.ServerPlayer;








/**
 * A button that allows the owner of the product display to specify the shop it belongs to.
 */
public class Edit_ShopInput extends TextInputElm {




    /**
     * Creates a new Edit_ShopInput.
     * @param display The target product display.
     */
    public Edit_ShopInput(final @NotNull ProductDisplay display) {
        super(
            display.getLevel(),
            null, "Transfer to another shop",
            new Txt("Send the name of the shop in chat!").color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR).bold().get(),
            new Edit_Input_S(display)
        );
    }


    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        updateDisplayedText();
        super.spawn(pos, animate);
    }






    public void updateDisplayedText() {
        final ProductDisplay display = GetDisplay.get(this);
        setDisplayedText(new Txt()
            .cat(new Txt("Shop: ").lightGray())
            .cat(new Txt(display.getShop().getDisplayName()).white())
            //TODO convert component to string before using it
        .get());
    }




    @Override
    protected boolean messageCallback(final @NotNull String s) {
        final ServerPlayer player = (ServerPlayer)canvas.getContext().getPlayer();
        final char c = s.charAt(0);
        if(c == '.' || c == ' ' || c == ',') {
            player.displayClientMessage(new Txt("Shop names can't start with \"" + c + "\"!").red().bold().get(), true);
        }
        else if(Character.isDigit(c)) {
            player.displayClientMessage(new Txt("Shop names can't start with a number!").red().bold().get(), true);
        }
        else {
            final ProductDisplay display = GetDisplay.get(this);
            display.changeShop(s, player);
            updateDisplayedText();
        }
        return true;
    }




    @Override
    public float getInteractionSizeRight() {
        return super.getInteractionSizeRight() - EditCanvas.COLOR_SELECTOR_W;
    }
}