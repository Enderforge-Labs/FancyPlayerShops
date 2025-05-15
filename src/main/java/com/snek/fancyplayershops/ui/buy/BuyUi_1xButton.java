package com.snek.fancyplayershops.ui.buy;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.ui.buy.styles.BuyUi_BuyButton_S;
import com.snek.fancyplayershops.ui.buy.styles.BuyUi_ConfirmButton_S;
import com.snek.fancyplayershops.ui.edit.EditUi;
import com.snek.fancyplayershops.ui.misc.ShopButton;
import com.snek.fancyplayershops.ui.misc.ShopFancyTextElm;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.composite.PolylineData;
import com.snek.framework.ui.composite.PolylineSetElm;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.SpaceUtils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class BuyUi_1xButton extends ShopButton {
    private boolean active = true;


    private static final @NotNull List<@NotNull Vector2f> design0 = new ArrayList<>();
    static {
        for(int i = 0; i < 8; ++i) {
            design0.add(SpaceUtils.rotateVec2(new Vector2f(0, 0.5f), (float)Math.toRadians(45) * (i + 0.5f)).add(0.5f, 0.5f));
        }
    }
    private static final @NotNull PolylineData[] design = new PolylineData[] {
        new PolylineData(
            EditUi.TOOLBAR_FG_COLOR, EditUi.TOOLBAR_FG_ALPHA,
            EditUi.TOOLBAR_FG_WIDTH, 0.03f,
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
            EditUi.TOOLBAR_FG_COLOR, EditUi.TOOLBAR_FG_ALPHA,
            EditUi.TOOLBAR_FG_WIDTH, 0.04f,
            new Vector2f(0.5f, 0.7f),
            new Vector2f(0.5f, 0.3f)
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


    public void updateColor(final boolean _active) {
        if(active == _active) return;
        active = _active;
        final BuyUi_BuyButton_S s = getStyle(BuyUi_BuyButton_S.class);
        s.setDefaultColor(active ? BuyUi_ConfirmButton_S.BASE_COLOR : BuyUi_ConfirmButton_S.BASE_COLOR_INACTIVE);
        applyAnimation(new Transition(4, Easings.expOut).targetBgColor(s.getDefaultBgColor()));
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
