package com.snek.fancyplayershops.graphics.hud.mainmenu;

import java.util.HashSet;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.data.ShopGroupManager;
import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.data.data_types.ShopGroup;
import com.snek.fancyplayershops.graphics.ScrollableList;
import com.snek.fancyplayershops.graphics.hud.core.styles.HudCanvasBack_S;
import com.snek.fancyplayershops.graphics.hud.core.styles.HudCanvasBackground_S;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_BrowseShopsButton;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_GroupEntry;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_GroupHeader;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_InfoButton;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_OpenStashButton;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_PreferencesButton;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_RecentActionsButton;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_StatsButton;
import com.snek.fancyplayershops.graphics.hud.mainmenu.styles.MainMenu_EmptyText_S;
import com.snek.fancyplayershops.graphics.hud.misc.elements.Hud_CloseButton;
import com.snek.fancyplayershops.graphics.misc.elements.TitleElm;
import com.snek.fancyplayershops.main.FancyPlayerShops;
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

    public static final float HEADER_H           = 0.05f;
    public static final float LIST_H             = 1f - FancyPlayerShops.LINE_H - HEADER_H - SQUARE_BUTTON_SIZE;
    public static final int   LIST_SIZE          = 7;

    private ScrollableList list;




    public MainMenuCanvas(final @NotNull HudContext _hud) {
        super(_hud, 1f, FancyPlayerShops.LINE_H, SQUARE_BUTTON_SIZE, new HudCanvasBackground_S(), new HudCanvasBack_S());
        final ServerPlayer player = (ServerPlayer)_hud.getPlayer();
        final ServerLevel  level  = (ServerLevel)player.level();
        Div e;

        // Add title
        e = bg.addChild(new TitleElm(level, new Txt("Your shops").white().bold().get()));
        e.setSize(new Vector2f(TitleElm.DEFAULT_W, FancyPlayerShops.LINE_H));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);


        // Add "no shops yet" text if the player doesn't own any shop
        final @Nullable HashSet<@NotNull Shop> shops = ShopManager.getShopsOfPlayer(player);
        if(shops == null || shops.isEmpty()) {
            e = bg.addChild(new SimpleTextElm(level, new MainMenu_EmptyText_S()));
            e.setSize(new Vector2f(1f, FancyPlayerShops.LINE_H));
            e.setAlignmentX(AlignmentX.CENTER);
            e.setPosY(1f - FancyPlayerShops.LINE_H * 2f);
        }


        // If groups are present
        else {

            // Add shop group header
            final List<ShopGroup> shopGroups = ShopGroupManager.getShopGroups(player);
            e = bg.addChild(new MainMenu_GroupHeader(_hud, shopGroups));
            e.setSize(new Vector2f(LIST_WIDTH, HEADER_H));
            e.setAlignmentX(AlignmentX.RIGHT);
            e.setPosY(1f - FancyPlayerShops.LINE_H - HEADER_H);

            // Create scrollable list
            final float list_elm_h = 1f / LIST_SIZE;
            list = (ScrollableList)bg.addChild(new ScrollableList(level, list_elm_h));
            list.setSize(new Vector2f(LIST_WIDTH, LIST_H));
            list.setAlignmentX(AlignmentX.RIGHT);
            list.setPosY(SQUARE_BUTTON_SIZE);


            // Add shop group entry displays
            for(int i = 0; i < shopGroups.size(); ++i) {
               list.storeElm(new MainMenu_GroupEntry(_hud, shopGroups.get(i)));
            }
        }


        // Add close button
        e = bg.addChild(new Hud_CloseButton(_hud));
        e.setSize(new Vector2f(FancyPlayerShops.LINE_H));
        e.setAlignment(AlignmentX.LEFT, AlignmentY.TOP);

        // Add info button
        e = bg.addChild(new MainMenu_InfoButton(_hud));
        e.setSize(new Vector2f(FancyPlayerShops.LINE_H));
        e.setAlignment(AlignmentX.RIGHT, AlignmentY.TOP);


        // Add bottom bar buttons
        final Div[] buttons = new Div[] {
            new MainMenu_BrowseShopsButton(_hud),
            new MainMenu_RecentActionsButton(_hud),
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