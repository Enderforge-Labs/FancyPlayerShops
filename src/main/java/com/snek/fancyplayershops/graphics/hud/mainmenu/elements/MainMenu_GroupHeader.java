package com.snek.fancyplayershops.graphics.hud.mainmenu.elements;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.data.data_types.ShopGroup;
import com.snek.fancyplayershops.graphics.hud.mainmenu.styles.MainMenu_GroupEntry_S;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;
import com.snek.frameworklib.graphics.functional.elements.__base_ButtonElm;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;




//TODO don't use an entity instead of making it invisible. To improve performance
public class MainMenu_GroupHeader extends SimpleButtonElm {




    //TODO update dynamically?
    private static long calcTotalBalance(final @NotNull List<ShopGroup> groups) {
        long r = 0;
        for(final ShopGroup g : groups) {
            r += g.getBalance();
        }
        return r;
    }
    private static int calcTotalItems(final @NotNull List<ShopGroup> groups) {
        int r = 0;
        for(final ShopGroup g : groups) {
            r += g.getShops().size();
        }
        return r;
    }
    public MainMenu_GroupHeader(final @NotNull HudContext context, final @NotNull List<ShopGroup> groups) {
        super(context.getLevel(), null, "Claim everything", 2, new MainMenu_GroupEntry_S());
        Div e;


        // Add shop group name header
        e = addChild(new SimpleTextElm(level, new Txt("Selling " + calcTotalItems(groups) + " items").get(), TextAlignment.LEFT, TextOverflowBehaviour.SCROLL));
        e.setSize(new Vector2f(MainMenu_GroupEntry.NAME_WIDTH, 1));
        e.setPosX(-0.5f + MainMenu_GroupEntry.NAME_WIDTH / 2 + MainMenu_GroupEntry.MARGIN_LEFT);
        e.setAlignmentY(AlignmentY.CENTER);
        ((SimpleTextElm)e).getStyle(SimpleTextElmStyle.class).setTransform(new Transform().scale(SimpleTextElmStyle.DEFAULT_TEXT_SCALE / 2f)); //TODO move to configurable style


        // Add shop group balance header
        e = addChild(new SimpleTextElm(level, new Txt(Utils.formatPriceShort(calcTotalBalance(groups))).get(), TextAlignment.LEFT, TextOverflowBehaviour.OVERFLOW));
        e.setSize(new Vector2f(MainMenu_GroupEntry.BALANCE_WIDTH, 1));
        e.setAlignment(AlignmentX.RIGHT, AlignmentY.CENTER);
        ((SimpleTextElm)e).getStyle(SimpleTextElmStyle.class).setTransform(new Transform().scale(SimpleTextElmStyle.DEFAULT_TEXT_SCALE / 2f)); //TODO move to configurable style
    }




    @Override
    public void onClick(@NotNull Player player, @NotNull ClickAction click) {
        super.onClick(player, click);
        __base_ButtonElm.playButtonSound(player);
        if(click == ClickAction.PRIMARY) {
            //TODO
        }
        else {
            //TODO
        }
    }
}
