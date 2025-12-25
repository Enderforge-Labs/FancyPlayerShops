package com.snek.fancyplayershops.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
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
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

import com.google.gson.Gson;
import com.snek.fancyplayershops.configs.Configs;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.ProductDisplay;
import com.snek.fancyplayershops.main.ProductDisplayKey;
import com.snek.fancyplayershops.graphics.ui.edit.elements.Edit_ColorSelector;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.UtilityClassBase;
import com.snek.frameworklib.utils.Utils;
import com.snek.frameworklib.utils.scheduler.RateLimiter;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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
    private static final Random rnd = new Random();





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
        return calcDisplayDirPath().resolve(display.getLevelId());
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

    // Rotation names
    private static final String[] ROTATION_NAMES = new String[] {
        "North",
        "Northwest",
        "West",
        "Southwest",
        "South",
        "Southeast",
        "East",
        "Northeast",
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
     * <p> Calling this method on a display that's already not registered will have no effect.
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
                FancyPlayerShops.LOGGER.error("Couldn't create the storage directory for the product display data of the level \"{}\"", display.getLevelId(), e);
            }


            // Skip deleted displays
            if(display.isDeleted()) {
                continue;
            }


            // Create this display's config file if absent, then save the JSON in it
            final File displayStorageFile = calcDisplayFilePath(display).toFile();
            try (final Writer writer = new FileWriter(displayStorageFile)) {
                new Gson().toJson(display, writer);
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
                ProductDisplay retrievedDisplay = null;
                try (final Reader reader = new FileReader(displayStorageFile)) {
                    retrievedDisplay = new Gson().fromJson(reader, ProductDisplay.class);
                } catch(final IOException e) {
                    FancyPlayerShops.LOGGER.error("Couldn't read the storage file of the product display \"{}\"", displayStorageFile.getName(), e);
                }

                // Recalculate transient members and update display maps
                if(retrievedDisplay != null) {
                    if(retrievedDisplay.reinitTransient()) {
                        registerDisplay(retrievedDisplay);
                    }
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
     * Removes all displays near the specified position, sending all the items to their owner's stash.
     * @param level The target level.
     * @param pos The center of the purge radius.
     * @param radius The maximum distance from pos displays can have in order to be purged.
     * @return The number of displays that were removed.
     */
    public static int purge(final @NotNull ServerLevel level, final @NotNull Vector3f pos, final float radius) {
        int r = 0;
        final List<ProductDisplay> displays = new ArrayList<>(displaysByCoords.values());
        for(final ProductDisplay display : displays) {
            if(display.getLevel() == level && display.calcDisplayPos().sub(pos).length() <= radius) {

                // Send feedback to affected player if they are online
                final Player owner = FrameworkLib.getServer().getPlayerList().getPlayer(display.getOwnerUuid());
                if(owner != null && !display.getItem().is(Items.AIR)) owner.displayClientMessage(new Txt()
                    .cat(new Txt("Your " + display.getDecoratedName() + " has been removed by an admin.").red())
                .get(), false);

                // Stash, claim and delete the display, then increase the purged displays counter
                display.stash();
                display.claimBalance();
                display.delete();
                ++r;
            }
        }
        return r;
    }




    /**
     * Forces the owners to pick up any of their displays near the specified position, sending the snapshots to their stashes.
     * @param level The target level.
     * @param pos The center of the radius.
     * @param radius The maximum distance from pos displays can have in order to be picked up.
     * @return The number of displays that were picked up.
     */
    public static int displace(final @NotNull ServerLevel level, final @NotNull Vector3f pos, final float radius) {
        int r = 0;
        final List<ProductDisplay> displays = new ArrayList<>(displaysByCoords.values());
        for(final ProductDisplay display : displays) {
            if(display.getLevel() == level && display.calcDisplayPos().sub(pos).length() <= radius) {

                // Send feedback to affected player if they are online
                final Player owner = FrameworkLib.getServer().getPlayerList().getPlayer(display.getOwnerUuid());
                if(owner != null && !display.getItem().is(Items.AIR)) owner.displayClientMessage(new Txt()
                    .cat(new Txt("Your " + display.getDecoratedName() + " was converted into an item by an admin.").red())
                .get(), false);

                // Stash and delete the display, then increase the displaced displays counter
                display.pickUp(false);
                display.delete();
                ++r;
            }
        }
        return r;
    }




    /**
     * Fill an area around the specified position with display.
     * @param level The target level.
     * @param pos The center of the fill area.
     * @param radius The maximum distance to reach on each cardinal direction.
     * @param owner The owner of the newly created displays.
     * @return The number of displays that were created.
     */
    public static int fill(final @NotNull ServerLevel level, final @NotNull Vector3f pos, final float radius, final @NotNull Player owner) {

        // Get a list of all registered items
        Registry<Item> itemRegistry = FrameworkLib.getServer().registryAccess().registryOrThrow(Registries.ITEM);
        final List<Item> itemList = new ArrayList<>();
        for(Item item : itemRegistry) {
            itemList.add(item);
        }

        // TODO generate randomly named groups
        // TODO test store A0B5
        // TODO test store 120B etc


        int r = 0;
        for(float i = pos.x - radius; i < pos.x + radius; ++i) {
            for(float j = pos.y - radius; j < pos.y + radius; ++j) {
                for(float k = pos.z - radius; k < pos.z + radius; ++k) {
                    final BlockPos blockPos = new BlockPos(MinecraftUtils.doubleToBlockCoords(new Vector3d(i, j, k)));
                    if(new Vector3f(i, j, k).distance(pos) <= radius && level.getBlockState(blockPos).isAir()) {
                        final ProductDisplay display = new ProductDisplay(level, blockPos, owner);
                        display.changeItem(itemList.get(Math.abs(rnd.nextInt() % itemList.size())).getDefaultInstance());
                        display.addDefaultRotation((float)Math.toRadians(45f) * (rnd.nextInt() % 8));
                        display.setStockLimit(1_000_000f);
                        display.changeStock(Math.abs(rnd.nextInt() % 1_000_000));
                        display.setPrice(Math.abs(rnd.nextLong() % 100_000));
                        display.addBalance(Math.abs(rnd.nextLong() % 100));
                        display.invalidateItemDisplay();
                        ++r;
                    }
                }
            }
        }
        return r;
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
     * @param display The display.
     * @return The created product display item.
     */
    public static @NotNull ItemStack createShopSnapshot(final @NotNull ProductDisplay display) {
        if(
            display.getItem().is(Items.AIR) &&
            display.getPrice() == Configs.getDisplay().price.getDefault() &&
            display.getMaxStock() == Configs.getDisplay().stock_limit.getDefault()
        ) {
            return getProductDisplayItemCopy();
        }

        // Get NBTs
        final ItemStack item = productDisplayItem.copy();
        final CompoundTag nbt = item.getOrCreateTag();
        final CompoundTag nbtDisplay = nbt.getCompound("display");
        final ListTag lore = nbtDisplay.getList("Lore", Tag.TAG_STRING);




        // Create and add display data NBT
        final CompoundTag data = new CompoundTag();

        data.putUUID  ("owner",      display.getOwnerUuid      ());
        data.putUUID  ("shop_uuid",  display.getShopUUID       ());
        data.putLong  ("price",      display.getPrice          ());
        data.putInt   ("stock",      display.getStock          ());
        data.putInt   ("max_stock",  display.getMaxStock       ());
        data.putFloat ("rotation",   display.getDefaultRotation());
        data.putFloat ("hue",        display.getColorThemeHue  ());
        data.putLong  ("balance",    display.getBalance        ());
        data.putString("item",       display.getSerializedItem ());
        data.putString("owner_name", FrameworkLib.getServer().getPlayerList().getPlayer(display.getOwnerUuid()).getName().getString());

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
            new Txt().cat(new Txt("Owner: "      ).lightGray().noItalic()).cat(new Txt(FrameworkLib.getServer().getPlayerList().getPlayer(display.getOwnerUuid()).getName().getString())).white().noItalic().get(),
            new Txt().cat(new Txt("Shop: "       ).lightGray().noItalic()).cat(new Txt(display.getShop().getDisplayName())).white().noItalic().get(), //TODO use colored text for display names? maybe? idk. might have to change the shop data too
            new Txt().cat(new Txt("Balance: "    ).lightGray().noItalic()).cat(new Txt(Utils.formatPrice(display.getBalance()))).gold().noItalic().get(),
            new Txt().cat(new Txt("Price: "      ).lightGray().noItalic()).cat(new Txt(Utils.formatPrice (display.getPrice   ()             ))).white().noItalic().get(),
            new Txt().cat(new Txt("Stock: "      ).lightGray().noItalic()).cat(new Txt(Utils.formatAmount(display.getStock   (), false, true))).white().noItalic().get(),
            new Txt().cat(new Txt("Stock limit: ").lightGray().noItalic()).cat(new Txt(Utils.formatAmount(display.getMaxStock(), false, true))).white().noItalic().get(),
            new Txt().cat(new Txt("Direction: "  ).lightGray().noItalic()).cat(new Txt(ROTATION_NAMES[(int)Math.round(display.getDefaultRotation() / Math.PI * 4) % 8])).white().noItalic().get(),
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
