package com.snek.fancyplayershops.graphics.hud.mainmenu;

import java.util.HashSet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.graphics.ScrollableList;
import com.snek.fancyplayershops.graphics.hud.core.styles.HudCanvasBack_S;
import com.snek.fancyplayershops.graphics.hud.core.styles.HudCanvasBackground_S;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_BrowseShopsButton;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_ClaimAllButton;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_InfoButton;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_OpenStashButton;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_PreferencesButton;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_StatsButton;
import com.snek.fancyplayershops.graphics.hud.mainmenu.styles.MainMenu_EmptyText_S;
import com.snek.fancyplayershops.graphics.hud.misc.elements.Hud_CloseButton;
import com.snek.fancyplayershops.graphics.misc.elements.TitleElm;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ShopFancyTextElm;
import com.snek.fancyplayershops.main.Shop;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.core.HudCanvas;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;








public class MainMenuCanvas extends HudCanvas {
    public static final float LIST_WIDTH         = 0.95f;
    public static final float ICON_NAME_RATIO    = 0.1f;
    public static final float ICON_NAME_SPACING  = 0.02f;

    public static final float LIST_H             = 1f - ShopFancyTextElm.LINE_H - SQUARE_BUTTON_SIZE;
    public static final int   LIST_SIZE          = 7;

    private ScrollableList list;




    public MainMenuCanvas(final @NotNull HudContext _hud) {
        super(_hud, 1f, ShopFancyTextElm.LINE_H, SQUARE_BUTTON_SIZE, new HudCanvasBackground_S(), new HudCanvasBack_S());
        final ServerPlayer player = (ServerPlayer)_hud.getPlayer();
        final ServerLevel  world  = (ServerLevel)player.level();
        Div e;

        // Add title
        e = bg.addChild(new TitleElm(world, new Txt("Your shops").white().bold().get()));
        e.setSize(new Vector2f(TitleElm.DEFAULT_W, ShopFancyTextElm.LINE_H));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);


        // Add "no shops yet" text if the player doesn't own any shop
        final @Nullable HashSet<@NotNull Shop> shops = ShopManager.getShopsOfPlayer(player);
        if(shops == null || shops.isEmpty()) {
            e = bg.addChild(new SimpleTextElm(world, new MainMenu_EmptyText_S()));
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


            // // For each shop group
            // final List<StashEntry> entries = new ArrayList<>(stash.values());
            // for(int i = 0; i < entries.size(); ++i) {
            //     final StashEntry entry = entries.get(i);

            //     // Add container for the stash entry
            //     final Div c = list.storeElm(new Div());
            //     c.setSize(new Vector2f(1f, list_elm_h));
            //     c.setAlignmentX(AlignmentX.CENTER);

            //     // Add item display
            //     e = c.addChild(new StashHud_ItemDisplay(_hud, entry.item));
            //     e.setSize(new Vector2f(ICON_NAME_RATIO - ICON_NAME_SPACING, 0.9f));
            //     e.setAlignment(AlignmentX.LEFT, AlignmentY.CENTER);

            //     // Add item name and count display
            //     e = c.addChild(new StashHud_ItemNameCount(_hud, entry.item, entry.getCount()));
            //     e.setSize(new Vector2f(1f - ICON_NAME_RATIO - ICON_NAME_SPACING, 1f));
            //     e.setAlignment(AlignmentX.RIGHT, AlignmentY.CENTER);
            // }
        }


        // Add close button
        e = bg.addChild(new Hud_CloseButton(_hud));
        e.setSize(new Vector2f(ShopFancyTextElm.LINE_H));
        e.setAlignment(AlignmentX.LEFT, AlignmentY.TOP);

        // Add info button
        e = bg.addChild(new MainMenu_InfoButton(_hud));
        e.setSize(new Vector2f(ShopFancyTextElm.LINE_H));
        e.setAlignment(AlignmentX.RIGHT, AlignmentY.TOP);


        // Add bottom bar buttons
        final Div[] buttons = new Div[] {
            // new MainMenuHud_ClaimAllButton(_hud), //FIXME move to top bar
            new MainMenu_BrowseShopsButton(_hud),
            // new Hud_CloseButton(_hud),
            new MainMenu_OpenStashButton(_hud),
            new MainMenu_StatsButton(_hud),
            new MainMenu_PreferencesButton(_hud)
        };
        for(int i = 0; i < buttons.length; ++i) {
            e = bg.addChild(buttons[i]);
            e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
            e.setPosX(BOTTOM_ROW_SHIFT * (i - (int)(buttons.length / 2f + 0.0001f)));
            e.setAlignmentY(AlignmentY.BOTTOM);
        }
        //TODO ^ merge duplicate code. this is used in many UIs and HUDs, it should prob be a method or something
    }
}

//TODO provide common icons in FrameworkLib