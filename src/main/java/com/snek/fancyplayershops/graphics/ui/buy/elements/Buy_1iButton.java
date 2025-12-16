package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.GetShop;
import com.snek.fancyplayershops.graphics.ui.buy.BuyCanvas;
import com.snek.fancyplayershops.graphics.ui.buy.styles.Buy_BuyButton_S;
import com.snek.fancyplayershops.graphics.ui.buy.styles.Buy_ConfirmButton_S;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.core.Canvas;
import com.snek.frameworklib.graphics.designs.ObjectDesigns;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.functional.elements.FancyButtonElm;
import com.snek.frameworklib.graphics.functional.elements.__base_ButtonElm;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;
import com.snek.frameworklib.utils.Easings;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Buy_1iButton extends FancyButtonElm {
    private boolean active = true;


    public Buy_1iButton(final @NotNull Shop _shop) {
        super(_shop.getLevel(), null, "Fill inventory", 1,  new Buy_BuyButton_S(_shop));

        // Create design
        final Div e = addChild(new PolylineSetElm(_shop.getLevel(), ObjectDesigns.MinecraftChest));
        e.setSize(new Vector2f(FancyPlayerShops.LINE_H / BuyCanvas.BUY_BUTTONS_W * Canvas.BOTTOM_ROW_CONTENT_SIZE, Canvas.BOTTOM_ROW_CONTENT_SIZE));
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
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        super.onClick(player, click);
        final Shop shop = GetShop.get(this);
        final int amount = Math.min(shop.getStock(), 64 * 9 * 4);
        final int oldStock = shop.getStock();

        if(player.getUUID().equals(shop.getOwnerUuid())) {
            shop.retrieveItem(player, amount, false);
        }
        else {
            shop.buyItem(player, amount, false);
        }
        if(shop.getStock() != oldStock) __base_ButtonElm.playButtonSound(player);
    }
}
