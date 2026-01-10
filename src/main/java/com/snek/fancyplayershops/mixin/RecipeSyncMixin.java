package com.snek.fancyplayershops.mixin;

import java.util.Collection;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.snek.fancyplayershops.main.FancyPlayerShops;

import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.world.item.crafting.Recipe;




@Mixin(ClientboundUpdateRecipesPacket.class)
public class RecipeSyncMixin {

    @ModifyVariable(
        method = "<init>",
        at = @At("HEAD"),
        argsOnly = true,
        ordinal = 0
    )
    private static Collection<Recipe<?>> filterCustomRecipes(Collection<Recipe<?>> recipes) {
        return recipes.stream()
            .filter(recipe -> recipe.getSerializer() != FancyPlayerShops.NBT_SHAPED_SERIALIZER)
            .toList()
        ;
    }
}