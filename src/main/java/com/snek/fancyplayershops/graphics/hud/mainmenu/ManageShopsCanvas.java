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
import com.snek.fancyplayershops.graphics.hud._mainmenu_.MainMenuCanvas;
import com.snek.fancyplayershops.graphics.hud.core.elements.HudCanvasBase;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.ManageShops_ShopEntry;
import com.snek.fancyplayershops.graphics.hud.mainmenu.elements.ManageShops_ShopHeader;
import com.snek.fancyplayershops.graphics.hud.mainmenu.styles.ManageShops_EmptyText_S;
import com.snek.fancyplayershops.graphics.misc.elements.Misc_BackButton;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.core.HudContext;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;








public class ManageShopsCanvas extends HudCanvasBase {
    public static final float ICON_NAME_RATIO    = 0.1f;
    public static final float ICON_NAME_SPACING  = 0.02f;

    public static final float HEADER_H           = 0.05f;
    public static final float LIST_H             = 1f - TITLE_H - HEADER_H - TOOLBAR_H;
    public static final int   LIST_SIZE          = 7;

    private ScrollableList list;




    public ManageShopsCanvas(final @NotNull HudContext _hud) {
        super(_hud, "Your shops", 1f, TITLE_H, TOOLBAR_H);
        final ServerPlayer player = (ServerPlayer)_hud.getPlayer();
        final ServerLevel  level  = (ServerLevel)player.level();
        Div e;


        // Add no products text if the player doesn't own any product display
        final @Nullable Set<@NotNull ProductDisplay> displays = ProductDisplayManager.getDisplaysOfPlayer(player);
        if(displays == null || displays.isEmpty()) {
            e = bg.addChild(new SimpleTextElm(level, new ManageShops_EmptyText_S()));
            e.setSize(new Vector2f(1f, TITLE_H));
            e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
        }


        // If displays are present
        else {

            // Add shop header
            final List<Shop> shops = ShopManager.getShops(player);
            e = bg.addChild(new ManageShops_ShopHeader(_hud, shops));
            e.setSize(new Vector2f(1f, HEADER_H));
            e.setAlignmentX(AlignmentX.LEFT);
            e.setPosY(1f - TITLE_H - HEADER_H);

            // Create scrollable list
            final float list_elm_h = 1f / LIST_SIZE;
            list = (ScrollableList)bg.addChild(new ScrollableList(level, list_elm_h));
            list.setSize(new Vector2f(1f, LIST_H));
            list.setAlignmentX(AlignmentX.LEFT);
            list.setPosY(TOOLBAR_H);


            // Add shop entry displays
            for(int i = 0; i < shops.size(); ++i) {
               list.storeElm(new ManageShops_ShopEntry(_hud, shops.get(i), list));
            }
        }


        // Add bottom bar buttons
        setToolbarButtons(new Div[] {
            new Misc_BackButton(_hud, () -> canvas.getContext().changeCanvas(new MainMenuCanvas(_hud)))
        });
    }
}
