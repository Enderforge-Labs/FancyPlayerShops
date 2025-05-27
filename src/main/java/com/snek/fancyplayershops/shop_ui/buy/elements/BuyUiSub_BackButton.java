package com.snek.fancyplayershops.shop_ui.buy.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.shop_ui.buy.BuyUi;
import com.snek.fancyplayershops.shop_ui.edit.styles.EditUi_SquareButton_S;
import com.snek.fancyplayershops.shop_ui.misc.elements.ShopButton;
import com.snek.fancyplayershops.ui._elements.UiCanvas;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.composite.elements.PolylineData;
import com.snek.framework.ui.composite.elements.PolylineSetElm;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class BuyUiSub_BackButton extends ShopButton {
    private int amountCache = 1;
    public void setAmountCache(final int _amountCache) { amountCache = _amountCache; }

    private static final @NotNull PolylineData[] design = new PolylineData[] {
        new PolylineData(
            UiCanvas.TOOLBAR_FG_COLOR, UiCanvas.TOOLBAR_FG_ALPHA,
            UiCanvas.TOOLBAR_FG_WIDTH, 0.06f,
            new Vector2f(0.6f,  0.3f),
            new Vector2f(1.0f,  0.3f),
            new Vector2f(1.0f,  0.7f),
            new Vector2f(0.05f, 0.7f)
        ),
        new PolylineData(
            UiCanvas.TOOLBAR_FG_COLOR, UiCanvas.TOOLBAR_FG_ALPHA,
            UiCanvas.TOOLBAR_FG_WIDTH, 0.06f,
            new Vector2f(0.2f, 0.9f),
            new Vector2f(0.0f, 0.7f),
            new Vector2f(0.2f, 0.5f)
        )
    };




    public BuyUiSub_BackButton(final @NotNull Shop _shop) {
        super(_shop, null, "Go back", 1,  new EditUi_SquareButton_S(_shop));

        // Create design
        final Div e = addChild(new PolylineSetElm(_shop.getWorld(), design));
        e.setSize(new Vector2f(UiCanvas.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }


    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        // Empty
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        final BuyUi ui = new BuyUi(shop);
        shop.changeCanvas(ui);
        ui.changeAmount(amountCache);
    }
}
