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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snek.fancyplayershops.data.BalanceManager;
import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.data.StashManager;
import com.snek.fancyplayershops.hud_ui._elements.HudCanvas;
import com.snek.fancyplayershops.input.ClickReceiver;
import com.snek.fancyplayershops.input.HoverReceiver;
import com.snek.fancyplayershops.input.MessageReceiver;
import com.snek.fancyplayershops.shop_ui._elements.ShopItemDisplay;
import com.snek.fancyplayershops.ui._elements.InteractionBlocker;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.scheduler.Scheduler;








/**
 * The main class of the mod FancyPlayerShops.
 * <p> This class caches the server instance and initializes all the event callbacks.
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




    public static Path getStorageDir() {
        return getServer().getWorldPath(LevelResource.ROOT).resolve("data/" + MOD_ID);
    }














    private static boolean fatal = false;
    public static void flagFatal() { fatal = true; }

    @Override
    public void onInitialize() {


        // Register commands
        CommandManager.register();




        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            serverInstance = server;


            // Read config files
            Configs.loadConfigs();
            if(fatal) return;


            // Create storage directories
            for(String path : new String[] { "shops", "stash", "balance" }) {
                try {
                    Files.createDirectories(FancyPlayerShops.getStorageDir().resolve(path));
                } catch(IOException e) {
                    e.printStackTrace();
                    flagFatal();
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
            Scheduler.loop(0, Configs.perf.animation_refresh_time.getValue(), () -> {
                Elm.processUpdateQueue();
                HudCanvas.updateActiveCanvases();
            });

            // Schedule hover manager loop
            Scheduler.loop(0, 1, () -> HoverReceiver.tick());

            // Schedule shop pull updates
            Scheduler.loop(0, 1, ShopManager::pullItems);

            // Schedule data saves
            Scheduler.loop(0, Configs.perf.data_save_frequency.getValue(), () -> {
                ShopManager.saveScheduledShops();
                StashManager.saveScheduledStashes();
                BalanceManager.saveScheduledBalances();
            });

            // Log initialization success
            LOGGER.info("FancyPlayerShops initialized. :3");




            // Create and register block click events (shop placement + prevents early clicks going through the shop)
            AttackBlockCallback.EVENT.register((player, world, hand, blockPos, direction) -> {
                return ClickReceiver.onClickBlock(world, player, hand, ClickAction.PRIMARY, blockPos.offset(direction.getNormal()));
            });
            UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
                InteractionResult r;
                r = ClickReceiver.onClickBlock(world, player, hand, ClickAction.SECONDARY, hitResult.getBlockPos().offset(hitResult.getDirection().getNormal()));
                if(r == InteractionResult.PASS) r = onItemUse(world, player, hand, hitResult);
                return r;
            });




            // Create and register entity click events (interaction blocker clicks)
            AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
                return ClickReceiver.onClickEntity(world, player, hand, ClickAction.PRIMARY, entity);
            });
            UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
                return ClickReceiver.onClickEntity(world, player, hand, ClickAction.SECONDARY, entity);
            });




            // Create and register item use events (prevents early clicks going through the shop)
            UseItemCallback.EVENT.register((player, world, hand) -> {
                InteractionResult r = ClickReceiver.onUseItem(world, player, hand);
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
                return !MessageReceiver.onMessage(message, sender);
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
     * @return FAIL if the player tried to place a shop, PASS otherwise.
     */
    public static @NotNull InteractionResult onItemUse(final @NotNull Level world, final @NotNull Player player, final @NotNull InteractionHand hand, final @NotNull BlockHitResult hitResult) {
        final ItemStack stack = player.getItemInHand(hand);
        if(stack != null && stack.getItem() == Items.PLAYER_HEAD && stack.hasTag() && stack.getTag().contains(ShopManager.SHOP_ITEM_NBT_KEY)) {

            // If the world is a server world and the player is allowed to modify the world
            if(world instanceof ServerLevel serverWorld && player.getAbilities().mayBuild) {
                int newCount = stack.getCount();

                // Calculate block position and create the new shop if no other shop is already there. Send a feedback message to the player
                final BlockPos blockPos = hitResult.getBlockPos().offset(hitResult.getDirection().getNormal());
                final CompoundTag tag = stack.getTag();
                if(ShopManager.findShop(blockPos, world) == null) {

                    // Spawn snapshot if the item has the snapshot tag
                    if(tag.contains(ShopManager.SNAPSHOT_NBT_KEY)) {
                        final CompoundTag data = tag.getCompound(FancyPlayerShops.MOD_ID + ".shop_data");
                        if(data.getUUID("owner").equals(player.getUUID())) {
                            new Shop(serverWorld, blockPos, player.getUUID(), data.getLong("price"), data.getInt("stock"), data.getInt("max_stock"), data.getFloat("rotation"), data.getFloat("hue"), data.getString("item"));
                            player.displayClientMessage(new Txt("Shop snapshot restored.").color(ShopManager.SHOP_ITEM_NAME_COLOR).bold().get(), true);
                            if(!player.getAbilities().instabuild) --newCount;
                        }
                        else {
                            player.displayClientMessage(new Txt("This shop belongs to " + data.getString("owner_name") + "! Only they can place it.").red().bold().get(), true);
                        }
                    }

                    // Spawn empty shop otherwise
                    else {
                        new Shop(serverWorld, blockPos, player);
                        player.displayClientMessage(new Txt("New shop created. Right click it to configure.").color(ShopManager.SHOP_ITEM_NAME_COLOR).bold().get(), true);
                        if(!player.getAbilities().instabuild) --newCount;
                    }
                }

                // Update the held item
                final ItemStack newStack = stack.copyWithCount(newCount);
                player.setItemInHand(hand, newStack);
                MinecraftUtils.sendClientSlotUpdate(player, player.getInventory().selected + 36, newStack);
            }

            // If not, send an error message to the player
            else player.displayClientMessage(new Txt("You cannot create a shop here!").red().bold().get(), true);
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }
}