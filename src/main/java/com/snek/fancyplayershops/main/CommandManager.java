package com.snek.fancyplayershops.main;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.data.StashManager;
import com.snek.fancyplayershops.graphics.hud.main_menu.MainMenuCanvas;
import com.snek.frameworklib.graphics.core.Context;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.utils.Txt;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;










/**
 * A utility class that registers and handles in-game commands.
 */
@SuppressWarnings("java:S1700") //! Bad member names
public abstract class CommandManager {
    private CommandManager() {}




    private static class HelpText {
        private static final String[] SHOP = {
            "/shop",
            "Open the main menu of FancyPlayerShops. From there, you will be able to view your shops, claim balances and access your stash"
        };


        private static class Op {
            private static final String[] OP = {
                "/shop op",
                "A collection of shop management commands only available to server operators."
            };

            private static final String[] SAVE_ALL = {
                "/shop op save-all",
                "Save any queued data instantly, skipping configured save cooldowns."
            };

            private static final String[] FORCE_RESTOCK = {
                "/shop op force-restock",
                "Forcefully restocks all active displays, skipping configured cooldowns."
            };


            private static class Bulk {
                private static final String[] BULK = {
                    "/shop op bulk",
                    "A collection of shop bulk management commands only available to server operators."
                };

                private static final String[] FILL = {
                    "/shop op bulk fill <radius>",
                    "Create randomized product displays in every block within a specified radius. This is meant for testing."
                };

                private static final String[] PURGE = {
                    "/shop op bulk purge <radius>",
                    "Remove all product displays within a specified radius. " +
                    "The stock and balance of deleted displays are automatically sent to their owner."
                };

                private static final String[] DISPLACE = {
                    "/shop op bulk displace <radius>",
                    "Convert all product displays within a specified radius into their item form. " +
                    "The display snapshots are automatically sent to their owner."
                };
            }
        }


        private static class Bulk {
            private static final String[] BULK = {
                "/shop bulk",
                "A collection of shop bulk management commands. " +
                "These only affect displays you own."
            };

            private static final String[] PURGE = {
                "/shop bulk purge <radius>",
                "Remove all product displays within a specified radius. " +
                "The stock and balance of deleted displays are automatically sent to you. " +
                "This only affects displays you own"
            };

            private static final String[] DISPLACE = {
                "/shop bulk displace <radius>",
                "Convert all product displays within a specified radius into their item form. " +
                "The display snapshots are automatically sent to you. " +
                "This only affects displays you own"
            };

            private static final String[] TRANSFER = {
                "/shop bulk transfer <player> <radius>",
                "Transfer all product displays within a specified radius to another player. " +
                "This only affects displays you own"
            };

            private static final String[] MOVE = {
                "/shop bulk move <shop> <radius>",
                "Move all product displays within a specified radius to another shop. " +
                "This only affects displays you own"
            };
        }


        private static final String[] CLOSEHUD = {
            "/shop close-hud",
            "Forcibly close any currently open HUD."
        };


        private static final String[] CLAIM = {
            "/shop claim",
            "Claim all of your shops' balances."
        };
    }








    /**
     * Registers the /shop command
     */
    public static void register() { CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {


        // Shop main menu
        dispatcher.register(Commands.literal("shop")
            .then(Commands.literal("help")
                .executes(context -> executeSendHelpMessage(context, HelpText.SHOP))
            )
            .executes(CommandManager::executeOpenMainMenu)


            // Balance claim
            .then(Commands.literal("claim")
                .then(Commands.literal("help")
                    .executes(context -> executeSendHelpMessage(context, HelpText.CLAIM))
                )
                .executes(CommandManager::executeClaim)
            )


            // Force close HUD
            .then(Commands.literal("close-hud")
                .then(Commands.literal("help")
                    .executes(context -> executeSendHelpMessage(context, HelpText.CLOSEHUD))
                )
                .executes(CommandManager::executeCloseHud)
            )


            // Operator commands
            .then(Commands.literal("op")
            .requires(source -> source.hasPermission(2))
                .then(Commands.literal("help")
                    .executes(context -> executeSendHelpMessage(context, HelpText.Op.OP))
                )
                .then(Commands.literal("save-all")
                    .then(Commands.literal("help")
                        .executes(context -> executeSendHelpMessage(context, HelpText.Op.SAVE_ALL))
                    )
                    .executes(CommandManager::executeSaveAll)
                )
                .then(Commands.literal("force-restock")
                    .then(Commands.literal("help")
                        .executes(context -> executeSendHelpMessage(context, HelpText.Op.FORCE_RESTOCK))
                    )
                    .executes(CommandManager::executeForceRestock)
                )
                .then(Commands.literal("bulk")
                .requires(source -> source.hasPermission(2))
                    .then(Commands.literal("help")
                        .executes(context -> executeSendHelpMessage(context, HelpText.Op.Bulk.BULK))
                    )
                    .then(Commands.literal("purge")
                        .then(Commands.literal("help")
                            .executes(context -> executeSendHelpMessage(context, HelpText.Op.Bulk.PURGE))
                        )
                        .then(Commands.argument("radius", FloatArgumentType.floatArg(0.1f))
                            .executes(CommandManager::executeBulkPurge)
                        )
                    )
                    .then(Commands.literal("displace")
                        .then(Commands.literal("help")
                            .executes(context -> executeSendHelpMessage(context, HelpText.Op.Bulk.DISPLACE))
                        )
                        .then(Commands.argument("radius", FloatArgumentType.floatArg(0.1f))
                            .executes(CommandManager::executeBulkDisplace)
                        )
                    )
                    .then(Commands.literal("fill")
                        .then(Commands.literal("help")
                            .executes(context -> executeSendHelpMessage(context, HelpText.Op.Bulk.FILL))
                        )
                        .then(Commands.argument("radius", FloatArgumentType.floatArg(0.1f, 10f))
                            .executes(CommandManager::executeBulkFill)
                        )
                    )
                )
                .then(Commands.literal("give")
                    .then(Commands.literal("t1")
                        .executes(context -> executeGiveDisplayItem(context, DisplayTier.T1, 1L))
                        .then(Commands.argument("amount", LongArgumentType.longArg(1L, 10000L))
                            .executes(context -> executeGiveDisplayItem(context, DisplayTier.T1, LongArgumentType.getLong(context, "amount")))
                        )
                    )
                    .then(Commands.literal("t2")
                        .executes(context -> executeGiveDisplayItem(context, DisplayTier.T2, 1L))
                        .then(Commands.argument("amount", LongArgumentType.longArg(1L, 10000L))
                            .executes(context -> executeGiveDisplayItem(context, DisplayTier.T2, LongArgumentType.getLong(context, "amount")))
                        )
                    )
                    .then(Commands.literal("t3")
                        .executes(context -> executeGiveDisplayItem(context, DisplayTier.T3, 1L))
                        .then(Commands.argument("amount", LongArgumentType.longArg(1L, 10000L))
                            .executes(context -> executeGiveDisplayItem(context, DisplayTier.T3, LongArgumentType.getLong(context, "amount")))
                        )
                    )
                    .then(Commands.literal("t4")
                        .executes(context -> executeGiveDisplayItem(context, DisplayTier.T4, 1L))
                        .then(Commands.argument("amount", LongArgumentType.longArg(1L, 10000L))
                            .executes(context -> executeGiveDisplayItem(context, DisplayTier.T4, LongArgumentType.getLong(context, "amount")))
                        )
                    )
                    .then(Commands.literal("t5")
                        .executes(context -> executeGiveDisplayItem(context, DisplayTier.T5, 1L))
                        .then(Commands.argument("amount", LongArgumentType.longArg(1L, 10000L))
                            .executes(context -> executeGiveDisplayItem(context, DisplayTier.T5, LongArgumentType.getLong(context, "amount")))
                        )
                    )
                    .then(Commands.literal("creative")
                        .executes(context -> executeGiveDisplayItem(context, DisplayTier.CREATIVE, 1L))
                        .then(Commands.argument("amount", LongArgumentType.longArg(1L, 10000L))
                            .executes(context -> executeGiveDisplayItem(context, DisplayTier.CREATIVE, LongArgumentType.getLong(context, "amount")))
                        )
                    )
                    .then(Commands.literal("all")
                        .executes(context -> executeGiveAllDisplayItems(context, 1L))
                        .then(Commands.argument("amount", LongArgumentType.longArg(1L, 10000L))
                            .executes(context -> executeGiveAllDisplayItems(context, LongArgumentType.getLong(context, "amount")))
                        )
                    )
                )
            )


            // Operator bulk commands
            .then(Commands.literal("bulk")
            // .requires(source -> source.hasPermission(2))
            //     .then(Commands.literal("help")
            //         .executes(context -> executeSendHelpMessage(context, HelpText.Op.Bulk.BULK))
            //     )
            //     .then(Commands.literal("purge")
            //         .then(Commands.literal("help")
            //             .executes(context -> executeSendHelpMessage(context, HelpText.Bulk.PURGE))
            //         )
            //         .then(Commands.argument("radius", FloatArgumentType.floatArg(0.1f))
            //             .executes(CommandManager::executeBulkPurge)
            //         )
            //     )
            //     .then(Commands.literal("displace")
            //         .then(Commands.literal("help")
            //             .executes(context -> executeSendHelpMessage(context, HelpText.Bulk.DISPLACE))
            //         )
            //         .then(Commands.argument("radius", FloatArgumentType.floatArg(0.1f))
            //             .executes(CommandManager::executeBulkDisplace)
            //         )
            //     )
            //TODO transfer
            //TODO move
            )
        );
    }); }

    //TODO add /shop op view stash <playerName|playerUUID>
    //TODO add /shop op view balance <playerName|playerUUID>
    //! ^ HUD can't delete or edit the items. use /shop op void to delete stuff

    //TODO add /shop op void balance <playerName|playerUUID>
    //TODO add /shop op void stash <playerName|playerUUID>
    //TODO add /shop op void shops <playerName|playerUUID>
    //TODO add /shop op void shop <playerName|playerUUID>  <shopName>
    //! ^ force /shop op confirm
    //! ^ "are you sure you want to delete 37,482 shops? This action cannot be undone. Use /shop op confirm to confirm"
    //FIXME LOG ALL OF THESE ACTIONS. THE OWNER SHOULD BE ABLE TO SEE THIS FROM MAIN-MENU > RECENT ACTIONS

    //TODO add /shop op collect balance <playerName|playerUUID>
    //TODO add /shop op collect stash <playerName|playerUUID>
    //TODO add /shop op collect shops <playerName|playerUUID>
    //TODO add /shop op collect shop <playerName|playerUUID> <shopName>


    //FIXME add suggestions for all of the completable commands

    //FIXME add "@Nullable Player operator" parameter to shop manager / shop methods
    //FIXME This is for operators that opened a player's stash or other view. it should stop them from modifying it, or add specific buttons for that





    //TODO add a command that lets owners transfer all of the shop blocks in a radius to the specified shop.


    //TODO add a likes/score system to shops






    public static int executeForceRestock(final @NotNull CommandContext<CommandSourceStack> context) {
        ProductDisplayManager.forcePullItems();
        return 1;
    }


    public static int executeSaveAll(final @NotNull CommandContext<CommandSourceStack> context) {
        StashManager.saveScheduledStashes();
        ProductDisplayManager.saveScheduledDisplays();
        ShopManager.saveScheduledShops();
        return 1;
    }


    public static int executeOpenMainMenu(final @NotNull CommandContext<CommandSourceStack> context) {
        final ServerPlayer player = context.getSource().getPlayer();
        final Vec3 pos = player.getPosition(1f);
        final HudContext hud = new HudContext(player);
        hud.spawn(new Vector3d(pos.x, pos.y, pos.z), true);
        hud.changeCanvas(new MainMenuCanvas(hud));
        return 1;
    }


    public static int executeClaim(final @NotNull CommandContext<CommandSourceStack> context) {
        final ServerPlayer player = context.getSource().getPlayer();
        // BalanceManager.claim(player);
        //FIXME claim all the shops
        return 1;
    }


    public static int executeCloseHud(final @NotNull CommandContext<CommandSourceStack> context) {
        final ServerPlayer player = context.getSource().getPlayer();
        Context.closeContexts(player);
        return 1;
    }


    public static int executeGiveAllDisplayItems(final @NotNull CommandContext<CommandSourceStack> context, final long count) {
        for(final var tier : DisplayTier.values()) {
            executeGiveDisplayItem(context, tier, count);
        }
        return 1;
    }


    public static int executeGiveDisplayItem(final @NotNull CommandContext<CommandSourceStack> context, final @NotNull DisplayTier tier, final long count) {
        final ServerPlayer player = context.getSource().getPlayer();
        StashManager.giveItem(player.getUUID(), ProductDisplayManager.getProductDisplayItemCopy(tier), count, true);
        return 1;
    }


    public static int executeBulkPurge(final @NotNull CommandContext<CommandSourceStack> context) {
        final ServerPlayer player = context.getSource().getPlayer();
        final float radius = FloatArgumentType.getFloat(context, "radius");
        final int n = ProductDisplay_BulkOperations.purge((ServerLevel)player.level(), player.getPosition(1f).toVector3f(), radius);
        player.displayClientMessage(new Txt("Purged " + n + " shops").get(), false);
        return 1;
    }


    public static int executeBulkDisplace(final @NotNull CommandContext<CommandSourceStack> context) {
        final ServerPlayer player = context.getSource().getPlayer();
        final float radius = FloatArgumentType.getFloat(context, "radius");
        final int n = ProductDisplay_BulkOperations.displace((ServerLevel)player.level(), player.getPosition(1f).toVector3f(), radius);
        player.displayClientMessage(new Txt("Converted " + n + " shops into items").get(), false);
        return 1;
    }


    public static int executeBulkFill(final @NotNull CommandContext<CommandSourceStack> context) {
        final ServerPlayer player = context.getSource().getPlayer();
        final float radius = FloatArgumentType.getFloat(context, "radius");
        final int n = ProductDisplay_BulkOperations.fill((ServerLevel)player.level(), player.getPosition(1f).toVector3f(), radius, player);
        player.displayClientMessage(new Txt("Created " + n + " shops").get(), false);
        return 1;
    }


    public static int executeSendHelpMessage(final @NotNull CommandContext<CommandSourceStack> context, final String[] message) {
        final ServerPlayer player = context.getSource().getPlayer();
        player.displayClientMessage(new Txt()
            .cat(new Txt(message[0]).bold().italic().lightGray())
            .cat(new Txt(": " + message[1]).italic().lightGray())
        .get(), false);
        return 1;
    }
}
