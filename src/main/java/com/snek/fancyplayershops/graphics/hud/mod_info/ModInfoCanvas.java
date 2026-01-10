package com.snek.fancyplayershops.graphics.hud.mod_info;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.graphics.hud.core.elements.HudCanvasBase;
import com.snek.fancyplayershops.graphics.hud.main_menu.MainMenuCanvas;
import com.snek.fancyplayershops.graphics.hud.mod_info.elements.ModInfo_1_CraftingGrid;
import com.snek.fancyplayershops.graphics.hud.mod_info.elements.ModInfo_NextButton;
import com.snek.fancyplayershops.graphics.hud.mod_info.elements.ModInfo_PrevButton;
import com.snek.fancyplayershops.graphics.misc.elements.Misc_BackButton;
import com.snek.fancyplayershops.main.DisplayTier;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.basic.elements.ItemElm;
import com.snek.frameworklib.graphics.basic.elements.TextElm;
import com.snek.frameworklib.graphics.basic.presets.ItemStyle_Gui;
import com.snek.frameworklib.graphics.basic.presets.TextStyle_Small;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;








public class ModInfoCanvas extends HudCanvasBase {
    public static final float P0_ITEM_H = 0.3f;
    public static final float P0_TEXT_H = (1f - P0_ITEM_H) / 2;

    public static final float P1_CRAFTING_GRID_H = 0.5f;
    public static final float P1_TEXT_H = (1f - P1_CRAFTING_GRID_H) / 2;


    // Page data
    private final List<Div> pages = new ArrayList<>();
    private final List<Component> pageNames = new ArrayList<>();
    private int activePageIndex = 0;
    private Div activePage      = null;

    // Getters
    public int getActivePageIndex() { return activePageIndex; }
    public int getPageNumber() { return pages.size(); }








    public ModInfoCanvas(final @NotNull HudContext context) {
        super(context, "Info", 1f, TITLE_H, TOOLBAR_H);
        final ServerPlayer player = (ServerPlayer)context.getPlayer();
        final ServerLevel  level  = (ServerLevel)player.level();
        Div p;
        Div e;




        // Add page 1
        p = new Div();
        addPage(p, "Selling items"); {

            // Add text A
            e = p.addChild(new TextElm(level, new TextStyle_Small()
                .withText(new Txt()
                    .cat(new Txt("Shops").color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR)).cat(" are a great way to sell your items.\n")
                    .cat("Other players can purchase products remotely\n")
                    .cat("or visit your shops for bulk options and orders.\n")
                    .white().get()
                )
            ));
            e.setSize(new Vector2f(1f, P0_TEXT_H));
            e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);


            // Add display item
            e = p.addChild(new ItemElm(context.getLevel(), new ItemStyle_Gui(
                ProductDisplayManager.getProductDisplayItemCopy(DisplayTier.T4),
                ModInfoCanvas.P0_ITEM_H
            )));
            e.setSize(new Vector2f(1f, P0_ITEM_H));
            e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);


            // Add text B
            e = p.addChild(new TextElm(level, new TextStyle_Small()
                .withText(new Txt()
                    .cat("To get started, craft a ").cat(new Txt("Product Display").color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR)).cat(".\n")
                    .cat("Once placed, you will be able to\n")
                    .cat("select the item to sell, set a price, and\n")
                    .cat("adjust other settings to your liking.\n")
                    .cat(new Txt("Enjoy :3").color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR))
                    .white().get()
                )
            ));
            e.setSize(new Vector2f(1f, P0_TEXT_H));
            e.setAlignment(AlignmentX.CENTER, AlignmentY.BOTTOM);
        }
        changePage(0);




        // Add page 2
        p = new Div();
        addPage(p, "Crafting a Display"); {

            // Add text
            e = p.addChild(new TextElm(level, new TextStyle_Small()
                .withText(new Txt()
                    .cat(new Txt("Basic").color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR)).cat(" product displays can be crafted\n")
                    .cat("using Vanilla materials.\n")
                    .cat("They have a maximum stock of ").cat(new Txt("123456").color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR)).cat(" items.")
                    //FIXME actually retrieve the max stock from configs
                    .white().get()
                )
            ));
            e.setSize(new Vector2f(1f, P1_TEXT_H));
            e.setAlignment(AlignmentX.CENTER, AlignmentY.TOP);


            // Add crafting grid
            e = p.addChild(new ModInfo_1_CraftingGrid(context));
            e.setSize(new Vector2f(1f, P1_CRAFTING_GRID_H));
            e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);


            // Add text
            e = p.addChild(new TextElm(level, new TextStyle_Small()
                .withText(new Txt()
                    .cat("They can be ").cat(new Txt("upgraded").color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR)).cat(" using various items\n")
                    .cat("to increase their storage, let them restock\n")
                    .cat("automatically and retrieve items wirelessly.")
                    .white().get()
                )
            ));
            e.setSize(new Vector2f(1f, P1_TEXT_H));
            e.setAlignment(AlignmentX.CENTER, AlignmentY.BOTTOM);
        }




        // // Add page 3
        // p = new Div();
        // addPage(p, "Shop commands"); {
        //     e = p.addChild(new TextElm(level, new ModInfo_2_Text_S()));
        //     e.setSize(new Vector2f(1f, 1f));
        //     e.setAlignment(AlignmentX.CENTER, AlignmentY.CENTER);
        // }




        // Add buttons
        setToolbarButtons(new Div[] {
            new ModInfo_PrevButton(context),
            new Misc_BackButton(context, () -> canvas.getContext().changeCanvas(new MainMenuCanvas(context))),
            new ModInfo_NextButton(context),
        });
    }








    /**
     * Changes the active page to the specified index.
     * @param pageIndex The index of the new page.
     */
    public void changePage(final int pageIndex) {

        // Change index
        activePageIndex = pageIndex;


        // Change page element
        if(activePage != null) {
            activePage.despawn(true);
            bg.removeChild(activePage);
        }
        activePage = bg.addChild(pages.get(pageIndex));
        activePage.spawn(context.getSpawnPos(), true);


        // Change title
        updateTitle(pageNames.get(pageIndex));
    }




    /**
     * Adds a new page.
     * <p>
     * This method also pre-calculates and sets the page element's size and position to align it with the canvas.
     * @param page The element containing the page's contents.
     * @param name The name of the new page. This will replace the canvas's title.
     */
    public void addPage(final @NotNull Div page, final @NotNull Component name) {
        page.setSize(new Vector2f(1f, 1f - TITLE_H - TOOLBAR_H));
        page.setPosY(TOOLBAR_H);
        pages.add(page);
        pageNames.add(name);
    }

    /**
     * Adds a new page.
     * @param page The element containing the page's contents.
     * @param name The name of the new page. This will replace the canvas's title.
     */
    public void addPage(final @NotNull Div page, final @NotNull String name) {
        addPage(page, new Txt(name).white().get());
    }
}