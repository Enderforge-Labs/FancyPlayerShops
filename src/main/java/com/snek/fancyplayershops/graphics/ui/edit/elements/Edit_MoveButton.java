package com.snek.fancyplayershops.graphics.ui.edit.elements;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.graphics.ui.edit.styles.Edit_SquareButton_S;
import com.snek.fancyplayershops.graphics.ui.misc.elements.SimpleShopButton;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.data_types.graphics.PolylineData;
import com.snek.frameworklib.graphics.core.Canvas;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.composite.elements.PolylineSetElm;
import com.snek.frameworklib.utils.GeometryUtils;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class Edit_MoveButton extends SimpleShopButton {
    private static final @NotNull List<PolylineData> design = new ArrayList<>();
    static {
        for(int i = 0; i < 4; ++i) {
            design.add(new PolylineData(
                Canvas.TOOLBAR_FG_COLOR, Canvas.TOOLBAR_FG_ALPHA,
                Canvas.TOOLBAR_FG_WIDTH, 0.06f,
                GeometryUtils.rotateVec2(new Vector2f(-0.15f, 0.4f), (float)Math.toRadians(90) * i).add(0.5f, 0.5f),
                GeometryUtils.rotateVec2(new Vector2f(+0.0f,  0.5f), (float)Math.toRadians(90) * i).add(0.5f, 0.5f),
                GeometryUtils.rotateVec2(new Vector2f(+0.15f, 0.4f), (float)Math.toRadians(90) * i).add(0.5f, 0.5f)
            ));
        }
    }




    public Edit_MoveButton(final @NotNull Shop _shop) {
        super(_shop, null, "Move shop", 1,  new Edit_SquareButton_S(_shop));

        // Create design
        final Div e = addChild(new PolylineSetElm(_shop.getWorld(), design.get(0), design.get(1), design.get(2), design.get(3)));
        e.setSize(new Vector2f(Canvas.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        super.onClick(player, click);


        // Pick up and delete shop
        shop.pickUp(true);
        shop.delete();


        // Send feedback message to the player
        player.displayClientMessage(new Txt()
            .cat(new Txt("Your " + shop.getDecoratedName() + " has been converted into an item."))
            .color(ShopManager.SHOP_ITEM_NAME_COLOR)
        .get(), false);
    }
}
