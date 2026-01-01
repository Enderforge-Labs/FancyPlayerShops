package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.fancyplayershops.GetDisplay;
import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.buy.BuyCanvas;
import com.snek.fancyplayershops.graphics.ui.buy.styles.Buy_Input_S;
import com.snek.frameworklib.graphics.functional.elements.TextInputElm;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.world.entity.player.Player;








public class Buy_AmountInputDisplay extends TextInputElm {
    private final @NotNull BuyCanvas menu;


    public Buy_AmountInputDisplay(final @NotNull ProductDisplay display, final @NotNull BuyCanvas _menu) {
        super(display.getLevel(), null, "Specify buy amount", new Txt("Send the amount in chat!").lightGray().get(), new Buy_Input_S(display));
        menu = _menu;
    }


    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        updateDisplayedText();
        super.spawn(pos, animate);
    }


    @Override
    protected boolean messageCallback(final @NotNull String s) {
        final ProductDisplay display = GetDisplay.get(this);
        final Player user = display.getuser();
        if(user == null) return false;

        try {

            // Try to set the new amount
            if(menu.attemptChangeAmount(user, Integer.parseInt(s))) updateDisplayedText();
            return true;

        } catch(final NumberFormatException e) {
            try {

                // Try to set the new amount
                if(menu.attemptChangeAmount(user, Float.parseFloat(s))) updateDisplayedText();
                return true;

            } catch(final NumberFormatException e2) {
                return false;
            }
        }
    }



    public void updateDisplayedText() {
        final ProductDisplay display = GetDisplay.get(this);
        setDisplayedText(new Txt()
            .cat(new Txt("Amount: ").lightGray())
            .cat(new Txt(Utils.formatAmount(menu.getAmount())).white())
            .cat(new Txt(" / " + display.getStock()).lightGray())
        .get());
    }
}
