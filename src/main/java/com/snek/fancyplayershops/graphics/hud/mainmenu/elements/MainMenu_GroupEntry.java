package com.snek.fancyplayershops.graphics.hud.mainmenu.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.data.data_types.ShopGroup;
import com.snek.fancyplayershops.graphics.ScrollableList;
import com.snek.fancyplayershops.graphics.hud.mainmenu.styles.MainMenu_GroupEntry_S;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;
import com.snek.frameworklib.graphics.interfaces.Scrollable;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;




//TODO don't use an entity instead of making it invisible. To improve performance
public class MainMenu_GroupEntry extends SimpleButtonElm implements Scrollable {

    public static final float BALANCE_WIDTH = 0.2f;
    public static final float NAME_WIDTH = 1f - BALANCE_WIDTH;

    protected final @Nullable ShopGroup groupInstance;




    //TODO update balance dynamically?
    public MainMenu_GroupEntry(final @NotNull HudContext context, final @Nullable ShopGroup groupInstance) {
        super(context.getLevel(), "Claim balance", "Edit group", 2, new MainMenu_GroupEntry_S());
        this.groupInstance = groupInstance;
        Div e;


        // Add shop group name header
        e = addChild(new SimpleTextElm(level, new Txt(groupInstance.getDisplayName()).get(), TextAlignment.LEFT, TextOverflowBehaviour.SCROLL));
        e.setSize(new Vector2f(NAME_WIDTH, 1));
        e.setAlignment(AlignmentX.LEFT, AlignmentY.CENTER);


        // Add shop group balance header
        e = addChild(new SimpleTextElm(level, new Txt(Utils.formatPriceShort(groupInstance.getBalance())).get(), TextAlignment.LEFT, TextOverflowBehaviour.OVERFLOW));
        e.setSize(new Vector2f(BALANCE_WIDTH, 1));
        e.setAlignment(AlignmentX.RIGHT, AlignmentY.CENTER);
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        super.onClick(player, click);
        //TODO
    }




    @Override
    public void onScroll(final @NotNull Player player, final float amount) {
        ((ScrollableList)getParent().getParent()).onScroll(player, amount);
    }
}
