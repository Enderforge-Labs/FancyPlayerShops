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






public class EnhancedShapedRecipeSerializer implements RecipeSerializer<EnhancedShapedRecipe> {
    public static final @NotNull String DYNAMIC_REF_PLACEHOLDER = "frameworklib:dynamic_ref";
    public static final @NotNull String ANY_NBT_PLACEHOLDER     = "frameworklib:any_nbt";
    public static final @NotNull String ALL_NBTS_PLACEHOLDER    = "frameworklib:all_nbts";
    public static final @NotNull String COUNT                   = "frameworklib:count";




    @Override
    public EnhancedShapedRecipe fromJson(final ResourceLocation id, final JsonObject json) {


        // Parse ingredients
        final JsonObject keyObj = json.getAsJsonObject("key");
        final Map<String, Ingredient> keyMap = new HashMap<>();
        final Map<String, Integer> ingredientRequiredCounts = new HashMap<>();
        final Map<String, ItemStack> dynamicReferenceIngredients = new HashMap<>();
        final Map<String, List<String>> anyNbtIngredients = new HashMap<>();
        final Map<String, List<String>> allNbtsIngredients = new HashMap<>();

        for(final Map.Entry<String, JsonElement> entry : keyObj.entrySet()) {
            final JsonObject ingredientObj = entry.getValue().getAsJsonObject();

            // Extract count if present
            final int count = ingredientObj.has(COUNT) ? ingredientObj.get(COUNT).getAsInt() : 1;
            ingredientRequiredCounts.put(entry.getKey(), count);


            // Dynamic stack reference case
            if(ingredientObj.has(DYNAMIC_REF_PLACEHOLDER)) {
                final String refId = ingredientObj.get(DYNAMIC_REF_PLACEHOLDER).getAsString();
                final ResourceLocation refLocation = new ResourceLocation(refId);
                final ItemStack refStack = EnhancedShapedRecipe.getItemStackReference(refLocation);
                if(refStack.isEmpty()) throw new JsonSyntaxException("Unknown ItemStack reference: " + refId);

                // Store for NBT checking later. Use vanilla ingredient for base item type matching
                dynamicReferenceIngredients.put(entry.getKey(), refStack);
                keyMap.put(entry.getKey(), Ingredient.of(refStack.getItem()));
            }

            // Any tag case
            else if(ingredientObj.has(ANY_NBT_PLACEHOLDER)) {
                final JsonArray nbtNamesArray = ingredientObj.get(ANY_NBT_PLACEHOLDER).getAsJsonArray();
                final List<String> nbtNamesList = new ArrayList<>();
                for(final JsonElement obj : nbtNamesArray.asList()) nbtNamesList.add(obj.getAsString());

                // Store for NBT checking later. Use empty ingredient as a placeholder
                anyNbtIngredients.put(entry.getKey(), nbtNamesList );
                keyMap.put(entry.getKey(), Ingredient.EMPTY);
            }

            // All tags case
            else if(ingredientObj.has(ALL_NBTS_PLACEHOLDER)) {
                final JsonArray nbtNamesArray = ingredientObj.get(ALL_NBTS_PLACEHOLDER).getAsJsonArray();
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
        final JsonObject resultObj = json.getAsJsonObject("result");
        ItemStack resultStack; {

            // Dynamic stack reference case
            if(resultObj.has(DYNAMIC_REF_PLACEHOLDER)) {
                final String refId = resultObj.get(DYNAMIC_REF_PLACEHOLDER).getAsString();
                final ResourceLocation refLocation = new ResourceLocation(refId);
                resultStack = EnhancedShapedRecipe.getItemStackReference(refLocation);
                if(resultStack.isEmpty()) throw new JsonSyntaxException("Unknown ItemStack reference: " + refId);
            }

            // Vanilla item case
            else {
                resultStack = ShapedRecipe.itemStackFromJson(resultObj);
            }
        }


        // Parse pattern
        final JsonArray patternArray = json.getAsJsonArray("pattern");
        final String[] pattern = new String[patternArray.size()];
        for(int i = 0; i < patternArray.size(); i++) {
            pattern[i] = patternArray.get(i).getAsString();
        }


        // Map character positions to slot indices for NBT checking
        final Map<Integer, Integer> requiredCountSlots = new HashMap<>();
        final Map<Integer, ItemStack> dynamicReferenceSlots = new HashMap<>();
        final Map<Integer, List<String>> anyNbtSlots = new HashMap<>();
        final Map<Integer, List<String>> allNbtsSlots = new HashMap<>();
        final int width = pattern[0].length();
        for(int i = 0; i < pattern.length; i++) {
            final String row = pattern[i];
            for(int j = 0; j < row.length(); j++) {
                final char c = row.charAt(j);
                if(c != ' ') {
                    final String cStr = String.valueOf(c);

                    // Save count
                    assert Require.nonNull(ingredientRequiredCounts.get(cStr), "extracted material count");
                    assert Require.positive(ingredientRequiredCounts.get(cStr), "specified material count"); {
                        final int slot = j + i * width;
                        requiredCountSlots.put(slot, ingredientRequiredCounts.get(cStr));
                    }

                    // Save ingredient type
                    if(dynamicReferenceIngredients.containsKey(cStr)) {
                        final int slot = j + i * width;
                        dynamicReferenceSlots.put(slot, dynamicReferenceIngredients.get(cStr));
                    }
                    else if(anyNbtIngredients.containsKey(cStr)) {
                        final int slot = j + i * width;
                        anyNbtSlots.put(slot, anyNbtIngredients.get(cStr));
                    }
                    else if(allNbtsIngredients.containsKey(cStr)) {
                        final int slot = j + i * width;
                        allNbtsSlots.put(slot, allNbtsIngredients.get(cStr));
                    }
                }
            }
        }


        // Read group and category
        final String group = json.has("group") ? json.get("group").getAsString() : "";
        final CraftingBookCategory category = json.has("category")
            ? CraftingBookCategory.CODEC.byName(json.get("category").getAsString(), CraftingBookCategory.MISC)
            : CraftingBookCategory.MISC
        ;


        // Return data
        return new EnhancedShapedRecipe(
            id, group, category, pattern, keyMap, resultStack,
            requiredCountSlots, dynamicReferenceSlots, anyNbtSlots, allNbtsSlots
        );
    }






    @Override
    public void toNetwork(final FriendlyByteBuf buffer, final EnhancedShapedRecipe recipe) {
        //! Empty method. Client and server never communicate this.
        //! The client doesn't know these recipes exist, so network stuff is not needed
        buffer.writeUtf("");
    }

    @Override
    public EnhancedShapedRecipe fromNetwork(final ResourceLocation id, final FriendlyByteBuf buffer) {
        //! Empty method. Client and server never communicate this.
        //! The client doesn't know these recipes exist, so network stuff is not needed
        buffer.readUtf();
        return null;
    }
}