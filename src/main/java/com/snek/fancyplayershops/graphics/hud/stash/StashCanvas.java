package com.snek.fancyplayershops.graphics.hud.stash;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.data.StashManager;
import com.snek.fancyplayershops.data.data_types.PlayerStash;
import com.snek.fancyplayershops.data.data_types.StashEntry;
import com.snek.fancyplayershops.graphics.ScrollableList;
import com.snek.fancyplayershops.graphics.hud.core.elements.HudCanvasBase;
import com.snek.fancyplayershops.graphics.hud.mainmenu.MainMenuCanvas;
import com.snek.fancyplayershops.graphics.hud.stash.elements.Stash_ProductEntry;
import com.snek.fancyplayershops.graphics.hud.stash.styles.Stash_EmptyText_S;
import com.snek.fancyplayershops.graphics.misc.elements.Misc_BackButton;
import com.snek.fancyplayershops.graphics.misc.elements.Misc_TitleElm;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;








public class StashCanvas extends HudCanvasBase {
    public static final float LIST_H    = 1f - FancyPlayerShops.LINE_H - FancyPlayerShops.SQUARE_BUTTON_SIZE;
    public static final int   LIST_SIZE = 7;

    private ScrollableList list;




    public StashCanvas(final @NotNull HudContext context) {
        super(context, 1f, FancyPlayerShops.LINE_H, FancyPlayerShops.SQUARE_BUTTON_SIZE);
        final ServerPlayer player = (ServerPlayer)context.getPlayer();
        final ServerLevel  level  = (ServerLevel)player.level();
        Div e;

        // Add title
        e = bg.addChild(new Misc_TitleElm(level, new Txt("Your stash").white().bold().get()));
        e.setSize(new Vector2f(Misc_TitleElm.DEFAULT_W, FancyPlayerShops.LINE_H));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);


        // Add "empty stash" text if the stash is empty
        final PlayerStash stash = StashManager.getStash(player);
        if(stash == null) {
            e = bg.addChild(new SimpleTextElm(level, new Stash_EmptyText_S()));
            e.setSize(new Vector2f(1f, FancyPlayerShops.LINE_H));
            e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
        }


        // If materials are present
        else {

            // Create scrollable list
            final float list_elm_h = 1f / LIST_SIZE;
            list = (ScrollableList)bg.addChild(new ScrollableList(level, list_elm_h));
            list.setSize(new Vector2f(1f, LIST_H));
            list.setAlignmentX(AlignmentX.RIGHT);
            list.setPosY(FancyPlayerShops.SQUARE_BUTTON_SIZE);


            //FIXME update stash view when the stash is changed
            // For each material
            final List<StashEntry> entries = new ArrayList<>(stash.values());
            for(int i = 0; i < entries.size(); ++i) {
                final StashEntry entry = entries.get(i);
                e = list.storeElm(new Stash_ProductEntry(context, entry.getItem(), entry.getCount(), list));
            }
        }


        // Add buttons
        final Div[] buttons = new Div[] {
            new Misc_BackButton(context, () ->
                canvas.getContext().changeCanvas(new MainMenuCanvas((HudContext)canvas.getContext()))
            ),
        };
        for(int i = 0; i < buttons.length; ++i) {
            e = bg.addChild(buttons[i]);
            e.setSize(new Vector2f(FancyPlayerShops.SQUARE_BUTTON_SIZE));
            e.setPosX(FancyPlayerShops.BOTTOM_ROW_SHIFT * (i - (int)(buttons.length / 2f + 0.0001f)));
            e.setAlignmentY(AlignmentY.BOTTOM);
        } //TODO merge duplicate code
    }
}


//TODO add "drop all" button - drops all of the items on the ground, as if you just pressed Q on them
//TODO add "fill inventory" button - fills your inventory with whatever item fits
//TODO add "clear stash" button - deletes all the items in your stash


//TODO add header, like the one in the main menu
//TODO maybe add the header as a special type of list with header? idk. might not be necessary