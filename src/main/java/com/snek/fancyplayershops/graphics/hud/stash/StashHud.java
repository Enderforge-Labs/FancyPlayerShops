package com.snek.fancyplayershops.graphics.hud.stash;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.data.PlayerStash;
import com.snek.fancyplayershops.data.StashEntry;
import com.snek.fancyplayershops.data.StashManager;
import com.snek.fancyplayershops.graphics.hud.core.styles.HudCanvasBack_S;
import com.snek.fancyplayershops.graphics.hud.core.styles.HudCanvasBackground_S;
import com.snek.fancyplayershops.graphics.hud.misc.elements.HudCloseButton;
import com.snek.fancyplayershops.graphics.hud.stash.elements.StashHud_EmptyText;
import com.snek.fancyplayershops.graphics.hud.stash.elements.StashHud_ItemDisplay;
import com.snek.fancyplayershops.graphics.hud.stash.elements.StashHud_ItemNameCount;
import com.snek.fancyplayershops.graphics.hud.stash.elements.StashHud_MaterialList;
import com.snek.fancyplayershops.graphics.misc.elements.TitleElm;
import com.snek.fancyplayershops.graphics.ui.misc.elements.ShopFancyTextElm;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.graphics.core.HudCanvas;
import com.snek.frameworklib.graphics.core.Div;
import com.snek.frameworklib.data_types.ui.AlignmentX;
import com.snek.frameworklib.data_types.ui.AlignmentY;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;








public class StashHud extends HudCanvas {
    public static final float LIST_WIDTH        = 0.9f;
    public static final float ITEM_NAME_RATIO    = 0.1f;
    public static final float ITEM_NAME_SPACING  = 0.02f;

    public static final float LIST_MARGIN_BOTTOM = 0.05f;
    public static final float LIST_H             = 1f - ShopFancyTextElm.LINE_H - SQUARE_BUTTON_SIZE - LIST_MARGIN_BOTTOM;
    public static final int   LIST_SIZE          = 7;

    private Div list;




    public StashHud(final @NotNull HudContext _hud) {
        super(_hud, 1f, ShopFancyTextElm.LINE_H, SQUARE_BUTTON_SIZE, new HudCanvasBackground_S(), new HudCanvasBack_S());
        Div e;

        // Add title
        e = bg.addChild(new TitleElm(((ServerLevel)context.getPlayer().level()), new Txt("Your stash").white().bold().get()));
        e.setSize(new Vector2f(TitleElm.DEFAULT_W, ShopFancyTextElm.LINE_H));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);


        // Add "empty stash" text if the stash is empty
        final PlayerStash stash = StashManager.getStash((ServerPlayer)(_hud.getPlayer()));
        if(stash == null) {
            e = bg.addChild(new StashHud_EmptyText(_hud));
            e.setSize(new Vector2f(1f, ShopFancyTextElm.LINE_H));
            e.setAlignmentX(AlignmentX.CENTER);
            e.setPosY(1f - ShopFancyTextElm.LINE_H * 2f);
        }


        // If materials are present
        else {

            // Create scrollable list
            list = bg.addChild(new StashHud_MaterialList());
            list.setSize(new Vector2f(LIST_WIDTH, LIST_H));
            list.setAlignmentX(AlignmentX.CENTER);
            list.setPosY(SQUARE_BUTTON_SIZE + LIST_MARGIN_BOTTOM);

            // For each material
            final float list_elm_h = 1f / LIST_SIZE;
            final List<StashEntry> entries = new ArrayList<>(stash.values());
            for(int i = 0; i < entries.size() && i < LIST_SIZE; ++i) {
                final StashEntry entry = entries.get(i);

                // Add container for the stash entry
                final Div c = list.addChild(new Div());
                c.setSize(new Vector2f(1f, list_elm_h));
                c.setAlignmentX(AlignmentX.CENTER);
                c.setPosY(LIST_H - LIST_H * list_elm_h * (i + 1));

                // Add item display
                e = c.addChild(new StashHud_ItemDisplay(_hud, entry.item));
                e.setSize(new Vector2f(ITEM_NAME_RATIO - ITEM_NAME_SPACING, 0.9f));
                e.setAlignment(AlignmentX.LEFT, AlignmentY.CENTER);

                // Add item name and count display
                e = c.addChild(new StashHud_ItemNameCount(_hud, entry.item, entry.getCount()));
                e.setSize(new Vector2f(1f - ITEM_NAME_RATIO - ITEM_NAME_SPACING, 1f));
                e.setAlignment(AlignmentX.RIGHT, AlignmentY.CENTER);
            }
        }


        // Add buttons
        final Div[] buttons = new Div[] {
            new HudCloseButton(_hud),
        };
        for(int i = 0; i < buttons.length; ++i) {
            e = bg.addChild(buttons[i]);
            e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
            e.setPosX(BOTTOM_ROW_SHIFT * (i - (int)(buttons.length / 2f + 0.0001f)));
            e.setAlignmentY(AlignmentY.BOTTOM);
        }
    }
}
