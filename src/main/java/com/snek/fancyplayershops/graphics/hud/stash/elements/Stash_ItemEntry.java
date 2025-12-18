package com.snek.fancyplayershops.graphics.hud.stash.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.ScrollableList;
import com.snek.fancyplayershops.graphics.hud.stash.styles.Stash_ItemEntry_Count_S;
import com.snek.fancyplayershops.graphics.hud.stash.styles.Stash_ItemEntry_Name_S;
import com.snek.fancyplayershops.graphics.hud.stash.styles.Stash_ItemEntry_S;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.functional.elements.SimpleButtonElm;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.graphics.interfaces.Scrollable;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;








public class Stash_ItemEntry extends SimpleButtonElm implements Scrollable {
    public static final float MARGIN_LEFT        = 0.05f;
    public static final float ITEM_WIDTH         = 0.1f;
    public static final float ITEM_NAME_SPACING  = 0.02f;




    public Stash_ItemEntry(final @NotNull HudContext context, final @NotNull ItemStack item, final @NotNull int count) {
        super(context.getLevel(), null, "Collect this item", 2, new Stash_ItemEntry_S());
        Div e;


        // Add item display
        e = addChild(new Stash_ItemDisplay(context, item));
        e.setSize(new Vector2f(ITEM_WIDTH - ITEM_NAME_SPACING, 0.65f));
        e.setPosX(-0.5f + ITEM_WIDTH / 2 + MARGIN_LEFT);
        e.setAlignmentY(AlignmentY.CENTER);


        final Div c = addChild(new Div());
        c.setSize(new Vector2f(1f - ITEM_WIDTH - ITEM_NAME_SPACING - MARGIN_LEFT, 1f));
        c.setAlignment(AlignmentX.RIGHT, AlignmentY.CENTER);

        // Add item name display
        e = c.addChild(new SimpleTextElm(context.getLevel(), new Stash_ItemEntry_Name_S()));
        e.setSize(new Vector2f(1f, 0.5f));
        e.setAlignmentY(AlignmentY.TOP);
        ((SimpleTextElm)e).getStyle(SimpleTextElmStyle.class).setText(new Txt(MinecraftUtils.getFancyItemName(item).getString()).white().get());


        // Add item count display
        e = c.addChild(new SimpleTextElm(context.getLevel(), new Stash_ItemEntry_Count_S()));
        e.setSize(new Vector2f(1f, 0.5f));
        e.setAlignmentY(AlignmentY.BOTTOM);
        ((SimpleTextElm)e).getStyle(SimpleTextElmStyle.class).setText(new Txt("\n" + Utils.formatAmount(count)).lightGray().get());
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click) {
        super.onClick(player, click);
        Clickable.playSound(player);
        //TODO
    }




    @Override
    public void onScroll(final @NotNull Player player, final float amount) {
        ((ScrollableList)(getParent().getParent())).onScroll(player, amount);
    }
}

//TODO add header
//TODO add buttons to collect all the items, drop all the items or clear the stash