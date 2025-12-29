package com.snek.fancyplayershops.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

import com.herrkatze.solsticeEconomy.modules.economy.EconomyManager;
import com.snek.fancyplayershops.configs.Configs;
import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.data.StashManager;
import com.snek.fancyplayershops.data.data_types.Shop;
import com.snek.frameworklib.input.MessageReceiver;
import com.snek.fancyplayershops.graphics.ui.ProductDisplay_Context;
import com.snek.fancyplayershops.graphics.ui.core.elements.ProductCanvasBase;
import com.snek.fancyplayershops.graphics.ui.core.elements.ProductItemDisplayElm;
import com.snek.fancyplayershops.graphics.ui.buy.BuyCanvas;
import com.snek.fancyplayershops.graphics.ui.details.DetailsCanvas;
import com.snek.fancyplayershops.graphics.ui.edit.EditCanvas;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.data_types.containers.Pair;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;
import com.snek.frameworklib.utils.scheduler.RateLimiter;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
















/**
 * This class manages a product display that is placed somewhere in a level.
 */
public class ProductDisplay {

    // Strings
    public static final Component EMPTY_PRODUCT_DISPLAY_NAME  = new Txt("[Empty]").italic().lightGray().get();
    public static final Component PRODUCT_DISPLAY_EMPTY_TEXT  = new Txt("This display is empty!").lightGray().get();
    public static final Component PRODUCT_DISPLAY_STOCK_TEXT  = new Txt("This display has no items in stock!").lightGray().get();
    public static final Component PRODUCT_DISPLAY_AMOUNT_TEXT = new Txt("This display doesn't have enough items in stock!").lightGray().get();



    // Basic data
    private transient @NotNull  ServerLevel           level;                            // The level this display was placed in
    // private           @NotNull  String                levelId;                          // The Identifier of the level
    private transient @Nullable ProductItemDisplayElm itemDisplay = null;               // The item display entity //! Searched when needed instead of on data loading because the chunk needs to be loaded in order to find the entity.
    private     final @NotNull  BlockPos              pos;                              // The position of the display
    private transient @NotNull  String                displayIdentifierCache_noLevel;   // The cached display identifier, not including the level
    private transient @NotNull  ProductDisplayKey     displayKeyCache;                  // The cached display key


    // Display data
    private transient @NotNull  ItemStack item = Items.AIR.getDefaultInstance(); // The configured item
    private           @NotNull  UUID      ownerUUID;                             // The UUID of the owner
    private                     long      balance         = 0;                   // The balance collected by the shop since it was last claimed
    // private           @NotNull String    serializedItem;                        // The item in serialized form //TODO REMOVE
    private                     long      price           = 0l;                  // The configured price for each item
    private                     int       stock           = 0;                   // The current stock
    private                     int       maxStock        = 0;                   // The configured maximum stock
    private                     float     defaultRotation = 0f;                  // The configured item rotation
    private                     float     colorThemeHue   = 0f;                  // The configured hue of the color theme
    // private           @NotNull  UUID      shopUUID;                              // The UUID of the shop this display has been assigned to
    private transient @NotNull  Shop      shop;                                  // The shop this display has been assigned to
    private                     boolean   nbtFilter       = true;                // The configured nbt filter setting


    // Stored items
    private transient @NotNull Map<@NotNull UUID, @NotNull Pair<@NotNull ItemStack, @NotNull Integer>> storedItems = new HashMap<>();
    public @NotNull Map<@NotNull UUID, @NotNull Pair<@NotNull ItemStack, @NotNull Integer>> getStoredItems() { return storedItems; }


    // Display state
    private transient @Nullable ProductDisplay_Context ui    = null;   // The UI context used for the display
    private transient @Nullable Player      user             = null;   // The current user of the display (the player that first opened a menu)
    private transient @Nullable Player      viewer           = null;   // The prioritized viewer
    private transient           boolean     deletionState    = false;  // True if the display has been deleted, false otherwise
    private transient           boolean     focusState       = false;  // True if the display is currently being looked at by at least one player, false otherwise
    private transient           boolean     focusStateNext   = false;  // The next focus state
    private transient @NotNull  RateLimiter menuOpenLimiter  = new RateLimiter();
    private transient           boolean     scheduledForSave = false;


    // Getters
    public @NotNull  ServerLevel            getLevel            () { return level;                           }
    // public @NotNull  String                 getLevelId          () { return levelId;                         }
    public @NotNull  BlockPos               getPos              () { return pos;                             }
    public @NotNull  ItemStack              getItem             () { return item;                            }
    // public @NotNull  String                 getSerializedItem   () { return serializedItem;                  } //TODO REMOVE
    public @NotNull  ProductItemDisplayElm  getItemDisplay      () { return findItemDisplayEntityIfNeeded(); }
    public @Nullable ProductDisplay_Context getUi               () { return ui;                              }
    public           long                   getPrice            () { return price;                           }
    public           long                   getBalance          () { return balance;                         }
    public           int                    getStock            () { return stock;                           }
    public           int                    getMaxStock         () { return maxStock;                        }
    public           float                  getDefaultRotation  () { return defaultRotation;                 }
    public           boolean                isFocused           () { return focusState;                      }
    public           boolean                isDeleted           () { return deletionState;                   }
    public @NotNull  UUID                   getOwnerUuid        () { return ownerUUID;                       }
    public @Nullable Player                 getuser             () { return user;                            }
    public @Nullable Player                 getViewer           () { return viewer;                          }
    public           boolean                isScheduledForSave  () { return scheduledForSave;                }
    public @NotNull  ProductDisplayKey      getKey              () { return displayKeyCache;                 }
    public @NotNull  String                 getIdentifierNoLevel() { return displayIdentifierCache_noLevel;  }
    public           float                  getColorThemeHue    () { return colorThemeHue;                   }
    // public @NotNull  UUID                   getShopUUID         () { return shopUUID;                        }
    public @NotNull  Shop                   getShop             () { return shop;                            }
    public           boolean                getNbtFilter        () { return nbtFilter;                       }


    // Setters
    public void setViewer          (final @Nullable Player  viewer        ) { this.viewer         = viewer;         }
    public void setScheduledForSave(final           boolean scheduled     ) { scheduledForSave    = scheduled;      }
    public void setFocusStateNext  (final           boolean nextFocusState) { this.focusStateNext = nextFocusState; }








    /**
     * Returns the active canvas of the product display's UI.
     * @return The active canvas, or null if either the UI or the canvas is null.
     */
    public @Nullable ProductCanvasBase getActiveCanvas() {
        if(ui == null) return null;
        return (ProductCanvasBase)ui.getActiveCanvas();
    }
    /**
     * Calculates the position display entities should be spawned at.
     * @return The absolute position of display entities.
     */
    public @NotNull Vector3d calcDisplayPos() {
        return new Vector3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
    }


    // /**
    //  * Computes the Identifier of the level.
    //  */
    // private void calcSerializedLevelId() {
    //     levelId = level.dimension().location().toString();
    // }


    // /**
    //  * Tries find the ServerLevel the level identifier belongs to.
    //  * @throws RuntimeException if the level Identifier is invalid or the ServerLevel cannot be found.
    //  */
    // private void calcDeserializedLevelId() throws RuntimeException {
    //     for(final ServerLevel w : FrameworkLib.getServer().getAllLevels()) {
    //         if(w.dimension().location().toString().equals(levelId)) {
    //             level = w;
    //             return;
    //         }
    //     }
    //     throw new RuntimeException("Invalid product display data: Specified level \"" + levelId + "\" was not found");
    // }


    /**
     * Computes and caches the display identifiers.
     */
    private void cacheDisplayIdentifier() {
        displayIdentifierCache_noLevel = calcDisplayIdentifier(pos);
    }
    /**
     * Computes and caches the display key.
     */
    private void cacheDisplayKey() {
        displayKeyCache = calcDisplayKey(pos, level);
    }


    /**
     * Calculates a display identifier from the position. This identifier doesn't include the level ID.
     * @param _pos The position.
     * @return The generated identifier.
     */
    public static String calcDisplayIdentifier(final @NotNull BlockPos _pos) {
        return String.format("%d,%d,%d", _pos.getX(), _pos.getY(), _pos.getZ());
    }
    /**
     * Calculates a display identifier from the position. This identifier doesn't include the level ID.
     * @param _pos The position.
     * @return The generated identifier.
     */
    public static ProductDisplayKey calcDisplayKey(final @NotNull BlockPos _pos, final @NotNull Level level) {
        return new ProductDisplayKey(_pos, level);
    }


    // /**
    //  * Reinitializes the transient members.
    //  * @return Whether the item and level id have been deserialized successfully.
    //  * <p> Displays whose data cannot be deserialized shouldn't be loaded as their save file is likely corrupted.
    // */
    // public boolean reinitTransient() {
    //     focusState      = false;
    //     focusStateNext  = false;
    //     menuOpenLimiter = new RateLimiter();
    //     cacheDisplayIdentifier();
    //     storedItems = new HashMap<>();


    //     // item = MinecraftUtils.deserializeItem(serializedItem);
    //     // if(item == null) {
    //     //     FancyPlayerShops.LOGGER.error("Couldn't deserialize item \"{}\"", serializedItem, new RuntimeException());
    //     //     return false;
    //     // }
    //     // try {
    //     //     calcDeserializedLevelId();
    //     // } catch(final RuntimeException e) {
    //     //     FancyPlayerShops.LOGGER.error("Couldn't deserialize level ID \"{}\"", levelId, e);
    //     //     return false;
    //     // }

    //     cacheDisplayKey();
    //     shop = ShopManager.registerDisplay(this, shopUUID);
    //     return true;
    // }
















    // /**
    //  * Creates a new ProductDisplay and saves it in its own file.
    //  * @param level The level the display has to be created in.
    //  * @param _pos The position of the new display.
    //  * @param owner The player that places the display.
    //  */
    // public ProductDisplay(final @NotNull ServerLevel level, final @NotNull BlockPos _pos, final @NotNull Player owner) {

    //     // Set display data
    //     this.level = level;
    //     ownerUUID = owner.getUUID();
    //     pos = _pos;
    //     price         = Configs.getDisplay().price.getDefault();
    //     maxStock      = Configs.getDisplay().stock_limit.getDefault();
    //     colorThemeHue = Configs.getDisplay().theme_hues.getValue()[Configs.getDisplay().theme.getDefault()];

    //     // Calculate serialized data and display identifier
    //     //!No need to check that serializedItem != null, the item being serialized is the default AIR item
    //     serializedItem = MinecraftUtils.serializeItem(item);
    //     calcSerializedLevelId();
    //     cacheDisplayIdentifier();
    //     cacheDisplayKey();
    //     shopUUID = ShopManager.DEFAULT_SHOP_UUID;
    //     shop = ShopManager.registerDisplay(this, ShopManager.DEFAULT_SHOP_UUID);

    //     // Create and spawn the Item Display entity
    //     itemDisplay = new ProductItemDisplayElm(this);
    //     itemDisplay.spawn(calcDisplayPos(), true);

    //     // Save the display
    //     ProductDisplayManager.registerDisplay(this);
    //     ProductDisplayManager.scheduleDisplaySave(this);
    // }




    // /**
    //  * Creates a new ProductDisplay and saves it in its own file.
    //  * @param level The level the display has to be created in.
    //  * @param _pos The position of the new display.
    //  * @param _ownerUUID The UUID of the player that places the display.
    //  * @param _price The price of the item.
    //  * @param _stock The current stock.
    //  * @param _maxStock The stock limit.
    //  * @param _rotation The default rotation of the item display.
    //  * @param _hue The hue of the color theme.
    //  * @param _serializedIitem The item in serialized form.
    //  */
    // public ProductDisplay(
    //     final @NotNull ServerLevel level, final @NotNull BlockPos _pos,
    //     final @NotNull UUID _ownerUUID, final UUID _shopUUID, final long _balance,
    //     final long _price, final int _stock, final int _maxStock, final float _rotation, final float _hue, final @NotNull String _serializedIitem
    // ) {

    //     // Set display data
    //     this.level = level;
    //     ownerUUID = _ownerUUID;
    //     balance = _balance;
    //     pos = _pos;
    //     price = _price;
    //     stock = _stock;
    //     maxStock = _maxStock;
    //     defaultRotation = _rotation;
    //     colorThemeHue = _hue;

    //     // Calculate serialized data
    //     calcSerializedLevelId();
    //     serializedItem = _serializedIitem;

    //     // Get members from serialized data and calculate display identifier
    //     final @Nullable ItemStack _item = MinecraftUtils.deserializeItem(serializedItem);
    //     if(_item == null) {
    //         FancyPlayerShops.LOGGER.error("Item of product display coudln't be deserialized. Using AIR as fallback", new RuntimeException());
    //         item = Items.AIR.getDefaultInstance();
    //     }
    //     else {
    //         item = _item;
    //     }
    //     cacheDisplayIdentifier();
    //     cacheDisplayKey();
    //     shopUUID = _shopUUID;
    //     shop = ShopManager.registerDisplay(this, _shopUUID);

    //     // Create and spawn the Item Display entity
    //     itemDisplay = new ProductItemDisplayElm(this);
    //     itemDisplay.spawn(calcDisplayPos(), true);

    //     // Save the display
    //     ProductDisplayManager.registerDisplay(this);
    //     ProductDisplayManager.scheduleDisplaySave(this);
    // }



    /**
     * Creates a new ProductDisplay and saves it in its own file.
     * @param level The level the display has to be created in.
     * @param _pos The position of the new display.
     * @param _ownerUUID The UUID of the player that places the display.
     * @param _price The price of the item.
     * @param _stock The current stock.
     * @param _maxStock The stock limit.
     * @param _rotation The default rotation of the item display.
     * @param _hue The hue of the color theme.
     * @param _serializedIitem The item in serialized form.
     */
    public ProductDisplay(
        final @NotNull UUID ownerUUID,
        final @NotNull UUID shopUUID,
        final long price,
        final int stock,
        final int maxStock,
        final float rotation,
        final float hue,
        final long balance,
        final boolean nbtFilter,
        final @NotNull BlockPos position,
        final @NotNull ServerLevel level,
        final @NotNull ItemStack item,
        final @NotNull Map<@NotNull UUID, @NotNull Pair<@NotNull ItemStack, @NotNull Integer>> storedItems
    ) {

        // Set basic data
        this.ownerUUID = ownerUUID;
        this.price = price;
        this.stock = stock;
        this.maxStock = maxStock;
        this.defaultRotation = rotation;
        this.colorThemeHue = hue;
        this.balance = balance;
        this.nbtFilter = nbtFilter;
        this.pos = position;
        this.level = level;
        this.item = item;
        this.storedItems = storedItems;

        // Register this display in the shop and store the shop instance locally
        this.shop = ShopManager.registerDisplay(this, shopUUID);

        // Recalculate identifier and key
        cacheDisplayIdentifier();
        cacheDisplayKey();

        // Create and spawn the Item Display entity
        itemDisplay = new ProductItemDisplayElm(this);
        itemDisplay.spawn(calcDisplayPos(), true);

        // Save the display
        ProductDisplayManager.registerDisplay(this);
        ProductDisplayManager.scheduleDisplaySave(this);
    }








    /**
     * Spawns or removes the focus displays and starts item animations depending on the set next focus state.
     */
    public void updateFocusState() {
        if(!menuOpenLimiter.attempt()) return;
        if(focusState != focusStateNext) {
            focusState = focusStateNext;

            if(focusState) {

                // Create details canvas
                ui = new ProductDisplay_Context(this, viewer);
                ui.spawn(MinecraftUtils.blockSourceCoords(pos), true);
                ui.changeCanvas(new DetailsCanvas(this));

                // Start item animation and turn off the CustomName
                getItemDisplay().enterFocusState();
            }
            else {

                // Despawn active UI
                ui.despawn(true);

                // Cancel chat input callbacks, then reset the user and renew the focus cooldown
                menuOpenLimiter.renewCooldown(ProductItemDisplayElm.D_TIME);
                if(user != null) MessageReceiver.removeCallback(user);
                user = null;

                // Turn the item display's custom name back on
                getItemDisplay().leaveFocusState();
            }
        }
    }




    public void invalidateItemDisplay() {
        if(itemDisplay != null) {
            itemDisplay.despawn(false);
            itemDisplay = null;
        }
        getItemDisplay();
    }




    /**
     * Finds the display entity connected to this product display and saves it to this.itemDisplay.
     * <p> If no connected entity is found, a new {@link ProductItemDisplayElm} is created.
     * @reutrn the item display.
     */
    private @NotNull ProductItemDisplayElm findItemDisplayEntityIfNeeded() {

        if(itemDisplay == null || itemDisplay.getEntity().isRemoved()) {
            itemDisplay = new ProductItemDisplayElm(this);
            itemDisplay.spawn(calcDisplayPos(), true);
        }
        return itemDisplay;
    }




    /**
     * Handles a single click event on this display.
     * @param player The player that clicked the display.
     * @param click The click type.
     * @return Whether the player has permission to click the display's UI.
     */
    public boolean onClick(final @NotNull Player player, final @NotNull ClickAction clickType) {


        // If the display is currently focused
        //! checking that the display is focused prevents erroneous "someone else is using the display" errors
        //! in case of clicks during the focus cooldown time or before the focus is actually registered
        if(isFocused()) {

            // If the display is not currently being used, flag the player as its user
            if(user == null) {
                if(clickType == ClickAction.PRIMARY) {
                    if(player.getUUID().equals(ownerUUID)) {
                        retrieveItem(player, 1, true);
                    }
                    else {
                        buyItem(player, 1, true);
                    }
                }
                else {
                    if(player.getUUID().equals(ownerUUID)) {
                        user = player;
                        openEditUi(player);
                    }
                    else {
                        if(canBuyUiBeOpened()) user = player;
                        openBuyUi(player, true);
                    }
                }

                //! The click succeeded, but there is no need to forward it to the current canvas as it will be replaced instantly
                return false;
            }


            // If the player that clicked has already opened a menu, it means they have permission
            else if(user == player) {
                return true;
            }


            // Send an error message to the player if someone else has already opened a menu in the same display, then return false
            else {
                if(clickType == ClickAction.SECONDARY) {
                    player.displayClientMessage(new Txt(
                        "Someone else is already using this display! Left click to " +
                        (player.getUUID().equals(ownerUUID) ? "retrieve" : "buy") +
                        " one item."
                    ).lightGray().get(), true);
                }
                else {
                    if(player.getUUID().equals(ownerUUID)) {
                        retrieveItem(player, 1, true);
                    }
                    else {
                        buyItem(player, 1, true);
                    }
                }
                return false;
            }
        }
        return false;
    }




    /**
     * Retrieves the specified amount of items from the display at no cost and gives them to the owner.
     * <p> Sends an error message to the player if the display is unconfigured or doesn't contain any item.
     * @param owner The owner of the display.
     * @param amount The amount of items to retrieve.
     * @param stashExcess Whether to stash the items that didn't fit in the inventory or send them back to the display.
     */
    public void retrieveItem(final @NotNull Player owner, final int amount, final boolean stashExcess) {
        if(item.is(Items.AIR)) {
            owner.displayClientMessage(PRODUCT_DISPLAY_EMPTY_TEXT, true);
        }
        else if(stock < 1) {
            owner.displayClientMessage(PRODUCT_DISPLAY_STOCK_TEXT, true);
        }
        else if(stock < amount) {
            owner.displayClientMessage(PRODUCT_DISPLAY_AMOUNT_TEXT.copy().append(new Txt(" Items left: " + stock).lightGray().get()), true);
        }
        else {
            ProductDisplayManager.scheduleDisplaySave(this);


            // Send feedback to the player
            final ItemStack _item = item.copyWithCount(amount);
            MinecraftUtils.attemptGive(owner, _item);
            final int stashedAmount = _item.getCount();
            final int retrievedAmount = amount - stashedAmount;

            if(retrievedAmount > 0) {
                owner.displayClientMessage(new Txt()
                    .cat(new Txt("You retrieved ").lightGray())
                    .cat(new Txt(Utils.formatAmount(retrievedAmount, true, true) + " " + MinecraftUtils.getFancyItemName(item).getString()).white())
                    .cat(new Txt(" from your product display.").lightGray())
                .get(), false);
                stock -= retrievedAmount;
            }
            else if(!stashExcess) {
                owner.displayClientMessage(new Txt("No item was retrieved. Your inventory is full.").lightGray().get(), false);
            }

            if(stashExcess && stashedAmount > 0) {
                StashManager.stashItem(owner.getUUID(), _item, stashedAmount);
                StashManager.scheduleStashSave(owner.getUUID());
                owner.displayClientMessage(new Txt()
                    .cat(new Txt("" + Utils.formatAmount(stashedAmount, true, true) + " ").white())
                    .cat(new Txt(MinecraftUtils.getFancyItemName(item)).white())
                    .cat(new Txt(" retrieved from your product display " + (stashedAmount > 1 ? "have" : "has") + " been sent to your stash.").lightGray())
                .get(), false);
                stock -= stashedAmount;
            }


            // Update active canvas
            final ProductCanvasBase activeCanvas = (ProductCanvasBase)ui.getActiveCanvas();
            if(activeCanvas != null) {
                activeCanvas.onStockChange();
            }
        }
    }




    /**
     * Makes a player buy a specified amount of items from the display.
     * <p> Sends an error message to the player if the display is unconfigured or doesn't contain enough items or the player cannot afford to buy the items.
     * @param buyer The player.
     * @param amount The amount of items to buy.
     * @param stashExcess Whether to stash the items that didn't fit in the inventory or send them back to the display.
     */
    public void buyItem(final @NotNull Player buyer, final int amount, final boolean stashExcess) {
        if(item.is(Items.AIR)) {
            buyer.displayClientMessage(PRODUCT_DISPLAY_EMPTY_TEXT, true);
        }
        else if(stock < 1) {
            buyer.displayClientMessage(PRODUCT_DISPLAY_STOCK_TEXT, true);
        }
        else if(stock < amount) {
            buyer.displayClientMessage(PRODUCT_DISPLAY_AMOUNT_TEXT.copy().append(new Txt(" Items left: " + stock).lightGray().get()), true);
        }
        else {
            final long totPrice = price * amount;

            if(EconomyManager.getCurrency(buyer.getUUID()) >= totPrice) {
                ProductDisplayManager.scheduleDisplaySave(this);
                EconomyManager.subtractCurrency(buyer.getUUID(), totPrice);
                addBalance(totPrice);


                // Send feedback to the player
                final ItemStack _item = item.copyWithCount(amount);
                MinecraftUtils.attemptGive(buyer, _item);
                final int stashedAmount = _item.getCount();
                final int retrievedAmount = amount - stashedAmount;

                if(retrievedAmount > 0) {
                    buyer.displayClientMessage(new Txt()
                        .cat(new Txt("Bought ").lightGray())
                        .cat(new Txt(Utils.formatAmount(retrievedAmount, true, true) + " " + MinecraftUtils.getFancyItemName(item).getString()).white())
                        .cat(new Txt(" for " + Utils.formatPrice(totPrice)).lightGray())
                    .get(), false);
                    stock -= retrievedAmount;
                }
                else if(!stashExcess) {
                    buyer.displayClientMessage(new Txt("No item was bought. Your inventory is full.").lightGray().get(), false);
                }

                if(stashExcess && stashedAmount > 0) {
                    StashManager.stashItem(buyer.getUUID(), _item, _item.getCount());
                    StashManager.scheduleStashSave(buyer.getUUID());
                    buyer.displayClientMessage(new Txt()
                        .cat(new Txt("" + Utils.formatAmount(stashedAmount, true, true) + " ").white())
                        .cat(new Txt(MinecraftUtils.getFancyItemName(item).getString()).white())
                        .cat(new Txt(" bought for " + Utils.formatPrice(totPrice) + " " + (stashedAmount > 1 ? "have" : "has") + " been sent to your stash.").lightGray())
                    .get(), false);
                    stock -= stashedAmount;
                }


                // Update active canvas
                final ProductCanvasBase activeCanvas = (ProductCanvasBase)ui.getActiveCanvas();
                if(activeCanvas != null) {
                    activeCanvas.onStockChange();
                }
            }
            else {
                buyer.displayClientMessage(new Txt("You don't have enough money to purchase this!").bold().red().get(), true);
            }
        }

        //TODO show undo button in transaction history to let players undo accidental purchases
        //TODO no need for retrieval undo as the owner can simply put the item back in
    }



    /**
     * Adds the specified amount to this display's balance and its shop's balance.
     * @param amount The amount to add. Must be >= 0
     */
    public void addBalance(final long amount) {
        this.balance += amount;
        shop.addBalance(amount);
    }


    /**
     * Sets the balance back to 0, without sending it to the owner's balance.
     * <p>
     * This also updates the display's shop's balance.
     */
    public void clearBalance() {
        shop.subBalance(getBalance());
        balance = 0;
    }


    //TODO call this from somewhere
    /**
     * Claims the current balance, sending it to the owner's balance.
     * <p>
     * This also updates the display's shop's balance.
     */
    public void claimBalance() {
        EconomyManager.addCurrency(ownerUUID, balance);
        clearBalance();
    }




    /**
     * Switches the active canvas with a new one and plays a sound.
     * @param canvas The new canvas.
     */
    public void changeCanvas(final @NotNull ProductCanvasBase canvas) {
        ui.changeCanvas(canvas);
        if(user != null) Clickable.playSound(user);
    }




    /**
     * Opens the edit display UI.
     * @param player The player.
     */
    public void openEditUi(final @NotNull Player player) {
        changeCanvas(new EditCanvas(this));
        getItemDisplay().enterEditState();
    }




    public boolean canBuyUiBeOpened() {
        return !item.is(Items.AIR);
    }

    /**
     * Attempts to opens the buy item UI.
     * @param player The player.
     * @param adjustItemDisplay Wether to change the size and position of the item display from the standard focused transform to the "edit" transform.
     */
    public boolean openBuyUi(final @NotNull Player player, final boolean adjustItemDisplay) {
        if(!canBuyUiBeOpened()) {
            player.displayClientMessage(PRODUCT_DISPLAY_EMPTY_TEXT, true);
            return false;
        }
        else {
            changeCanvas(new BuyCanvas(this));
            if(adjustItemDisplay) getItemDisplay().enterEditState();
            return true;
        }
    }




    /**
     * Tries to set a new price for the item and sends an error message to the user if it's invalid.
     *     <p> Prices are automatically rounded to the nearest hundredth.
     *     <p> Prices under 0.01 are rounded to 0.01
     *     <p> Prices under 0.00001 are rounded to 0.
     *     <p> Negative values are considered invalid and return false without changing the price.
     *     <p> Values above MAX_PRICE are also considered invalid.
     * @param _newPrice The new price
     * @return Whether the new value could be set.
     */
    public boolean setPrice(final double _newPrice) {

        // Convert price input from double to long, avoiding precision errors
        long newPrice = (long)_newPrice; newPrice = newPrice * 100l + Math.round((_newPrice - newPrice) * 100d);

        // Check if the price is valid and proceed accordingly
        if(newPrice < 0) {
            if(user != null) user.displayClientMessage(new Txt("The price cannot be negative").red().bold().get(), true);
            return false;
        }
        if(newPrice > Configs.getDisplay().price.getMax()) {
            if(user != null) user.displayClientMessage(new Txt("The price cannot be greater than " + Utils.formatPrice(Configs.getDisplay().price.getMax())).red().bold().get(), true);
            return false;
        }
        else if(newPrice < 0.00001) price = 0;
        else if(newPrice < 0.01000) price = 1;
        else price = newPrice;
        ProductDisplayManager.scheduleDisplaySave(this);
        return true;
    }




    /**
     * Tries to set a new stock limit for the item and sends an error message to the user if it's invalid.
     *     <p> Amounts are rounded to the nearest integer.
     *     <p> Negative values and 0 are considered invalid and return false without changing the stock limit.
     *     <p> Values above MAX_STOCK or that are higher than the display's storage capacity are also considered invalid. //TODO implement shop tiers
     * @param newStockLimit The new stock limit.
     * @return Whether the new value could be set.
     */
    public boolean setStockLimit(final float newStockLimit) {
        if(newStockLimit < 0.9999) {
            if(user != null) user.displayClientMessage(new Txt("The stock limit must be at least 1").red().bold().get(), true);
            return false;
        }
        if(newStockLimit > Configs.getDisplay().stock_limit.getMax()) {
            if(user != null) user.displayClientMessage(new Txt("The stock limit cannot be greater than " + Utils.formatAmount(Configs.getDisplay().stock_limit.getMax(), false, true)).red().bold().get(), true);
            return false;
        }
        else maxStock = Math.round(newStockLimit);
        ProductDisplayManager.scheduleDisplaySave(this);
        return true;
    }




    /**
     * Adds a specified rotation to the default rotation of the item display and saves the product display to its file.
     * @param _rotation The rotation to add. Negative values are allowed and are converted to their positive equivalent.
     */
    public void addDefaultRotation(final float _rotation) {

        // Add value to default rotation and save the display
        //! Reduce range to [-2pi, 2pi], then add 2pi to wrap negative values and reduce to [-2pi, 2pi] again
        final double r = Math.PI * 2d;
        defaultRotation = (float)(((defaultRotation + _rotation) % r + r) % r);
        ProductDisplayManager.scheduleDisplaySave(this);
    }




    /**
     * Changes the item sold by this display and saves it to its file.
     * @param newItem the new item.
     */
    public void changeItem(final @NotNull ItemStack newItem) {

        // Stash the current items
        stash();

        // Serialize the item value, then change it and save the display. Send a message to the player if the item cannot be serialized
        // final @Nullable String _serializedItem = MinecraftUtils.serializeItem(newItem);
        // if(_serializedItem == null) {
        //     if(user != null) {
        //         FancyPlayerShops.LOGGER.error("New item of product display coudln't be serialized", new RuntimeException());
        //         user.displayClientMessage(new Txt("An error occurred while trying to change the display's item: This item cannot be serialized").get(), false);
        //     }
        // }
        // else {
            item = newItem.copyWithCount(1);
            // serializedItem = _serializedItem;
            ProductDisplayManager.scheduleDisplaySave(this);
        // }
    }




    /**
     * Forcefully changes the stock of this display.
     */
    public void changeStock(final int stock) {
        this.stock = stock;
    }




    /**
     * Sends the items stored in this display to the owner's stash.
     * <p> This method also sets the display's stock to 0.
     */
    public void stash() {
        if(stock == 0) return;
        if(item.is(Items.AIR)) return;


        // Stash items
        StashManager.stashItem(ownerUUID, item, stock);
        StashManager.scheduleStashSave(ownerUUID);


        // Reset stock
        stock = 0;
        ProductDisplayManager.scheduleDisplaySave(this);
    }




    /**
     * Deletes this display without stashing the items or giving the player an unconfigured display item.
     * <p> Any item left in the display is fully deleted and cannot be recovered.
     * <p> The balance is also deleted and cannot be recovered.
     * <p> The save file of this display is deleted as well.
     */
    public void delete() {
        if(!deletionState) {
            deletionState = true;

            // Despawn the ui context and the item display
            if(ui != null) ui.despawn(true);
            getItemDisplay().stopLoopAnimation();
            getItemDisplay().despawn(true);

            // Delete the data associated with this display
            ShopManager.unregisterDisplay(this);
            ProductDisplayManager.deleteDisplay(this);
        }
    }




    /**
     * Converts this display into a snapshot and sends it to the owner's inventory or stash.
     * <p>
     * Notice: This method does NOT delete the display. Call {@link #delete()} to avoid item duplications.
     * @param tryInventory Whether to try placing the item in the owner's inventory.
     *     If the inventory is full or {@code tryInventory == true}, the item is sent to their stash.
     */
    public void pickUp(final boolean tryInventory) {
        final Player owner = FrameworkLib.getServer().getPlayerList().getPlayer(ownerUUID);


        // Create the snapshot and try to send it to the owner's inventory
        final @NotNull ItemStack snapshot = ProductDisplayManager.createShopSnapshot(this);
        boolean giveResult = false;
        if(tryInventory) {
            giveResult = MinecraftUtils.attemptGive(owner, snapshot);
        }


        // If the inventory was full, send it to the stash
        if(!giveResult) {
            StashManager.stashItem(ownerUUID, snapshot, 1);
            StashManager.scheduleStashSave(ownerUUID);
        }
    }




    /**
     * Checks if the provided item stack can be held by this display.
     * This takes into account the current NBT filter setting.
     * @param itemStack The item stack to check.
     * @return True if the item can be held by this display, false otherwise.
     */
    public boolean isItemCompatible(final @NotNull ItemStack itemStack) {
        if(nbtFilter) {
            return ItemStack.isSameItemSameTags(itemStack, item);
        }
        else {
            return MinecraftUtils.getItemKey(itemStack).equals(MinecraftUtils.getItemKey(item));
        }
    }




    /**
     * Tries to retrieve items from nearby inventories.
     * <p> This call has no effect if the display is fully stocked.
     */
    public void pullItems() {
        if(stock >= maxStock || item.is(Items.AIR)) return;
        final int oldStock = stock;

        pullItems(new BlockPos(+0, +0, +0));
        pullItems(new BlockPos(+0, +0, +1));
        pullItems(new BlockPos(+0, +0, -1));
        pullItems(new BlockPos(+0, +1, +0));
        pullItems(new BlockPos(+0, -1, +0));
        pullItems(new BlockPos(+1, +0, +0));
        pullItems(new BlockPos(-1, +0, +0));

        // Update stock and save the display
        if(oldStock == stock) return;
        ProductDisplayManager.scheduleDisplaySave(this);

        // Update active canvas
        final ProductCanvasBase activeCanvas = (ProductCanvasBase)ui.getActiveCanvas();
        if(activeCanvas != null) {
            activeCanvas.onStockChange();
        }
    }




    /**
     * Tries to retrieve items from a specified position relative to the display.
     * <p> This call has no effect if the display is fully stocked.
     * @param rel The position of the inventory, relative to the display.
     */
    public void pullItems(final @NotNull BlockPos rel) {
        if(stock >= maxStock) return;                                           // Skip pull if display is full
        final BlockPos targetPos = pos.offset(rel);                             // Calculate inventory position
        final ChunkPos chunkPos = new ChunkPos(pos);                            // Calculate inventory chunk position
        if(!getLevel().hasChunk(chunkPos.x, chunkPos.z)) return;                // Skip pull if inventory is unloaded
        final BlockEntity be = level.getBlockEntity(targetPos);                 // Get block entity data
        if(be == null) return;                                                  // Skip pull if inventory is not a block entity
        if(be instanceof final BaseContainerBlockEntity cbe && cbe.isEmpty()) return; // Skip pull if inventory is empty


        // Calculate side and find the storage block
        final Direction direction = (rel.getX() + rel.getY() + rel.getZ() == 0) ? null : Direction.getNearest(-rel.getX(), -rel.getY(), rel.getZ());
        final Storage<ItemVariant> storage = ItemStorage.SIDED.find(level, targetPos, direction);


        // If a storage block is found, loop through its slots
        if(storage != null) {
            for(final StorageView<ItemVariant> slot : storage) {
                final ItemVariant variant = slot.getResource();
                final long amount = slot.getAmount();

                // If the slot is not empty
                if(!variant.isBlank() && amount > 0) {

                    // If the item in the slot matches the item sold by the display
                    if(isItemCompatible(variant.toStack((int) amount))) {
                        try(final Transaction tx = Transaction.openOuter()) {
                            final long missing = (long)maxStock - stock;
                            final long extracted = slot.extract(variant, missing, tx);
                            if(extracted > 0) {
                                tx.commit();
                                stock += extracted;
                                //FIXME increase the stock of the item stack that matches the NBTs. use the UUID
                                if(extracted == missing) return;
                            }
                        }
                    }
                }
            }
        }
    }




    /**
     * Changes the owner of this display and sends a feedback message to the old owner.
     * <p> If the new owner and the current one are the same player, this call will have no effect.
     * @param newOwner The new owner.
     */
    public void changeOwner(final @NotNull Player newOwner) {
        if(ownerUUID.equals(newOwner.getUUID())) return;
        final Player oldOwner = FrameworkLib.getServer().getPlayerList().getPlayer(ownerUUID);


        // Send feedback to old owner
        oldOwner.displayClientMessage(new Txt()
            .cat("You successfully transferred the ownership of your " + getDecoratedName() + " to ")
            .cat("" + newOwner.getName().getString() + ".")
        .color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR).get(), false);


        // Send feedback to new owner
        newOwner.displayClientMessage(new Txt()
            .cat("" + oldOwner.getName().getString() + " transferred ownership of their " + getDecoratedName() + " to you.")
            .cat("\nYou can find it at the coords [" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]")
            .cat(" in the dimension " + level.dimension().location().toString())
        .color(ProductDisplayManager.DISPLAY_ITEM_NAME_COLOR).get(), false);


        // Actually change the owner and save the display, then force it to unfocus to prevent the previous owner from accessing the UI
        focusStateNext = false;
        updateFocusState();
        ownerUUID = newOwner.getUUID();
        ProductDisplayManager.scheduleDisplaySave(this);
    }




    /**
     * Changes the hue of the color theme.
     * @param _hue The new hue value.
     */
    public void setColorThemeHue(final float _hue) {
        colorThemeHue = _hue;
        ProductDisplayManager.scheduleDisplaySave(this);
    }

    /**
     * Returns the main color of the color theme.
     * @return The RGB color value.
     */
    public Vector3i getThemeColor1() {
        return Utils.HSVtoRGB(new Vector3f(colorThemeHue, Configs.getDisplay().theme_saturation_main.getValue(), Configs.getDisplay().theme_luminosity_main.getValue()));
    }

    /**
     * Returns the secondary color of the color theme.
     * @return The RGB color value.
     */
    public Vector3i getThemeColor2() {
        return Utils.HSVtoRGB(new Vector3f(colorThemeHue, Configs.getDisplay().theme_saturation_secondary.getValue(), Configs.getDisplay().theme_luminosity_secondary.getValue()));
    }








    /**
     * Computes the name of the display.
     * This is meant for use in sentences.
     * The name is not capitalized and the display name is prefixed with "display " and enclosed in double quotes.
     * @return The name of the display, or "empty product display" if unconfigured.
     */
    public @NotNull String getDecoratedName() {
        return item.is(Items.AIR) ? "empty product display" : "product display \"" + MinecraftUtils.getFancyItemName(item).getString() + "\"";
    }


    /**
     * Computes the name of the display.
     * This is meant for use in stand-alone text elements such as titles.
     * The name is capitalized and doesn't contain any additional text.
     * @return The name of the display, or "Empty product display" if unconfigured.
     */
    public @NotNull String getStandaloneName() {
        return item.is(Items.AIR) ? "Empty product display" : MinecraftUtils.getFancyItemName(item).getString();
    }





    /**
     * Changes the shop of the display to the one whose display name matches the provided string.
     * <p>
     * If the name doesn't match any existing shop, a new one is created.
     * @param name The name of the new shop.
     */
    public void changeShop(final @NotNull String name, final @NotNull ServerPlayer owner) {
        Shop newShop = null;


        // Try to find the shop
        for(final Shop newShopCandidate : ShopManager.getShops(owner)) {
            if(newShopCandidate.getDisplayName().equals(name)) {
                newShop = newShopCandidate;
            }
        }


        // Create a new shop if one with the specified display name doesn't already exist
        if(newShop == null) {
            newShop = new Shop(name, ownerUUID);
            ShopManager.createShop(newShop);
        }


        // Change shop and update shop references
        ShopManager.unregisterDisplay(this);
        shop = ShopManager.registerDisplay(this, newShop.getUuid());
    }




    /**
     * Changes the NBT filter setting of this product display.
     * @param newNbtFilter The new setting. True filters for NBTs, false matches the item ID.
     */
    public void changeNbtFilterSetting(final boolean newNbtFilter) {
        if(nbtFilter != newNbtFilter) {
            nbtFilter = newNbtFilter;

            // If the new setting filters NBTs, send non-compatible items to the stash
            if(nbtFilter) {
                //TODO
            }

            // If the new setting allows any NBT, change nothing
            // This setting already allows any currently stored item
        }
    }
}