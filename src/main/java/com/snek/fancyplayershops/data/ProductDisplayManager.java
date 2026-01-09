package com.snek.fancyplayershops.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.configs.Configs;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.main.ProductDisplayKey;
import com.snek.fancyplayershops.main.ProductDisplay_Serializer;
import com.snek.fancyplayershops.graphics.ui.edit.elements.Edit_ColorSelector;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.UtilityClassBase;
import com.snek.frameworklib.utils.Utils;
import com.snek.frameworklib.utils.scheduler.RateLimiter;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
















/**
 * A class that handles active product displays and takes care of loading and saving their data.
 */
public final class ProductDisplayManager extends UtilityClassBase {
    private static final DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder()
        .appendPattern("MMMM d, yyyy 'at' h:mm ")
        .appendText(java.time.temporal.ChronoField.AMPM_OF_DAY,
            java.util.Map.of(0L, "am", 1L, "pm"))
        .toFormatter(Locale.ENGLISH)
    ;
    private ProductDisplayManager() {}





    /**
     * Calculates the path to the directory where product displays are saved.
     * @return The path to the save file directory.
     */
    public static @NotNull Path calcDisplayDirPath() {
        return FancyPlayerShops.getStorageDir().resolve("product displays");
    }

    /**
     * Calculates the path to the directory where the save file of the specified product display is saved.
     * @param display The product display.
     * @return The path to the directory of the save file of {@code display}.
     */
    public static @NotNull Path calcDisplayFileDirPath(final @NotNull ProductDisplay display) {
        return calcDisplayDirPath().resolve(MinecraftUtils.getLevelId(display.getLevel()));
    }

    /**
     * Calculates the path to the save file of the specified product display.
     * @param display The product display.
     * @return The path to the save file of {@code display}.
     */
    public static @NotNull Path calcDisplayFilePath(final @NotNull ProductDisplay display) {
        return calcDisplayFileDirPath(display).resolve(display.getIdentifierNoLevel() + ".json");
    }








    // Stores the displays of players, identifying them by their owner's UUID and their coordinates and level in the format "x,y,z,levelId"
    private static final @NotNull Map<@NotNull ProductDisplayKey, @Nullable ProductDisplay>                   displaysByCoords   = new HashMap<>();
    private static final @NotNull Map<@NotNull UUID,              @Nullable HashSet<@NotNull ProductDisplay>> displaysByOwner = new HashMap<>();
    private static boolean dataLoaded = false;
    public static @NotNull Map<@NotNull ProductDisplayKey, @Nullable ProductDisplay>                   getDisplaysByCoords() { return displaysByCoords; }
    public static @NotNull Map<@NotNull UUID,              @Nullable HashSet<@NotNull ProductDisplay>> getDisplaysByOwner()  { return displaysByOwner; }
    public static @Nullable Set<@NotNull ProductDisplay> getDisplaysOfPlayer(final @NotNull Player player) { return displaysByOwner.get(player.getUUID()); }

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
    private static final @NotNull ItemStack productDisplayItem;

    // Product display item texture
    private static final @NotNull String DISPLAY_ITEM_TEXTURE =
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZj" +
        "I3ODQzMDdiODkyZjUyYjkyZjc0ZmE5ZGI0OTg0YzRmMGYwMmViODFjNjc1MmU1ZWJhNjlhZDY3ODU4NDI3ZSJ9fX0="
    ;

    // Product display item name
    public static final @NotNull Vector3i DISPLAY_ITEM_NAME_COLOR = new Vector3i(175, 140, 190);
    public static final @NotNull Component DISPLAY_ITEM_NAME =
        new Txt("Product display").noItalic().bold().color(DISPLAY_ITEM_NAME_COLOR)
    .get();

    // Product display item description
    private static final @NotNull Vector3i DISPLAY_ITEM_DESCRITPION_COLOR = new Vector3i(225, 180, 230);
    private static final @NotNull Component[] DISPLAY_ITEM_DESCRITPION = {
        new Txt().cat(new Txt("Product displays").color(DISPLAY_ITEM_DESCRITPION_COLOR)).cat(new Txt(" allow you to sell items to other players.").white()).noItalic().get(),
        new Txt().cat(new Txt("Place this anywhere and ").white()).cat(new Txt("right click").color(DISPLAY_ITEM_DESCRITPION_COLOR)).cat(new Txt(" it to get started!").white()).noItalic().get(),
        new Txt("").noItalic().get()
    };




    // Initialize display item stack
    static {

        // Create item and set custom name
        productDisplayItem = MinecraftUtils.createCustomHead(DISPLAY_ITEM_TEXTURE, false);
        productDisplayItem.setHoverName(DISPLAY_ITEM_NAME);

        // Set identification tag
        MinecraftUtils.addTag(productDisplayItem, DISPLAY_ITEM_NBT_KEY);

        // Set lore
        final ListTag lore = new ListTag();
        for(final Component line : DISPLAY_ITEM_DESCRITPION) {
            lore.add(StringTag.valueOf(Component.Serializer.toJson(line)));
        }
        productDisplayItem.getOrCreateTagElement("display").put("Lore", lore);
    }







    /**
     * Registers the displays in the runtime maps.
     * <p> Calling this method on a display that's already registered will have no effect.
     */
    public static void registerDisplay(final @NotNull ProductDisplay display) {
        if(displaysByCoords.put(display.getKey(), display) == null) {
            displaysByOwner.putIfAbsent(display.getOwnerUuid(), new HashSet<>());
            displaysByOwner.get(display.getOwnerUuid()).add(display);

            final ChunkPos chunkPos = new ChunkPos(display.getPos());
            chunkDisplayAmount.putIfAbsent(chunkPos, 0);
            chunkDisplayAmount.put(chunkPos, chunkDisplayAmount.get(chunkPos) + 1);
        }
    }

    /**
     * Unregisters the display from the runtime maps.
     * <p>
     * Calling this method on a display that's already not registered will have no effect.
     * <p>
     * This method doesn't remove the display from the world or modify its stock and balance in any way.
     */
    public static void unregisterDisplay(final @NotNull ProductDisplay display) {
        if(displaysByCoords.remove(display.getKey(), display)) {
            final HashSet<ProductDisplay> set = displaysByOwner.get(display.getOwnerUuid());
            if(set != null) set.remove(display);

            final ChunkPos chunkPos = new ChunkPos(display.getPos());
            chunkDisplayAmount.put(chunkPos, chunkDisplayAmount.get(chunkPos) - 1);
        }
    }

    public static boolean chunkHasDisplays(final @NotNull ChunkPos chunkPos) {
        final Integer n = chunkDisplayAmount.get(chunkPos);
        return n != null && n > 0;
    }








    /**
     * Schedules a display for data saving.
     * Call saveScheduledDisplays() to save schedules displays.
     * @param display The display to save.
     */
    public static void scheduleDisplaySave(final @NotNull ProductDisplay display) {
        if(!display.isScheduledForSave()) {
            scheduledForSaving.add(display);
            display.setScheduledForSave(true);
        }
    }

    /**
     * Saves the data of all the displays schedules for saving in their config files.
     */
    public static void saveScheduledDisplays() {

        for(final ProductDisplay display : scheduledForSaving) {

            // Create directory for the level
            final Path levelStorageDir = calcDisplayFileDirPath(display);
            try {
                Files.createDirectories(levelStorageDir);
            } catch(final IOException e) {
                FancyPlayerShops.LOGGER.error("Couldn't create the storage directory for the product display data of the level \"{}\"", MinecraftUtils.getLevelId(display.getLevel()), e);
            }


            // Skip deleted displays
            if(display.isRemoved()) {
                continue;
            }


            // Create this display's config file if absent, then save the JSON in it
            final File displayStorageFile = calcDisplayFilePath(display).toFile();
            try (final Writer writer = new FileWriter(displayStorageFile)) {
                writer.write(ProductDisplay_Serializer.serialize(display));
            } catch(final IOException e) {
                FancyPlayerShops.LOGGER.error("Couldn't create the storage file for the product display \"{}\"", display.getIdentifierNoLevel(), e);
            }


            // Flag the display as not scheduled
            display.setScheduledForSave(false);
        }
        scheduledForSaving = new ArrayList<>();
    }








    /**
     * Loads all the player product displays into the runtime map if needed.
     * <p> Must be called on server started event (After the levels are loaded!).
     * <p> If the data has already been loaded, the call will have no effect.
     */
    public static void loadDisplays() {
        if(dataLoaded) return;
        dataLoaded = true;


        for(final File levelStorageDir : calcDisplayDirPath().toFile().listFiles()) {

            // For each display file
            final File[] displayStorageFiles = levelStorageDir.listFiles();
            if(displayStorageFiles != null) for(final File displayStorageFile : displayStorageFiles) {

                // Read file and deserialize the data
                try {
                    final String serializedDisplay = Files.readString(displayStorageFile.toPath());
                    final ProductDisplay retrievedDisplay = ProductDisplay_Serializer.deserialize(serializedDisplay, null, null);
                    registerDisplay(retrievedDisplay);
                } catch(final IOException e) {
                    FancyPlayerShops.LOGGER.error("Couldn't read the storage file of the product display \"{}\"", displayStorageFile.getName(), e);
                }
            }
        }
    }








    /**
     * Returns the {@link ProductDisplay} instance present at a certain block position.
     * @param pos The block position.
     * @param level The level the display is in.
     * @return The display, or null if no display is there.
    */
    public static ProductDisplay findDisplay(final @NotNull BlockPos pos, final @NotNull Level level) {
        return displaysByCoords.get(ProductDisplay.calcDisplayKey(pos, level));
    }




    /**
     * Deletes the data associated with this hop instance.
     * @param display The display to delete.
     */
    public static void deleteDisplay(final @NotNull ProductDisplay display) {

        // Remove display from the runtime maps
        unregisterDisplay(display);

        // Delete the config file
        final File displayStorageFile = calcDisplayFilePath(display).toFile();
        displayStorageFile.delete();
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
            updateSnapshot = new ArrayList<>(displaysByCoords.values());
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
     * Returns a copy if the default product display item.
     * @return A copy of the product display item.
     */
    public static @NotNull ItemStack getProductDisplayItemCopy() {
        return productDisplayItem.copy();
    }




    /**
     * Creates a product display item containing the informations required to fully restore the provided display.
     * <p>
     * Unconfigured displays are deleted instead.
     * @param display The display.
     * @return The created product display item.
     */
    public static @NotNull ItemStack createShopSnapshot(final @NotNull ProductDisplay display) {
        if(display.getItem().is(Items.AIR)) {
            return getProductDisplayItemCopy();
        }

        // Get NBTs
        final ItemStack item = productDisplayItem.copy();
        final CompoundTag nbt = item.getOrCreateTag();
        final CompoundTag nbtDisplay = nbt.getCompound("display");
        final ListTag lore = nbtDisplay.getList("Lore", Tag.TAG_STRING);




        // Create and add display data NBT
        final CompoundTag data = new CompoundTag();
        data.putUUID  ("owner", display.getOwnerUuid());
        data.putString("owner_name", MinecraftUtils.getPlayerByUUID(display.getOwnerUuid()).getName().getString());
        data.putString("product_display_data", ProductDisplay_Serializer.serialize(display));


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
