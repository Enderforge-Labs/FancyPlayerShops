package com.snek.fancyplayershops.mixin.recipes.enhanced;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.fancyplayershops.recipes.EnhancedShapedRecipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;




/**
 * A mixin for FrameworkLib's {@link EnhancedShapedRecipe}.
 * <p>
 * Minecraft Vanilla can only handle crafting recipes that consume at most 1 item from each ingredient slot.
 * <p>
 * This mixin detects {@link EnhancedShapedRecipe}s and replaces their item consumption logic with a custom one that
 * takes the amount of items
 */
@Mixin(ResultSlot.class)
public abstract class IngredientConsumerMixin {

    @Shadow
    private CraftingContainer craftSlots;

    @Shadow
    private Player player;

    @Shadow
    protected abstract void checkTakeAchievements(ItemStack stack);




    @Inject(method = "onTake", at = @At("HEAD"), cancellable = true)
    public void onTake(final Player player, final ItemStack stack, final CallbackInfo ci) {

        // Get current recipe
        final var recipeHolder = player.level()
            .getRecipeManager()
            .getRecipeFor(RecipeType.CRAFTING, craftSlots, player.level())
            .orElse(null)
        ;

        // Remove custom amount if the recipe is a EnhancedShapedRecipe
        if(recipeHolder != null && recipeHolder instanceof final EnhancedShapedRecipe enhancedRecipe) {
            final Map<Integer, Integer> requiredCounts = enhancedRecipe.getRequiredCounts();

            //! Vanilla logic with custom item removal amount
            //! Vanilla uses an hard coded 1 and has some extra logic after this for loop
            this.checkTakeAchievements(stack);
            final NonNullList<ItemStack> nonNullList = player.level().getRecipeManager().getRemainingItemsFor(RecipeType.CRAFTING, this.craftSlots, player.level());
            for(int i = 0; i < nonNullList.size(); ++i) {
                final ItemStack itemStack = this.craftSlots.getItem(i);
                if (!itemStack.isEmpty()) {
                    this.craftSlots.removeItem(i, requiredCounts.get(i));
                }
            }
            //! Stop the vanilla method from running
            ci.cancel();
        }

        //! If the recipe is not a EnhancedShapedRecipe, don't cancel the callback info
        //! This lets the Vanilla method execute the defualt logic
    }
}