package com.snek.fancyplayershops.graphics.ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.buy.BuyUi;
import com.snek.fancyplayershops.graphics.ui.buy.styles.BuyUi_BuyButton_S;
import com.snek.fancyplayershops.graphics.ui.buy.styles.BuyUi_ConfirmButton_S;
import com.snek.fancyplayershops.graphics.ui.misc.elements.FancyShopButton;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ShopFancyTextElm;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.ui.AlignmentX;
import com.snek.frameworklib.data_types.ui.AlignmentY;
import com.snek.frameworklib.data_types.ui.PolylineData;
import com.snek.frameworklib.graphics.core.Canvas;
import com.snek.frameworklib.graphics.core.Div;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;
import com.snek.frameworklib.utils.Easings;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class BuyUi_1sButton extends FancyShopButton {
    private boolean active = true;


    private static final @NotNull PolylineData[] design = new PolylineData[] {
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.07f,
            new Vector2f(0.8f, 0.3f),
            new Vector2f(0.8f, 0.0f),
            new Vector2f(0.0f, 0.0f),
            new Vector2f(0.0f, 0.3f),
            new Vector2f(1.0f, 0.3f)
        ),
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.07f,
            new Vector2f(0.8f, 0.6f),
            new Vector2f(0.8f, 0.9f),
            new Vector2f(0.0f, 0.9f),
            new Vector2f(0.0f, 0.6f),
            new Vector2f(1.0f, 0.6f)
        ),
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.07f,
            new Vector2f(0.2f, 0.3f),
            new Vector2f(0.2f, 0.6f)
        ),
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.07f,
            new Vector2f(1.0f, 0.6f),
            new Vector2f(1.0f, 0.3f)
        )
    };




    public BuyUi_1sButton(final @NotNull Shop _shop) {
        super(_shop, null, "Buy 64 items", 1,  new BuyUi_BuyButton_S(_shop));

        // Create design
        final Div e = addChild(new PolylineSetElm(_shop.getWorld(), design));
        e.setSize(new Vector2f(ShopFancyTextElm.LINE_H / BuyUi.BUY_BUTTONS_W * Canvas.BOTTOM_ROW_CONTENT_SIZE, Canvas.BOTTOM_ROW_CONTENT_SIZE));
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
        super.onClick(player, click);
        if(active) playButtonSound(player);
        if(player.getUUID().equals(shop.getOwnerUuid())) {
            shop.retrieveItem(player, 64, true);
        }
        else {
            shop.buyItem(player, 64, true);
        }
    }
}
