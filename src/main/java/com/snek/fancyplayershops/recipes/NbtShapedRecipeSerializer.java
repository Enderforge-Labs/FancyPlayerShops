package com.snek.fancyplayershops.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.snek.frameworklib.debug.Require;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;






//TODO rename to DynamicShapedRecipeSerializer
public class NbtShapedRecipeSerializer implements RecipeSerializer<DynamicShapedRecipe> {
    public static final @NotNull String DYNAMIC_REF_PLACEHOLDER = "frameworklib:dynamic_ref";
    public static final @NotNull String ANY_NBT_PLACEHOLDER     = "frameworklib:any_nbt";
    public static final @NotNull String ALL_NBTS_PLACEHOLDER    = "frameworklib:all_nbts";
    public static final @NotNull String COUNT                   = "frameworklib:count";

    @Override
    public DynamicShapedRecipe fromJson(ResourceLocation id, JsonObject json) {


        // Parse ingredients
        JsonObject keyObj = json.getAsJsonObject("key");
        Map<String, Ingredient> keyMap = new HashMap<>();
        Map<String, Integer> ingredientRequiredCounts = new HashMap<>();
        Map<String, ItemStack> dynamicReferenceIngredients = new HashMap<>();
        Map<String, List<String>> anyNbtIngredients = new HashMap<>();
        Map<String, List<String>> allNbtsIngredients = new HashMap<>();

        for(Map.Entry<String, JsonElement> entry : keyObj.entrySet()) {
            JsonObject ingredientObj = entry.getValue().getAsJsonObject();

            // Extract count if present
            int count = ingredientObj.has(COUNT) ? ingredientObj.get(COUNT).getAsInt() : 1;
            ingredientRequiredCounts.put(entry.getKey(), count);


            // Dynamic stack reference case
            if(ingredientObj.has(DYNAMIC_REF_PLACEHOLDER)) {
                String refId = ingredientObj.get(DYNAMIC_REF_PLACEHOLDER).getAsString();
                ResourceLocation refLocation = new ResourceLocation(refId);
                ItemStack refStack = DynamicShapedRecipe.getItemStackReference(refLocation);
                if(refStack.isEmpty()) throw new JsonSyntaxException("Unknown ItemStack reference: " + refId);

                // Store for NBT checking later. Use vanilla ingredient for base item type matching
                dynamicReferenceIngredients.put(entry.getKey(), refStack);
                keyMap.put(entry.getKey(), Ingredient.of(refStack.getItem()));
            }

            // Any tag case
            else if(ingredientObj.has(ANY_NBT_PLACEHOLDER)) {
                JsonArray nbtNamesArray = ingredientObj.get(ANY_NBT_PLACEHOLDER).getAsJsonArray();
                final List<String> nbtNamesList = new ArrayList<>();
                for(final JsonElement obj : nbtNamesArray.asList()) nbtNamesList.add(obj.getAsString());

                // Store for NBT checking later. Use empty ingredient as a placeholder
                anyNbtIngredients.put(entry.getKey(), nbtNamesList );
                keyMap.put(entry.getKey(), Ingredient.EMPTY);
            }

            // All tags case
            else if(ingredientObj.has(ALL_NBTS_PLACEHOLDER)) {
                JsonArray nbtNamesArray = ingredientObj.get(ALL_NBTS_PLACEHOLDER).getAsJsonArray();
                final List<String> nbtNamesList = new ArrayList<>();
                for(final JsonElement obj : nbtNamesArray.asList()) nbtNamesList.add(obj.getAsString());

                // Store for NBT checking later. Use empty ingredient as a placeholder
                allNbtsIngredients.put(entry.getKey(), nbtNamesList );
                keyMap.put(entry.getKey(), Ingredient.EMPTY);
            }

            // Vanilla item case
            else {
                keyMap.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
            }
        }


        // Parse result
        JsonObject resultObj = json.getAsJsonObject("result");
        ItemStack resultStack; {

            // Dynamic stack reference case
            if(resultObj.has(DYNAMIC_REF_PLACEHOLDER)) {
                String refId = resultObj.get(DYNAMIC_REF_PLACEHOLDER).getAsString();
                ResourceLocation refLocation = new ResourceLocation(refId);
                resultStack = DynamicShapedRecipe.getItemStackReference(refLocation);
                if(resultStack.isEmpty()) throw new JsonSyntaxException("Unknown ItemStack reference: " + refId);
            }

            // Vanilla item case
            else {
                resultStack = ShapedRecipe.itemStackFromJson(resultObj);
            }
        }


        // Parse pattern
        JsonArray patternArray = json.getAsJsonArray("pattern");
        String[] pattern = new String[patternArray.size()];
        for(int i = 0; i < patternArray.size(); i++) {
            pattern[i] = patternArray.get(i).getAsString();
        }


        // Map character positions to slot indices for NBT checking
        Map<Integer, Integer> requiredCountSlots = new HashMap<>();
        Map<Integer, ItemStack> dynamicReferenceSlots = new HashMap<>();
        Map<Integer, List<String>> anyNbtSlots = new HashMap<>();
        Map<Integer, List<String>> allNbtsSlots = new HashMap<>();
        int width = pattern[0].length();
        for(int i = 0; i < pattern.length; i++) {
            String row = pattern[i];
            for(int j = 0; j < row.length(); j++) {
                char c = row.charAt(j);
                if(c != ' ') {
                    final String cStr = String.valueOf(c);

                    // Save count
                    assert Require.nonNull(ingredientRequiredCounts.get(cStr), "extracted material count");
                    assert Require.positive(ingredientRequiredCounts.get(cStr), "specified material count"); {
                        int slot = j + i * width;
                        requiredCountSlots.put(slot, ingredientRequiredCounts.get(cStr));
                    }

                    // Save ingredient type
                    if(dynamicReferenceIngredients.containsKey(cStr)) {
                        int slot = j + i * width;
                        dynamicReferenceSlots.put(slot, dynamicReferenceIngredients.get(cStr));
                    }
                    else if(anyNbtIngredients.containsKey(cStr)) {
                        int slot = j + i * width;
                        anyNbtSlots.put(slot, anyNbtIngredients.get(cStr));
                    }
                    else if(allNbtsIngredients.containsKey(cStr)) {
                        int slot = j + i * width;
                        allNbtsSlots.put(slot, allNbtsIngredients.get(cStr));
                    }
                }
            }
        }


        // Read group and category
        String group = json.has("group") ? json.get("group").getAsString() : "";
        CraftingBookCategory category = json.has("category")
            ? CraftingBookCategory.CODEC.byName(json.get("category").getAsString(), CraftingBookCategory.MISC)
            : CraftingBookCategory.MISC
        ;


        // Return data
        return new DynamicShapedRecipe(
            id, group, category, pattern, keyMap, resultStack,
            requiredCountSlots, dynamicReferenceSlots, anyNbtSlots, allNbtsSlots
        );
    }






    @Override
    public void toNetwork(FriendlyByteBuf buffer, DynamicShapedRecipe recipe) {
        //! Empty method. Client and server never communicate this.
        //! The client doesn't know these recipes exist, so network stuff is not needed
        buffer.writeUtf("");
    }

    @Override
    public DynamicShapedRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        //! Empty method. Client and server never communicate this.
        //! The client doesn't know these recipes exist, so network stuff is not needed
        buffer.readUtf();
        return null;
    }

    // @Override
    // public DynamicShapedRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
    //     ShapedRecipe base = RecipeSerializer.SHAPED_RECIPE.fromNetwork(id, buffer);

    //     int nbtSlotsCount = buffer.readVarInt();
    //     Map<Integer, ItemStack> nbtRequiredSlots = new HashMap<>();
    //     for(int i = 0; i < nbtSlotsCount; i++) {
    //         int slot = buffer.readVarInt();
    //         ItemStack stack = buffer.readItem();
    //         nbtRequiredSlots.put(slot, stack);
    //     }

    //     return new DynamicShapedRecipe(base, nbtRequiredSlots);
    // }




    // @Override
    // public void toNetwork(FriendlyByteBuf buffer, DynamicShapedRecipe recipe) {
    //     RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);

    //     buffer.writeVarInt(recipe.getRequiredSlots().size());
    //     for(Map.Entry<Integer, ItemStack> entry : recipe.getRequiredSlots().entrySet()) {
    //         buffer.writeVarInt(entry.getKey());
    //         buffer.writeItem(entry.getValue());
    //     }
    // }
}