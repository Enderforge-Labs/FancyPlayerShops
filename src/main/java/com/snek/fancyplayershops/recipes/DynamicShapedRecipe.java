package com.snek.fancyplayershops.recipes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.frameworklib.utils.MinecraftUtils;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;








/**
 * A custom, fully server-side, ready-to-use shaped crafting recipe that supports NBTs and runtime material/result definitions.
 * <p>
 * Use {@code "type": "frameworklib:dynamic_crafting_shaped"} in your recipe {@code .json} to enable NBTs for it.
 * <p>
 * To perfectly match an ItemStack: {@code "frameworklib:dynamic_ref": "id"}.
 * Placeholders are instantiated at runtime using {@link #registerDynamicReference(ResourceLocation, ItemStack)}
 * <p>
 * To match any ItemStack that contains at least one NBT tag: {@code "frameworklib:any_nbt": ["list", "of", "names"]}
 * <p>
 * To match any ItemStack that contains all the specified NBT tags: {@code "frameworklib:all_nbts": ["list", "of", "names"]}
 */
public class DynamicShapedRecipe extends ShapedRecipe {

    // Dynamic item references
    private static final Map<ResourceLocation, ItemStack> itemStackReferences = new HashMap<>();


    // Recipe data - provided by the serializer
    private final Map<Integer, Integer> requiredCounts;
    private final Map<Integer, ItemStack> dynamicReferenceSlots;
    private final Map<Integer, List<String>> anyNbtSlots;
    private final Map<Integer, List<String>> allNbtsSlots;




    public static void registerDynamicReference(ResourceLocation id, ItemStack stack) {
        itemStackReferences.put(id, stack.copy());
        System.out.println("Registered ItemStack reference: " + id); //FIXME replace with proper logging
    }

    public static ItemStack getItemStackReference(ResourceLocation id) {
        ItemStack stack = itemStackReferences.get(id);
        return stack != null ? stack.copy() : ItemStack.EMPTY;
    }




    public DynamicShapedRecipe(
        ResourceLocation id,
        String group,
        CraftingBookCategory category,
        String[] pattern,
        Map<String, Ingredient> key,
        ItemStack result,
        Map<Integer, Integer> requiredCounts,
        Map<Integer, ItemStack> dynamicReferenceSlots,
        Map<Integer, List<String>> anyNbtSlots,
        Map<Integer, List<String>> allNbtsSlots
    ) {
        super(id, group, category,
            pattern[0].length(), pattern.length,
            dissolvePattern(pattern, key, pattern[0].length(), pattern.length),
            result
        );
        this.requiredCounts = requiredCounts;
        this.dynamicReferenceSlots = dynamicReferenceSlots;
        this.anyNbtSlots = anyNbtSlots;
        this.allNbtsSlots = allNbtsSlots;
    }

    private static NonNullList<Ingredient> dissolvePattern(String[] pattern, Map<String, Ingredient> key, int width, int height) {
        NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);
        for(int i = 0; i < pattern.length; i++) {
            String row = pattern[i];
            for(int j = 0; j < row.length(); j++) {
                char c = row.charAt(j);
                if(c != ' ') {
                    ingredients.set(j + i * width, key.get(String.valueOf(c)));
                }
            }
        }
        return ingredients;
    }




    @Override
    public boolean matches(CraftingContainer container, Level level) {
        for(int i = 0; i < container.getContainerSize(); ++i) {

            // Check count
            Integer requiredCount = requiredCounts.get(i);
            if(requiredCount != null) {
                ItemStack actual = container.getItem(i);
                if(actual.getCount() < requiredCount) {
                    return false;
                }
            }

            // Check references for slots that require it
            final var dynamicRef = dynamicReferenceSlots.get(i);
            if(dynamicRef != null) {
                ItemStack actual = container.getItem(i);
                if(!ItemStack.isSameItemSameTags(actual, dynamicRef)) {
                    return false;
                }
                continue;
            }

            // Check anyNbt for slots that require it
            final var anyNbtList = anyNbtSlots.get(i);
            if(anyNbtList != null) {
                boolean hasTags = false;
                ItemStack actual = container.getItem(i);
                for(final String nbtTagName : anyNbtList) {
                    if(MinecraftUtils.hasTag(actual, nbtTagName)) {
                        hasTags = true;
                        break;
                    }
                }
                if(!hasTags) {
                    return false;
                }
                continue;
            }

            // Check allNbts for slots that require it
            final var allNbtsList = allNbtsSlots.get(i);
            if(allNbtsList != null) {
                ItemStack actual = container.getItem(i);
                for(final String nbtTagName : allNbtsList) {
                    if(!MinecraftUtils.hasTag(actual, nbtTagName)) {
                        return false;
                    }
                }
                continue;
            }

            //FIXME this doesn't allow for mirrored recipes. Vanilla always allows mirrors
            // Check vanilla pattern matching last
            final Ingredient ingredient = getIngredients().get(i);
            if(!ingredient.test(container.getItem(i))) {
                return false;
            }
        }

        // Return true if all conditions pass
        return true;
    }


//! 19  19  19
//!  2  62   2
//! 30  62  30

    // @Override
    // public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
    //     NonNullList<ItemStack> remaining = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);

    //     for(int i = 0; i < remaining.size(); i++) {
    //         ItemStack stack = container.getItem(i);

    //         if(!stack.isEmpty()) {
    //             Integer requiredCount = requiredCounts.get(i);
    //             if(requiredCount != null && requiredCount > 1) {
    //                 ItemStack copy = stack.copy();
    //                 copy.shrink(requiredCount - 1); // -1 because vanilla consumes 1 already
    //                 if(!copy.isEmpty()) {
    //                     remaining.set(i, copy);
    //                 }
    //             }
    //         }
    //     }

    //     return remaining;
    // }




    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FancyPlayerShops.NBT_SHAPED_SERIALIZER;
    }

    public Map<Integer, ItemStack> getDynamicReferences() {
        return dynamicReferenceSlots;
    }

    public Map<Integer, Integer> getRequiredCounts() {
        return requiredCounts;
    }
}