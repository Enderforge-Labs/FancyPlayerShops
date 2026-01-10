package com.snek.fancyplayershops.mixin;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.main.DisplayTier;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.frameworklib.FrameworkLib;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.ShapedRecipe;







//TODO move to framework lib
//TODO add a custom recipe list that allows nbts or something

/**
 * A mixin that detects display crafting recipes and replaces the default steve head in the output slot with a display item.
 * Minecraft 1.20.1 doesn't allow NBTs in recipe outputs.
 * This is a workaround that allows the mod to set custom NBT data in the result slot.
 */
@Mixin(CraftingMenu.class)
public abstract class CraftingScreenHandlerMixin {


    // Shadow the result field to access it directly
    @Shadow private ResultContainer resultSlots;




    /**
     * Detects the current items in the crafting grid and replaces the output with the display item if necessary.
     */
    @Inject(method = "slotsChanged", at = @At("TAIL"))
    private void onCraftUpdate(final @NotNull Container inventory, final @NotNull CallbackInfo ci) {
        if(inventory.getContainerSize() != 9) return;


        // For each display tier
        for(final var tier: DisplayTier.values()) {

            // Get recipe manager from the server and retrieve the display's recipe
            final RecipeManager recipeManager = FrameworkLib.getServer().getRecipeManager();
            final ResourceLocation recipeId = new ResourceLocation(FancyPlayerShops.MOD_ID, tier.getId());
            final Optional<? extends Recipe<?>> recipeOptional = recipeManager.byKey(recipeId);
            if(recipeOptional.isEmpty()) throw new RuntimeException("The crafting recipe of the display item could not be found: " + recipeId);
            final Recipe<?> recipe = recipeOptional.get();


            // Check if the grid matches the recipe
            if(recipe instanceof final ShapedRecipe r) {
                if(r.matches((CraftingContainer)inventory, null)) { //! Level parameter isn't actually used by the function

                    // Replace the output item
                    resultSlots.setItem(0, ProductDisplayManager.getProductDisplayItemCopy(tier));
                    ((CraftingMenu)(Object)this).broadcastChanges();

                    // Stop checking recipes
                    break;
                }
            }
        }
    }
}