package com.snek.fancyplayershops.recipes;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;







public class NbtShapedRecipeSerializer implements RecipeSerializer<NbtShapedRecipe> {

    @Override
    public NbtShapedRecipe fromJson(ResourceLocation id, JsonObject json) {
        JsonObject keyObj = json.getAsJsonObject("key");
        Map<String, Ingredient> keyMap = new HashMap<>();
        Map<String, ItemStack> customReferences = new HashMap<>(); // Track which keys need NBT checking

        for (Map.Entry<String, JsonElement> entry : keyObj.entrySet()) {
            JsonObject ingredientObj = entry.getValue().getAsJsonObject();

            if (ingredientObj.has("fancyplayershops:item_stack_reference")) {
                String refId = ingredientObj.get("fancyplayershops:item_stack_reference").getAsString();
                ResourceLocation refLocation = new ResourceLocation(refId);
                ItemStack refStack = NbtShapedRecipe.getItemStackReference(refLocation);

                if (refStack.isEmpty()) {
                    throw new JsonSyntaxException("Unknown ItemStack reference: " + refId);
                }

                // Store for NBT checking later
                customReferences.put(entry.getKey(), refStack);
                // Use vanilla ingredient for base item type matching
                keyMap.put(entry.getKey(), Ingredient.of(refStack.getItem()));
            } else {
                keyMap.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
            }
        }

        // Parse result
        JsonObject resultObj = json.getAsJsonObject("result");
        ItemStack resultStack;

        if (resultObj.has("fancyplayershops:item_stack_reference")) {
            String refId = resultObj.get("fancyplayershops:item_stack_reference").getAsString();
            ResourceLocation refLocation = new ResourceLocation(refId);
            resultStack = NbtShapedRecipe.getItemStackReference(refLocation);

            if (resultStack.isEmpty()) {
                throw new JsonSyntaxException("Unknown ItemStack reference: " + refId);
            }
        } else {
            resultStack = ShapedRecipe.itemStackFromJson(resultObj);
        }

        // Parse pattern
        JsonArray patternArray = json.getAsJsonArray("pattern");
        String[] pattern = new String[patternArray.size()];
        for (int i = 0; i < patternArray.size(); i++) {
            pattern[i] = patternArray.get(i).getAsString();
        }

        // Map character positions to slot indices for NBT checking
        Map<Integer, ItemStack> nbtRequiredSlots = new HashMap<>();
        int width = pattern[0].length();
        for (int i = 0; i < pattern.length; i++) {
            String row = pattern[i];
            for (int j = 0; j < row.length(); j++) {
                char c = row.charAt(j);
                if (c != ' ' && customReferences.containsKey(String.valueOf(c))) {
                    int slot = j + i * width;
                    nbtRequiredSlots.put(slot, customReferences.get(String.valueOf(c)));
                }
            }
        }

        String group = json.has("group") ? json.get("group").getAsString() : "";
        CraftingBookCategory category = json.has("category")
            ? CraftingBookCategory.CODEC.byName(json.get("category").getAsString(), CraftingBookCategory.MISC)
            : CraftingBookCategory.MISC;

        return new NbtShapedRecipe(id, group, category, pattern, keyMap, resultStack, nbtRequiredSlots);
    }


    @Override
    public NbtShapedRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        ShapedRecipe base = RecipeSerializer.SHAPED_RECIPE.fromNetwork(id, buffer);

        int nbtSlotsCount = buffer.readVarInt();
        Map<Integer, ItemStack> nbtRequiredSlots = new HashMap<>();
        for (int i = 0; i < nbtSlotsCount; i++) {
            int slot = buffer.readVarInt();
            ItemStack stack = buffer.readItem();
            nbtRequiredSlots.put(slot, stack);
        }

        return new NbtShapedRecipe(base, nbtRequiredSlots);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, NbtShapedRecipe recipe) {
        RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);

        buffer.writeVarInt(recipe.getRequiredSlots().size());
        for (Map.Entry<Integer, ItemStack> entry : recipe.getRequiredSlots().entrySet()) {
            buffer.writeVarInt(entry.getKey());
            buffer.writeItem(entry.getValue());
        }
    }
}