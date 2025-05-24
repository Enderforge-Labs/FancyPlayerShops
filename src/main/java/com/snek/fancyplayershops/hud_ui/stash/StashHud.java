package com.snek.fancyplayershops.hud_ui.stash;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.data.PlayerStash;
import com.snek.fancyplayershops.data.StashEntry;
import com.snek.fancyplayershops.data.StashManager;
import com.snek.fancyplayershops.hud_ui._elements.Hud;
import com.snek.fancyplayershops.hud_ui._elements.HudCanvas;
import com.snek.fancyplayershops.hud_ui.stash.elements.StashHud_EmptyText;
import com.snek.fancyplayershops.hud_ui.stash.elements.StashHud_ItemDisplay;
import com.snek.fancyplayershops.hud_ui.stash.elements.StashHud_ItemName;
import com.snek.fancyplayershops.hud_ui.stash.elements.StashHud_Title;
import com.snek.fancyplayershops.shop_ui.misc.elements.ShopFancyTextElm;
import com.snek.fancyplayershops.ui._elements.UiBorder;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.Div;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;








public class StashHud extends HudCanvas {
    public static final float ENTRY_WIDTH = 0.9f;
    public static final float ITEM_NAME_RATIO = 0.1f;
    public static final float ITEM_NAME_SPACING = 0.02f;




    public StashHud(final @NotNull Hud _hud) {
        super(_hud, 1f, ShopFancyTextElm.LINE_H, UiBorder.DEFAULT_HEIGHT);
        Div e;

        // Add title
        e = bg.addChild(new StashHud_Title((ServerLevel)(hud.getPlayer().level())));
        e.setSize(new Vector2f(1f, ShopFancyTextElm.LINE_H));
        e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);


        // Add "empty stash" text if the stash is empty
        final PlayerStash stash = StashManager.getStash((ServerPlayer)(_hud.getPlayer()));
        if(stash == null) {
            e = bg.addChild(new StashHud_EmptyText(_hud));
            e.setSize(new Vector2f(1f, ShopFancyTextElm.LINE_H));
            e.setAlignmentX(AlignmentX.CENTER);
            e.setPosY(1f - ShopFancyTextElm.LINE_H * 2f);
        }


        // If materials are present, iterate through them
        else {
            final List<StashEntry> entries = new ArrayList<>(stash.values());
            for(int i = 0; i < entries.size(); ++i) {
                final StashEntry entry = entries.get(i);

                // Add container for the stash entry
                final Div c = bg.addChild(new Div());
                c.setSize(new Vector2f(ENTRY_WIDTH, ShopFancyTextElm.LINE_H));
                c.setAlignmentX(AlignmentX.CENTER);
                c.setPosY(1f - ShopFancyTextElm.LINE_H * (i + 2));

                // Add item display
                e = c.addChild(new StashHud_ItemDisplay(_hud, entry.item));
                e.setSize(new Vector2f(ITEM_NAME_RATIO - ITEM_NAME_SPACING, 1f));
                e.setAlignmentX(AlignmentX.LEFT);

                // Add item name display
                e = c.addChild(new StashHud_ItemName(_hud, entry.item));
                e.setSize(new Vector2f(1f - ITEM_NAME_RATIO - ITEM_NAME_SPACING, 1f));
                e.setAlignmentX(AlignmentX.RIGHT);
            }
        }
    }
}
