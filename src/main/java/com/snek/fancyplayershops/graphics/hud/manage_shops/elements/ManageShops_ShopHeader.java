package com.snek.fancyplayershops.graphics.hud.manage_shops.elements;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.data.data_types.Shop;
import com.snek.fancyplayershops.graphics.hud.manage_shops.styles.ManageShops_ShopEntry_S;
import com.snek.fancyplayershops.graphics.misc.styles.TextStyle_Small;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.graphics.basic.elements.TextElm;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.functional.elements.ButtonElm;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;




public class ManageShops_ShopHeader extends ButtonElm {
    private final List<Shop> shops;




    //TODO update dynamically?
    private long calcTotalBalance() {
        long r = 0;
        for(final Shop s : shops) {
            r += s.getBalance();
        }
        return r;
    }


    private int calcTotalItems() {
        int r = 0;
        for(final Shop s : shops) {
            r += s.getDisplays().size();
        }
        return r;
    }




    public ManageShops_ShopHeader(final @NotNull HudContext context, final @NotNull List<Shop> shops) {
        super(context.getLevel(), null, "Claim everything", 2, new ManageShops_ShopEntry_S());
        this.shops = shops;
        Div e;


        // Add shop name header
        final int productNum = calcTotalItems();
        e = addChild(new TextElm(level, new TextStyle_Small()
            .withText(new Txt("" + productNum + " product" + (productNum > 1 ? "s" : "")).get())
            .withTextAlignment(TextAlignment.LEFT)
            .withTextOverflowBehaviour(TextOverflowBehaviour.SCROLL)
        ));
        e.setSize(new Vector2f(ManageShops_ShopEntry.NAME_WIDTH, 1));
        e.setPosX(-0.5f + ManageShops_ShopEntry.NAME_WIDTH / 2 + ManageShops_ShopEntry.MARGIN_LEFT);
        e.setAlignmentY(AlignmentY.CENTER);


        // Add shop balance header
        e = addChild(new TextElm(level, new TextStyle_Small()
            .withText(new Txt(Utils.formatPriceShort(calcTotalBalance())).get())
            .withTextAlignment(TextAlignment.LEFT)
            .withTextOverflowBehaviour(TextOverflowBehaviour.SCROLL)
        ));
        e.setSize(new Vector2f(ManageShops_ShopEntry.BALANCE_WIDTH, 1));
        e.setAlignment(AlignmentX.RIGHT, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        Clickable.playSound(player);

        // Claim all shops
        for(final Shop s : shops) {
            s.claimBalance();
        }
    }
}
