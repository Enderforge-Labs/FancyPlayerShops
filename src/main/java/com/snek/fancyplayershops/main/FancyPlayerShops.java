package com.snek.fancyplayershops.main;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.BlockHitResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snek.fancyplayershops.data.BalanceManager;
import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.data.StashManager;
import com.snek.fancyplayershops.ui.InteractionBlocker;
import com.snek.fancyplayershops.ui.ShopItemDisplay;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.scheduler.Scheduler;








/**
 * The main class of the mod FancyPlayerShops.
 * <p> This class defines the Shop item, caches the server instance and initializes all the event callbacks.
 */
public class FancyPlayerShops implements ModInitializer {

    // Server instance
    private static @Nullable MinecraftServer serverInstance = null;
    public  static @NotNull  MinecraftServer getServer() {
        if(serverInstance == null) {
            throw new NullPointerException("Server instance requested before initialization");
        }
        return serverInstance;
    }

    // Mod ID and console logger
    public static final @NotNull String MOD_ID = "fancyplayershops";
    public static final @NotNull Logger LOGGER = LoggerFactory.getLogger(MOD_ID);




    // Shop item data
    //! Don't use the name or tooltip to check the item. Shops should work even when renamed in an anvil or modified by mods
    private static final @NotNull ItemStack shopItem;
    private static final @NotNull String SHOP_ITEM_NBT_KEY = MOD_ID + ".item.shop_item";

    // Shop item texture
    private static final @NotNull String SHOP_ITEM_TEXTURE =
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZj" +
        "I3ODQzMDdiODkyZjUyYjkyZjc0ZmE5ZGI0OTg0YzRmMGYwMmViODFjNjc1MmU1ZWJhNjlhZDY3ODU4NDI3ZSJ9fX0="
    ;

    // Shop item name
    public static final @NotNull Vector3i SHOP_ITEM_NAME_COLOR = new Vector3i(175, 140, 190);
    public static final @NotNull Component SHOP_ITEM_NAME =
        new Txt("Item Shop").noItalic().bold().color(SHOP_ITEM_NAME_COLOR) //FIXME specify sold item name in shop snapshots
    .get();

    // Shop item description
    private static final @NotNull Vector3i SHOP_ITEM_DESCRITPION_COLOR = new Vector3i(225, 180, 230);
    private static final @NotNull Component[] SHOP_ITEM_DESCRITPION = {
        new Txt().cat(new Txt("A ").white()).cat(new Txt("shop").color(SHOP_ITEM_DESCRITPION_COLOR)).cat(new Txt(" that allows you to sell items to other players.").white()).noItalic().get(),
        new Txt().cat(new Txt("Place this anywhere and ").white()).cat(new Txt("right click").color(SHOP_ITEM_DESCRITPION_COLOR)).cat(new Txt(" it to get started!").white()).noItalic().get(),
        new Txt("").noItalic().get()
    };




    // Initialize shop item stack
    static {

        // Create item and set custom name
        shopItem = MinecraftUtils.createCustomHead(SHOP_ITEM_TEXTURE);
        shopItem.setHoverName(SHOP_ITEM_NAME);

        // Set identification tag
        final CompoundTag nbt = shopItem.getOrCreateTag();
        nbt.putBoolean(SHOP_ITEM_NBT_KEY, true);

        // Set lore
        final ListTag lore = new ListTag();
        for(final Component line : SHOP_ITEM_DESCRITPION) {
            lore.add(StringTag.valueOf(Component.Serializer.toJson(line)));
        }
        shopItem.getOrCreateTagElement("display").put("Lore", lore);
    }




    public static Path getStorageDir() {
        return getServer().getWorldPath(LevelResource.ROOT).resolve("data/" + MOD_ID);
    }














    private boolean fatal = false;
    @Override
    public void onInitialize() {


        // Register commands
        CommandManager.register();




        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            serverInstance = server;


            // Create storage directories
            for(String path : new String[] { "shops", "stash" }) {
                try {
                    Files.createDirectories(FancyPlayerShops.getStorageDir().resolve(path));
                } catch(IOException e) {
                    e.printStackTrace();
                    fatal = true;
                    return;
                }
            }
        });




        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            if(fatal) return;

            // Load persistent data
            ShopManager.loadShops();
            StashManager.loadStashes();
            BalanceManager.loadBalances();


            // Schedule UI element update loop
            Scheduler.loop(0, Elm.TRANSITION_REFRESH_TIME, Elm::processUpdateQueue);

            // Schedule focus manager loop
            Scheduler.loop(0, 1, () -> HoverManager.tick(server.getAllLevels()));

            // Schedule shop pull updates
            Scheduler.loop(0, 1, ShopManager::pullItems);

            // Log initialization success
            LOGGER.info("FancyPlayerShops initialized. :3");




            // Create and register block click events (shop placement + prevents early clicks going through the shop)
            AttackBlockCallback.EVENT.register((player, world, hand, blockPos, direction) -> {
                return ClickManager.onClickBlock(world, player, hand, ClickAction.PRIMARY, blockPos.offset(direction.getNormal()));
            });
            UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
                InteractionResult r;
                r = ClickManager.onClickBlock(world, player, hand, ClickAction.SECONDARY, hitResult.getBlockPos().offset(hitResult.getDirection().getNormal()));
                if(r == InteractionResult.PASS) r = onItemUse(world, player, hand, hitResult);
                return r;
            });




            // Create and register entity click events (interaction blocker clicks)
            AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
                return ClickManager.onClickEntity(world, player, hand, ClickAction.PRIMARY, entity);
            });
            UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
                return ClickManager.onClickEntity(world, player, hand, ClickAction.SECONDARY, entity);
            });




            // Create and register item use events (prevents early clicks going through the shop)
            UseItemCallback.EVENT.register((player, world, hand) -> {
                InteractionResult r = ClickManager.onUseItem(world, player, hand);
                if(r == InteractionResult.FAIL) return InteractionResultHolder.fail(player.getItemInHand(hand));
                /**/                       else return InteractionResultHolder.pass(player.getItemInHand(hand));
            });




            // Register scheduler
            ServerTickEvents.END_SERVER_TICK.register(_server -> {
                Scheduler.tick();
            });




            // Register entity display purge
            ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
                Elm.onEntityLoad(entity);
                ShopItemDisplay.onEntityLoad(entity);
                InteractionBlocker.onEntityLoad(entity);
            });




            // Register chat input handler
            ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
                return !ChatManager.onMessage(message, sender);
            });
        });
    }








    /**
     * Callback for item use events.
     * <p> Checks if the held item is a shop item. If it is, it spawns a new shop at the targeted location.
     * @param world The world.
     * @param player The player that clicked.
     * @param hand The hand used.
     * @param hitResult The hit result of the click action.
     * @return SUCCESS if the player tried to place a shop, PASS otherwise.
     */
    public static @NotNull InteractionResult onItemUse(final @NotNull Level world, final @NotNull Player player, final @NotNull InteractionHand hand, final @NotNull BlockHitResult hitResult) {
        final ItemStack stack = player.getItemInHand(hand);
        if(stack != null && stack.getItem() == Items.PLAYER_HEAD && stack.hasTag() && stack.getTag().contains(SHOP_ITEM_NBT_KEY)) {

            // If the world is a server world and the player is allowed to modify the world
            if(world instanceof ServerLevel serverWorld && player.getAbilities().mayBuild) {

                // Consume item if the player is not in creative mode
                if(!player.getAbilities().instabuild) stack.setCount(stack.getCount() - 1);

                // Calculate block position and create the new shop if no other shop is already there. Send a feedback message to the player
                final BlockPos blockPos = hitResult.getBlockPos().offset(hitResult.getDirection().getNormal());
                if(ShopManager.findShop(blockPos, world) == null) {
                    new Shop(serverWorld, blockPos, player);
                    player.displayClientMessage(new Txt("New shop created! Right click it to configure.").color(FancyPlayerShops.SHOP_ITEM_NAME_COLOR).bold().get(), true);
                }
            }

            // If not, send an error message to the player
            else player.displayClientMessage(new Txt("You cannot create a shop here!").red().bold().get(), true);
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
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
        return shopItem.copy(); //FIXME actually copy the shop data
    }
}