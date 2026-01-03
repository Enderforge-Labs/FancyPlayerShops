package com.snek.fancyplayershops.graphics.hud.mainmenu.elements;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.data.data_types.Shop;
import com.snek.fancyplayershops.graphics.hud.mainmenu.styles.ManageShops_ShopEntry_S;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;




//TODO don't use an entity instead of making it invisible. To improve performance
public class ManageShops_ShopHeader extends SimpleButtonElm {
    private final List<Shop> shops;




    //TODO update dynamically?
    private long calcTotalBalance() {
        long r = 0;
        for(final Shop g : shops) {
            r += g.getBalance();
        }
        return r;
    }


    private int calcTotalItems() {
        int r = 0;
        for(final Shop g : shops) {
            r += g.getDisplays().size();
        }
        return r;
    }




    public ManageShops_ShopHeader(final @NotNull HudContext context, final @NotNull List<Shop> groups) {
        super(context.getLevel(), null, "Claim everything", 2, new ManageShops_ShopEntry_S());
        this.shops = groups;
        Div e;


        // Add shop group name header
        final int productNum = calcTotalItems();
        e = addChild(new SimpleTextElm(level, new Txt("" + productNum + " product" + (productNum > 1 ? "s" : "")).get(), TextAlignment.LEFT, TextOverflowBehaviour.SCROLL));
        e.setSize(new Vector2f(ManageShops_ShopEntry.NAME_WIDTH, 1));
        e.setPosX(-0.5f + ManageShops_ShopEntry.NAME_WIDTH / 2 + ManageShops_ShopEntry.MARGIN_LEFT);
        e.setAlignmentY(AlignmentY.CENTER);
        ((SimpleTextElm)e).getStyle(SimpleTextElmStyle.class).setTransform(new Transform().scale(SimpleTextElmStyle.DEFAULT_TEXT_SCALE / 2f)); //TODO move to configurable style


        // Add shop group balance header
        e = addChild(new SimpleTextElm(level, new Txt(Utils.formatPriceShort(calcTotalBalance())).get(), TextAlignment.LEFT, TextOverflowBehaviour.OVERFLOW));
        e.setSize(new Vector2f(ManageShops_ShopEntry.BALANCE_WIDTH, 1));
        e.setAlignment(AlignmentX.RIGHT, AlignmentY.CENTER);
        ((SimpleTextElm)e).getStyle(SimpleTextElmStyle.class).setTransform(new Transform().scale(SimpleTextElmStyle.DEFAULT_TEXT_SCALE / 2f)); //TODO move to configurable style
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
