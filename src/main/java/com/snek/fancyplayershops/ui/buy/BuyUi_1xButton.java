package com.snek.fancyplayershops.ui.buy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.buy.styles.BuyUi_BuyButton_S;
import com.snek.fancyplayershops.ui.edit.EditUi;
import com.snek.fancyplayershops.ui.misc.ShopButton;
import com.snek.fancyplayershops.ui.misc.ShopFancyTextElm;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.composite.PolylineData;
import com.snek.framework.ui.composite.PolylineSetElm;
import com.snek.framework.ui.elements.styles.FancyTextElmStyle;
import com.snek.framework.utils.SpaceUtils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class BuyUi_1xButton extends ShopButton {
    private static final @NotNull PolylineData[] design = new PolylineData[] {
        new PolylineData(
            EditUi.TOOLBAR_FG_COLOR, EditUi.TOOLBAR_FG_ALPHA,
            EditUi.TOOLBAR_FG_WIDTH, 0.04f,
            new Vector2f(0.0f, 0.0f),
            new Vector2f(0.3f, 0.4f),
            new Vector2f(0.7f, 0.4f),
            new Vector2f(1.0f, 0.9f).sub(0.02f, 0.05f)
        ),
        new PolylineData(
            EditUi.TOOLBAR_FG_COLOR, EditUi.TOOLBAR_FG_ALPHA,
            EditUi.TOOLBAR_FG_WIDTH, 0.06f,
            SpaceUtils.rotateVec2(new Vector2f(-0.2f, -0.0f), (float)Math.toRadians(15)).add(1, 0.9f),
            SpaceUtils.rotateVec2(new Vector2f(+0.0f, -0.0f), (float)Math.toRadians(15)).add(1, 0.9f),
            SpaceUtils.rotateVec2(new Vector2f(+0.0f, -0.2f), (float)Math.toRadians(15)).add(1, 0.9f)
        )
    };




    public BuyUi_1xButton(final @NotNull Shop _shop){
        super(_shop, null, "Buy 1 item", 1,  new BuyUi_BuyButton_S(_shop));

        // Create design
        final Div e = addChild(new PolylineSetElm(_shop.getWorld(), design));
        e.setSize(new Vector2f(ShopFancyTextElm.LINE_H / BuyUi.BUY_BUTTONS_W * EditUi.BOTTOM_ROW_CONTENT_SIZE, EditUi.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        // Empty
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        if(player.getUUID().equals(shop.getOwnerUuid())) {
            shop.retrieveItem(player, 1);
        }
        else {
            shop.buyItem(player, 1);
        }
    }
}
