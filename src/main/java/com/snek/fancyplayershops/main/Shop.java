package com.snek.fancyplayershops.main;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

import com.herrkatze.solsticeEconomy.modules.economy.EconomyManager;
import com.snek.fancyplayershops.data.BalanceManager;
import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.data.StashManager;
import com.snek.fancyplayershops.input.MessageReceiver;
import com.snek.fancyplayershops.shop_ui._elements.ShopCanvas;
import com.snek.fancyplayershops.shop_ui._elements.ShopItemDisplay;
import com.snek.fancyplayershops.shop_ui.buy.BuyUi;
import com.snek.fancyplayershops.shop_ui.details.DetailsUi;
import com.snek.fancyplayershops.shop_ui.edit.EditUi;
import com.snek.fancyplayershops.ui._elements.InteractionBlocker;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.functional.ButtonElm;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;
import com.snek.framework.utils.scheduler.RateLimiter;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
















/**
 * This class manages a player shop that is placed somewhere in a world.
 */
public class Shop {

    // Animation data
    public static final int CANVAS_ANIMATION_DELAY = 5;
    public static final int CANVAS_ROTATION_TIME = 8;


    // Strings
    public static final Component EMPTY_SHOP_NAME  = new Txt("[Empty]").italic().lightGray().get();
    public static final Component SHOP_EMPTY_TEXT  = new Txt("This shop is empty!").lightGray().get();
    public static final Component SHOP_STOCK_TEXT  = new Txt("This shop has no items in stock!").lightGray().get();
    public static final Component SHOP_AMOUNT_TEXT = new Txt("This shop doesn't have enough items in stock!").lightGray().get();



    // Basic data
    private transient @NotNull  ServerLevel     world;                          // The world this shop was placed in
    private           @NotNull  String          worldId;                        // The Identifier of the world
    private transient @Nullable ShopItemDisplay itemDisplay = null;             // The item display entity //! Searched when needed instead of on data loading because the chunk needs to be loaded in order to find the entity.
    private           @NotNull  BlockPos        pos;                            // The position of the shop
    private transient @NotNull  String          shopIdentifierCache_noWorld;    // The cached shop identifier, not including the world
    private transient @NotNull  ShopKey         shopKeyCache;                   // The cached shop key


    // Shop data
    private transient @NotNull ItemStack item = Items.AIR.getDefaultInstance(); // The configured item
    private           @NotNull UUID      ownerUUID;                             // The UUID of the owner
    private           @NotNull String    serializedItem;                        // The item in serialized form
    private                    int       stock           = 0;                   // The current stock
    private                    long      price           = 0l;                  // The configured price for each item
    private                    int       maxStock        = 0;                   // The configured maximum stock
    private                    float     defaultRotation = 0f;                  // The configured item rotation
    private                    float     colorThemeHue   = 0f;                  // The configured hue of the color theme


    // Shop state
    private transient @Nullable InteractionBlocker interactionBlocker = null;   // The interaction entity used to block client-side clicks
    private transient @Nullable ShopCanvas               activeCanvas = null;   // The menu that is currently being displayed to the viewer
    private transient @Nullable Player                           user = null;   // The current user of the shop (the player that first opened a menu)
    private transient @Nullable Player                         viewer = null;   // The prioritized viewer
    private transient           boolean                 deletionState = false;  // True if the shop has been deleted, false otherwise
    private transient           boolean                    focusState = false;  // True if the shop is currently being looked at by at least one player, false otherwise
    private transient           boolean                focusStateNext = false;  // The next focus state
    private transient           int                     lastDirection = 0;      // The current cartinal or intercardinal direction of the canvas, 0 to 7
    private transient @NotNull  RateLimiter     canvasRotationLimiter = new RateLimiter();
    private transient @NotNull  RateLimiter           menuOpenLimiter = new RateLimiter();
    private transient boolean scheduledForSave = false;


    // Accessors
    public @NotNull  ServerLevel     getWorld          () { return world;           }
    public @NotNull  String          getWorldId        () { return worldId;         }
    public @NotNull  BlockPos        getPos            () { return pos;             }
    public @NotNull  ItemStack       getItem           () { return item;            }
    public @NotNull  String          getSerializedItem () { return serializedItem;  }
    public @NotNull  ShopItemDisplay getItemDisplay    () { return findItemDisplayEntityIfNeeded(); }
    public @Nullable ShopCanvas      getActiveCanvas   () { return activeCanvas;    }
    public           long            getPrice          () { return price;           }
    public           int             getStock          () { return stock;           }
    public           int             getMaxStock       () { return maxStock;        }
    public           float           getDefaultRotation() { return defaultRotation; }
    public           int             getCanvasDirection() { return lastDirection;   }
    public           boolean         isFocused         () { return focusState;      }
    public           boolean         isDeleted         () { return deletionState;   }
    public @NotNull  UUID            getOwnerUuid      () { return ownerUUID;       }
    public @Nullable Player          getuser           () { return user;            }
    public @Nullable Player          getViewer         () { return viewer;          }
    public           boolean         isScheduledForSave() { return scheduledForSave; }
    public           void            setViewer         (final @Nullable Player _viewer) { viewer = _viewer; }
    public           void            setScheduledForSave(final boolean scheduled) { scheduledForSave = scheduled; }
    public           void            setFocusStateNext (final boolean _nextFocusState) { focusStateNext = _nextFocusState; }
    public @NotNull  ShopKey         getKey            () { return shopKeyCache; }
    public @NotNull  String          getIdentifierNoWorld() { return shopIdentifierCache_noWorld; }
    public           float           getColorThemeHue  () { return colorThemeHue; }








    /**
     * Calculates the position display entities should be spawned at.
     * @return The absolute position of display entities.
     */
    public @NotNull Vector3d calcDisplayPos() {
        return new Vector3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
    }


    /**
     * Computes the Identifier of the world.
     */
    private void calcSerializedWorldId() {
        worldId = world.dimension().location().toString();
    }


    /**
     * Tries find the ServerLevel the world identifier belongs to.
     * @throws RuntimeException if the world Identifier is invalid or the ServerLevel cannot be found.
     */
    private void calcDeserializedWorldId() throws RuntimeException {
        for(final ServerLevel w : FancyPlayerShops.getServer().getAllLevels()) {
            if(w.dimension().location().toString().equals(worldId)) {
                world = w;
                return;
            }
        }
        throw new RuntimeException("Invalid shop data: Specified world \"" + worldId + "\" was not found");
    }


    /**
     * Computes and caches the shop identifiers.
     */
    private void cacheShopIdentifier() {
        shopIdentifierCache_noWorld = calcShopIdentifier(pos);
    }
    /**
     * Computes and caches the shop key.
     */
    private void cacheShopKey() {
        shopKeyCache = calcShopKey(pos, world);
    }


    /**
     * Calculates a shop identifier from the position. This identifier doesn't include the world ID.
     * @param _pos The position.
     * @return The generated identifier.
     */
    public static String calcShopIdentifier(final @NotNull BlockPos _pos) {
        return String.format("%d,%d,%d", _pos.getX(), _pos.getY(), _pos.getZ());
    }
    /**
     * Calculates a shop identifier from the position. This identifier doesn't include the world ID.
     * @param _pos The position.
     * @return The generated identifier.
     */
    public static ShopKey calcShopKey(final @NotNull BlockPos _pos, final @NotNull Level _world) {
        return new ShopKey(_pos, _world);
    }


    /**
     * Reinitializes the transient members.
     * @return Whether the item and world id have been deserialized successfully.
     * <p> Shops whose data cannot be deserialized shouldn't be loaded as their save file is likely corrupted.
    */
    public boolean reinitTransient() {
        focusState            = false;
        focusStateNext        = false;
        lastDirection         = 0;
        canvasRotationLimiter = new RateLimiter();
        menuOpenLimiter = new RateLimiter();
        cacheShopIdentifier();
        try {
            item = MinecraftUtils.deserializeItem(serializedItem);
            calcDeserializedWorldId();
            cacheShopKey();
            return true;
        } catch(final RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }
















    /**
     * Creates a new Shop and saves it in its own file.
     * @param world The world the shop has to be created in.
     * @param _pos The position of the new shop.
     * @param owner The player that places the shop.
     */
    public Shop(final @NotNull ServerLevel _world, final @NotNull BlockPos _pos, final @NotNull Player owner) {

        // Set shop data
        world = _world;
        ownerUUID = owner.getUUID();
        pos = _pos;
        price         = Configs.shop.price.getDefault();
        maxStock      = Configs.shop.stock_limit.getDefault();
        colorThemeHue = Configs.shop.theme_hues.getValue()[Configs.shop.theme.getDefault()];

        // Calculate serialized data and shop identifier
        serializedItem = MinecraftUtils.serializeItem(item);
        calcSerializedWorldId();
        cacheShopIdentifier();
        cacheShopKey();

        // Create and spawn the Item Display entity
        itemDisplay = new ShopItemDisplay(this);
        itemDisplay.spawn(calcDisplayPos());

        // Save the shop
        ShopManager.scheduleShopSave(this);
        ShopManager.registerShop(this);
    }




    /**
     * Creates a new Shop and saves it in its own file.
     * @param _world The world the shop has to be created in.
     * @param _pos The position of the new shop.
     * @param _ownerUUID The UUID of the player that places the shop.
     * @param _price The price of the item.
     * @param _stock The current stock.
     * @param _maxStock The stock limit.
     * @param _rotation The default rotation of the item display.
     * @param _hue The hue of the color theme.
     * @param _serializedIitem The item in serialized form.
     */
    public Shop(
        final @NotNull ServerLevel _world, final @NotNull BlockPos _pos, final @NotNull UUID _ownerUUID,
        final long _price, final int _stock, final int _maxStock, final float _rotation, final float _hue, final @NotNull String _serializedIitem
    ) {

        // Set shop data
        world = _world;
        ownerUUID = _ownerUUID;
        pos = _pos;
        price = _price;
        stock = _stock;
        maxStock = _maxStock;
        defaultRotation = _rotation;
        colorThemeHue = _hue;

        // Calculate serialized data
        calcSerializedWorldId();
        serializedItem = _serializedIitem;

        // Get members from serialized data and calculate shop identifier
        item = MinecraftUtils.deserializeItem(serializedItem);
        cacheShopIdentifier();
        cacheShopKey();

        // Create and spawn the Item Display entity
        itemDisplay = new ShopItemDisplay(this);
        itemDisplay.spawn(calcDisplayPos());

        // Save the shop
        ShopManager.scheduleShopSave(this);
        ShopManager.registerShop(this);
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
                if(activeCanvas != null) activeCanvas.despawnNow();
                activeCanvas = new DetailsUi(this);
                if(lastDirection != 0) {
                    activeCanvas.applyAnimationNowRecursive(calcCanvasRotationAnimation(0, lastDirection));
                    itemDisplay.applyAnimationNowRecursive(calcItemDisplayRotationAnimation(0, lastDirection));
                }
                activeCanvas.spawn(calcDisplayPos());

                // Create or recreate interaction blocker
                if(interactionBlocker != null) interactionBlocker.despawn();
                interactionBlocker = new InteractionBlocker(getWorld(), 1.01f, 1.01f);
                interactionBlocker.spawn(new Vector3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));

                // Start item animation and turn off the CustomName
                getItemDisplay().enterFocusState();
            }
            else {

                // Despawn active canvas
                activeCanvas.despawn();
                activeCanvas = null;

                // Despawn interaction blocker
                interactionBlocker.despawn();
                interactionBlocker = null;

                // Cancel chat input callbacks, then reset the user and renew the focus cooldown
                menuOpenLimiter.renewCooldown(ShopItemDisplay.D_TIME);
                if(user != null) MessageReceiver.removeCallback(user);
                user = null;

                // Turn the item display's custom name back on
                getItemDisplay().leaveFocusState();
            }
        }
    }




    public void invalidateItemDisplay() {
        if(itemDisplay != null) {
            itemDisplay.despawnNow();
            itemDisplay = null;
        }
        getItemDisplay();
    }




    /**
     * Finds the display entity connected to this shop and saves it to this.itemDisplay.
     * <p> If no connected entity is found, a new ShopItemDisplay is created.
     * @reutrn the item display.
     */
    private @NotNull ShopItemDisplay findItemDisplayEntityIfNeeded() {

        if(itemDisplay == null || itemDisplay.getEntity().isRemoved()) {
            itemDisplay = new ShopItemDisplay(this);
            itemDisplay.spawn(calcDisplayPos());
        }
        return itemDisplay;
    }




    /**
     * Handles a single click event on this shop block.
     * @param player The player that clicked the shop.
     * @param click The click type.
     */
    public void onClick(final @NotNull Player player, final @NotNull ClickAction clickType) {


        // If the shop is currently focused
        //! checking that the shop is focused prevents erroneous "someone else is using the shop" errors
        //! in case of clicks during the focus cooldown time or before the focus is actually registered
        if(isFocused()) {

            // If the shop is not currently being used, flag the player as its user
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
            }


            // If the player that clicked has already opened a menu, forward the click event to it
            else if(user == player) {
                activeCanvas.forwardClick(player, clickType);
            }


            // Send an error message to the player if someone else has already opened a menu in the same shop
            else {
                if(clickType == ClickAction.SECONDARY) {
                    player.displayClientMessage(new Txt(
                        "Someone else is already using this shop! Left click to " +
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
            }
        }
    }




    /**
     * Retrieves the specified amount of items from the shop at no cost and gives them to the owner.
     * <p> Sends an error message to the player if the shop is unconfigured or doesn't contain any item.
     * @param owner The owner of the shop.
     * @param amount The amount of items to retrieve.
     * @param stashExcess Whether to stash the items that didn't fit in the inventory or send them back to the shop.
     */
    public void retrieveItem(final @NotNull Player owner, final int amount, final boolean stashExcess) {
        if(item.getItem() == Items.AIR) {
            owner.displayClientMessage(SHOP_EMPTY_TEXT, true);
        }
        else if(stock < 1) {
            owner.displayClientMessage(SHOP_STOCK_TEXT, true);
        }
        else if(stock < amount) {
            owner.displayClientMessage(SHOP_AMOUNT_TEXT.copy().append(new Txt(" Items left: " + stock).lightGray().get()), true);
        }
        else {
            ShopManager.scheduleShopSave(this);


            // Send feedback to the player
            final ItemStack _item = item.copyWithCount(amount);
            MinecraftUtils.attemptGive(owner, _item);
            final int stashedAmount = _item.getCount();
            final int retrievedAmount = amount - stashedAmount;

            if(retrievedAmount > 0) {
                owner.displayClientMessage(new Txt()
                    .cat(new Txt("You retrieved ").lightGray())
                    .cat(new Txt(Utils.formatAmount(retrievedAmount, true, true) + " " + MinecraftUtils.getFancyItemName(item).getString()).white())
                    .cat(new Txt(" from your shop.").lightGray())
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
                    .cat(new Txt(" retrieved from your shop " + (stashedAmount > 1 ? "have" : "has") + " been sent to your stash.").lightGray())
                .get(), false);
                stock -= stashedAmount;
            }


            // Update active canvas
            if(activeCanvas != null) {
                activeCanvas.onStockChange();
            }
        }
    }




    /**
     * Makes a player buy a specified amount of items from the shop.
     * <p> Sends an error message to the player if the shop is unconfigured or doesn't contain enough items or the player cannot afford to buy the items.
     * @param buyer The player.
     * @param amount The amount of items to buy.
     * @param stashExcess Whether to stash the items that didn't fit in the inventory or send them back to the shop.
     */
    public void buyItem(final @NotNull Player buyer, final int amount, final boolean stashExcess) {
        if(item.getItem() == Items.AIR) {
            buyer.displayClientMessage(SHOP_EMPTY_TEXT, true);
        }
        else if(stock < 1) {
            buyer.displayClientMessage(SHOP_STOCK_TEXT, true);
        }
        else if(stock < amount) {
            buyer.displayClientMessage(SHOP_AMOUNT_TEXT.copy().append(new Txt(" Items left: " + stock).lightGray().get()), true);
        }
        else {
            final long totPrice = price * amount;

            if(EconomyManager.getCurrency(buyer.getUUID()) >= totPrice) {
                ShopManager.scheduleShopSave(this);
                EconomyManager.subtractCurrency(buyer.getUUID(), totPrice);
                BalanceManager.addBalance(ownerUUID, totPrice);
                BalanceManager.saveBalance(ownerUUID);


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
     * Switches the active canvas with a new one (after a delay to avoid overlapping them).
     * @param canvas The new canvas.
     */
    public void changeCanvas(final @NotNull ShopCanvas canvas) {
        activeCanvas = canvas;

        // Adjust rotation if needed
        if(lastDirection != 0) {
            final Animation animation = calcCanvasRotationAnimation(0, lastDirection);
            for(final Div c : canvas.getBg().getChildren()) {
                c.applyAnimationNowRecursive(animation);
            }
        }

        // Spawn canvas into the world and play a sound to the user
        canvas.spawn(calcDisplayPos());
        if(user != null) ButtonElm.playButtonSound(user);
    }




    /**
     * Opens the edit shop UI.
     * @param player The player.
     */
    public void openEditUi(final @NotNull Player player) {
        changeCanvas(new EditUi(this));
        getItemDisplay().enterEditState();
    }




    public boolean canBuyUiBeOpened() {
        return item.getItem() != Items.AIR;
    }
    /**
     * Attempts to opens the buy item UI.
     * @param player The player.
     * @param adjustItemDisplay Wether to change the size and position of the item display from the standard focused transform to the "edit" transform.
     */
    public boolean openBuyUi(final @NotNull Player player, final boolean adjustItemDisplay) {
        if(!canBuyUiBeOpened()) {
            player.displayClientMessage(SHOP_EMPTY_TEXT, true);
            return false;
        }
        else {
            changeCanvas(new BuyUi(this));
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
     *     <p> Values above MAX_PRICE are also considered invalid. //TODO add to config file
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
        if(newPrice > Configs.shop.price.getMax()) {
            if(user != null) user.displayClientMessage(new Txt("The price cannot be greater than " + Utils.formatPrice(Configs.shop.price.getMax())).red().bold().get(), true);
            return false;
        }
        else if(newPrice < 0.00001) price = 0;
        else if(newPrice < 0.01000) price = 1;
        else price = newPrice;
        ShopManager.scheduleShopSave(this);
        return true;
    }




    /**
     * Tries to set a new stock limit for the item and sends an error message to the user if it's invalid.
     *     <p> Amounts are rounded to the nearest integer.
     *     <p> Negative values and 0 are considered invalid and return false without changing the stock limit.
     *     <p> Values above MAX_STOCK or that are higher than the shop's storage capacity are also considered invalid. //TODO implement shop tiers
     * @param newStockLimit The new stock limit.
     * @return Whether the new value could be set.
     */
    public boolean setStockLimit(final float newStockLimit) {
        if(newStockLimit < 0.9999) {
            if(user != null) user.displayClientMessage(new Txt("The stock limit must be at least 1").red().bold().get(), true);
            return false;
        }
        if(newStockLimit > Configs.shop.stock_limit.getMax()) {
            if(user != null) user.displayClientMessage(new Txt("The stock limit cannot be greater than " + Utils.formatAmount(Configs.shop.stock_limit.getMax(), false, true)).red().bold().get(), true);
            return false;
        }
        else maxStock = Math.round(newStockLimit);
        ShopManager.scheduleShopSave(this);
        return true;
    }




    /**
     * Adds a specified rotation to the default rotation of the item display and saves the shop to its file.
     * @param _rotation The rotation to add.
     */
    public void addDefaultRotation(final float _rotation) {

        // Add value to default rotation and save the shop
        defaultRotation = (float)((defaultRotation + _rotation) % (Math.PI * 2));
        ShopManager.scheduleShopSave(this);
    }




    /**
     * Changes the item sold by this shop and saves it to its file.
     * @param _item the new item.
     */
    public void changeItem(final @NotNull ItemStack _item) {

        // Stash the current items
        stash();

        // Change item value, then serialize it and save the shop
        item = _item.copyWithCount(1);
        serializedItem = MinecraftUtils.serializeItem(item);
        ShopManager.scheduleShopSave(this);
    }




    /**
     * Updates the rotation of the active canvas to face the current viewer.
     */
    public void updateCanvasRotation() {
        if(!canvasRotationLimiter.attempt() || activeCanvas == null) return;

        // Calculate target direction
        final Vec3 playerPos = viewer.getPosition(1f);                      // Get player position
        final double dx = pos.getX() + 0.5d - playerPos.x;                  // Calculate X difference
        final double dz = pos.getZ() + 0.5d - playerPos.z;                  // Calculate Z difference
        final double angle = Math.toDegrees(Math.atan2(-dx, dz));           // Calculate angle from position difference
        final int targetDir = (int)Math.round((angle + 180d) / 45d) % 8;    // Convert from degrees to direction

        // Apply animations and update the current direction if needed
        if(targetDir != lastDirection) {
            activeCanvas.applyAnimationRecursive(calcCanvasRotationAnimation(lastDirection, targetDir));
            getItemDisplay().applyAnimationRecursive(calcItemDisplayRotationAnimation(lastDirection, targetDir));
            lastDirection = targetDir;
            canvasRotationLimiter.renewCooldown(CANVAS_ROTATION_TIME);
        }
    }




    /**
     * Calculates the animations required to face from a specified direction to another one.
     * @param from The starting direction. 0 to 7.
     * @param to The new direction to face. 0 to 7.
     * @return The canvas animation.
     */
    public static @NotNull Animation calcCanvasRotationAnimation(final int from, final int to) {
        final float rotation = -Math.toRadians(to * 45f - from * 45f);
        return new Animation(
            new Transition(CANVAS_ROTATION_TIME, Easings.cubicOut)
            .additiveTransform(new Transform().rotGlobalY(rotation))
        );
    }
    /**
     * Calculates the animations required to face from a specified direction to another one.
     * @param from The starting direction. 0 to 7.
     * @param to The new direction to face. 0 to 7.
     * @return The item display animation.
     */
    public static @NotNull Animation calcItemDisplayRotationAnimation(final int from, final int to) {
        final float rotation = -Math.toRadians(to * 45f - from * 45f);
        return new Animation(
            new Transition(CANVAS_ROTATION_TIME, Easings.cubicOut)
            .additiveTransform(new Transform().rotGlobalY(rotation).rotY(- rotation))
        );
    }




    /**
     * Sends the items stored in this shop to the owner's stash.
     * <p> This method also sets the shop's stock to 0.
     */
    public void stash() {
        if(stock == 0) return;
        if(item.getItem() == Items.AIR) return;


        // Stash items
        StashManager.stashItem(ownerUUID, item, stock);
        StashManager.scheduleStashSave(ownerUUID);


        // Send feedback to the player
        final Player owner = FancyPlayerShops.getServer().getPlayerList().getPlayer(ownerUUID);
        if(owner != null && item.getItem() != Items.AIR && stock > 0) {
            owner.displayClientMessage(new Txt()
                .cat("" + Utils.formatAmount(stock, true, true) + " ")
                .cat(MinecraftUtils.getFancyItemName(item).getString())
                .cat(" " + (stock > 1 ? "have" : "has") + " been sent to your stash.")
            .lightGray().get(), false);
        }


        // Reset stock
        stock = 0;
        ShopManager.scheduleShopSave(this);
    }




    /**
     * Deletes this shop without stashing the items or giving the player an unconfigured shop.
     * <p> Any item left in the shop is fully deleted and cannot be recovered.
     * <p> The save file of this shop is also deleted.
     */
    public void delete() {
        if(!deletionState) {
            deletionState = true;

            // Despawn the entities
            if(activeCanvas != null) activeCanvas.despawn();
            if(interactionBlocker != null) interactionBlocker.despawn();
            getItemDisplay().stopLoopAnimation();
            getItemDisplay().despawn();

            // Delete the data associated with this shop
            ShopManager.deleteShop(this);
        }
    }




    /**
     * Tries to retrieve items from nearby inventories.
     * <p> This call has no effect if the shop is fully stocked.
     */
    public void pullItems() {
        if(stock >= maxStock || item.getItem() == Items.AIR) return;
        final int oldStock = stock;

        pullItems(new BlockPos(+0, +0, +0));
        pullItems(new BlockPos(+0, +0, +1));
        pullItems(new BlockPos(+0, +0, -1));
        pullItems(new BlockPos(+0, +1, +0));
        pullItems(new BlockPos(+0, -1, +0));
        pullItems(new BlockPos(+1, +0, +0));
        pullItems(new BlockPos(-1, +0, +0));

        // Update stock and save the shop
        if(oldStock == stock) return;
        ShopManager.scheduleShopSave(this);

        // Update active canvas
        if(activeCanvas != null) {
            activeCanvas.onStockChange();
        }
    }




    /**
     * Tries to retrieve items from a specified position relative to the shop.
     * <p> This call has no effect if the shop is fully stocked.
     * @param rel The position of the inventory, relative to the shop.
     */
    public void pullItems(final @NotNull BlockPos rel) {
        if(stock >= maxStock) return;                                           // Skip pull if shop is full
        final BlockPos targetPos = pos.offset(rel);                             // Calculate inventory position
        final ChunkPos chunkPos = new ChunkPos(pos);                            // Calculate inventory chunk position
        if(!getWorld().hasChunk(chunkPos.x, chunkPos.z)) return;                    // Skip pull if inventory is unloaded
        final BlockEntity be = world.getBlockEntity(targetPos);                 // Get block entity data
        if(be == null) return;                                                      // Skip pull if inventory is not a block entity
        if(be instanceof BaseContainerBlockEntity cbe && cbe.isEmpty()) return;     // Skip pull if inventory is empty


        // Calculate side and find the storage block
        final Direction direction = (rel.getX() + rel.getY() + rel.getZ() == 0) ? null : Direction.getNearest(-rel.getX(), -rel.getY(), rel.getZ());
        final Storage<ItemVariant> storage = ItemStorage.SIDED.find(world, targetPos, direction);


        // If a storage block is found, loop through its slots
        if(storage != null) {
            for(StorageView<ItemVariant> slot : storage) {
                final ItemVariant variant = slot.getResource();
                final long amount = slot.getAmount();

                // If the slot is not empty
                if(!variant.isBlank() && amount > 0) {
                    final ItemStack stackInSlot = variant.toStack((int) amount);

                    // If the item in the slot matches the item sold by the shop
                    if(ItemStack.isSameItemSameTags(stackInSlot, item)) {
                        try(final Transaction tx = Transaction.openOuter()) {
                            final long missing = (long)maxStock - stock;
                            final long extracted = slot.extract(variant, missing, tx);
                            if(extracted > 0) {
                                tx.commit();
                                stock += extracted;
                                if(extracted == missing) return;
                            }
                        }
                    }
                }
            }
        }
    }




    /**
     * Changes the owner of this shop and sends a feedback message to the old owner.
     * <p> If the new owner and the current one are the same player, this call will have no effect.
     * @param newOwner The new owner.
     */
    public void changeOwner(final @NotNull Player newOwner) {
        if(ownerUUID.equals(newOwner.getUUID())) return;
        final Player oldOwner = FancyPlayerShops.getServer().getPlayerList().getPlayer(ownerUUID);


        // Send feedback to old owner
        oldOwner.displayClientMessage(new Txt()
            .cat("You successfully transferred the ownership of your " + getDecoratedName() + " to ")
            .cat("" + newOwner.getName().getString() + ".")
        .color(ShopManager.SHOP_ITEM_NAME_COLOR).get(), false);


        // Send feedback to new owner
        newOwner.displayClientMessage(new Txt()
            .cat("" + oldOwner.getName().getString() + " transferred ownership of their " + getDecoratedName() + " to you.")
            .cat("\nYou can find it at the coords [" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]")
            .cat(" in the dimension " + world.dimension().location().toString())
        .color(ShopManager.SHOP_ITEM_NAME_COLOR).get(), false);


        // Actually change the owner and save the shop, then force it to unfocus to prevent the previous owner from accessing the UI
        focusStateNext = false;
        updateFocusState();
        ownerUUID = newOwner.getUUID();
        ShopManager.scheduleShopSave(this);
    }




    /**
     * Changes the hue of the color theme.
     * @param _hue The new hue value.
     */
    public void setColorThemeHue(final float _hue) {
        colorThemeHue = _hue;
        ShopManager.scheduleShopSave(this);
    }

    /**
     * Returns the main color of the color theme.
     * @return The RGB color value.
     */
    public Vector3i getThemeColor1() {
        return Utils.HSVtoRGB(new Vector3f(colorThemeHue, Configs.shop.theme_saturation_main.getValue(), Configs.shop.theme_luminosity_main.getValue()));
    }

    /**
     * Returns the secondary color of the color theme.
     * @return The RGB color value.
     */
    public Vector3i getThemeColor2() {
        return Utils.HSVtoRGB(new Vector3f(colorThemeHue, Configs.shop.theme_saturation_secondary.getValue(), Configs.shop.theme_luminosity_secondary.getValue()));
    }








    public @NotNull String getDecoratedName() {
        return item.getItem() == Items.AIR ? "empty shop" : "shop \"" + MinecraftUtils.getFancyItemName(item).getString() + "\"";
    }
}