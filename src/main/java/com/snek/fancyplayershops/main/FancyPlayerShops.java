package com.snek.fancyplayershops.main;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.BlockHitResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snek.fancyplayershops.configs.Configs;
import com.snek.fancyplayershops.data.shop.Shop_Manager;
import com.snek.fancyplayershops.data.stash.StashManager;
import com.snek.fancyplayershops.data.display.DisplayTier;
import com.snek.fancyplayershops.data.display.ProductDisplay;
import com.snek.fancyplayershops.data.display.ProductDisplay_Key;
import com.snek.fancyplayershops.data.display.ProductDisplay_Manager;
import com.snek.fancyplayershops.data.display.ProductDisplay_Serializer;
import com.snek.fancyplayershops.events.DisplayEvents;
import com.snek.fancyplayershops.events.data.DisplayCreationReason;
import com.snek.frameworkconfig.FrameworkConfig;
import com.snek.fancyplayershops.graphics.ui.buy.BuyCanvas;
import com.snek.fancyplayershops.graphics.ui.core.elements.ProductItemDisplayElm;
import com.snek.fancyplayershops.graphics.ui.details.DetailsCanvas;
import com.snek.fancyplayershops.graphics.ui.edit.EditCanvas;
import com.snek.fancyplayershops.input.HoverReceiver;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.data_types.graphics.Direction;
import com.snek.frameworklib.utils.common.MinecraftUtils;
import com.snek.frameworklib.utils.common.NetworkUtils;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.scheduler.Scheduler;








/**
 * The main class of the mod FancyPlayerShops.
 * <p> This class initializes all the event callbacks.
 */
public class FancyPlayerShops implements ModInitializer {

    // Mod ID and console logger
    public static final @NotNull String MOD_ID = "fancyplayershops";
    public static final @NotNull Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final ResourceLocation PHASE_ID = new ResourceLocation(MOD_ID, "phase_id");




    //TODO REMOVE
    public static Path getStorageDir() {
        return FrameworkLib.getServer().getWorldPath(LevelResource.ROOT).resolve("data/" + MOD_ID);
    }
    //TODO REMOVE
    public static Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir().resolve(MOD_ID);
    }














    private static boolean fatal = false;
    public static void flagFatal() { fatal = true; }

    @Override
    public void onInitialize() {

        // Register commands
        CommandManager.register();


        // Force display item cration
        // This loads them in the reference map, which is needed in order to use FrameworkLib's dynamic item references
        try {
            Class.forName("com.snek.fancyplayershops.data.display.ProductDisplayManager");
        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Register initialization
        ServerLifecycleEvents.SERVER_STARTING.addPhaseOrdering(FrameworkConfig.PHASE_ID, PHASE_ID);
        ServerLifecycleEvents.SERVER_STARTING.addPhaseOrdering(FrameworkLib.PHASE_ID, PHASE_ID);
        ServerLifecycleEvents.SERVER_STARTING.register(PHASE_ID, server -> {


            // Create storage directories //TODO remove, replace with frameworkconfig
            try {
                // Files.createDirectories(ShopManager.calcShopDirPath());
                // Files.createDirectories(ProductDisplayManager.calcDisplayDirPath());
                Files.createDirectories(StashManager.calcStashDirPath());
            }
            catch(final IOException e) {
                FancyPlayerShops.LOGGER.error("Couldn't create storage directory", e);
                flagFatal();
                return;
            }


            // Read config files
            Configs.loadConfigs();


            // Stop if errors occurred
            if(fatal) return;
        });




        ServerLifecycleEvents.SERVER_STARTED.register(PHASE_ID, server -> {
            if(fatal) return;




            // Register shop event listeners
            DisplayEvents.STOCK_CHANGED.register((display, oldStock, newStock) -> {
                EditCanvas   .__callback_onStockChange(display, oldStock, newStock);
                BuyCanvas    .__callback_onStockChange(display, oldStock, newStock);
                DetailsCanvas.__callback_onStockChange(display, oldStock, newStock);
            });




            // Load persistent data
            Shop_Manager.loadShops(); //! Must be loaded before displays //TODO prob not needed with frameworkconfig
            //! Force load displays to update chunk counts. This is needed in order to optimize focus detection.
            //! Lazy loading would cause the optimized detection system to not work at all
            ProductDisplay_Manager.REF.forceLoadAll();
            StashManager.loadStashes(); //TODO prob not needed with frameworkconfig

            // Schedule product display focus detection
            Scheduler.loop(0, 1, HoverReceiver::tick);

            // Schedule product display pull updates
            Scheduler.loop(0, 1, ProductDisplay_Manager::pullItems);

            //TODO remove. this won't be needed anymore, after updating all the stuff
            // Schedule data saves
            Scheduler.loop(0, 1, () -> {
                // ShopManager.saveScheduledShops();
                // ProductDisplayManager.saveScheduledDisplays();
                StashManager.saveScheduledStashes();
            });




            // Create and register block click events (display placement + prevents early clicks going through the display)
            UseBlockCallback.EVENT.addPhaseOrdering(FrameworkLib.PHASE_ID, PHASE_ID);
            UseBlockCallback.EVENT.register(PHASE_ID, (player, level, hand, hitResult) ->
                onItemUse(level, player, hand, hitResult)
            );




            // Register item display fix
            ServerEntityEvents.ENTITY_LOAD.addPhaseOrdering(FrameworkLib.PHASE_ID, PHASE_ID);
            ServerEntityEvents.ENTITY_LOAD.register(PHASE_ID, (entity, level) ->
                ProductItemDisplayElm.onEntityLoad_item(entity)
            );




            // Log initialization
            LOGGER.info("FancyPlayerShops initialized. :3");
        });
    }








    /**
     * Callback for item use events.
     * <p> Checks if the held item is a product display item. If it is, it spawns a new product display at the targeted location.
     * @param level The level.
     * @param player The player that clicked.
     * @param hand The hand used.
     * @param hitResult The hit result of the click action.
     * @return FAIL if the player tried to place a product display, PASS otherwise.
     */
    public static @NotNull InteractionResult onItemUse(final @NotNull Level level, final @NotNull Player player, final @NotNull InteractionHand hand, final @NotNull BlockHitResult hitResult) {
        final ItemStack stack = player.getItemInHand(hand);
        if(stack != null && stack.is(Items.PLAYER_HEAD) && MinecraftUtils.hasTag(stack, ProductDisplay_Manager.DISPLAY_ITEM_NBT_KEY)) {

            // If the level is a server level and the player is allowed to modify the level
            if(level instanceof final ServerLevel serverLevel && player.getAbilities().mayBuild) {
                int newCount = stack.getCount();

                // Calculate block position and create the new display if no other display is already there. Send a feedback message to the player
                final BlockPos blockPos = hitResult.getBlockPos().offset(hitResult.getDirection().getNormal());
                final CompoundTag tag = stack.getTag();
                final UUID displayUUID = new ProductDisplay_Key(blockPos, level).getUUID();
                if(ProductDisplay_Manager.REF.get(displayUUID) == null) {

                    // Spawn snapshot if the item has the snapshot tag
                    if(MinecraftUtils.hasTag(stack, ProductDisplay_Manager.SNAPSHOT_NBT_KEY)) {
                        final CompoundTag data = tag.getCompound(MOD_ID + ".snapshot_data");
                        if(data.getUUID("owner").equals(player.getUUID())) {
                            ((ProductDisplay_Serializer)ProductDisplay_Manager.REF.getSerializer()).deserialize(data.getString("product_display_data"), serverLevel, blockPos);
                            player.displayClientMessage(new Txt("Display snapshot restored").lightGray().bold().get(), true);
                            if(!player.getAbilities().instabuild) --newCount;
                        }
                        else {
                            player.displayClientMessage(new Txt("This product display belongs to " + data.getString("owner_name") + "! Only they can place it").red().bold().get(), true);
                        }
                    }

                    // Spawn empty product display otherwise
                    else {
                        final double angleRadians = Math.atan2(-(player.getZ() - blockPos.getZ()), player.getX() - blockPos.getX()) + Math.PI / 2;
                        final DisplayTier tier = DisplayTier.fromNumericalId(tag.getInt("tier"));
                        final ProductDisplay display = new ProductDisplay(
                            /* ownerUUID   */ player.getUUID(),
                            /* shopUUID    */ Shop_Manager.DEFAULT_SHOP_UUID,
                            /* price       */ 1000l,
                            /* stock       */ 0,
                            /* maxStock    */ tier.getCapacity(),
                            /* direction   */ Direction.fromRadians(angleRadians),
                            /* hue         */ Configs.getDisplay().theme_hues.getValue()[Configs.getDisplay().theme.getDefault()],
                            /* balance     */ 0l,
                            /* nbtFilter   */ true,
                            /* position    */ blockPos,
                            /* level       */ serverLevel,
                            /* tier        */ tier,
                            /* item        */ Items.AIR.getDefaultInstance(),
                            /* storedItems */ new HashMap<>()
                        );
                        player.displayClientMessage(new Txt("New product display created. Right click it to configure").lightGray().bold().get(), true);
                        if(!player.getAbilities().instabuild) --newCount;
                        DisplayEvents.DISPLAY_CREATED.invoker().onDisplayCreate(display, DisplayCreationReason.NEW);
                    }
                }

                // Update the held item
                final ItemStack newStack = stack.copyWithCount(newCount);
                player.setItemInHand(hand, newStack);
                NetworkUtils.sendClientSlotUpdate(player, player.getInventory().selected + 36, newStack);
            }

            // If not, send an error message to the player
            else player.displayClientMessage(new Txt("You cannot place a product display here!").red().bold().get(), true);
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }
}