package com.snek.fancyplayershops.shop_ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.shop_ui.buy.BuyUi;
import com.snek.fancyplayershops.shop_ui.buy.styles.BuyUi_BuyButton_S;
import com.snek.fancyplayershops.shop_ui.buy.styles.BuyUi_ConfirmButton_S;
import com.snek.fancyplayershops.shop_ui.edit.EditUi;
import com.snek.fancyplayershops.shop_ui.misc.elements.ShopButton;
import com.snek.fancyplayershops.shop_ui.misc.elements.ShopFancyTextElm;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.composite.PolylineData;
import com.snek.framework.ui.composite.PolylineSetElm;
import com.snek.framework.utils.Easings;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class BuyUi_1iButton extends ShopButton {
    private boolean active = true;


    private static final @NotNull PolylineData[] design = new PolylineData[] {
        new PolylineData(
            EditUi.TOOLBAR_FG_COLOR, EditUi.TOOLBAR_FG_ALPHA,
            EditUi.TOOLBAR_FG_WIDTH, 0.07f,
            new Vector2f(0.0f, 0.0f),
            new Vector2f(0.0f, 1.0f),
            new Vector2f(1.0f, 1.0f),
            new Vector2f(1.0f, 0.0f),
            new Vector2f(0.0f, 0.0f)
        ),
        new PolylineData(
            EditUi.TOOLBAR_FG_COLOR, EditUi.TOOLBAR_FG_ALPHA,
            EditUi.TOOLBAR_FG_WIDTH, 0.06f,
            new Vector2f(0.0f, 0.66f),
            new Vector2f(1.0f, 0.66f)
        ),
        new PolylineData(
            EditUi.TOOLBAR_FG_COLOR, EditUi.TOOLBAR_FG_ALPHA,
            EditUi.TOOLBAR_FG_WIDTH, 0.06f,
            new Vector2f(0.5f, 0.76f),
            new Vector2f(0.5f, 0.46f)
        )
    };




    public BuyUi_1iButton(final @NotNull Shop _shop){
        super(_shop, null, "Fill inventory", 1,  new BuyUi_BuyButton_S(_shop));

        // Create design
        final Div e = addChild(new PolylineSetElm(_shop.getWorld(), design));
        e.setSize(new Vector2f(ShopFancyTextElm.LINE_H / BuyUi.BUY_BUTTONS_W * EditUi.BOTTOM_ROW_CONTENT_SIZE, EditUi.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        // Empty
    }


    public void updateColor(final boolean _active) {
        if(active == _active) return;
        active = _active;
        final BuyUi_BuyButton_S s = getStyle(BuyUi_BuyButton_S.class);
        s.setDefaultColor(active ? BuyUi_ConfirmButton_S.BASE_COLOR : BuyUi_ConfirmButton_S.BASE_COLOR_INACTIVE);
        applyAnimation(new Transition(4, Easings.expOut).targetBgColor(s.getDefaultBgColor()));
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        final int amount = Math.min(shop.getStock(), 64 * 9 * 4);
        final int oldStock = shop.getStock();

        if(player.getUUID().equals(shop.getOwnerUuid())) {
            shop.retrieveItem(player, amount, false);
        }
        else {
            shop.buyItem(player, amount, false);
        }
        if(shop.getStock() != oldStock) playButtonSound(player);
    }
}
