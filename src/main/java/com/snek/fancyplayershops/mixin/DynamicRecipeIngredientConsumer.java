package com.snek.fancyplayershops.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.fancyplayershops.recipes.DynamicShapedRecipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;




@Mixin(ResultSlot.class)
public abstract class DynamicRecipeIngredientConsumer {

    @Shadow
    private CraftingContainer craftSlots;

    @Shadow
    private Player player;

    @Shadow
    protected abstract void checkTakeAchievements(ItemStack stack);




    @Inject(method = "onTake", at = @At("HEAD"), cancellable = true)
    public void onTake(Player player, ItemStack stack, CallbackInfo ci) {

        // Get current recipe
        final var recipeHolder = player.level()
            .getRecipeManager()
            .getRecipeFor(RecipeType.CRAFTING, craftSlots, player.level())
            .orElse(null)
        ;

        // Remove custom amount if the recipe is a DynamicShapedRecipe
        if(recipeHolder != null && recipeHolder instanceof DynamicShapedRecipe dynamicRecipe) {
            Map<Integer, Integer> requiredCounts = dynamicRecipe.getRequiredCounts();

            //! Vanilla logic with custom item removal amount - Vanilla uses an hard coded 1
            this.checkTakeAchievements(stack);
            NonNullList<ItemStack> nonNullList = player.level().getRecipeManager().getRemainingItemsFor(RecipeType.CRAFTING, this.craftSlots, player.level());
            for(int i = 0; i < nonNullList.size(); ++i) {
                ItemStack itemStack = this.craftSlots.getItem(i);
                ItemStack itemStack2 = (ItemStack)nonNullList.get(i);
                if (!itemStack.isEmpty()) {
                    this.craftSlots.removeItem(i, requiredCounts.get(i));
                    itemStack = this.craftSlots.getItem(i);
                }
            }
            //! Stop the vanilla method from running
            ci.cancel();
        }

        //! If the recipe is not a DynamicShapedRecipe, don't cancel the callback info
        //! This lets the Vanilla method execute the defualt logic
    }



    // @Redirect(
    //     method = "onTake",
    //     at = @At(
    //         value = "INVOKE",
    //         target = "Lnet/minecraft/world/inventory/CraftingContainer;removeItem(II)Lnet/minecraft/world/item/ItemStack;"
    //     )
    // )
    // private ItemStack removeItem(CraftingContainer container, int slot, int amount) {

    //     // Get current recipe
    //     final var recipeHolder = player.level()
    //         .getRecipeManager()
    //         .getRecipeFor(RecipeType.CRAFTING, container, player.level())
    //         .orElse(null)
    //     ;

    //     // Remove custom amount if the recipe is custom
    //     if(recipeHolder != null && recipeHolder instanceof DynamicShapedRecipe dynamicRecipe) {
    //         Map<Integer, Integer> requiredCounts = dynamicRecipe.getRequiredCounts();
    //         for(var c : requiredCounts.entrySet()) System.out.print("" + c.getValue() + ", ");
    //         Integer requiredCount = requiredCounts.get(slot);
    //         System.out.println("removing " + (requiredCount != null ? requiredCount : 1) + " items from slot #" + slot);
    //         container.removeItem(slot, requiredCount != null ? requiredCount : 1);

    //         //! Return an empty item stack. This signals ResultSlot.onTake to skip the remaining logic
    //         return ItemStack.EMPTY;
    //     }

    //     // Remove normal amount otherwise
    //     else {
    //         return container.removeItem(slot, amount);
    //     }
    // }
}