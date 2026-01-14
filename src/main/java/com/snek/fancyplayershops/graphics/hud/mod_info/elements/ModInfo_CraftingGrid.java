package com.snek.fancyplayershops.graphics.hud.mod_info.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import com.snek.fancyplayershops.main.DisplayTier;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.data_types.graphics.Axis2;
import com.snek.frameworklib.enhanced_recipes.shaped.EnhancedShapedRecipe;
import com.snek.frameworklib.graphics.basic.presets.ItemElm_Gui;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.graphics.designs.SymbolDesigns;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.layout.Flex;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;




public class ModInfo_CraftingGrid extends Div {
    public static final float GRID_SIZE = 0.8f;
    public static final float SLOT_SIZE = GRID_SIZE / 3f;
    public static final float SPACING = 0.1f;




    public ModInfo_CraftingGrid(final @NotNull HudContext context, final @NotNull DisplayTier tier, final float xyRatio) {
        super();
        Div e;


        // Add Flex container
        final Div flex = addChild(new Flex(Axis2.X)); {

            // Add 3x3
            final Div grid = flex.addChild(new Div());
            grid.setSize(new Vector2f(GRID_SIZE * xyRatio, GRID_SIZE));
            grid.setAlignmentY(AlignmentY.CENTER); {

                // Get recipe. If the recipe exists
                final var optRecipe = FrameworkLib.getServer().getRecipeManager().byKey(new ResourceLocation(FancyPlayerShops.MOD_ID, tier.getId()));
                if(optRecipe.isPresent()) {
                    final var enhancedRecipe = (EnhancedShapedRecipe)optRecipe.get();
                    final var ingredients = enhancedRecipe.getIngredients();

                    // For each ingredient, create a new item display
                    for(int i = 0; i < ingredients.size(); ++i) {
                        final var items = ingredients.get(i).getItems();
                        final var firstItem = items.length == 0 ? Items.AIR.getDefaultInstance() : items[0];
                        final var overrideId = enhancedRecipe.getClientOverrides().get(i);
                        final var overrideItem = overrideId == null ? null : EnhancedShapedRecipe.getItemStackReference(new ResourceLocation(overrideId));
                        e = grid.addChild(new ItemElm_Gui(context.getLevel(), overrideItem != null ? overrideItem : firstItem));
                        e.setSize(new Vector2f(1f / 3f));
                        e.setAlignmentX(switch(i % 3) { case 0 -> AlignmentX.LEFT; case 1 -> AlignmentX.CENTER; default -> AlignmentX.RIGHT;  });
                        e.setAlignmentY(switch(i / 3) { case 0 -> AlignmentY.TOP;  case 1 -> AlignmentY.CENTER; default -> AlignmentY.BOTTOM; });
                    }

                    // Add arrow
                    e = flex.addChild(new Div());
                    e.setSize(new Vector2f(SLOT_SIZE * xyRatio + 2f * SPACING, SLOT_SIZE));
                    e.setAlignmentY(AlignmentY.CENTER);
                    e.addDesign(context.getLevel(), SymbolDesigns.ArrowPointingRight);

                    // Add result slot
                    e = flex.addChild(new ItemElm_Gui(context.getLevel(), enhancedRecipe.getResultItem(context.getLevel().registryAccess())));
                    e.setSize(new Vector2f(SLOT_SIZE * xyRatio, SLOT_SIZE));
                    e.setAlignmentY(AlignmentY.CENTER);
                }
            }
        }
    }
}
