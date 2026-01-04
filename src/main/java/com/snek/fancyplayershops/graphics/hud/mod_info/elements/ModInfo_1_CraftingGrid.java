package com.snek.fancyplayershops.graphics.hud.mod_info.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.graphics.hud.mod_info.ModInfoCanvas;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.graphics.basic.elements.ItemElm;
import com.snek.frameworklib.graphics.basic.styles.ConfigurableItemElmStyle;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.designs.SymbolDesigns;
import com.snek.frameworklib.graphics.layout.Div;

import net.minecraft.resources.ResourceLocation;




public class ModInfo_1_CraftingGrid extends Div {
    public static final float PARENT_RATIO = ModInfoCanvas.P1_CRAFTING_GRID_H;
    public static final Vector2f GRID_SIZE = new Vector2f(PARENT_RATIO, 1f);
    public static final Vector2f SLOT_SIZE = new Vector2f(PARENT_RATIO, 1f).div(3f);




    public ModInfo_1_CraftingGrid(final @NotNull HudContext context) {
        super();
        Div e;


        // Add 3x3
        final Div c = addChild(new Div());
        c.setSize(GRID_SIZE);
        c.setAlignmentY(AlignmentY.CENTER); {

            // Get recipe. If the recipe exists
            final var optRecipe = FrameworkLib.getServer().getRecipeManager().byKey(new ResourceLocation(FancyPlayerShops.MOD_ID, "product_display_item"));
            if(optRecipe.isPresent()) {
                final var ingredients = optRecipe.get().getIngredients();

                // For each ingredient, create a new item display
                for(int i = 0; i < ingredients.size(); ++i) {
                    e = c.addChild(new ItemElm(context.getLevel(), new ConfigurableItemElmStyle(ingredients.get(i).getItems()[0], SLOT_SIZE.x)));
                    e.setSize(new Vector2f(1f / 3f));
                    e.setAlignmentX(switch(i % 3){ case 0 -> AlignmentX.LEFT; case 1 -> AlignmentX.CENTER; default -> AlignmentX.RIGHT;  });
                    e.setAlignmentY(switch(i / 3){ case 0 -> AlignmentY.TOP;  case 1 -> AlignmentY.CENTER; default -> AlignmentY.BOTTOM; });
                }
            }
        }


        // Add arrow
        e = addChild(new Div());
        e.setSize(SLOT_SIZE);
        e.setAlignmentY(AlignmentY.CENTER);
        e.addDesign(context.getLevel(), SymbolDesigns.ArrowPointingRight);


        // Add result slot
        e = addChild(new ItemElm(context.getLevel(), new ConfigurableItemElmStyle(ProductDisplayManager.getProductDisplayItemCopy(), SLOT_SIZE.x)));
        e.setSize(SLOT_SIZE);
        e.setAlignmentY(AlignmentY.CENTER);
    }
}
