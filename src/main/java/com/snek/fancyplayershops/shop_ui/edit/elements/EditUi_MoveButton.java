package com.snek.fancyplayershops.shop_ui.edit.elements;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.shop_ui.edit.EditUi;
import com.snek.fancyplayershops.shop_ui.edit.styles.EditUi_SquareButton_S;
import com.snek.fancyplayershops.shop_ui.misc.elements.ShopButton;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.composite.PolylineData;
import com.snek.framework.ui.composite.PolylineSetElm;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.SpaceUtils;
import com.snek.framework.utils.Txt;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








public class EditUi_MoveButton extends ShopButton {
    private static final @NotNull List<@NotNull PolylineData> design = new ArrayList<>();
    static {
        for(int i = 0; i < 4; ++i) {
            design.add(new PolylineData(
                EditUi.TOOLBAR_FG_COLOR, EditUi.TOOLBAR_FG_ALPHA,
                EditUi.TOOLBAR_FG_WIDTH, 0.06f,
                SpaceUtils.rotateVec2(new Vector2f(-0.15f, 0.4f), (float)Math.toRadians(90) * i).add(0.5f, 0.5f),
                SpaceUtils.rotateVec2(new Vector2f(+0.0f,  0.5f), (float)Math.toRadians(90) * i).add(0.5f, 0.5f),
                SpaceUtils.rotateVec2(new Vector2f(+0.15f, 0.4f), (float)Math.toRadians(90) * i).add(0.5f, 0.5f)
            ));
        }
    }




    public EditUi_MoveButton(final @NotNull Shop _shop) {
        super(_shop, null, "Move shop", 1,  new EditUi_SquareButton_S(_shop));

        // Create design
        final Div e = addChild(new PolylineSetElm(_shop.getWorld(), design.get(0), design.get(1), design.get(2), design.get(3)));
        e.setSize(new Vector2f(EditUi.BOTTOM_ROW_CONTENT_SIZE));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
    }




    @Override
    public void updateDisplay(final @Nullable Component textOverride) {
        // Empty
    }


    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        final boolean giveResult = MinecraftUtils.attemptGive(player, ShopManager.createShopSnapshot(shop));
        if(!giveResult) {
            player.displayClientMessage(new Txt("Cannot move the shop! Your inventory is full.").red().bold().get(), false);
        }
        else {

            // Send feedback message to the player
            player.displayClientMessage(new Txt()
                .cat(new Txt("Your " + shop.getDecoratedName() + " has been converted into an item."))
            .color(ShopManager.SHOP_ITEM_NAME_COLOR).get(), false);


            // Delete shop
            shop.delete();
        }
    }
}
