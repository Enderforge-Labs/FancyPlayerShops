package com.snek.fancyplayershops.recipes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.debug.Require;
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
 * A custom, fully server-side, ready-to-use shaped crafting recipe that supports
 * complete NBT matching, NBT tag matching, item counts and runtime item references.
 * <p>
 * The ID of this recipe type is {@code "frameworklib:enhanced_crafting_shaped"}.
 * <p><ul><li>
 *     To perfectly match an ItemStack: {@code "frameworklib:dynamic_ref": "id"}.
 *     Placeholders are instantiated at runtime using {@link #registerDynamicReference(ResourceLocation, ItemStack)}.
 *     Available for both ingredients and result.
 *     Not compatible with {@code any_nbt}, {@code all_nbts} or Vanilla's {@code item} and {@code tag}.
 * </li><li>
 *     To match any ItemStack that contains at least one NBT tag: {@code "frameworklib:any_nbt": ["list", "of", "names"]}.
 *     Only available for ingredients.
 *     Not compatible with {@code dynamic_ref}, {@code all_nbts} or Vanilla's {@code item} and {@code tag}.
 * </li><li>
 *     To match any ItemStack that contains all the specified NBT tags: {@code "frameworklib:all_nbts": ["list", "of", "names"]}.
 *     Only available for ingredients.
 *     Not compatible with {@code dynamic_ref}, {@code any_nbt} or Vanilla's {@code item} and {@code tag}.
 * </li><li>
 *     To require more than 1 item: {@code "frameworklib:count": count}.
 *     Only available for ingredients.
 *     This is compatible with {@code dynamic_ref}, {@code all_nbts}, {@code any_nbt}, and Vanilla's {@code item} and {@code tag}.
 *     For the result, you can use Vanilla's {@code "count": count}.
 * </li></ul></p>
 */
public class EnhancedShapedRecipe extends ShapedRecipe {

    // Dynamic item references
    private static final @NotNull Map<@NotNull ResourceLocation, @Nullable ItemStack> itemStackReferences = new HashMap<>();


    // Recipe data - provided by the serializer
    private final @NotNull Map<@NotNull Integer, @Nullable Integer>      requiredCounts;
    private final @NotNull Map<@NotNull Integer, @Nullable ItemStack>    dynamicReferenceSlots;
    private final @NotNull Map<@NotNull Integer, @Nullable List<String>> anyNbtSlots;
    private final @NotNull Map<@NotNull Integer, @Nullable List<String>> allNbtsSlots;




    /**
     * Registers a dynamic ItemStack reference.
     * <p>
     * All references must be registered before the recipes are loaded. Your mod's onInitialize() is a good place for this.
     * @param id The ID of the dynamic item reference. This must match the ID specified in the recipe's Json file.
     * @param stack The ItemStack to link to the provided ID. The stack's count is ignored. Use proper count parameters for that.
     */
    public static @NotNull void registerDynamicReference(final @NotNull ResourceLocation id, final @NotNull ItemStack stack) {
        assert Require.nonNull(id, "id");
        assert Require.nonNull(stack, "stack");
        itemStackReferences.put(id, stack.copy());
        FrameworkLib.LOGGER.info("Registered ItemStack reference: {}", id);
    }

    /**
     * Retrieves the ItemStack associated with the provided ID.
     * @param id The ID of the dynamic ItemStack reference.
     * @return The ItemStack if it exists, or null if it still hasn't been registered (or it doesn't exist).
     */
    public static @Nullable ItemStack getItemStackReference(final @NotNull ResourceLocation id) {
        assert Require.nonNull(id, "id");
        final @Nullable ItemStack stack = itemStackReferences.get(id);
        return stack != null ? stack.copy() : ItemStack.EMPTY;
    }




    public EnhancedShapedRecipe(
        final ResourceLocation id,
        final String group,
        final CraftingBookCategory category,
        final String[] pattern,
        final Map<String, Ingredient> key,
        final ItemStack result,
        final Map<Integer, Integer> requiredCounts,
        final Map<Integer, ItemStack> dynamicReferenceSlots,
        final Map<Integer, List<String>> anyNbtSlots,
        final Map<Integer, List<String>> allNbtsSlots
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




    /**
     * Turns a recipe pattern into a list of {@link Ingredient}s.
     * @param pattern The pattern.
     * @param key A map that associates each pattern identifier with an {@link Ingredient}.
     * @param width The width of the recipe.
     * @param height The height of the recipe.
     * @return The list of {@link Ingredient}s.
     */
    private static @NotNull NonNullList<@NotNull Ingredient> dissolvePattern(
        final @NotNull String[] pattern,
        final @NotNull Map<@NotNull String, @NotNull Ingredient> key,
        final int width, final int height
    ) {
        final NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);
        for(int i = 0; i < pattern.length; i++) {
            final String row = pattern[i];
            for(int j = 0; j < row.length(); j++) {
                final char c = row.charAt(j);
                if(c != ' ') {
                    ingredients.set(j + i * width, key.get(String.valueOf(c)));
                }
            }
        }
        return ingredients;
    }




    @Override
    public boolean matches(final CraftingContainer container, final Level level) {
        for(int i = 0; i < container.getContainerSize(); ++i) {

            // Check count
            final Integer requiredCount = requiredCounts.get(i);
            if(requiredCount != null) {
                final ItemStack actual = container.getItem(i);
                if(actual.getCount() < requiredCount) {
                    return false;
                }
            }

            // Check references for slots that require it
            final var dynamicRef = dynamicReferenceSlots.get(i);
            if(dynamicRef != null) {
                final ItemStack actual = container.getItem(i);
                if(!ItemStack.isSameItemSameTags(actual, dynamicRef)) {
                    return false;
                }
                continue;
            }

            // Check anyNbt for slots that require it
            final var anyNbtList = anyNbtSlots.get(i);
            if(anyNbtList != null) {
                boolean hasTags = false;
                final ItemStack actual = container.getItem(i);
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
                final ItemStack actual = container.getItem(i);
                for(final String nbtTagName : allNbtsList) {
                    if(!MinecraftUtils.hasTag(actual, nbtTagName)) {
                        return false;
                    }
                }
                continue;
            }

            //FIXME this doesn't allow for mirrored recipes. Vanilla always allows mirrors

            //FIXME this doesn't allow for all smaller recipes. it should check all possible positions
            // Check vanilla pattern matching last
            final Ingredient ingredient = getIngredients().get(i);
            if(!ingredient.test(container.getItem(i))) {
                return false;
            }
        }

        // Return true if all conditions pass
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

    public Map<Integer, ItemStack> getDynamicReferences() {
        return dynamicReferenceSlots;
    }

    public Map<Integer, Integer> getRequiredCounts() {
        return requiredCounts;
    }
}