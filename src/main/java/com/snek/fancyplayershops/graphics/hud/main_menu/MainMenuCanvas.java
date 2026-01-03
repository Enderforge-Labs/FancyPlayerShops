package com.snek.fancyplayershops.graphics.hud.main_menu;


import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.graphics.hud.core.elements.HudCanvasBase;
import com.snek.fancyplayershops.graphics.hud.main_menu.elements.MainMenu_BrowseShopsButton;
import com.snek.fancyplayershops.graphics.hud.main_menu.elements.MainMenu_InfoButton;
import com.snek.fancyplayershops.graphics.hud.main_menu.elements.MainMenu_ManageShopsButton;
import com.snek.fancyplayershops.graphics.hud.main_menu.elements.MainMenu_OpenStashButton;
import com.snek.fancyplayershops.graphics.hud.main_menu.elements.MainMenu_PreferencesButton;
import com.snek.fancyplayershops.graphics.hud.main_menu.elements.MainMenu_RecentActionsButton;
import com.snek.fancyplayershops.graphics.hud.main_menu.elements.MainMenu_StatsButton;
import com.snek.fancyplayershops.graphics.hud.main_menu.elements.MainMenu_Summary;
import com.snek.fancyplayershops.graphics.hud.main_menu.elements.MainMenu_ViewOrdersButton;
import com.snek.fancyplayershops.graphics.hud.misc.elements.Hud_CloseButton;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.layout.HoverableDiv;




public class MainMenuCanvas extends HudCanvasBase {
    public static final Vector2f MAIN_BUTTONS_BB = new Vector2f(0.96f, 0.5f);
    public static final float MAIN_BUTTONS_DISTANCE = 0.02f;
    public static final Vector2f MAIN_BUTTONS_BB_POS = new Vector2f(0f, 1f - TITLE_H - MAIN_BUTTONS_BB.y - MAIN_BUTTONS_DISTANCE);



    public MainMenuCanvas(final @NotNull HudContext context) {
        super(context, "Fancy Player Shops", 1f, TITLE_H, TOOLBAR_H);
        Div e;


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


        // Add summary
        e = bg.addChild(new MainMenu_Summary(context));
        e.setSize(new Vector2f(1f, 1f - MAIN_BUTTONS_BB.y - TITLE_H - TOOLBAR_H));
        e.setPosY(TOOLBAR_H);


        // Add bottom bar buttons
        setToolbarButtons(new Div[] {
            new MainMenu_RecentActionsButton(context),
            new MainMenu_OpenStashButton(context),
            new Hud_CloseButton(context),
            new MainMenu_ViewOrdersButton(context),
            new MainMenu_InfoButton(context),
        });
    }
}
