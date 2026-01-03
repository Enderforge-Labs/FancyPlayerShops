package com.snek.fancyplayershops.graphics.hud._mainmenu_;


import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.hud._mainmenu_.elements.MainMenu_BrowseShopsButton;
import com.snek.fancyplayershops.graphics.hud._mainmenu_.elements.MainMenu_InfoButton;
import com.snek.fancyplayershops.graphics.hud._mainmenu_.elements.MainMenu_ManageShopsButton;
import com.snek.fancyplayershops.graphics.hud._mainmenu_.elements.MainMenu_OpenStashButton;
import com.snek.fancyplayershops.graphics.hud._mainmenu_.elements.MainMenu_PreferencesButton;
import com.snek.fancyplayershops.graphics.hud._mainmenu_.elements.MainMenu_RecentActionsButton;
import com.snek.fancyplayershops.graphics.hud._mainmenu_.elements.MainMenu_StatsButton;
import com.snek.fancyplayershops.graphics.hud.core.elements.HudCanvasBase;
import com.snek.fancyplayershops.graphics.hud.misc.elements.Hud_CloseButton;
import com.snek.fancyplayershops.graphics.misc.elements.Misc_TitleElm;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.layout.HoverableDiv;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;




public class MainMenuCanvas extends HudCanvasBase {
    public static final Vector2f MAIN_BUTTONS_BB = new Vector2f(0.96f, 0.5f);
    public static final float MAIN_BUTTONS_DISTANCE = 0.02f;
    public static final Vector2f MAIN_BUTTONS_BB_POS = new Vector2f(0f, 1f - FancyPlayerShops.LINE_H - MAIN_BUTTONS_BB.y - MAIN_BUTTONS_DISTANCE);



    public MainMenuCanvas(final @NotNull HudContext context) {
        super(context, 1f, FancyPlayerShops.LINE_H, FancyPlayerShops.SQUARE_BUTTON_SIZE);
        final ServerPlayer player = (ServerPlayer)context.getPlayer();
        final ServerLevel  level  = (ServerLevel)player.level();
        Div e;


        // Add title
        e = bg.addChild(new Misc_TitleElm(level, new Txt("Fancy Player Shops").white().bold().get()));
        e.setSize(new Vector2f(Misc_TitleElm.DEFAULT_W, FancyPlayerShops.LINE_H));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);


        // Add main buttons
        final Div mainButtonsBB = bg.addChild(new HoverableDiv());
        mainButtonsBB.setSize(MAIN_BUTTONS_BB);
        mainButtonsBB.setPos(MAIN_BUTTONS_BB_POS);
        final Vector2f parentSize = mainButtonsBB.getAbsSize();
        final Vector2f buttonSize = new Vector2f(1f).sub(new Vector2f(1f).div(parentSize).mul(new Vector2f(MAIN_BUTTONS_DISTANCE))).div(2f); {

            // Browse shops
            e = mainButtonsBB.addChild(new MainMenu_BrowseShopsButton(context));
            e.setSize(buttonSize);
            e.setAlignment(AlignmentX.LEFT, AlignmentY.TOP);

            // Manage shops
            e = mainButtonsBB.addChild(new MainMenu_ManageShopsButton(context));
            e.setSize(buttonSize);
            e.setAlignment(AlignmentX.LEFT, AlignmentY.BOTTOM);

            // Statistics
            e = mainButtonsBB.addChild(new MainMenu_StatsButton(context));
            e.setSize(buttonSize);
            e.setAlignment(AlignmentX.RIGHT, AlignmentY.TOP);

            // Preferences
            e = mainButtonsBB.addChild(new MainMenu_PreferencesButton(context));
            e.setSize(buttonSize);
            e.setAlignment(AlignmentX.RIGHT, AlignmentY.BOTTOM);
        }


        // Add bottom bar buttons
        final Div[] buttons = new Div[] {
            new MainMenu_RecentActionsButton(context),
            new MainMenu_OpenStashButton(context),
            new Hud_CloseButton(context),
            new MainMenu_InfoButton(context), //FIXME replace this duplicate button with something useful
            new MainMenu_InfoButton(context),
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
