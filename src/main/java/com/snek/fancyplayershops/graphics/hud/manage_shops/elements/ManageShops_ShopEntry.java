package com.snek.fancyplayershops.graphics.hud.manage_shops.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.data.data_types.Shop;
import com.snek.fancyplayershops.graphics.ScrollableList;
import com.snek.fancyplayershops.graphics.hud.manage_shops.styles.ManageShops_ShopEntry_S;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.graphics.basic.elements.TextElm;
import com.snek.frameworklib.graphics.basic.presets.TextStyle_Small;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.functional.elements.ButtonElm;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.graphics.interfaces.Scrollable;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;




public class ManageShops_ShopEntry extends ButtonElm implements Scrollable {

    public static final float MARGIN_LEFT = 0.05f;
    public static final float BALANCE_WIDTH = 0.2f;
    public static final float NAME_WIDTH = 1f - MARGIN_LEFT - BALANCE_WIDTH;

    private final @Nullable Shop shopInstance;
    private final @NotNull ScrollableList parentList;




    //TODO update balance dynamically?
    public ManageShops_ShopEntry(final @NotNull HudContext context, final @NotNull Shop shopInstance, final @NotNull ScrollableList parentList) {
        super(context.getLevel(), "Claim balance", "Edit shop", 2, new ManageShops_ShopEntry_S());
        this.shopInstance = shopInstance;
        this.parentList = parentList;
        Div e;


        // Add shop name
        final int productNum = shopInstance.getDisplays().size();
        e = addChild(new TextElm(level, new TextStyle_Small()
            .withText(new Txt(shopInstance.getDisplayName()).cat("\n★★★☆☆ | " + productNum + " product" + (productNum > 1 ? "s" : "")).get()) //TODO make this dynamic and real
            .withTextAlignment(TextAlignment.LEFT)
            .withTextOverflowBehaviour(TextOverflowBehaviour.SCROLL)
        ));
        e.setSize(new Vector2f(NAME_WIDTH, 1));
        e.setPosX(-0.5f + NAME_WIDTH / 2 + MARGIN_LEFT);
        e.setAlignmentY(AlignmentY.CENTER);


        // Add shop balance
        e = addChild(new TextElm(level, new TextStyle_Small()
            .withText(new Txt(Utils.formatPriceShort(shopInstance.getBalance())).get())
            .withTextAlignment(TextAlignment.LEFT)
            .withTextOverflowBehaviour(TextOverflowBehaviour.OVERFLOW)
        ));
        e.setSize(new Vector2f(BALANCE_WIDTH, 1));
        e.setAlignment(AlignmentX.RIGHT, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        Clickable.playSound(player);

        // Claim balance (left click)
        if(click == ClickAction.PRIMARY) {
            shopInstance.claimBalance();
        }

        // Open edit shop canvas (right click)
        else {
            //TODO
        }
    }




    @Override
    public void onScroll(final @NotNull Player player, final float amount) {
        parentList.onScroll(player, amount);
    }
}
