package com.snek.fancyplayershops.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.resource.Resource;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

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
    @Shadow
    private CraftingResultInventory result;




    /**
     * Detects the current items in the crafting grid and replaces the output with the shop item if necessary.
     */
    @Inject(method = "onContentChanged", at = @At("TAIL"))
    private void onCraftUpdate(Inventory inventory, CallbackInfo ci) {
        if(FancyPlayerShops.getServer() == null) throw new RuntimeException("Couldn't handle crafting content change: server instance is null.");
        if(inventory.size() != 9) return;


        // Get recipe manager from a world and retrieve the shop's recipe
        final ServerWorld world = FancyPlayerShops.getServer().getWorlds().iterator().next();
        final RecipeManager recipeManager = world.getRecipeManager();
        final Identifier recipeId = new Identifier(FancyPlayerShops.MOD_ID, "shop_item");
        final Optional<? extends Recipe<?>> recipeOptional = recipeManager.get(recipeId);
        if(recipeOptional.isEmpty()) throw new RuntimeException("The crafting recipe of the shop item could not be found: " + recipeId + ".");
        final Recipe<?> recipe = recipeOptional.get();


        // Check if the grid matches the recipe
        if (recipe instanceof ShapedRecipe r) {
            if(r.matches((CraftingInventory)inventory, world)) {

                // Replace the output item
                CraftingScreenHandler self = (CraftingScreenHandler)(Object)this;
                result.setStack(0, FancyPlayerShops.shopItem.copy());
                self.sendContentUpdates();
            }
        }
    }
}