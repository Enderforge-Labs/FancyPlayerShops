package com.snek.fancyplayershops.mixin.recipes.enhanced;

import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.recipes.EnhancedShapedRecipe;

import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.world.item.crafting.Recipe;




/**
 * A mixin for FrameworkLib's {@link EnhancedShapedRecipe}.
 * <p>
 * By default, Minecraft Vanilla sends all registered recipes to the client.
 * When the client receives a recipe it doesn't recognize, it disconnects instantly.
 * <p>
 * This mixin modifies the list of recipes passed to {@link ClientboundUpdateRecipesPacket}'s constructor,
 * filtering out any {@link EnhancedShapedRecipe} recipes.
 * This stops their packets from ever being sent to the client.
 */
@SuppressWarnings("java:S1118") //! Add private constructor to hide the public implicit one
@Mixin(ClientboundUpdateRecipesPacket.class)
public class RecipePacketSuppressorMixin {


    @ModifyVariable(
        method = "<init>",
        at = @At("HEAD"),
        argsOnly = true,
        ordinal = 0
    )
    private static Collection<Recipe<?>> filterCustomRecipes(final Collection<Recipe<?>> recipes) {
        return recipes.stream()
            .filter(recipe -> recipe.getSerializer() != FancyPlayerShops.NBT_SHAPED_SERIALIZER)
            .toList()
        ;
    }
}