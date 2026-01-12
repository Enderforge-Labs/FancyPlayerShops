package com.snek.fancyplayershops.mixin.recipes.enhanced;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.snek.fancyplayershops.main.FancyPlayerShops;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;




/**
 * A mixin for FrameworkLib's {@link EnhancedShapedRecipe}.
 * <p>
 * Not having a client-side mod makes it so the player cannot see the output item.
 * <p>
 * This mixin sends a packet to the client to force it to display the proper result.
 */
@SuppressWarnings("java:S1118") //! Add private constructor to hide the public implicit one
@Mixin(CraftingMenu.class)
public class ClientResultSlotSyncMixin {


    @Inject(method = "slotChangedCraftingGrid", at = @At("RETURN"))
    private static void syncCustomRecipeResult(
        final AbstractContainerMenu menu,
        final Level level,
        final Player player,
        final CraftingContainer container,
        final ResultContainer result,
        final CallbackInfo ci
    ) {

        // Get result item stack. If it's not empty
        final int RESULT_SLOT_INDEX = 0;
        final ItemStack craftResult = result.getItem(RESULT_SLOT_INDEX);
        if(!craftResult.isEmpty()) {

            // Check if this recipe is a EnhancedShapedRecipe. If it is
            final RecipeManager recipeManager = level.getRecipeManager();
            final Optional<CraftingRecipe> recipe = recipeManager.getRecipeFor(RecipeType.CRAFTING, container, level);
            if(recipe.isPresent() && recipe.get().getSerializer() == FancyPlayerShops.NBT_SHAPED_SERIALIZER) {

                // Force send the result slot to the client
                ((ServerPlayer)player).connection.send(
                    new ClientboundContainerSetSlotPacket(
                        menu.containerId,
                        menu.incrementStateId(),
                        RESULT_SLOT_INDEX,
                        craftResult
                    )
                );
            }
        }
    }
}