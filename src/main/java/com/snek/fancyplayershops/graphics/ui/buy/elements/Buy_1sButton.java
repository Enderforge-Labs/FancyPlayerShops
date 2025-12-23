package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.GetShop;
import com.snek.fancyplayershops.graphics.ui.buy.BuyCanvas;
import com.snek.fancyplayershops.graphics.ui.buy.styles.Buy_BuyButton_S;
import com.snek.fancyplayershops.graphics.ui.buy.styles.Buy_ConfirmButton_S;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.designs.ItemDesigns;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.functional.elements.FancyButtonElm;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;
import com.snek.frameworklib.utils.Easings;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Buy_1sButton extends FancyButtonElm {
    private boolean active = true;


    public Buy_1sButton(final @NotNull ProductDisplay _shop) {
        super(_shop.getLevel(), null, "Buy 64 items", 1,  new Buy_BuyButton_S(_shop));

        // Create design
        final Div e = addChild(new PolylineSetElm(_shop.getLevel(), ItemDesigns.CoinPile));
        e.setSize(new Vector2f(FancyPlayerShops.LINE_H / BuyCanvas.BUY_BUTTONS_W * FancyPlayerShops.BOTTOM_ROW_CONTENT_SIZE, FancyPlayerShops.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        // Empty
    }


    public void updateColor(final boolean _active) {
        if(active == _active) return;
        active = _active;
        final Buy_BuyButton_S s = getStyle(Buy_BuyButton_S.class);
        s.setDefaultColor(active ? Buy_ConfirmButton_S.BASE_COLOR : Buy_ConfirmButton_S.BASE_COLOR_INACTIVE);
        applyAnimation(new Transition(4, Easings.expOut).targetBgColor(s.getDefaultBgColor()));
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);

        // Play sound and buy items
        final ProductDisplay shop = GetShop.get(this);
        if(active) Clickable.playSound(player);
        if(player.getUUID().equals(shop.getOwnerUuid())) {
            shop.retrieveItem(player, 64, true);
        }
        else {
            shop.buyItem(player, 64, true);
        }
    }
}
