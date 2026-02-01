package com.snek.fancyplayershops.data.display;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.configs.Configs;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.frameworkconfig.data.DataManager;
import com.snek.fancyplayershops.graphics.ui.edit.elements.Edit_ColorSelector;
import com.snek.frameworklib.enhanced_recipes.shaped.EnhancedShapedRecipe;
import com.snek.frameworklib.utils.common.MinecraftUtils;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.common.Utils;
import com.snek.frameworklib.utils.scheduler.RateLimiter;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
















/**
 * A class that handles active product displays and takes care of loading and saving their data.
 */
@SuppressWarnings("java:S6548") //! Singleton implementation
public class ProductDisplay_Manager extends DataManager<ProductDisplay> {

    // Static manager reference
    public static final ProductDisplay_Manager REF = new ProductDisplay_Manager();




    private static final DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder()
        .appendPattern("MMMM d, yyyy 'at' h:mm ")
        .appendText(java.time.temporal.ChronoField.AMPM_OF_DAY,
            java.util.Map.of(0L, "am", 1L, "pm"))
        .toFormatter(Locale.ENGLISH)
    ;
    private ProductDisplay_Manager() {
        super(FancyPlayerShops.MOD_ID, "product displays", new ProductDisplay_Serializer());
    }








    // Stores the displays of players, identifying them by their owner's UUID
    private static final @NotNull Map<UUID, Map<UUID, ProductDisplay>> displaysByOwner = new HashMap<>();
    public  static final @NotNull Map<UUID, Map<UUID, ProductDisplay>> getDisplaysByOwner() { return displaysByOwner; }

    // Async update list
    private static int updateIndex = 0;
    private static @NotNull List<@NotNull ProductDisplay> updateSnapshot = List.of();
    private static final @NotNull RateLimiter updateCycleLimiter = new RateLimiter();

    // Keeps track of the amount of displays in each chunk
    private static final @NotNull Map<@NotNull ChunkPos, @Nullable Integer> chunkDisplayAmount = new HashMap<>();

    // The list of displays scheduled for saving
    private static @NotNull List<@NotNull ProductDisplay> scheduledForSaving = new ArrayList<>();




    // Product display item data
    //! Don't use the name or tooltip to check the item. Displays should work even when renamed in an anvil or modified by mods
    public  static final @NotNull String DISPLAY_ITEM_NBT_KEY = FancyPlayerShops.MOD_ID + ".item.display_item";
    public  static final @NotNull String SNAPSHOT_NBT_KEY  = DISPLAY_ITEM_NBT_KEY + ".snapshot";
    private static final @NotNull List<ItemStack> productDisplayItems = new ArrayList<>();

    // Product display item name
    public static final @NotNull Vector3i DISPLAY_ITEM_NAME_COLOR = new Vector3i(175, 140, 190);
    public static final @NotNull String DISPLAY_ITEM_NAME = "Product display";

    // Product display item description
    private static final @NotNull Vector3i DISPLAY_ITEM_DESCRITPION_COLOR = new Vector3i(225, 180, 230);
    private static final @NotNull Component[] DISPLAY_ITEM_DESCRITPION = {
        new Txt().cat(new Txt("Product displays").color(DISPLAY_ITEM_DESCRITPION_COLOR)).cat(new Txt(" allow you to sell items to other players.").white()).noItalic().get(),
        new Txt().cat(new Txt("Place this anywhere and ").white()).cat(new Txt("right click").color(DISPLAY_ITEM_DESCRITPION_COLOR)).cat(new Txt(" it to get started!").white()).noItalic().get(),
        new Txt("").noItalic().get()
    };




    static {

        // Initialize display item stacks
        for(final var tier : DisplayTier.values()) {


            // Create item and set custom name
            final var item = MinecraftUtils.createCustomHead(tier.getTexture(), false);
            item.setHoverName(new Txt(DISPLAY_ITEM_NAME + " - " + tier.name()).noItalic().bold().color(DISPLAY_ITEM_NAME_COLOR).get());


            // Set identification tag and tier tag (base item tag needs a copy to spawn the right tier of display)
            //! Add the ID itself as a tag. This lets crafting recipes recognize the item
            MinecraftUtils.addTag(item, DISPLAY_ITEM_NBT_KEY);
            MinecraftUtils.addTag(item, new ResourceLocation(FancyPlayerShops.MOD_ID, tier.getId()).toString());
            item.getOrCreateTag().putInt("tier", tier.getNumericalId());


            // Set lore
            final ListTag lore = new ListTag();
            for(final String s : tier.getStatsLines()) {
                lore.add(StringTag.valueOf(Component.Serializer.toJson(new Txt(s).gray().noItalic().get())));
            }
            lore.add(StringTag.valueOf(Component.Serializer.toJson(Component.empty())));
            for(final Component line : DISPLAY_ITEM_DESCRITPION) lore.add(StringTag.valueOf(Component.Serializer.toJson(line)));
            item.getOrCreateTagElement("display").put("Lore", lore);


            // Set item reference (creative one shouldn't be in any recipe)
            productDisplayItems.add(item);


            // Register recipe items
            if(tier != DisplayTier.CREATIVE) {
                EnhancedShapedRecipe.registerDynamicReference(
                    new ResourceLocation(FancyPlayerShops.MOD_ID, tier.getId()),
                    ProductDisplay_Manager.getProductDisplayItemCopy(tier)
                );
            }
        }
    }








    @Override
    public void afterPut(final @NotNull UUID uuid, final @NotNull ProductDisplay data) {
        final ChunkPos chunkPos = new ChunkPos(data.getPos());
        chunkDisplayAmount.putIfAbsent(chunkPos, 0);
        chunkDisplayAmount.put(chunkPos, chunkDisplayAmount.get(chunkPos) + 1);
        ProductDisplay_Manager.getDisplaysByOwner().computeIfAbsent(data.getOwnerUuid(), list -> { return new HashMap<>(); }).put(data.getUUID(), data);
    }


    @Override
    public void afterRemove(final @NotNull UUID uuid, final @NotNull ProductDisplay data) {
        final ChunkPos chunkPos = new ChunkPos(data.getPos());
        chunkDisplayAmount.put(chunkPos, chunkDisplayAmount.get(chunkPos) - 1);
        ProductDisplay_Manager.getDisplaysByOwner().get(data.getOwnerUuid()).remove(data.getUUID());
    }


    /**
     * Checks if the specified chunk contains any product displays.
     * @param chunkPos The position of the chunk to check.
     * @return True if the chunk contains at least 1 display, false otherwise.
     */
    public static boolean chunkHasDisplays(final @NotNull ChunkPos chunkPos) {
        final Integer n = chunkDisplayAmount.get(chunkPos);
        return n != null && n > 0;
    }







    /**
     * Updates PULL_UPDATES_PER_TICK displays each call, making them pull items from nearby inventories.
     * <p> Must be called each server tick.
     */
    @SuppressWarnings("java:S127") //! Index changed by the loop's body
    public static void pullItems() {
        if(!updateCycleLimiter.attempt()) return;


        // Refresh snapshot if needed
        if(updateIndex == 0) {
            updateSnapshot = new ArrayList<>(REF.getCache().values());
        }


        // Update displays
        for(int i = 0; i < Configs.getPerf().pulls_per_tick.getValue() && updateIndex < updateSnapshot.size(); ++updateIndex) {

            final ProductDisplay display = updateSnapshot.get(updateIndex);
            final ChunkPos chunkPos = new ChunkPos(display.getPos());
            if(display.getLevel().hasChunk(chunkPos.x, chunkPos.z)) {
                display.pullItems();
                ++i;
            }
        }


        // Reset snapshot if this iteration reached its end
        if(updateIndex >= updateSnapshot.size()) {
            updateCycleLimiter.renewCooldown(Configs.getPerf().pull_cycle_frequency.getValue());
            updateIndex = 0;
        }
    }




    /**
     * Forcefully updates all active displays, making them pull items from nearby inventories.
     * <p>
     * This bypasses configured restock limits.
     */
    public static void forcePullItems() {
        for(final ProductDisplay display : REF.getCache().values()) {
            final ChunkPos chunkPos = new ChunkPos(display.getPos());
            if(display.getLevel().hasChunk(chunkPos.x, chunkPos.z)) {
                display.pullItems();
            }
        }
    }








    /**
     * Returns a copy if the default product display item.
     * @param tier The tier of shop to create.
     * @return A copy of the product display item.
     */
    public static @NotNull ItemStack getProductDisplayItemCopy(final @NotNull DisplayTier tier) {
        return productDisplayItems.get(tier.ordinal()).copy();
    }




    /**
     * Creates a product display item containing the informations required to fully restore the provided display.
     * <p>
     * Unconfigured displays are deleted instead.
     * @param display The display.
     * @return The created product display item.
     */
    public static @NotNull ItemStack createDisplaySnapshot(final @NotNull ProductDisplay display) {
        if(display.getItem().is(Items.AIR)) {
            return getProductDisplayItemCopy(display.getTier());
        }

        // Get NBTs
        final ItemStack item = productDisplayItems.get(display.getTier().ordinal()).copy();
        final CompoundTag nbt = item.getOrCreateTag();
        final CompoundTag nbtDisplay = nbt.getCompound("display");
        final ListTag lore = nbtDisplay.getList("Lore", Tag.TAG_STRING);




        // Create and add display data NBT
        final CompoundTag data = new CompoundTag();
        data.putUUID  ("owner", display.getOwnerUuid());
        data.putString("owner_name", MinecraftUtils.getPlayerByUUID(display.getOwnerUuid()).getName().getString());
        data.putString("product_display_data", REF.getSerializer().serialize(display));


        // Create description
        final Component[] extraDescriptionLines = {
            new Txt()
                .cat(new Txt("This ").white().noItalic())
                .cat(new Txt("snapshot").color(DISPLAY_ITEM_DESCRITPION_COLOR).noItalic())
                .cat(new Txt(" was captured on ").white().noItalic())
                .cat(new Txt(LocalDateTime.now().format(timeFormatter)).color(DISPLAY_ITEM_DESCRITPION_COLOR).noItalic())
                .cat(new Txt(".").white().noItalic())
            .get(),
            new Txt()
                .cat(new Txt("It will automatically ").white().noItalic())
                .cat(new Txt("restore").color(DISPLAY_ITEM_DESCRITPION_COLOR).noItalic())
                .cat(new Txt(" its stock and settings once placed.").white().noItalic())
            .get(),
            new Txt().get(),
            new Txt().cat(new Txt("Owner: "      ).lightGray().noItalic()).cat(new Txt(MinecraftUtils.getPlayerByUUID(display.getOwnerUuid()).getName().getString())).white().noItalic().get(),
            new Txt().cat(new Txt("Shop: "       ).lightGray().noItalic()).cat(new Txt(display.getShop().getDisplayName())).white().noItalic().get(), //TODO use colored text for display names? maybe? idk. might have to change the shop data too
            new Txt().cat(new Txt("Balance: "    ).lightGray().noItalic()).cat(new Txt(Utils.formatPrice(display.getBalance()))).gold().noItalic().get(),
            new Txt().cat(new Txt("Price: "      ).lightGray().noItalic()).cat(new Txt(Utils.formatPrice (display.getPrice   ()             ))).white().noItalic().get(),
            new Txt().cat(new Txt("Stock: "      ).lightGray().noItalic()).cat(new Txt(Utils.formatAmount(display.getStock   (), false, true))).white().noItalic().get(),
            new Txt().cat(new Txt("Stock limit: ").lightGray().noItalic()).cat(new Txt(Utils.formatAmount(display.getMaxStock(), false, true))).white().noItalic().get(),
            new Txt().cat(new Txt("NBT filter:  ").lightGray().noItalic()).cat(new Txt(display.getNbtFilter() ? "on" : "off")).white().noItalic().get(),
            new Txt().cat(new Txt("Direction: "  ).lightGray().noItalic()).cat(new Txt(display.getDefaultDirection().getName())).white().noItalic().get(),
            new Txt().cat(new Txt("Color: "      ).lightGray().noItalic()).cat(new Txt("█")).color(Utils.HSVtoRGB(new Vector3f(display.getColorThemeHue(), Edit_ColorSelector.S, Edit_ColorSelector.V))).noItalic().get(),
            new Txt().get()
        };
        for(final Component line : extraDescriptionLines) {
            lore.add(StringTag.valueOf(Component.Serializer.toJson(line)));
        }

        nbtDisplay.put("Lore", lore);
        nbt.put("display", nbtDisplay);
        nbt.put(FancyPlayerShops.MOD_ID + ".snapshot_data", data);




        // Set new NBTs and return the item
        item.setTag(nbt);
        item.setHoverName(new Txt()
            .cat(new Txt("Snapshot").color(DISPLAY_ITEM_NAME_COLOR).bold().noItalic())
            .cat(new Txt(" - ").white())
            .cat(new Txt(display.getStandaloneName()).white().bold().noItalic())
        .get());
        MinecraftUtils.addTag(item, SNAPSHOT_NBT_KEY);
        return item;
    }
}
