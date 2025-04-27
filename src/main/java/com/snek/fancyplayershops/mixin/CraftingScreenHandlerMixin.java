package com.snek.fancyplayershops.mixin;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.util.Identifier;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.fancyplayershops.main.FancyPlayerShops;








/**
 * A mixin that detects shop crafting recipes and replaces the default steve head in the output slot with a shop item.
 * Minecraft 1.20.1 doesn't allow NBTs in recipe outputs.
 * This is a workaround that allows the mod to set custom NBT data in the result slot.
 */
@Mixin(CraftingScreenHandler.class)
public abstract class CraftingScreenHandlerMixin {


    // Shadow the result field to access it directly
    @Shadow private CraftingResultInventory result;




    /**
     * Detects the current items in the crafting grid and replaces the output with the shop item if necessary.
     */
    @Inject(method = "onContentChanged", at = @At("TAIL"))
    private void onCraftUpdate(Inventory inventory, CallbackInfo ci) {
        if(FancyPlayerShops.getServer() == null) throw new RuntimeException("Couldn't handle crafting content change: server instance is null.");
        if(inventory.size() != 9) return;


        // Get recipe manager from the server and retrieve the shop's recipe
        final RecipeManager recipeManager = FancyPlayerShops.getServer().getRecipeManager();
        final Identifier recipeId = new Identifier(FancyPlayerShops.MOD_ID, "shop_item");
        final Optional<? extends Recipe<?>> recipeOptional = recipeManager.get(recipeId);
        if(recipeOptional.isEmpty()) throw new RuntimeException("The crafting recipe of the shop item could not be found: " + recipeId + ".");
        final Recipe<?> recipe = recipeOptional.get();


        // Check if the grid matches the recipe
        if(recipe instanceof ShapedRecipe r) {
            if(r.matches((CraftingInventory)inventory, null)) { //! World parameter isn't actually used by the function

                // Replace the output item
                result.setStack(0, FancyPlayerShops.shopItem.copy());
                ((CraftingScreenHandler)(Object)this).sendContentUpdates();
            }
        }
    }
}