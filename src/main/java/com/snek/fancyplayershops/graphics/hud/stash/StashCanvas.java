package com.snek.fancyplayershops.graphics.hud.stash;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.data.StashManager;
import com.snek.fancyplayershops.data.data_types.PlayerStash;
import com.snek.fancyplayershops.data.data_types.StashEntry;
import com.snek.fancyplayershops.graphics.ScrollableList;
import com.snek.fancyplayershops.graphics.hud.core.styles.HudCanvasBack_S;
import com.snek.fancyplayershops.graphics.hud.core.styles.HudCanvasBackground_S;
import com.snek.fancyplayershops.graphics.hud.stash.elements.Stash_ItemDisplay;
import com.snek.fancyplayershops.graphics.hud.stash.elements.Stash_ItemNameCount;
import com.snek.fancyplayershops.graphics.hud.stash.elements.Stash_Sub_BackButton;
import com.snek.fancyplayershops.graphics.hud.stash.styles.Stash_EmptyText_S;
import com.snek.fancyplayershops.graphics.misc.elements.TitleElm;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ShopFancyTextElm;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.core.HudCanvas;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;








public class StashCanvas extends HudCanvas {
    public static final float LIST_WIDTH         = 0.95f;
    public static final float ITEM_NAME_RATIO    = 0.1f;
    public static final float ITEM_NAME_SPACING  = 0.02f;

    public static final float LIST_H             = 1f - ShopFancyTextElm.LINE_H - SQUARE_BUTTON_SIZE;
    public static final int   LIST_SIZE          = 7;

    private ScrollableList list;




    public StashCanvas(final @NotNull HudContext context) {
        super(context, 1f, ShopFancyTextElm.LINE_H, SQUARE_BUTTON_SIZE, new HudCanvasBackground_S(), new HudCanvasBack_S());
        final ServerPlayer player = (ServerPlayer)context.getPlayer();
        final ServerLevel  world  = (ServerLevel)player.level();
        Div e;

        // Add title
        e = bg.addChild(new TitleElm(world, new Txt("Your stash").white().bold().get()));
        e.setSize(new Vector2f(TitleElm.DEFAULT_W, ShopFancyTextElm.LINE_H));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);


        // Add "empty stash" text if the stash is empty
        final PlayerStash stash = StashManager.getStash(player);
        if(stash == null) {
            e = bg.addChild(new SimpleTextElm(world, new Stash_EmptyText_S()));
            e.setSize(new Vector2f(1f, ShopFancyTextElm.LINE_H));
            e.setAlignmentX(AlignmentX.CENTER);
            e.setPosY(1f - ShopFancyTextElm.LINE_H * 2f);
        }


        // If materials are present
        else {

            // Create scrollable list
            final float list_elm_h = 1f / LIST_SIZE;
            list = (ScrollableList)bg.addChild(new ScrollableList(world, list_elm_h));
            list.setSize(new Vector2f(LIST_WIDTH, LIST_H));
            list.setAlignmentX(AlignmentX.RIGHT);
            list.setPosY(SQUARE_BUTTON_SIZE);


            //FIXME update stash view when the stash is changed
            // For each material
            final List<StashEntry> entries = new ArrayList<>(stash.values());
            for(int i = 0; i < entries.size(); ++i) {
                final StashEntry entry = entries.get(i);

                // Add container for the stash entry
                final Div c = list.storeElm(new Div());
                c.setSize(new Vector2f(1f, list_elm_h));
                c.setAlignmentX(AlignmentX.CENTER);

                // Add item display
                e = c.addChild(new Stash_ItemDisplay(context, entry.item));
                e.setSize(new Vector2f(ITEM_NAME_RATIO - ITEM_NAME_SPACING, 0.9f));
                e.setAlignment(AlignmentX.LEFT, AlignmentY.CENTER);

                // Add item name and count display
                e = c.addChild(new Stash_ItemNameCount(context, entry.item, entry.getCount()));
                e.setSize(new Vector2f(1f - ITEM_NAME_RATIO - ITEM_NAME_SPACING, 1f));
                e.setAlignment(AlignmentX.RIGHT, AlignmentY.CENTER);
            }
        }


        // Add buttons
        final Div[] buttons = new Div[] {
            new Stash_Sub_BackButton(context),
        };
        for(int i = 0; i < buttons.length; ++i) {
            e = bg.addChild(buttons[i]);
            e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
            e.setPosX(BOTTOM_ROW_SHIFT * (i - (int)(buttons.length / 2f + 0.0001f)));
            e.setAlignmentY(AlignmentY.BOTTOM);
        }
    }
}
