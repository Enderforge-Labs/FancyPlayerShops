// package com.snek.fancyplayershops.graphics.hud.mainmenu;

// import java.util.List;

// import org.jetbrains.annotations.NotNull;
// import org.joml.Vector2f;

// import com.snek.fancyplayershops.graphics.hud.core.styles.HudCanvasBack_S;
// import com.snek.fancyplayershops.graphics.hud.core.styles.HudCanvasBackground_S;
// import com.snek.fancyplayershops.graphics.hud.misc.elements.HudCloseButton;
// import com.snek.fancyplayershops.graphics.misc.elements.TitleElm;
// import com.snek.fancyplayershops.graphics.ui.misc.elements.ShopFancyTextElm;
// import com.snek.frameworklib.data_types.graphics.AlignmentX;
// import com.snek.frameworklib.data_types.graphics.AlignmentY;
// import com.snek.frameworklib.graphics.layout.Div;
// import com.snek.frameworklib.graphics.core.HudCanvas;
// import com.snek.frameworklib.graphics.core.HudContext;
// import com.snek.frameworklib.utils.Txt;

// import net.minecraft.server.level.ServerLevel;








// public class MainMenuHud extends HudCanvas {
//     public static final float LIST_WIDTH        = 0.9f;
//     public static final float ITEM_NAME_RATIO    = 0.1f;
//     public static final float ITEM_NAME_SPACING  = 0.02f;

//     public static final float LIST_MARGIN_BOTTOM = 0.05f;
//     public static final float LIST_H             = 1f - ShopFancyTextElm.LINE_H - SQUARE_BUTTON_SIZE - LIST_MARGIN_BOTTOM;
//     public static final int   LIST_SIZE          = 7;

//     private Div list;




//     public MainMenuHud(final @NotNull HudContext hud) {
//         super(hud, 1f, ShopFancyTextElm.LINE_H, SQUARE_BUTTON_SIZE, new HudCanvasBackground_S(), new HudCanvasBack_S());
//         Div e;

//         // Add title
//         e = bg.addChild(new TitleElm(((ServerLevel)context.getPlayer().level()), new Txt("Your shops").white().bold().get()));
//         e.setSize(new Vector2f(TitleElm.DEFAULT_W, ShopFancyTextElm.LINE_H));
//         e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);


//         // Add "no shpos" text if the player doesn't own any shop
//         final List<ShopGroup> groups = ShopGroupsManager.getGroups(hud.getPlayer());
//         // if(stash == null) {
//         //     e = bg.addChild(new StashHud_EmptyText(_hud));
//         //     e.setSize(new Vector2f(1f, ShopFancyTextElm.LINE_H));
//         //     e.setAlignmentX(AlignmentX.CENTER);
//         //     e.setPosY(1f - ShopFancyTextElm.LINE_H * 2f);
//         }


//         //TODO add a category for uncategorized shops
//         // If shops are present
//         else {

//             // Create scrollable list
//             list = bg.addChild(new StashHud_MaterialList());
//             list.setSize(new Vector2f(LIST_WIDTH, LIST_H));
//             list.setAlignmentX(AlignmentX.CENTER);
//             list.setPosY(SQUARE_BUTTON_SIZE + LIST_MARGIN_BOTTOM);

//             // For each material
//             final float list_elm_h = 1f / LIST_SIZE;
//             final List<StashEntry> entries = new ArrayList<>(stash.values());
//             for(int i = 0; i < entries.size() && i < LIST_SIZE; ++i) {
//                 final StashEntry entry = entries.get(i);

//                 // Add container for the stash entry
//                 final Div c = list.addChild(new Div());
//                 c.setSize(new Vector2f(1f, list_elm_h));
//                 c.setAlignmentX(AlignmentX.CENTER);
//                 c.setPosY(LIST_H - LIST_H * list_elm_h * (i + 1));

//                 // Add item display
//                 e = c.addChild(new StashHud_ItemDisplay(_hud, entry.item));
//                 e.setSize(new Vector2f(ITEM_NAME_RATIO - ITEM_NAME_SPACING, 0.9f));
//                 e.setAlignment(AlignmentX.LEFT, AlignmentY.CENTER);

//                 // Add item name and count display
//                 e = c.addChild(new StashHud_ItemNameCount(_hud, entry.item, entry.getCount()));
//                 e.setSize(new Vector2f(1f - ITEM_NAME_RATIO - ITEM_NAME_SPACING, 1f));
//                 e.setAlignment(AlignmentX.RIGHT, AlignmentY.CENTER);
//             }
//         }


//         // Add buttons
//         final Div[] buttons = new Div[] {
//             new HudCloseButton(_hud),
//         };
//         for(int i = 0; i < buttons.length; ++i) {
//             e = bg.addChild(buttons[i]);
//             e.setSize(new Vector2f(SQUARE_BUTTON_SIZE));
//             e.setPosX(BOTTOM_ROW_SHIFT * (i - (int)(buttons.length / 2f + 0.0001f)));
//             e.setAlignmentY(AlignmentY.BOTTOM);
//         }
//     }
// }