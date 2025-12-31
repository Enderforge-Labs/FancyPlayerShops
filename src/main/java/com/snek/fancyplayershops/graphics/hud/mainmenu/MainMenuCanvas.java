package com.snek.fancyplayershops.graphics.hud.mainmenu;

import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.data.data_types.Shop;
import com.snek.fancyplayershops.graphics.ScrollableList;
import com.snek.fancyplayershops.graphics.hud.core.elements.HudCanvasBase;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_BrowseShopsButton;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_ShopEntry;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_ShopHeader;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_InfoButton;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_OpenStashButton;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_PreferencesButton;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_RecentActionsButton;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.MainMenu_StatsButton;
import com.snek.fancyplayershops.graphics.hud.mainmenu.styles.MainMenu_EmptyText_S;
import com.snek.fancyplayershops.graphics.hud.misc.elements.Hud_CloseButton;
import com.snek.fancyplayershops.graphics.misc.elements.TitleElm;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;








public class MainMenuCanvas extends HudCanvasBase {
    public static final float ICON_NAME_RATIO    = 0.1f;
    public static final float ICON_NAME_SPACING  = 0.02f;

    public static final float HEADER_H           = 0.05f;
    public static final float LIST_H             = 1f - FancyPlayerShops.LINE_H - HEADER_H - FancyPlayerShops.SQUARE_BUTTON_SIZE;
    public static final int   LIST_SIZE          = 7;

    private ScrollableList list;




    public MainMenuCanvas(final @NotNull HudContext _hud) {
        super(_hud, 1f, FancyPlayerShops.LINE_H, FancyPlayerShops.SQUARE_BUTTON_SIZE);
        final ServerPlayer player = (ServerPlayer)_hud.getPlayer();
        final ServerLevel  level  = (ServerLevel)player.level();
        Div e;

        // Add title
        e = bg.addChild(new TitleElm(level, new Txt("Your shops").white().bold().get()));
        e.setSize(new Vector2f(TitleElm.DEFAULT_W, FancyPlayerShops.LINE_H));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);


        // Add no products text if the player doesn't own any product display
        final @Nullable Set<@NotNull ProductDisplay> displays = ProductDisplayManager.getDisplaysOfPlayer(player);
        if(displays == null || displays.isEmpty()) {
            e = bg.addChild(new SimpleTextElm(level, new MainMenu_EmptyText_S()));
            e.setSize(new Vector2f(1f, FancyPlayerShops.LINE_H));
            e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
        }


        // If displays are present
        else {

            // Add shop header
            final List<Shop> shops = ShopManager.getShops(player);
            e = bg.addChild(new MainMenu_ShopHeader(_hud, shops));
            e.setSize(new Vector2f(1f, HEADER_H));
            e.setAlignmentX(AlignmentX.LEFT);
            e.setPosY(1f - FancyPlayerShops.LINE_H - HEADER_H);

            // Create scrollable list
            final float list_elm_h = 1f / LIST_SIZE;
            list = (ScrollableList)bg.addChild(new ScrollableList(level, list_elm_h));
            list.setSize(new Vector2f(1f, LIST_H));
            list.setAlignmentX(AlignmentX.LEFT);
            list.setPosY(FancyPlayerShops.SQUARE_BUTTON_SIZE);


            // Add shop entry displays
            for(int i = 0; i < shops.size(); ++i) {
               list.storeElm(new MainMenu_ShopEntry(_hud, shops.get(i), list));
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
            e.setSize(new Vector2f(FancyPlayerShops.SQUARE_BUTTON_SIZE));
            e.setPosX(FancyPlayerShops.BOTTOM_ROW_SHIFT * (i - (int)(buttons.length / 2f + 0.0001f)));
            e.setAlignmentY(AlignmentY.BOTTOM);
        }
        //TODO ^ merge duplicate code. this is used in many UIs and HUDs, it should prob be a method or something
    }
}

//TODO provide common icons in FrameworkLib