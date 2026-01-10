package com.snek.fancyplayershops.mixin;

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




@Mixin(CraftingMenu.class)
public class CraftingMenuMixin {

    @Inject(
        method = "slotChangedCraftingGrid",
        at = @At("RETURN")
    )
    private static void syncCustomRecipeResult(
        AbstractContainerMenu menu,
        Level level,
        Player player,
        CraftingContainer container,
        ResultContainer result,
        CallbackInfo ci
    ) {

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            ItemStack craftResult = result.getItem(0);
            if (!craftResult.isEmpty()) {
                RecipeManager recipeManager = level.getRecipeManager();
                Optional<CraftingRecipe> recipe = recipeManager.getRecipeFor(
                    RecipeType.CRAFTING,
                    container,
                    level
                );
                // Check if the recipe used was the custom type
                if (recipe.isPresent() && recipe.get().getSerializer() == FancyPlayerShops.NBT_SHAPED_SERIALIZER) {
                    // Force send the result slot to the client
                    serverPlayer.connection.send(
                        new ClientboundContainerSetSlotPacket(
                            menu.containerId,
                            menu.incrementStateId(),
                            0, // result slot
                            craftResult
                        )
                    );
               }
            }
        }
    }
}