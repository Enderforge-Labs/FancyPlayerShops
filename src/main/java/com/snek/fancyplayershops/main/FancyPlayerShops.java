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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snek.fancyplayershops.ui.InteractionBlocker;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.utils.MinecraftUtils;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.scheduler.Scheduler;








/**
 * The main class of the mod FancyPlayerShops.
 */
public class FancyPlayerShops implements ModInitializer {

    // Server instance
    private static @Nullable MinecraftServer serverInstance = null;
    public static @Nullable MinecraftServer getServer() { return serverInstance; }

    // Mod ID and console logger
    public static final String MOD_ID = "fancyplayershops";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    // Shop item data
    //! Don't use the name or tooltip to check the item. Shops should work even when renamed in an anvil or modified by mods
    private static final @NotNull ItemStack shopItem;
    public static final String SHOP_ITEM_NBT_KEY = MOD_ID + ".item.shop_item";

    // Shop item texture
    public static final String SHOP_ITEM_TEXTURE =
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZj" +
        "I3ODQzMDdiODkyZjUyYjkyZjc0ZmE5ZGI0OTg0YzRmMGYwMmViODFjNjc1MmU1ZWJhNjlhZDY3ODU4NDI3ZSJ9fX0="
        //! Idk why this is on 2 lines. I like being able to see all of it
    ;

    // Shop item name
    public static final Vector3i SHOP_ITEM_NAME_COLOR = new Vector3i(140, 190, 160);
    public static final Text   SHOP_ITEM_NAME =
        new Txt("Item Shop").noItalic().bold().color(SHOP_ITEM_NAME_COLOR) //FIXME specify sold item name in shop snapshots
    .get();

    // Shop item description
    public static final Vector3i SHOP_ITEM_DESCRITPION_COLOR = new Vector3i(180, 230, 200);
    public static final Text[] SHOP_ITEM_DESCRITPION = {
        new Txt().cat(new Txt("A ").white()).cat(new Txt("shop").color(SHOP_ITEM_DESCRITPION_COLOR)).cat(new Txt(" that allows you to sell items to other players.").white()).noItalic().get(),
        new Txt().cat(new Txt("Place this anywhere and ").white()).cat(new Txt("right click").color(SHOP_ITEM_DESCRITPION_COLOR)).cat(new Txt(" it to get started!").white()).noItalic().get(),
        new Txt("").noItalic().get()
    };




    // Initialize shop item stack
    static {

        // Create item and set custom name
        shopItem = MinecraftUtils.createCustomHead(SHOP_ITEM_TEXTURE);
        shopItem.setCustomName(SHOP_ITEM_NAME);

        // Set identification tag
        NbtCompound nbt = shopItem.getOrCreateNbt();
        nbt.putBoolean(SHOP_ITEM_NBT_KEY, true);

        // Set lore
        NbtList lore = new NbtList();
        for(Text line : SHOP_ITEM_DESCRITPION) {
            lore.add(NbtString.of(Text.Serializer.toJson(line)));
        }
        shopItem.getOrCreateSubNbt("display").put("Lore", lore);
    }








    @Override
    public void onInitialize() {
        ShopCommand.register();




        ServerLifecycleEvents.SERVER_STARTED.register(server -> {

            // Save server instance
            serverInstance = server;

            // Load shop data
            Shop.loadData();

            // Schedule UI element update loop
            Scheduler.loop(0, Elm.TRANSITION_REFRESH_TIME, Elm::processUpdateQueue);

            // Schedule focus features loop
            Scheduler.loop(0, 1, () -> FocusFeatures.tick(server.getWorlds()));

            // Log initialization success
            LOGGER.info("FancyPlayerShops initialized. :3");
        });




        // Create and register block click events (shop placement + prevents early clicks going through the shop)
        AttackBlockCallback.EVENT.register((player, world, hand, blockPos, direction) -> {
            ActionResult r;
            r = ClickFeatures.onClickBlock(world, player, hand, ClickType.LEFT, blockPos.add(direction.getVector()));
            return r;
        });
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            ActionResult r;
            r = ClickFeatures.onClickBlock(world, player, hand, ClickType.RIGHT, hitResult.getBlockPos().add(hitResult.getSide().getVector()));
            if(r == ActionResult.PASS) r = onItemUse(world, player, hand, hitResult);
            return r;
        });




        // Create and register entity click events (interaction blocker clicks)
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            return ClickFeatures.onClickEntity(world, player, hand, ClickType.LEFT, entity);
        });
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            return ClickFeatures.onClickEntity(world, player, hand, ClickType.RIGHT, entity);
        });




        // Create and register item use events (prevents early clicks going through the shop)
        UseItemCallback.EVENT.register((player, world, hand) -> {
            final ActionResult r = ClickFeatures.onUseItem(world, player, hand);
            if(r == ActionResult.FAIL) return TypedActionResult.fail(player.getStackInHand(hand));
            /**/                  else return TypedActionResult.pass(player.getStackInHand(hand));
        });




        // Register scheduler
        ServerTickEvents.END_SERVER_TICK.register(server -> { Scheduler.tick(); });


        // Register entity display purge
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            Elm.onEntityLoad(entity);
            InteractionBlocker.onEntityLoad(entity);
        });




        // Register chat input handler
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
            return !ChatInput.onMessage(message, sender);
        });
    }








    /**
     * Callback for item use events.
     * Checks if the held item is a shop item. If it is, it spawns a new shop at the targeted location.
     * @param world The world.
     * @param player The player that clicked.
     * @param hand The hand used.
     * @param hitResult The hit result of the click action.
     * @return SUCCESS if the player tried to place a shop, PASS otherwise.
     */
    public static ActionResult onItemUse(World world, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getStackInHand(hand);
        if(stack.getItem() == Items.PLAYER_HEAD && stack.hasNbt() && stack.getNbt().contains(SHOP_ITEM_NBT_KEY)) {

            // If the world is a server world and the player is allowed to modify the world
            if(world instanceof ServerWorld serverWorld && player.getAbilities().allowModifyWorld) {

                // Remove item if the player is not in creative mode
                if(!player.getAbilities().creativeMode) stack.setCount(stack.getCount() - 1);

                // Calculate block position and create the new shop if no other shop is already there. Send a feedback message to the player
                BlockPos blockPos = hitResult.getBlockPos().add(hitResult.getSide().getVector());
                if(Shop.findShop(blockPos, world) == null) {
                    new Shop(serverWorld, blockPos, player);
                    player.sendMessage(new Txt("New shop created! Right click it to configure.").green().get(), true);
                }
            }

            // If not, send an error message to the player
            else player.sendMessage(new Txt("You cannot create a shop here!").darkRed().get(), true);
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }








    public static final @NotNull ItemStack getShopItemCopy() {
        return shopItem.copy();
    }
    public static final @NotNull ItemStack createShopSnapshot() {
        return shopItem.copy(); //FIXME actually copy the shop data
    }
}