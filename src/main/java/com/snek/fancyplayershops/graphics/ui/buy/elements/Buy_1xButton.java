package com.snek.fancyplayershops.graphics.ui.buy.elements;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.GetShop;
import com.snek.fancyplayershops.graphics.ui.buy.BuyCanvas;
import com.snek.fancyplayershops.graphics.ui.buy.styles.Buy_BuyButton_S;
import com.snek.fancyplayershops.graphics.ui.buy.styles.Buy_ConfirmButton_S;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ShopFancyTextElm;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.data_types.graphics.PolylineData;
import com.snek.frameworklib.graphics.core.Canvas;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.functional.elements.FancyButtonElm;
import com.snek.frameworklib.graphics.functional.elements.__base_ButtonElm;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.GeometryUtils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Buy_1xButton extends FancyButtonElm {
    private boolean active = true;


    private static final @NotNull List<@NotNull Vector2f> design0 = new ArrayList<>();
    static {
        for(int i = 0; i < 8; ++i) {
            design0.add(GeometryUtils.rotateVec2(new Vector2f(0, 0.5f), (float)Math.toRadians(45) * (i + 0.5f)).add(0.5f, 0.5f));
        }
    }
    private static final @NotNull PolylineData[] design = {
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.03f,
            design0.get(0),
            design0.get(1),
            design0.get(2),
            design0.get(3),
            design0.get(4),
            design0.get(5),
            design0.get(6),
            design0.get(7),
            design0.get(0)
        ),
        new PolylineData(
            Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
            Canvas.TOOLBAR_FG_WIDTH, 0.04f,
            new Vector2f(0.5f, 0.7f),
            new Vector2f(0.5f, 0.3f)
        )
    };




    public Buy_1xButton(final @NotNull Shop _shop) {
        super(_shop.getWorld(), null, "Buy 1 item", 1,  new Buy_BuyButton_S(_shop));

        // Create design
        final Div e = addChild(new PolylineSetElm(_shop.getWorld(), design));
        e.setSize(new Vector2f(ShopFancyTextElm.LINE_H / BuyCanvas.BUY_BUTTONS_W * Canvas.BOTTOM_ROW_CONTENT_SIZE, Canvas.BOTTOM_ROW_CONTENT_SIZE));
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

        // Play sound and buy items
        final Shop shop = GetShop.get(this);
        if(active) __base_ButtonElm.playButtonSound(player);
        if(player.getUUID().equals(shop.getOwnerUuid())) {
            shop.retrieveItem(player, 1, true);
        }
        else {
            shop.buyItem(player, 1, true);
        }
    }
}
