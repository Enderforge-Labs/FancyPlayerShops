package com.snek.fancyplayershops.recipes;

import java.util.HashMap;
import java.util.Map;

import com.snek.fancyplayershops.main.FancyPlayerShops;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
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
 * Use {@code "type": "frameworklib:nbt_crafting_shaped"} in your recipe {@code .json} to enable NBTs for it.
 * <p>
 * You can then use {@code "frameworklib:item_stack_reference": "id"} as a placeholder for your item (instead of {@code "item": "id"}).
 * Placeholders are defined in runtime using {@link #registerItemStackReference(ResourceLocation, ItemStack)}
 */
public class NbtShapedRecipe extends ShapedRecipe {
    private static final Map<ResourceLocation, ItemStack> itemStackReferences = new HashMap<>();
    private final Map<Integer, ItemStack> nbtRequiredSlots;


    public static void registerItemStackReference(ResourceLocation id, ItemStack stack) {
        itemStackReferences.put(id, stack.copy());
        System.out.println("Registered ItemStack reference: " + id); //FIXME replace with proper logging
    }

    public static ItemStack getItemStackReference(ResourceLocation id) {
        ItemStack stack = itemStackReferences.get(id);
        return stack != null ? stack.copy() : ItemStack.EMPTY;
    }


    // public NbtShapedRecipe(ShapedRecipe base) {
    //     super(
    //         base.getId(),
    //         base.getGroup(),
    //         base.category(),
    //         base.getWidth(),
    //         base.getHeight(),
    //         base.getIngredients(),
    //         base.getResultItem(null)
    //     );
    // }

    public NbtShapedRecipe(
        ResourceLocation id,
        String group,
        CraftingBookCategory category,
        String[] pattern,
        Map<String, Ingredient> key,
        ItemStack result,
        Map<Integer, ItemStack> nbtRequiredSlots
    ) {
        super(id, group, category,
            pattern[0].length(), pattern.length,
            dissolvePattern(pattern, key, pattern[0].length(), pattern.length),
            result
        );
        this.nbtRequiredSlots = nbtRequiredSlots;
    }

    private static NonNullList<Ingredient> dissolvePattern(String[] pattern, Map<String, Ingredient> key, int width, int height) {
        NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);

        for (int i = 0; i < pattern.length; i++) {
            String row = pattern[i];
            for (int j = 0; j < row.length(); j++) {
                char c = row.charAt(j);
                if (c != ' ') {
                    ingredients.set(j + i * width, key.get(String.valueOf(c)));
                }
            }
        }

        return ingredients;
    }

    public NbtShapedRecipe(ShapedRecipe base, Map<Integer, ItemStack> nbtRequiredSlots) {
        super(
            base.getId(),
            base.getGroup(),
            base.category(),
            base.getWidth(),
            base.getHeight(),
            base.getIngredients(),
            base.getResultItem(null)
        );
        this.nbtRequiredSlots = nbtRequiredSlots;
    }



    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        return super.assemble(container, registryAccess);
    }



    @Override
    public boolean matches(CraftingContainer container, Level level) {
        System.out.println("NBT RECIPE MATCHES CHECK");

        // First check vanilla pattern matching
        if (!super.matches(container, level)) {
            return false;
        }

        // Then check NBT for slots that require it
        for (Map.Entry<Integer, ItemStack> entry : nbtRequiredSlots.entrySet()) {
            int slot = entry.getKey();
            ItemStack required = entry.getValue();
            ItemStack actual = container.getItem(slot);

            if (!ItemStack.isSameItemSameTags(actual, required)) {
                System.out.println("NBT mismatch at slot " + slot);
                return false;
            }
        }

        System.out.println("Recipe matches with NBT!");
        return true;
    }



    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FancyPlayerShops.NBT_SHAPED_SERIALIZER;
    }

    public Map<Integer, ItemStack> getRequiredSlots() {
        return nbtRequiredSlots;
    }
}