package com.snek.fancyplayershops.graphics.ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.snek.fancyplayershops.GetShop;
import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.graphics.ui.edit.EditCanvas;
import com.snek.fancyplayershops.graphics.ui.edit.styles.Edit_Input_S;
import com.snek.fancyplayershops.graphics.ui.misc.styles.ShopTextInput_S;
import com.snek.frameworklib.graphics.functional.elements.TextInputElm;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;








/**
 * A button that allows the owner of the shop to specify the shop's group.
 */
public class Edit_GroupInput extends TextInputElm {




    /**
     * Creates a new Edit_GroupInput.
     * @param _shop The target shop.
     */
    public Edit_GroupInput(final @NotNull ProductDisplay _shop) {
        super(
            _shop.getLevel(),
            null, "Transfer to another shop",
            new Txt("Send the name of the shop in chat!").color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR).bold().get(),
            new Edit_Input_S(_shop)
        );
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
            .cat("Shop: ")
            .cat(shop.getShop().getDisplayName())
            //TODO convert component to string before using it
        .white().get());
        flushStyle();
    }




    @Override
    protected boolean messageCallback(final @NotNull String s) {
        final ServerPlayer player = (ServerPlayer)canvas.getContext().getPlayer();
        final char c = s.charAt(0);
        if(c == '.' || c == ' ' || c == ',') {
            player.displayClientMessage(new Txt("Shop names can't start with \"" + c + "\"!").red().bold().get(), true);
            return false;
        }
        else if(Character.isDigit(c)) {
            player.displayClientMessage(new Txt("Shop names can't start with a number!").red().bold().get(), true);
            return false;
        }
        else {
            final ProductDisplay shop = GetShop.get(this);
            shop.changeGroup(s, player);
            return true;
        }
    }




    @Override
    public float getInteractionSizeRight() {
        return super.getInteractionSizeRight() - EditCanvas.COLOR_SELECTOR_W;
    }
}