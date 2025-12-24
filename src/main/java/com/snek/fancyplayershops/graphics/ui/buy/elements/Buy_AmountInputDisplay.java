package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.fancyplayershops.GetShop;
import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.buy.BuyCanvas;
import com.snek.fancyplayershops.graphics.ui.buy.styles.Buy_Input_S;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.graphics.functional.elements.TextInputElm;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;








public class Buy_AmountInputDisplay extends TextInputElm {
    private final @NotNull BuyCanvas menu;


    public Buy_AmountInputDisplay(final @NotNull ProductDisplay _shop, final @NotNull BuyCanvas _menu) {
        super(_shop.getLevel(), null, "Specify buy amount", new Txt("Send the amount in chat!").color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR).get(), new Buy_Input_S(_shop));
        menu = _menu;
    }


    @Override
    public void spawn(final @NotNull Vector3d pos, final boolean animate) {
        updateDisplay(null);
        super.spawn(pos, animate);
    }


    @Override
    protected boolean messageCallback(final @NotNull String s) {
        final ProductDisplay shop = GetShop.get(this);
        final Player user = shop.getuser();
        if(user == null) return false;

        try {

            // Try to set the new amount
            return menu.attemptChangeAmount(user, Integer.parseInt(s));

        } catch(NumberFormatException e) {
            try {

                // Try to set the new amount
                return menu.attemptChangeAmount(user, Float.parseFloat(s));

            } catch(NumberFormatException e2) {
                return false;
            }
        }
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        final ProductDisplay shop = GetShop.get(this);
        getStyle(SimpleTextElmStyle.class).setText(textOverride != null ? textOverride : new Txt()
            .cat(new Txt("Amount: ").lightGray())
            .cat(new Txt(Utils.formatAmount(menu.getAmount())).white())
            .cat(new Txt(" / " + shop.getStock()).lightGray())
        .get());
        flushStyle();
    }
}
