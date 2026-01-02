package com.snek.fancyplayershops.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import com.snek.fancyplayershops.data.data_types.PlayerStash;
import com.snek.fancyplayershops.data.data_types.StashEntry;
import com.snek.frameworklib.data_types.containers.Pair;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.graphics.layout.Div;
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
import net.minecraft.world.entity.player.Inventory;
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


    /**
     * Computes the name of the direction of the item from its rotation.
     * @param rotation The rotation of the item, in eighths.
     * @return The direction of the item as a string.
     */
    public static String getOrientationName(final int rotation) {
        return ROTATION_NAMES[rotation];
    }
    private static final String[] ROTATION_NAMES = new String[] {
        //! By default, item elements face north. 0° = North
        "North", "Northwest", "West", "Southwest", "South", "Southeast", "East", "Northeast"
    };


    // Basic data
    private transient @NotNull  ServerLevel           level;                            // The level this display was placed in
    private transient @Nullable ProductItemDisplayElm itemDisplay = null;               // The item display entity //! Searched when needed instead of on data loading because the chunk needs to be loaded in order to find the entity.
    private     final @NotNull  BlockPos              pos;                              // The position of the display
    private transient @NotNull  String                displayIdentifierCache_noLevel;   // The cached display identifier, not including the level
    private transient @NotNull  ProductDisplayKey     displayKeyCache;                  // The cached display key


    // Display data
    private transient @NotNull  ItemStack item = Items.AIR.getDefaultInstance(); // The configured item
    private           @NotNull  UUID      ownerUUID;                             // The UUID of the owner
    private                     long      balance         = 0;                   // The balance collected by the shop since it was last claimed
    private                     long      price           = 0l;                  // The configured price for each item
    private                     long      stock           = 0;                   // The current stock
    private                     long      maxStock        = 0;                   // The configured maximum stock
    private                     int       defaultRotation = 0;                   // The configured item rotation, in eighths
    private                     float     colorThemeHue   = 0f;                  // The configured hue of the color theme
    private transient @NotNull  Shop      shop;                                  // The shop this display has been assigned to
    private                     boolean   nbtFilter       = true;                // The configured nbt filter setting


    // Stored items
    private transient @NotNull UUID itemUUID; // The calculated UUID of the item. Used to compare items to store or remove
    private transient @NotNull Map<@NotNull UUID, @NotNull Pair<@NotNull ItemStack, @NotNull Long>> storedItems = new HashMap<>();
    public @NotNull Map<@NotNull UUID, @NotNull Pair<@NotNull ItemStack, @NotNull Long>> getStoredItems() { return storedItems; }


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
    public @NotNull  BlockPos               getPos              () { return pos;                             }
    public @NotNull  ItemStack              getItem             () { return item;                            }
    public @NotNull  ProductItemDisplayElm  getItemDisplay      () { return findItemDisplayEntityIfNeeded(); }
    public @Nullable ProductDisplay_Context getUi               () { return ui;                              }
    public           long                   getPrice            () { return price;                           }
    public           long                   getBalance          () { return balance;                         }
    public           long                   getStock            () { return stock;                           }
    public           long                   getMaxStock         () { return maxStock;                        }
    public           int                    getDefaultRotation  () { return defaultRotation;                 }
    public           boolean                isFocused           () { return focusState;                      }
    public           boolean                isDeleted           () { return deletionState;                   }
    public @NotNull  UUID                   getOwnerUuid        () { return ownerUUID;                       }
    public @Nullable Player                 getuser             () { return user;                            }
    public @Nullable Player                 getViewer           () { return viewer;                          }
    public           boolean                isScheduledForSave  () { return scheduledForSave;                }
    public @NotNull  ProductDisplayKey      getKey              () { return displayKeyCache;                 }
    public @NotNull  String                 getIdentifierNoLevel() { return displayIdentifierCache_noLevel;  }
    public           float                  getColorThemeHue    () { return colorThemeHue;                   }
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



    /**
     * Creates a new ProductDisplay and saves it in its own file.
     * @param ownerUUID The UUID of the owner of this display.
     * @param shopUUID The UUID of this display's shop.
     * @param price The price of the item.
     * @param stock The current stock.
     * @param maxStock The stock limit.
     * @param rotation The default rotation of the item display, in eighths.
     * @param hue The hue of the color theme.
     * @param balance The balance collected by this display.
     * @param nbtFilter The NBT filter setting.
     * @param position The position of this display.
     * @param level The level this display is placed in.
     * @param item The item filter of this display.
     * @param storedItems The list of item variants stored in this display.
     */
    public ProductDisplay(
        final @NotNull UUID ownerUUID,
        final @NotNull UUID shopUUID,
        final long price,
        final long stock,
        final long maxStock,
        final int rotation,
        final float hue,
        final long balance,
        final boolean nbtFilter,
        final @NotNull BlockPos position,
        final @NotNull ServerLevel level,
        final @NotNull ItemStack item,
        final @NotNull Map<@NotNull UUID, @NotNull Pair<@NotNull ItemStack, @NotNull Long>> storedItems
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

        // Calculate item UUID
        itemUUID = MinecraftUtils.calcItemUUID(item);

        // Register this display in the shop and store the shop instance locally
        this.shop = ShopManager.registerDisplay(this, shopUUID);

        // Recalculate identifier and key
        displayIdentifierCache_noLevel = calcDisplayIdentifier(pos);
        displayKeyCache = calcDisplayKey(pos, level);

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
     * Tries to click the disclaimer element.
     * @param player The player that clicked the display.
     * @param click The click type.
     * @return True if the disclaimer exists and was clicked, false otherwise
     */
    private boolean attemptDisclaimerClick(final @NotNull Player player, final @NotNull ClickAction clickType) {
        final @Nullable ProductCanvasBase canvas = getActiveCanvas();
        if(canvas != null) {
            final @Nullable Div disclaimerElm = canvas.getDisclaimerElm();
            if(disclaimerElm != null) {
                return disclaimerElm.forwardClick(player, clickType);
            }
        }
        return false;
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

            // If the display is not currently being used
            if(user == null) {

                // If the disclaimer was clicked, return false (no permission to click UIs)
                final boolean disclaimerClickResult = attemptDisclaimerClick(player, clickType);
                if(disclaimerClickResult) {
                    return false;
                }

                // If the player left clicked, buy or retrieve an item
                if(clickType == ClickAction.PRIMARY) {
                    if(player.getUUID().equals(ownerUUID)) {
                        retrieveItem(player, 1, true);
                    }
                    else {
                        buyItem(player, 1);
                    }
                }

                // If the player right clicked, open the buy or edit canvas
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
                        buyItem(player, 1);
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
     * @param amount The amount of items to retrieve. Must be > 0.
     * @param stashExcess Whether to stash the items that didn't fit in the inventory or send them back to the display.
     */
    public void retrieveItem(final @NotNull Player owner, final long amount, final boolean stashExcess) {
        assert Require.positive(amount, "retrieve amount");

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

            // Transfer items
            final var transferStats = sendItemsToPlayer(owner, amount);
            final long stashedAmount = transferStats.getSecond();

            // Send feedback messages
            owner.displayClientMessage(new Txt()
                .cat(new Txt("You retrieved ").lightGray())
                .cat(new Txt(Utils.formatAmount(amount, true, true) + " " + MinecraftUtils.getFancyItemName(item).getString()).white())
                .cat(new Txt(" from your product display").lightGray())
            .get(), false);

            // Send feedback message for the stashed items
            if(stashedAmount > 0) {
                owner.displayClientMessage(new Txt()
                    .cat(new Txt(Utils.formatAmount(stashedAmount, true, true) + " ").white())
                    .cat(new Txt(MinecraftUtils.getFancyItemName(item).getString() + " ").white())
                    .cat(new Txt("that didn't fit in your inventory ").lightGray())
                    .cat(new Txt((stashedAmount > 1 ? "have" : "has") + " been sent to your stash").lightGray())
                .get(), false);
            }


            // Update active canvas
            if(getActiveCanvas() != null) {
                getActiveCanvas().onStockChange();
            }
        }
    }



    /**
     * Makes a player buy a specified amount of items from the display.
     * <p> Sends an error message to the player if the display is unconfigured or doesn't contain enough items or the player cannot afford to buy the items.
     * @param buyer The player.
     * @param amount The amount of items to buy. Must be > 0.
     */
    public void buyItem(final @NotNull Player buyer, final long amount) {
        assert Require.positive(amount, "buy amount");

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

            // Handle payment
            final long totPrice = price * amount;
            if(EconomyManager.getCurrency(buyer.getUUID()) >= totPrice) {
                ProductDisplayManager.scheduleDisplaySave(this);
                EconomyManager.subtractCurrency(buyer.getUUID(), totPrice);
                addBalance(totPrice);


                // Transfer items
                final var transferStats = sendItemsToPlayer(buyer, amount);
                final long stashedAmount = transferStats.getSecond();


                // Send feedback messages
                buyer.displayClientMessage(new Txt()
                    .cat(new Txt("Bought ").lightGray())
                    .cat(new Txt(Utils.formatAmount(amount, true, true) + " " + MinecraftUtils.getFancyItemName(item).getString()).white())
                    .cat(new Txt(" for " + Utils.formatPrice(totPrice)).lightGray())
                .get(), false);

                // Send feedback message for the stashed items
                if(stashedAmount > 0) {
                    buyer.displayClientMessage(new Txt()
                        .cat(new Txt(Utils.formatAmount(stashedAmount, true, true) + " ").white())
                        .cat(new Txt(MinecraftUtils.getFancyItemName(item).getString() + " " ).white())
                        .cat(new Txt("that didn't fit in your inventory ").lightGray())
                        .cat(new Txt((stashedAmount > 1 ? "have" : "has") + " been sent to your stash").lightGray())
                    .get(), false);
                }


                // Update active canvas
                if(getActiveCanvas() != null) {
                    getActiveCanvas().onStockChange();
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
    public boolean setStockLimit(final double newStockLimit) {
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
     * @param _rotation The rotation to add, in eighths.
     *     Negative values are allowed and are converted to their positive equivalent.
     */
    public void addDefaultRotation(final int _rotation) {

        // Add value to default rotation and save the display
        //! Reduce range to [-7, 7], then add 8 to shift range to [1, 15], then reduce to [0, 7]
        defaultRotation = ((defaultRotation + _rotation) % 8 + 8) % 8;
        ProductDisplayManager.scheduleDisplaySave(this);
    }




    /**
     * Changes the item sold by this display and saves it to its file.
     * <p>
     * This method also stashes any item that isn't compatible with the new data.
     * @param newItem the new item.
     */
    public void changeItem(final @NotNull ItemStack newItem) {

        // Change the item value and recalculate the UUID
        item = newItem.copyWithCount(1);
        itemUUID = MinecraftUtils.calcItemUUID(item);

        // Stash incompatible stacks
        stashIncompatible();

        // Save the display
        ProductDisplayManager.scheduleDisplaySave(this);
    }




    /**
     * Sends the items stored in this display to the owner's inventory/stash.
     * <p>
     * This method also sets the display's stock to 0 and clears the list of stored items.
     * @param playerFeedback Whether to send the player a feedback message.
     */
    public void stash(final boolean playerFeedback) {
        if(stock == 0) return;
        if(item.is(Items.AIR)) return;

        // For each item
        for(var entry : storedItems.entrySet()) {

            // Stash it
            final ItemStack entryItem  = entry.getValue().getFirst();
            final long      entryCount = entry.getValue().getSecond();
            StashManager.giveItem(ownerUUID, entryItem, entryCount, playerFeedback);
        }

        // Clear stored items and reset stock, then save this display
        storedItems.clear();
        stock = 0;
        ProductDisplayManager.scheduleDisplaySave(this);
    }


    /**
     * Sends any incompatible item stored in this display to the owner's inventory/stash.
     */
    public void stashIncompatible() {
        if(stock == 0) return;
        if(item.is(Items.AIR)) return;

        // Check each item individually
        long givenAmount = 0;
        long stashedAmount = 0;
        final var iterator = storedItems.entrySet().iterator();
        while(iterator.hasNext()) {

            // If it's incompatible
            final var entry = iterator.next();
            final ItemStack entryItem = entry.getValue().getFirst();
            if(isItemCompatible(entryItem, entry.getKey()) == null) {

                // Stash it and remove it from the list of stored items
                final long entryCount = entry.getValue().getSecond();
                final var giveStats = StashManager.giveItem(ownerUUID, entryItem, entryCount, false);
                iterator.remove();
                stock -= entryCount;
                givenAmount += giveStats.getFirst();
                stashedAmount += giveStats.getSecond();
            }
        }

        // Send feedback message to the player
        final @Nullable Player player = MinecraftUtils.getPlayerByUUID(ownerUUID);
        if(player != null) {
            final long removedAmount = givenAmount + stashedAmount;
            if(removedAmount > 0) {
                player.displayClientMessage(new Txt()
                    .cat(new Txt("You picked up ").lightGray())
                    .cat(new Txt(Utils.formatAmount(removedAmount, true, true) + " ").white())
                    .cat(new Txt("incompatible ").lightGray())
                    .cat(new Txt(MinecraftUtils.getFancyItemName(item).getString() + " " ).white())
                    .cat(new Txt("from the product display").lightGray())
                .get(), false);
            }
            if(stashedAmount > 0) {
                player.displayClientMessage(new Txt()
                    .cat(new Txt(Utils.formatAmount(stashedAmount, true, true) + " ").white())
                    .cat(new Txt(MinecraftUtils.getFancyItemName(item).getString() + " " ).white())
                    .cat(new Txt("that didn't fit in your inventory ").lightGray())
                    .cat(new Txt((stashedAmount > 1 ? "have" : "has") + " been sent to your stash").lightGray())
                .get(), false);
            }
        }

        // Save this display
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
     * @param playerFeedback Whether to send the player a feedback message.
     */
    public void pickUp(final boolean playerFeedback) {

        // Create the snapshot and give it to the player
        final @NotNull ItemStack snapshot = ProductDisplayManager.createShopSnapshot(this);
        StashManager.giveItem(ownerUUID, snapshot, 1, playerFeedback);
    }




    /**
     * Checks if the provided item stack can be held by this display.
     * <p>
     * This takes into account the current NBT filter setting.
     * @param itemStack The item stack to check.
     * @return The item's UUID if it can be held by this display, null otherwise.
     */
    public @Nullable UUID isItemCompatible(final @NotNull ItemStack itemStack) {
        return isItemCompatible(itemStack, MinecraftUtils.calcItemUUID(itemStack));
    }

    /**
     * Checks if the provided item stack can be held by this display.
     * <p>
     * This takes into account the current NBT filter setting.
     * @param itemStack The item stack to check.
     * @param itemStackUUID The UUID of the item stack to check.
     * @return The item's UUID if it can be held by this display, null otherwise.
     */
    public @Nullable UUID isItemCompatible(final @NotNull ItemStack itemStack, final @NotNull UUID itemStackUUID) {
        if(!MinecraftUtils.getItemKey(itemStack).equals(MinecraftUtils.getItemKey(item))) {
            return null;
        }
        if(nbtFilter) {
            return itemStackUUID.equals(itemUUID) ? itemStackUUID : null;
        }
        else {
            return itemStackUUID;
        }
    }




    /**
     * Tries to retrieve items from nearby inventories.
     * <p> This call has no effect if the display is fully stocked.
     */
    public void pullItems() {
        if(stock >= maxStock || item.is(Items.AIR)) return;
        final long oldStock = stock;

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
        if(getActiveCanvas() != null) {
            getActiveCanvas().onStockChange();
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

                    // If the items in the slot match the item sold by the display
                    final ItemStack itemStack = variant.toStack((int) amount);
                    final @Nullable UUID variantUUID = isItemCompatible(itemStack);
                    if(variantUUID != null) {

                        // If the items can be extracted
                        try(final Transaction tx = Transaction.openOuter()) {
                            final long missing = maxStock - stock;
                            final long extracted = slot.extract(variant, missing, tx);
                            if(extracted > 0) {
                                tx.commit();

                                // Add them to this display's storage
                                storeItems(variantUUID, itemStack, (int)extracted);
                                //! Total stock is updated by storeItems
                                //! Display is saved by storeItems

                                // Return if the display is full
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
        final Player oldOwner = MinecraftUtils.getPlayerByUUID(ownerUUID);


        // Send feedback to old owner
        oldOwner.displayClientMessage(new Txt()
            .cat(new Txt("You successfully transferred the ownership of your ").lightGray())
            .cat(new Txt(getDecoratedName()).white())
            .cat(new Txt(" to ").lightGray())
            .cat(new Txt(newOwner.getName().getString()).white())
        .get(), false);


        // Send feedback to new owner
        newOwner.displayClientMessage(new Txt()
            .cat(new Txt(oldOwner.getName().getString()).white())
            .cat(new Txt(" transferred ownership of their ").lightGray())
            .cat(new Txt(getDecoratedName()).white())
            .cat(new Txt(" to you.\nYou can find it at the coords [" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]").lightGray())
            .cat(new Txt(" in the dimension " + level.dimension().location().toString()).lightGray())
        .get(), false);


        // Force the display to unfocus in order to close any UI
        focusStateNext = false;
        updateFocusState();


        // Unregister the display from the runtime maps to update any linked data,
        // then change the owner, save the shop and register the display again
        ShopManager.unregisterDisplay(this);
        ProductDisplayManager.unregisterDisplay(this);
        ownerUUID = newOwner.getUUID();
        ProductDisplayManager.registerDisplay(this);
        ShopManager.registerDisplay(this, getShop().getUuid());
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
        return Utils.HSVtoRGB(new Vector3f(
            colorThemeHue,
            Configs.getDisplay().theme_saturation_main.getValue(),
            Configs.getDisplay().theme_luminosity_main.getValue())
        );
    }

    /**
     * Returns the secondary color of the color theme.
     * @return The RGB color value.
     */
    public Vector3i getThemeColor2() {
        return Utils.HSVtoRGB(new Vector3f(
            colorThemeHue,
            Configs.getDisplay().theme_saturation_secondary.getValue(),
            Configs.getDisplay().theme_luminosity_secondary.getValue())
        );
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
                stashIncompatible();
            }

            // If the new setting allows any NBT, change nothing
            // This setting already allows any currently stored item

            // Save the display
            ProductDisplayManager.scheduleDisplaySave(this);
        }
    }





    /**
     * Sends the specified amount of items to the player.
     * <p>
     * This updates the list of stored items, as well as the total stock of the product display.
     * <p>
     * This method also saves the display and accounts for the NBT filtering setting.
     * <p>
     * Items that don't fit in the player's inventory are sent to their stash.
     * <p>
     * @param player The player.
     * @param amount The amount of items to send. Must be {@code < stock} and {@code > 0}.
     * @return A pair of Longs representing the amount of items sent to the player's inventory and the amount of items sent to their stash.
     */
    public @NotNull Pair<Long, Long> sendItemsToPlayer(final @NotNull Player player, final long amount) {
        assert Require.condition(amount <= stock, "Amount of items to transfer cannot be higher than the current stock");
        assert Require.positive(amount, "item amount");


        // Create a shuffled list of entries
        //! Polling variants randomly makes it impossible to predict the next items by checking previous purchases or reading the NBTs
        final var entries = new ArrayList<>(storedItems.entrySet());
        Collections.shuffle(entries);


        // Give item variants one by one and keep count of the stats
        //! Not distributing avoids filling the player's inventory/stash with absurd amounts of different variants of the same item
        long left = amount;
        long givenAmount = 0;
        long stashedAmount = 0;
        for(final var entry : entries) {
            if(left <= 0) break;

            // Cache item and count, then calculate the amount of items to take and give them to the player
            final ItemStack entryItem  = entry.getValue().getFirst();
            final long      entryCount = entry.getValue().getSecond();
            final long amountTaken = Math.min(entryCount, left);
            final var stashStats = StashManager.giveItem(player.getUUID(), entryItem, amountTaken, false);

            // Update counters
            left -= amountTaken;
            givenAmount   += stashStats.getFirst();
            stashedAmount += stashStats.getSecond();

            // Delete entry if its stock is 0. Update its stock otherwise
            if(amountTaken == entryCount) storedItems.remove(entry.getKey());
            else storedItems.put(entry.getKey(), Pair.from(entryItem, entryCount - amountTaken));
        }


        // Update total stock, then save the display and return the stats
        assert Require.condition(left == 0, "The amount of items to send left is not 0. This should never happen");
        stock -= amount;
        ProductDisplayManager.scheduleDisplaySave(this);
        return Pair.from(givenAmount, stashedAmount);
    }




    /**
     * Stores the specified item stack into this display's storage.
     * <p>
     * This updates the item instance's stock, as well as the total stock of the product display.
     * @param itemStackUUID The UUID of the item stack.
     * @param itemStack The item stack to store.
     * @param amount The amount of items to store. Must be {@code > 0}.
     */
    public void storeItems(final @NotNull UUID itemStackUUID, final @NotNull ItemStack itemStack, final long amount) {
        storedItems.compute(itemStackUUID, (key, existing) -> {

            // If the item doesn't exist yet, create a new entry
            if(existing == null) return Pair.from(itemStack.copyWithCount(1), amount);

            // If the item exists, add amount to the existing stock
            else return Pair.from(existing.getFirst(), existing.getSecond() + amount);
        });

        // Update total stock and save the display
        stock += amount;
        ProductDisplayManager.scheduleDisplaySave(this);
    }








    /**
     * Tries to restock the display using the items in the owner's inventory and stash.
     */
    public void attemptRestock() {
        final @Nullable Player owner = MinecraftUtils.getPlayerByUUID(ownerUUID);
        if(owner == null) return;
        int takenFromInventory = 0;
        int takenFromStash = 0;


        // Check each slot in the player's inventory (hotbar + 27 inventory slots)
        final Inventory inventory = owner.getInventory();
        for(final ItemStack inventoryItem : inventory.items) {
            final long taken = attemptRestock(inventoryItem, inventoryItem.getCount());
            takenFromInventory += taken;
            inventoryItem.setCount(inventoryItem.getCount() - (int)taken);
            if(isFull()) {
                finalizeRestock(owner, takenFromInventory, takenFromStash);
                return;
            }
        }


        // Check the player's offhand slot
        /* Not a loop :3 */ {
            final ItemStack offhandItem = owner.getOffhandItem();
            final long taken = attemptRestock(offhandItem, offhandItem.getCount());
            takenFromInventory += taken;
            offhandItem.setCount(offhandItem.getCount() - (int)taken);
            if(isFull()) {
                finalizeRestock(owner, takenFromInventory, takenFromStash);
                return;
            }
        }


        // Check each slot in the player's stash
        final @Nullable PlayerStash stash = StashManager.getStash((ServerPlayer)owner);
        final var iterator = stash.entrySet().iterator();
        while(iterator.hasNext()) {
            final var entry = iterator.next();
            final StashEntry stashEntry = entry.getValue();
            final long taken = attemptRestock(stashEntry.getItem(), stashEntry.getCount());
            takenFromStash += taken;
            if(taken == stashEntry.getCount()) {
                iterator.remove();
            }
            else if(taken > 0) {
                stashEntry.remove(taken);
            }
            if(isFull()) {
                finalizeRestock(owner, takenFromInventory, takenFromStash);
                return;
            }
        }


        // Finalize restock (in case the display wasn't filled)
        finalizeRestock(owner, takenFromInventory, takenFromStash);
    }


    /**
     * Tries to restock the display using the provided item stack.
     * <p>
     * This method modified the display's stock and stores a copy of the items.
     * <p>
     * The provided item stack is not modified.
     * @param stack The item stack. Can be empty.
     * @param count The number of available items.
     * @return The amount of transferred items.
     */
    private long attemptRestock(final @NotNull ItemStack stack, final long count) {

        // Return 0 if the item is empty or not compatible
        if(stack.isEmpty()) return 0;
        final @Nullable UUID comparisonResult = isItemCompatible(stack);
        if(comparisonResult == null) return 0;

        // If the item is compatible, store the new items and return their count
        final long left = maxStock - stock;
        final long transferred = Math.min(left, count);
        storeItems(comparisonResult, stack, transferred);
        return transferred;
    }


    /**
     * Finalizes a restock operation, calling stock change callbacks and sending feedback messages to the owner.
     * @param owner The owner of the display. Only used for optimization. Their UUID must match this.ownerUUID.
     * @param takenFromInventory The number of items taken from the owner's inventory.
     * @param takenFromStash The number of items taken from the owner's stash.
     */
    private void finalizeRestock(final @NotNull Player owner, final int takenFromInventory, final int takenFromStash) {
        if(getActiveCanvas() != null) {
            getActiveCanvas().onStockChange();
        }

        if(takenFromInventory > 0 && takenFromStash > 0) {
            owner.displayClientMessage(new Txt()
                .cat(new Txt("You restocked your ").lightGray())
                .cat(new Txt(getDecoratedName()).white())
                .cat(new Txt(" with ").lightGray())
                .cat(new Txt(Utils.formatAmount(takenFromInventory, true, true)).white())
                .cat(new Txt(" items from your inventory and ").lightGray())
                .cat(new Txt(Utils.formatAmount(takenFromStash, true, true)).white())
                .cat(new Txt(" items from your stash").lightGray())
            .get(), false);
        }
        else if(takenFromInventory > 0) {
            owner.displayClientMessage(new Txt()
                .cat(new Txt("You restocked your ").lightGray())
                .cat(new Txt(getDecoratedName()).white())
                .cat(new Txt(" with ").lightGray())
                .cat(new Txt(Utils.formatAmount(takenFromInventory, true, true)).white())
                .cat(new Txt(" items from your inventory").lightGray())
            .get(), false);
        }
        else if(takenFromStash > 0) {
            owner.displayClientMessage(new Txt()
                .cat(new Txt("You restocked your ").lightGray())
                .cat(new Txt(getDecoratedName()).white())
                .cat(new Txt(" with ").lightGray())
                .cat(new Txt(Utils.formatAmount(takenFromStash, true, true)).white())
                .cat(new Txt(" items from your stash").lightGray())
            .get(), false);
        }
        else {
            owner.displayClientMessage(
                new Txt("None of the items in your inventory and stash are compatible!").red().bold()
            .get(), true);
        }
    }




    /**
     * Checks if the product display is full.
     * @return Whether the product display is full.
     */
    public boolean isFull() {
        return stock >= maxStock;
    }
}