package com.snek.fancyplayershops.shop_ui.edit.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.shop_ui.edit.styles.EditUi_SquareButton_S;
import com.snek.fancyplayershops.shop_ui.misc.elements.SimpleShopButton;
import com.snek.fancyplayershops.ui._elements.UiCanvas;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.data_types.ui.PolylineData;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.composite.elements.PolylineSetElm;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class EditUi_OpenBuyMenuButton extends SimpleShopButton {
    private static final @NotNull PolylineData[] design = new PolylineData[] {
        new PolylineData(
            UiCanvas.TOOLBAR_FG_COLOR, UiCanvas.TOOLBAR_FG_ALPHA,
            UiCanvas.TOOLBAR_FG_WIDTH, 0.03f,
            new Vector2f(1.0f, 0.6f),
            new Vector2f(0.4f, 0.0f),
            new Vector2f(0.0f, 0.0f),
            new Vector2f(0.0f, 0.4f),
            new Vector2f(0.6f, 1.0f),
            new Vector2f(1.0f, 0.6f)
        )
    };




    public EditUi_OpenBuyMenuButton(final @NotNull Shop _shop) {
        super(_shop, null, "Open buy menu", 1, new EditUi_SquareButton_S(_shop));

        // Create design
        final Div e = addChild(new PolylineSetElm(_shop.getWorld(), design));
        e.setSize(new Vector2f(UiCanvas.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        shop.openBuyUi(player, false);
    }
}
