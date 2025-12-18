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
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

import com.google.gson.Gson;
import com.snek.fancyplayershops.configs.Configs;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.fancyplayershops.main.Shop;
import com.snek.fancyplayershops.main.ShopKey;
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
 * A class that handles active shops and takes care of loading and saving their data.
 */
public final class ShopManager extends UtilityClassBase {
    private static final DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder()
        .appendPattern("MMMM d, yyyy 'at' h:mm ")
        .appendText(java.time.temporal.ChronoField.AMPM_OF_DAY,
            java.util.Map.of(0L, "am", 1L, "pm"))
        .toFormatter(Locale.ENGLISH)
    ;
    private ShopManager() {}
    private static final Random rnd = new Random();




    // Stores the shops of players, identifying them by their owner's UUID and their coordinates and level in the format "x,y,z,levelId"
    private static final @NotNull Map<@NotNull ShopKey, @Nullable Shop> shopsByCoords = new HashMap<>();
    private static final @NotNull Map<@NotNull UUID,    @Nullable HashSet<@NotNull Shop>> shopsByOwner  = new HashMap<>();
    private static boolean dataLoaded = false;
    public static @Nullable HashSet<@NotNull Shop> getShopsOfPlayer(final @NotNull Player player) { return shopsByOwner.get(player.getUUID()); }

    // Async update list
    private static int updateIndex = 0;
    private static @NotNull List<@NotNull Shop> updateSnapshot = List.of();
    private static final @NotNull RateLimiter updateCycleLimiter = new RateLimiter();

    // Keeps track of the amount of shops in each chunk
    private static final @NotNull Map<@NotNull ChunkPos, @Nullable Integer> chunkShopNumber = new HashMap<>();

    // The list of shops scheduled for saving
    private static @NotNull List<@NotNull Shop> scheduledForSaving = new ArrayList<>();




    // Shop item data
    //! Don't use the name or tooltip to check the item. Shops should work even when renamed in an anvil or modified by mods
    public  static final @NotNull String SHOP_ITEM_NBT_KEY = FancyPlayerShops.MOD_ID + ".item.shop_item";
    public  static final @NotNull String SNAPSHOT_NBT_KEY  = SHOP_ITEM_NBT_KEY + ".snapshot";
    private static final @NotNull ItemStack shopItem;

    // Shop item texture
    private static final @NotNull String SHOP_ITEM_TEXTURE =
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZj" +
        "I3ODQzMDdiODkyZjUyYjkyZjc0ZmE5ZGI0OTg0YzRmMGYwMmViODFjNjc1MmU1ZWJhNjlhZDY3ODU4NDI3ZSJ9fX0="
    ;

    // Shop item name
    public static final @NotNull Vector3i SHOP_ITEM_NAME_COLOR = new Vector3i(175, 140, 190);
    public static final @NotNull Component SHOP_ITEM_NAME =
        new Txt("Item Shop").noItalic().bold().color(SHOP_ITEM_NAME_COLOR)
    .get();

    // Shop item description
    private static final @NotNull Vector3i SHOP_ITEM_DESCRITPION_COLOR = new Vector3i(225, 180, 230);
    private static final @NotNull Component[] SHOP_ITEM_DESCRITPION = {
        new Txt().cat(new Txt("Shops").color(SHOP_ITEM_DESCRITPION_COLOR)).cat(new Txt(" allow you to sell items to other players.").white()).noItalic().get(),
        new Txt().cat(new Txt("Place this anywhere and ").white()).cat(new Txt("right click").color(SHOP_ITEM_DESCRITPION_COLOR)).cat(new Txt(" it to get started!").white()).noItalic().get(),
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




    // Initialize shop item stack
    static {

        // Create item and set custom name
        shopItem = MinecraftUtils.createCustomHead(SHOP_ITEM_TEXTURE, false);
        shopItem.setHoverName(SHOP_ITEM_NAME);

        // Set identification tag
        MinecraftUtils.addTag(shopItem, SHOP_ITEM_NBT_KEY);

        // Set lore
        final ListTag lore = new ListTag();
        for(final Component line : SHOP_ITEM_DESCRITPION) {
            lore.add(StringTag.valueOf(Component.Serializer.toJson(line)));
        }
        shopItem.getOrCreateTagElement("display").put("Lore", lore);
    }







    /**
     * Registers the shop in the runtime maps.
     * <p> Calling this method on a shop that's already registered will have no effect.
     */
    public static void registerShop(final @NotNull Shop shop) {
        if(shopsByCoords.put(shop.getKey(), shop) == null) {
            shopsByOwner.putIfAbsent(shop.getOwnerUuid(), new HashSet<>());
            shopsByOwner.get(shop.getOwnerUuid()).add(shop);

            final ChunkPos chunkPos = new ChunkPos(shop.getPos());
            chunkShopNumber.putIfAbsent(chunkPos, 0);
            chunkShopNumber.put(chunkPos, chunkShopNumber.get(chunkPos) + 1);
        }
    }

    /**
     * Unregisters the shop from the runtime maps.
     * <p> Calling this method on a shop that's already not registered will have no effect.
     */
    public static void unregisterShop(final @NotNull Shop shop) {
        if(shopsByCoords.remove(shop.getKey(), shop)) {
            final HashSet<Shop> set = shopsByOwner.get(shop.getOwnerUuid());
            if(set != null) set.remove(shop);

            final ChunkPos chunkPos = new ChunkPos(shop.getPos());
            chunkShopNumber.put(chunkPos, chunkShopNumber.get(chunkPos) - 1);
        }
    }

    public static boolean chunkHasShops(final @NotNull ChunkPos chunkPos) {
        final Integer n = chunkShopNumber.get(chunkPos);
        return n != null && n > 0;
    }








    /**
     * Schedules a shop for data saving.
     * Call saveScheduledShops() to save schedules shops.
     * @param shop The shop to save.
     */
    public static void scheduleShopSave(final @NotNull Shop shop) {
        if(!shop.isScheduledForSave()) {
            scheduledForSaving.add(shop);
            shop.setScheduledForSave(true);
        }
    }

    /**
     * Saves the data of all the shops schedules for saving in their config files.
     */
    public static void saveScheduledShops() {

        for(final Shop shop : scheduledForSaving) {

            // Create directory for the level
            final Path levelStorageDir = FancyPlayerShops.getStorageDir().resolve("shops/" + shop.getLevelId());
            try {
                Files.createDirectories(levelStorageDir);
            } catch(final IOException e) {
                FancyPlayerShops.LOGGER.error("Couldn't create the storage directory for the shop data of the level \"{}\"",shop.getLevelId(), e);
            }


            // Skip deleted shops
            if(shop.isDeleted()) {
                continue;
            }


            // Create this shop's config file if absent, then save the JSON in it
            final File shopStorageFile = new File(levelStorageDir + "/" + shop.getIdentifierNoLevel() + ".json");
            try (final Writer writer = new FileWriter(shopStorageFile)) {
                new Gson().toJson(shop, writer);
            } catch(final IOException e) {
                FancyPlayerShops.LOGGER.error("Couldn't create the storage file for the shop \"{}\"", shop.getIdentifierNoLevel(), e);
            }


            // Flag the shop as not scheduled
            shop.setScheduledForSave(false);
        }
        scheduledForSaving = new ArrayList<>();
    }




    /**
     * Loads all the player shops into the runtime map if needed.
     * <p> Must be called on server started event (After the levels are loaded!).
     * <p> If the data has already been loaded, the call will have no effect.
     */
    public static void loadShops() {
        if(dataLoaded) return;
        dataLoaded = true;


        for(final File levelStorageDir : FancyPlayerShops.getStorageDir().resolve("shops").toFile().listFiles()) {

            // For each shop file
            final File[] shopStorageFiles = levelStorageDir.listFiles();
            if(shopStorageFiles != null) for(final File shopStorageFile : shopStorageFiles) {

                // Read file and deserialize the data
                Shop retrievedShop = null;
                try (final Reader reader = new FileReader(shopStorageFile)) {
                    retrievedShop = new Gson().fromJson(reader, Shop.class);
                } catch(final IOException e) {
                    FancyPlayerShops.LOGGER.error("Couldn't read the storage file of the shop \"{}\"", shopStorageFile.getName(), e);
                }

                // Recalculate transient members and update shop maps
                if(retrievedShop != null) {
                    if(retrievedShop.reinitTransient()) {
                        registerShop(retrievedShop);
                    }
                }
            }
        }
    }








    /**
     * Returns the Shop instance present at a certain block position.
     * @param pos The block position.
     * @param level The level the shop is in.
     * @return The shop, or null if no shop is there.
    */
    public static Shop findShop(final @NotNull BlockPos pos, final @NotNull Level level) {
        return shopsByCoords.get(Shop.calcShopKey(pos, level));
    }




    /**
     * Deletes the data associated with this hop instance.
     * @param shop The shop to delete.
     */
    public static void deleteShop(final @NotNull Shop shop) {

        // Remove shop from the runtime maps
        unregisterShop(shop);

        // Delete the config file
        final File shopStorageFile = FancyPlayerShops.getStorageDir().resolve("shops/" + shop.getLevelId() + "/" + shop.getIdentifierNoLevel() + ".json").toFile();
        shopStorageFile.delete();
    }








    /**
     * Updates PULL_UPDATES_PER_TICK shops each call, making them pull items from nearby inventories.
     * <p> Must be called each server tick.
     */
    public static void pullItems() {
        if(!updateCycleLimiter.attempt()) return;


        // Refresh snapshot if needed
        if(updateIndex == 0) {
            updateSnapshot = new ArrayList<>(shopsByCoords.values());
        }

        // Update shops
        for(int i = 0; i < Configs.getPerf().pulls_per_tick.getValue() && updateIndex < updateSnapshot.size(); ++updateIndex) {

            final Shop shop = updateSnapshot.get(updateIndex);
            final ChunkPos chunkPos = new ChunkPos(shop.getPos());
            if(shop.getLevel().hasChunk(chunkPos.x, chunkPos.z)) {
                shop.pullItems();
                ++i;
            }
        }

        // Reset snapshop if this iteration reached its end
        if(updateIndex >= updateSnapshot.size()) {
            updateCycleLimiter.renewCooldown(Configs.getPerf().pull_cycle_frequency.getValue());
            updateIndex = 0;
        }
    }







    /**
     * Removes all shops near the specified position, sending all the items to their owner's stash.
     * @param level The target level.
     * @param pos The center of the purge radius.
     * @param radius The maximum distance from pos shops can have in order to be purged.
     * @return The number of shops that were removed.
     */
    public static int purge(final @NotNull ServerLevel level, final @NotNull Vector3f pos, final float radius) {
        int r = 0;
        final List<Shop> shops = new ArrayList<>(shopsByCoords.values());
        for(final Shop shop : shops) {
            if(shop.getLevel() == level && shop.calcDisplayPos().sub(pos).length() <= radius) {

                // Send feedback to affected player if they are online
                final Player owner = FrameworkLib.getServer().getPlayerList().getPlayer(shop.getOwnerUuid());
                if(owner != null && !shop.getItem().is(Items.AIR)) owner.displayClientMessage(new Txt()
                    .cat(new Txt("Your " + shop.getDecoratedName() + " has been removed by an admin.").red())
                .get(), false);

                // Stash, claim and delete the shop, then increase the purged shops counter
                shop.stash();
                shop.claimBalance();
                shop.delete();
                ++r;
            }
        }
        return r;
    }




    /**
     * Forces the owners to pick up any of their shops near the specified position, sending the snapshots to their stashes.
     * @param level The target level.
     * @param pos The center of the radius.
     * @param radius The maximum distance from pos shops can have in order to be picked up.
     * @return The number of shops that were picked up.
     */
    public static int displace(final @NotNull ServerLevel level, final @NotNull Vector3f pos, final float radius) {
        int r = 0;
        final List<Shop> shops = new ArrayList<>(shopsByCoords.values());
        for(final Shop shop : shops) {
            if(shop.getLevel() == level && shop.calcDisplayPos().sub(pos).length() <= radius) {

                // Send feedback to affected player if they are online
                final Player owner = FrameworkLib.getServer().getPlayerList().getPlayer(shop.getOwnerUuid());
                if(owner != null && !shop.getItem().is(Items.AIR)) owner.displayClientMessage(new Txt()
                    .cat(new Txt("Your " + shop.getDecoratedName() + " was converted into an item by an admin.").red())
                .get(), false);

                // Stash and delete the shop, then increase the displaced shops counter
                shop.pickUp(false);
                shop.delete();
                ++r;
            }
        }
        return r;
    }




    /**
     * Fill an area around the specified position with shop blocks.
     * @param level The target level.
     * @param pos The center of the fill area.
     * @param radius The maximum distance to reach on each cardinal direction.
     * @param owner The owner of the newly created shops.
     * @return The number of shops that were created.
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
                        final Shop shop = new Shop(level, blockPos, owner);
                        shop.changeItem(itemList.get(Math.abs(rnd.nextInt() % itemList.size())).getDefaultInstance());
                        shop.addDefaultRotation((float)Math.toRadians(45f) * (rnd.nextInt() % 8));
                        shop.setStockLimit(1_000_000f);
                        shop.changeStock(Math.abs(rnd.nextInt() % 1_000_000));
                        shop.setPrice(rnd.nextLong() % 100_000);
                        shop.addBalance(rnd.nextLong() % 100);
                        shop.invalidateItemDisplay();
                        ++r;
                    }
                }
            }
        }
        return r;
    }








    /**
     * Returns a copy if the default shop item.
     * @return A copy of the shop item.
     */
    public static @NotNull ItemStack getShopItemCopy() {
        return shopItem.copy();
    }




    /**
     * Creates a shop item containing the informations required to fully restore the provided shop.
     * @param shop The shop.
     * @return The created shop item.
     */
    public static @NotNull ItemStack createShopSnapshot(final @NotNull Shop shop) {
        if(
            shop.getItem().is(Items.AIR) &&
            shop.getPrice() == Configs.getShop().price.getDefault() &&
            shop.getMaxStock() == Configs.getShop().stock_limit.getDefault()
        ) {
            return getShopItemCopy();
        }

        // Get NBTs
        final ItemStack item = shopItem.copy();
        final CompoundTag nbt = item.getOrCreateTag();
        final CompoundTag display = nbt.getCompound("display");
        final ListTag lore = display.getList("Lore", Tag.TAG_STRING);




        // Create and add shop data NBT
        final CompoundTag data = new CompoundTag();

        data.putUUID  ("owner",      shop.getOwnerUuid      ());
        data.putUUID  ("group_uuid", shop.getShopGroupUUID  ());
        data.putLong  ("price",      shop.getPrice          ());
        data.putInt   ("stock",      shop.getStock          ());
        data.putInt   ("max_stock",  shop.getMaxStock       ());
        data.putFloat ("rotation",   shop.getDefaultRotation());
        data.putFloat ("hue",        shop.getColorThemeHue  ());
        data.putLong  ("balance",    shop.getBalance        ());
        data.putString("item",       shop.getSerializedItem ());
        data.putString("owner_name", FrameworkLib.getServer().getPlayerList().getPlayer(shop.getOwnerUuid()).getName().getString());

        final Component[] extraDescriptionLines = {
            new Txt()
                .cat(new Txt("This ").white().noItalic())
                .cat(new Txt("snapshot").color(SHOP_ITEM_DESCRITPION_COLOR).noItalic())
                .cat(new Txt(" was captured on ").white().noItalic())
                .cat(new Txt(LocalDateTime.now().format(timeFormatter)).color(SHOP_ITEM_DESCRITPION_COLOR).noItalic())
                .cat(new Txt(".").white().noItalic())
            .get(),
            new Txt()
                .cat(new Txt("It will automatically ").white().noItalic())
                .cat(new Txt("restore").color(SHOP_ITEM_DESCRITPION_COLOR).noItalic())
                .cat(new Txt(" its stock and settings when placed.").white().noItalic())
            .get(),
            new Txt().get(),
            new Txt().cat(new Txt("Owner: "      ).lightGray().noItalic()).cat(new Txt(FrameworkLib.getServer().getPlayerList().getPlayer(shop.getOwnerUuid()).getName().getString())).white().noItalic().get(),
            new Txt().cat(new Txt("Group: "      ).lightGray().noItalic()).cat(new Txt(shop.getShopGroup().getDisplayName())).white().noItalic().get(), //TODO use colored text for shop names? maybe? idk. might have to change the group data too
            new Txt().cat(new Txt("Balance: "    ).lightGray().noItalic()).cat(new Txt(Utils.formatPrice(shop.getBalance()))).gold().noItalic().get(),
            new Txt().cat(new Txt("Price: "      ).lightGray().noItalic()).cat(new Txt(Utils.formatPrice (shop.getPrice   ()             ))).white().noItalic().get(),
            new Txt().cat(new Txt("Stock: "      ).lightGray().noItalic()).cat(new Txt(Utils.formatAmount(shop.getStock   (), false, true))).white().noItalic().get(),
            new Txt().cat(new Txt("Stock limit: ").lightGray().noItalic()).cat(new Txt(Utils.formatAmount(shop.getMaxStock(), false, true))).white().noItalic().get(),
            new Txt().cat(new Txt("Direction: "  ).lightGray().noItalic()).cat(new Txt(ROTATION_NAMES[(int)Math.round(shop.getDefaultRotation() / Math.PI * 4) % 8])).white().noItalic().get(),
            new Txt().cat(new Txt("Color: "      ).lightGray().noItalic()).cat(new Txt("█")).color(Utils.HSVtoRGB(new Vector3f(shop.getColorThemeHue(), Edit_ColorSelector.S, Edit_ColorSelector.V))).noItalic().get(),
            new Txt().get()
        };
        for(final Component line : extraDescriptionLines) {
            lore.add(StringTag.valueOf(Component.Serializer.toJson(line)));
        }

        display.put("Lore", lore);
        nbt.put("display", display);
        nbt.put(FancyPlayerShops.MOD_ID + ".shop_data", data);




        // Set new NBTs and return the item
        item.setTag(nbt);
        item.setHoverName(new Txt()
            .cat(new Txt("Shop snapshot").color(SHOP_ITEM_NAME_COLOR).bold().noItalic())
            .cat(new Txt(" - ").white())
            .cat(new Txt(shop.getStandaloneName()).white().bold().noItalic())
        .get());
        MinecraftUtils.addTag(item, SNAPSHOT_NBT_KEY);
        return item;
    }
}
