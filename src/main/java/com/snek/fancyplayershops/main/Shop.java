package com.snek.fancyplayershops.main;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import org.joml.Vector3d;

import com.google.gson.JsonParser;
import com.herrkatze.solsticeEconomy.modules.economy.EconomyManager;
import com.mojang.serialization.JsonOps;
import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.ui.InteractionBlocker;
import com.snek.fancyplayershops.ui.ShopCanvas;
import com.snek.fancyplayershops.ui.ShopItemDisplay;
import com.snek.fancyplayershops.ui.buy.BuyUi;
import com.snek.fancyplayershops.ui.details.DetailsUi;
import com.snek.fancyplayershops.ui.edit.EditUi;
import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.animations.Transform;
import com.snek.framework.data_types.animations.Transition;
import com.snek.framework.data_types.containers.Pair;
import com.snek.framework.ui.Div;
import com.snek.framework.ui.functional.ButtonElm;
import com.snek.framework.utils.Easings;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;
import com.snek.framework.utils.scheduler.RateLimiter;
import com.snek.framework.utils.scheduler.Scheduler;
import com.snek.framework.utils.scheduler.TaskHandler;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display.ItemDisplay;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
















/**
 * This class manages a player shop that is placed somewhere in a world.
 */
public class Shop {

    // Limits
    public static final double MAX_PRICE = 100_000_000_000d;
    public static final int    MAX_STOCK =       1_000_000;


    // Animation data
    public static final int CANVAS_ANIMATION_DELAY = 5;
    public static final int CANVAS_ROTATION_TIME = 8;


    // Strings
    public  static final Component EMPTY_SHOP_NAME = new Txt("[Empty]").italic().lightGray().get();
    private static final Component SHOP_EMPTY_TEXT = new Txt("This shop is empty!").lightGray().get();
    private static final Component SHOP_STOCK_TEXT = new Txt("This shop has no items in stock!").lightGray().get();




    // Basic data
    private transient @NotNull  ServerLevel     world;                          // The world this shop was placed in
    private           @NotNull  String          worldId;                        // The Identifier of the world
    // private           @NotNull  UUID            itemDisplayUUID;                // The UUID of the item display //TODO REMOVE
    private transient @Nullable ShopItemDisplay itemDisplay = null;             // The item display entity //! Searched when needed instead of on data loading because the chunk needs to be loaded in order to find the entity.
    private           @NotNull  BlockPos        pos;                            // The position of the shop
    private transient @NotNull  String          shopIdentifierCache;            // The cached shop identifier
    private transient @NotNull  String          shopIdentifierCache_noWorld;    // The cached shop identifier, without including the world


    // Shop data
    private transient @NotNull ItemStack item = Items.AIR.getDefaultInstance(); // The configured item
    private           @NotNull UUID      ownerUUID;                             // The UUID of the owner
    private           @NotNull String    serializedItem;                        // The item in serialized form
    private                    int       stock           = 0;                   // The current stock
    private                    double    price           = 1_000_000;           // The configured price for each item
    private                    int       maxStock        = 1_000;               // The configured maximum stock
    private                    float     defaultRotation = 0f;                  // The configured item rotation


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
    private transient @Nullable TaskHandler interactionBlockerDeletionHandler;


    // Accessors
    public @NotNull  ServerLevel     getWorld          () { return world;           }
    public @NotNull  String          getWorldId        () { return worldId;         }
    public @NotNull  BlockPos        getPos            () { return pos;             }
    public @NotNull  ItemStack       getItem           () { return item;            }
    public @NotNull  ShopItemDisplay getItemDisplay    () { return findItemDisplayEntityIfNeeded(); }
    public @Nullable ShopCanvas      getActiveCanvas   () { return activeCanvas;    }
    public           double          getPrice          () { return price;           }
    public           int             getStock          () { return stock;           }
    public           int             getMaxStock       () { return maxStock;        }
    public           float           getDefaultRotation() { return defaultRotation; }
    public           int             getCanvasDirection() { return lastDirection;   }
    public           boolean         isFocused         () { return focusState;      }
    public           boolean         isDeleted         () { return deletionState;   }
    public @NotNull  UUID            getOwnerUuid      () { return ownerUUID;       }
    public @Nullable Player          getuser           () { return user;            }
    public @Nullable Player          getViewer         () { return viewer;          }
    public           void            setViewer         (final @Nullable Player  _viewer        ) { viewer         = _viewer;         }
    public           void            setFocusStateNext (final           boolean _nextFocusState) { focusStateNext = _nextFocusState; }
    public @NotNull  String          getIdentifier     () { return shopIdentifierCache; }
    public @NotNull  String          getIdentifierNoWorld() { return shopIdentifierCache_noWorld; }








    /**
     * Calculates the position display entities should be spawned at.
     * @return The absolute position of display entities.
     */
    public @NotNull Vector3d calcDisplayPos() {
        return new Vector3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
    }


    /**
     * Computes the serialized form of the item.
     * @throws RuntimeException if the item cannot be serialized.
     */
    private void calcSerializedItem() {
        final var result = ItemStack.CODEC.encode(item, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).result();
        if(result.isEmpty()) {
            throw new RuntimeException("Could not serialize shop item");
        }
        serializedItem = result.get().toString();
    }


    /**
     * Computes the ItemStack form of the item, reading data from its serialized version.
     * @throws RuntimeException if the item cannot be deserialized.
     */
    public void calcDeserializedItem() {
        final var result = ItemStack.CODEC.decode(JsonOps.INSTANCE, JsonParser.parseString(serializedItem)).result();
        if(result.isEmpty()) {
            throw new RuntimeException("Could not deserialize shop item");
        }
        item = result.get().getFirst();
    }


    /**
     * Computes the Identifier of the world.
     */
    private void calcSerializedWorldId() {
        worldId = world.dimension().location().toString();
    }


    /**
     * Tries find the ServerLevel the world identifier belongs to.
     * @param server The server instance.
     * @throws RuntimeException if the world Identifier is invalid or the ServerLevel cannot be found.
     */
    private void calcDeserializedWorldId(MinecraftServer server) throws RuntimeException {
        for(final ServerLevel w : server.getAllLevels()) {
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
        shopIdentifierCache         = calcShopIdentifier(pos, worldId);
        shopIdentifierCache_noWorld = calcShopIdentifier(pos);
    }


    /**
     * Calculates a shop identifier from a position and the world ID.
     * @param _pos The position.
     * @param worldId The world ID.
     * @return The generated identifier.
     */
    public static String calcShopIdentifier(final @NotNull BlockPos _pos, final @NotNull String worldId) {
        return calcShopIdentifier(_pos) + "," + worldId;
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
     * Reinitializes the transient members.
     * @return Whether the item and world id have been deserialized successfully.
     * <p> Shops whose data cannot be deserialized shouldn't be loaded as their save file is likely corrupted.
    */
    public boolean reinitTransient(){
        focusState            = false;
        focusStateNext        = false;
        lastDirection         = 0;
        canvasRotationLimiter = new RateLimiter();
        menuOpenLimiter = new RateLimiter();
        cacheShopIdentifier();
        try {
            calcDeserializedItem();
            calcDeserializedWorldId(FancyPlayerShops.getServer());
            return true;
        } catch (RuntimeException e) {
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
        world = _world;
        ownerUUID = owner.getUUID();
        pos = _pos;

        // Get members from serialized data and calculate shop identifier
        calcSerializedItem();
        calcSerializedWorldId();
        cacheShopIdentifier();

        // Create and spawn the Item Display entity
        itemDisplay = new ShopItemDisplay(this);
        // itemDisplayUUID = itemDisplay.getEntity().getUuid(); //TODO REMOVE
        itemDisplay.spawn(calcDisplayPos());

        // Save the shop
        ShopManager.saveShop(this);
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
                    final Pair<Animation, Animation> animations = calcCanvasRotationAnimation(0, lastDirection);
                    activeCanvas.applyAnimationNowRecursive(animations.getFirst());
                    itemDisplay.applyAnimationNowRecursive(animations.getSecond());
                }
                activeCanvas.spawn(calcDisplayPos());

                // Create interaction blocker
                //! Only spawning a new one if the deletion handler is present doesn't work as it can be left there from previous calls while being cancelled.
                //! Cancelling it regardless and removing the entity to spawn a new one makes sure the interaction blocker is never null when it's needed
                if(interactionBlockerDeletionHandler != null) interactionBlockerDeletionHandler.cancel();
                if(interactionBlocker != null) interactionBlocker.despawn();
                interactionBlocker = new InteractionBlocker(this);
                interactionBlocker.spawn(new Vector3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));

                // Start item animation and turn off the CustomName
                getItemDisplay().enterFocusState();
            }
            else {

                // Despawn active canvas
                activeCanvas.despawn();
                activeCanvas = null;

                // Despawn interaction blocker after despawn animations end
                if(interactionBlockerDeletionHandler != null) interactionBlockerDeletionHandler.cancel();
                interactionBlockerDeletionHandler = Scheduler.schedule(ShopItemDisplay.D_TIME, () -> {
                    interactionBlocker.despawn();
                    interactionBlocker = null;
                });

                // Cancel chat input callbacks, then reset the user and renew the focus cooldown
                menuOpenLimiter.renewCooldown(ShopItemDisplay.D_TIME);
                if(user != null) ChatManager.removeCallback(user);
                user = null;

                // Turn the item display's custom name back on
                getItemDisplay().leaveFocusState();
            }
        }
    }




    public void invalidateItemDisplay(){
        System.out.println("[INVALIDATED]");
        if(itemDisplay != null) {
            System.out.println("[DESPAWNED]");
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

        // final ItemDisplay rawItemDisplay = (ItemDisplay)(world.getEntity(itemDisplayUUID));
        if(itemDisplay == null || itemDisplay.getEntity().isRemoved()) {
            // rawItemDisplay == null || rawItemDisplay.isRemoved()) {
            itemDisplay = new ShopItemDisplay(this);
            itemDisplay.spawn(calcDisplayPos());
        }
        //     itemDisplayUUID = itemDisplay.getEntity().getUuid();
        //     ShopManager.saveShop(this);
        // }
        // else {
        //     System.out.println("used item display " + itemDisplay.getEntity().getUuid().toString());
        // }
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
                        retrieveItem(player);
                    }
                    else {
                        buyItem(player, 1);
                    }
                }
                else {
                    user = player;
                    if(player.getUUID().equals(ownerUUID)) {
                        openEditUi(player);
                    }
                    else {
                        openBuyUi(player);
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
                        retrieveItem(player);
                    }
                    else {
                        buyItem(player, 1);
                    }
                }
            }
        }
    }




    /**
     * Retrieves one item from the shop at no cost and gives it to the owner.
     * <p> Sends an error message to the player if the shop is unconfigured or doesn't contain any item.
     * @param owner The owner of the shop.
     */
    public void retrieveItem(final @NotNull Player owner) {
        if(item.getItem() == Items.AIR) {
            owner.displayClientMessage(SHOP_EMPTY_TEXT, true);
        }
        else if(stock < 1) {
            owner.displayClientMessage(new Txt("This shop has no items in stock!").lightGray().get(), true);
        }
        else {
            --stock;
            ShopManager.saveShop(this);
            final ItemStack _item = item.copyWithCount(1);


            // Send feedback to the player
            if(MinecraftUtils.attemptGive(owner, _item)) {
                owner.displayClientMessage(new Txt()
                    .cat(new Txt("You retrieved 1x ").lightGray())
                    .cat(MinecraftUtils.getFancyItemName(item))
                    .cat(new Txt(" from your shop.").lightGray())
                .get(), false);
            }
            else {
                owner.displayClientMessage(new Txt()
                    .cat(new Txt("1x ").lightGray())
                    .cat(MinecraftUtils.getFancyItemName(item))
                    .cat(new Txt(" retrieved from your shop has been sent to your stash.").lightGray())
                .get(), false);
            }
        }
    }




    /**
     * Makes a player buy a specified amount of items from the shop.
     * <p> Sends an error message to the player if the shop is unconfigured or doesn't contain enough item.
     * @param player The player.
     * @param amount The amount of items to buy.
     */
    public void buyItem(final @NotNull Player player, final int amount) {
        if(item.getItem() == Items.AIR) {
            player.displayClientMessage(SHOP_EMPTY_TEXT, true);
        }
        else if(stock < 1) {
            player.displayClientMessage(SHOP_STOCK_TEXT, true);
        }
        else if(stock < amount) {
            player.displayClientMessage(SHOP_STOCK_TEXT.copy().append(new Txt(" Items left: " + stock).lightGray().get()), true);
        }
        else {
            final long totPrice = Math.round(price * amount * 100);
            if(EconomyManager.getCurrency(player.getUUID()) >= totPrice) {
                stock -= amount;
                ShopManager.saveShop(this);
                EconomyManager.subtractCurrency(player.getUUID(), totPrice);
                final ItemStack _item = item.copyWithCount(amount);


                // Send feedback to the player
                if(MinecraftUtils.attemptGive(player, _item)) {
                    player.displayClientMessage(new Txt()
                        .cat(new Txt("Bought " + Utils.formatAmount(amount, true, true)).lightGray().cat(" "))
                        .cat(MinecraftUtils.getFancyItemName(item))
                        .cat(new Txt(" for " + Utils.formatPrice((double)totPrice / 100)).lightGray())
                    .get(), false);
                }
                else {
                    player.displayClientMessage(new Txt()
                        .cat("" + Utils.formatAmount(amount, true, true) + " ")
                        .cat(MinecraftUtils.getFancyItemName(item))
                        .cat(new Txt(" bought for " + Utils.formatPrice((double)totPrice / 100) + " " + (amount > 1 ? "have" : "has") + " been sent to your stash.").lightGray())
                    .get(), false);
                }
            }
            else {
                player.displayClientMessage(new Txt("You don't have enough money to purchase this!").bold().red().get(), true);
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
            final Pair<Animation, Animation> animations = calcCanvasRotationAnimation(0, lastDirection);
            for(final Div c : canvas.getBg().getChildren()) {
                c.applyAnimationNowRecursive(animations.getFirst());
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




    /**
     * Opens the buy item UI.
     * @param player The player.
     */
    public void openBuyUi(final @NotNull Player player) {
        if(item.getItem() == Items.AIR) {
            player.displayClientMessage(SHOP_EMPTY_TEXT, true);
        }
        else {
            changeCanvas(new BuyUi(this));
            getItemDisplay().enterEditState(); //TODO use a more generic name? for the animations too
        }
    }




    /**
     * Tries to set a new price for the item and sends an error message to the user if it's invalid.
     *     <p> Prices are automatically rounded to the nearest hundredth.
     *     <p> Prices under 0.01 are rounded to 0.01
     *     <p> Prices under 0.00001 are rounded to 0.
     *     <p> Negative values are considered invalid and return false without changing the price.
     *     <p> Values above MAX_PRICE are also considered invalid. //TODO add to config file
     * @param newPrice The new price
     * @return Whether the new value could be set.
     */
    public boolean setPrice(final double newPrice) {
        if(newPrice < 0) {
            if(user != null) user.displayClientMessage(new Txt("The price cannot be negative").red().bold().get(), true);
            return false;
        }
        if(newPrice > MAX_PRICE) {
            if(user != null) user.displayClientMessage(new Txt("The price cannot be greater than " + Utils.formatPrice(MAX_PRICE)).red().bold().get(), true);
            return false;
        }
        else if(newPrice < 0.00001) price = 0d;
        else if(newPrice < 0.01000) price = 0.01;
        else price = Math.round(newPrice * 100d) / 100d;
        ShopManager.saveShop(this);
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
        if(newStockLimit > MAX_STOCK) {
            if(user != null) user.displayClientMessage(new Txt("The stock limit cannot be greater than " + Utils.formatAmount(MAX_STOCK, false, true)).red().bold().get(), true);
            return false;
        }
        else maxStock = Math.round(newStockLimit);
        ShopManager.saveShop(this);
        return true;
    }




    /**
     * Adds a specified rotation to the default rotation of the item display and saves the shop to its file.
     * @param _rotation The rotation to add.
     */
    public void addDefaultRotation(final float _rotation) {

        // Add value to default rotation and save the shop
        defaultRotation += _rotation;
        ShopManager.saveShop(this);
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
        calcSerializedItem();
        ShopManager.saveShop(this);
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
            final Pair<Animation, Animation> animations = calcCanvasRotationAnimation(lastDirection, targetDir);
            activeCanvas.applyAnimationRecursive(animations.getFirst());
            getItemDisplay().applyAnimationRecursive(animations.getSecond());
            lastDirection = targetDir;
            canvasRotationLimiter.renewCooldown(CANVAS_ROTATION_TIME);
        }
    }




    /**
     * Calculates the animations required to face from a specified direction to another one.
     * @param from The starting direction.
     * @param to The new direction to face.
     * @return The canvas animation and the item display animation.
     */
    public static @NotNull Pair<@NotNull Animation, @NotNull Animation> calcCanvasRotationAnimation(final int from, final int to) {
        final float rotation = -Math.toRadians(to * 45f - from * 45f);
        return Pair.from(
            new Animation(
                new Transition(CANVAS_ROTATION_TIME, Easings.cubicOut)
                .additiveTransform(new Transform().rotGlobalY(rotation))
            ),
            new Animation(
                new Transition(CANVAS_ROTATION_TIME, Easings.cubicOut)
                .additiveTransform(new Transform().rotGlobalY(rotation).rotY(- rotation))
            )
        );
    }




    /**
     * Sends the items stored in this shop to the owner's stash.
     * <p> This method also sets the shop's stock to 0.
     */
    public void stash(){
        // FIXME ACTUALLY STASH ITEMS
        // TODO ^ add /shop stash command
        //BUG remove previous item stock and send it to the owner's stash to prevent duplication bugs
        //BUG remove previous item stock and send it to the owner's stash to prevent duplication bugs
        //BUG remove previous item stock and send it to the owner's stash to prevent duplication bugs


        // Send feedback to the player
        if(item.getItem() != Items.AIR && stock > 0) {
            user.displayClientMessage(new Txt()
                .cat("" + Utils.formatAmount(stock, true, true) + " ").lightGray()
                .cat(MinecraftUtils.getFancyItemName(item))
                .cat(new Txt(" " + (stock > 1 ? "have" : "has") + " been sent to your stash."))
            .get(), false);
        }


        // Reset stock
        stock = 0;
        ShopManager.saveShop(this);
    }




    /**
     * Deletes this shop without stashing the items.
     * <p> Any item left in the shop is fully deleted and cannot be recovered.
     * <p> The save file of this shop is also deteled.
     */
    public void delete() {
        if(!deletionState) {
            deletionState = true;

            // Despawn the entities
            if(activeCanvas != null) activeCanvas.despawn();
            if(interactionBlocker != null) interactionBlocker.despawn();
            getItemDisplay().stopLoopAnimation();
            getItemDisplay().despawn();

            // Give the player a default shop item
            final ItemStack _item =  FancyPlayerShops.getShopItemCopy();
            if(!MinecraftUtils.attemptGive(user, _item)) {
                user.displayClientMessage(new Txt()
                    .cat("1x ").lightGray()
                    .cat(MinecraftUtils.getFancyItemName(_item))
                    .cat(new Txt(" has been sent to your stash."))
                .get(), false);
            }
            // FIXME ACTUALLY STASH ITEMS

            // Delete the data associated with this shop
            ShopManager.deleteShop(this);
        }
    }




    /**
     * Tries to retrieve items from nearby inventories.
     * <p> This call has no effect if the shop is fully stocked.
     */
    public void pullItems(){
        if(stock >= maxStock || item.getItem() == Items.AIR) return;
        final ChunkPos chunkPos = new ChunkPos(pos);
        if(!getWorld().hasChunk(chunkPos.x, chunkPos.z)) return;
        final int oldStock = stock;

        pullItems(new BlockPos(0, 0, 0));
        pullItems(new BlockPos(0, 0, +1));
        pullItems(new BlockPos(0, 0, -1));
        pullItems(new BlockPos(0, +1, 0));
        pullItems(new BlockPos(0, -1, 0));
        pullItems(new BlockPos(+1, 0, 0));
        pullItems(new BlockPos(-1, 0, 0));

        // Save shop and update detatils display if needed
        if(oldStock == stock) return;
        ShopManager.saveShop(this);
        if(activeCanvas != null && activeCanvas instanceof DetailsUi c) {
            c.getValues().updateDisplay();
        }
    }




    /**
     * Tries to retrieve items from a specified position relative to the shop.
     * <p> This call has no effect if the shop is fully stocked.
     * @param rel The position of the inventory, relative to the shop.
     */
    public void pullItems(final @NotNull BlockPos rel){
        if(stock >= maxStock) return;
        final BlockPos targetPos = pos.offset(rel);
        final ChunkPos chunkPos = new ChunkPos(pos);
        if(!getWorld().hasChunk(chunkPos.x, chunkPos.z)) return;
        final BlockEntity be = world.getBlockEntity(targetPos);
        if(be == null) return;


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
}