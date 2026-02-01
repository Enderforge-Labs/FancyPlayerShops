package com.snek.fancyplayershops.main;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.commands.arguments.EntityArgument;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.snek.fancyplayershops.configs.Configs;
import com.snek.fancyplayershops.data.shop.Shop_Manager;
import com.snek.fancyplayershops.data.stash.StashManager;
import com.snek.fancyplayershops.data.display.DisplayTier;
import com.snek.fancyplayershops.data.display.ProductDisplay_Manager;
import com.snek.fancyplayershops.graphics.hud.main_menu.MainMenuCanvas;
import com.snek.frameworklib.data_types.containers.Option;
import com.snek.frameworklib.data_types.containers.Pair;
import com.snek.frameworklib.graphics.core.Context;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.common.Utils;
import com.snek.frameworklib.utils.common.Utils.DurationLabelType;
import com.snek.frameworklib.utils.scheduler.Scheduler;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;










/**
 * A utility class that registers and handles in-game commands.
 */
@SuppressWarnings("java:S1700") //! Bad member names
public abstract class CommandManager {
    private CommandManager() {}
    private static final @NotNull Map<@NotNull UUID, @Nullable Pair<Runnable, Long>> awaitingConfirmation = new HashMap<>();


    /**
     * Stores a function and calls it when the player runs the command /shop confirm.
     * Only each player's last stored function is executed.
     * @param player The player.
     * @param warningMessage The warning message to show the player.
     * @param function The function to store.
     *     The full message sent to them is "&ltwarningMessage&gt. Run "/shop confirm" to continue.".
     */
    public static void requireConfirmation(final @NotNull Player player, final @NotNull String warningMessage, final @NotNull Runnable function) {
        awaitingConfirmation.put(player.getUUID(), Pair.from(function, Scheduler.getTickNum()));
        final String durationStr = Utils.formatDuration(Configs.getMisc().confirm_timeout.getValue(), DurationLabelType.INITIAL);
        player.displayClientMessage(new Txt(warningMessage + ". Run \"/shop confirm\" within " + durationStr + " to continue.").yellow().get(), false);
    }


    /**
     * Confirms the last command sent by a player.
     * @param context The command context.
     * @return 1.
     */
    public static int confirm(final @NotNull CommandContext<CommandSourceStack> context) {
        final Player player = context.getSource().getPlayer();
        final var function = awaitingConfirmation.get(player.getUUID());
        if(function != null) {
            if(Scheduler.getTickNum() - function.getSecond() >= Configs.getMisc().confirm_timeout.getValue()) {
                player.displayClientMessage(new Txt("This command has expired!").lightGray().get(), false);
            }
            else {
                function.getFirst().run();
                awaitingConfirmation.remove(player.getUUID());
            }
        }
        else {
            player.displayClientMessage(new Txt("There is nothing to confirm!").lightGray().get(), false);
        }
        return 1;
    }








    private static class HelpText {
        private static final String[] _s = {
            "/shop",
            "Open the main menu of FancyPlayerShops. From there, you will be able to view your shops, claim balances and access your stash."
        };

        private static final String[] CONFIRM = {
            "/shop confirm",
            "Confirm the previous command. This is required by some commands that modify large amounts of displays or change important things."
        };

        private static final String[] CLAIM = {
            "/shop claim",
            "Claim all of your shops' balances."
        };

        private static final String[] CLOSEHUD = {
            "/shop close-hud",
            "Forcibly close any currently open HUD."
        };


        private static class Op {
            private static final String[] _s = {
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
                private static final String[] _s = {
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

                private static final String[] TRANSFER = {
                    "/shop bulk transfer <player> <radius>",
                    "Transfer all product displays within a specified radius to another player."
                };

                private static final String[] DISPLACE = {
                    "/shop op bulk displace <radius>",
                    "Convert all product displays within a specified radius into their item form. " +
                    "The display snapshots are automatically sent to their owner."
                };
            }
        }


        private static class Bulk {
            private static final String[] _s = {
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
    }








    /**
     * Registers the /shop command
     */
    public static void register() { CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {


        // Shop main menu
        dispatcher.register(Commands.literal("shop")
            .then(appendHelpText(HelpText._s))
            .executes(CommandManager::executeOpenMainMenu)


            // Command confirmation
            .then(Commands.literal("confirm")
                .then(appendHelpText(HelpText.CONFIRM))
                .executes(CommandManager::confirm)
            )


            // Balance claim
            .then(Commands.literal("claim")
                .then(appendHelpText(HelpText.CLAIM))
                .executes(CommandManager::executeClaim)
            )


            // Force close HUD
            .then(Commands.literal("close-hud")
                .then(appendHelpText(HelpText.CLOSEHUD))
                .executes(CommandManager::executeCloseHud)
            )


            // Operator commands
            .then(Commands.literal("op")
                .requires(source -> source.hasPermission(2))
                .then(appendHelpText(HelpText.Op._s))
                .then(Commands.literal("save-all")
                    .then(appendHelpText(HelpText.Op.SAVE_ALL))
                    .executes(CommandManager::executeSaveAll)
                )
                .then(Commands.literal("force-restock")
                    .then(appendHelpText(HelpText.Op.FORCE_RESTOCK))
                    .executes(CommandManager::executeForceRestock)
                )
                .then(Commands.literal("bulk")
                .requires(source -> source.hasPermission(2))
                    .then(appendHelpText(HelpText.Op.Bulk._s))
                    .then(Commands.literal("purge")
                        .then(appendHelpText(HelpText.Op.Bulk.PURGE))
                        .then(Commands.argument("radius", FloatArgumentType.floatArg(0.1f))
                            .executes(context -> executeBulkPurge(context, true))
                        )
                    )
                    .then(Commands.literal("displace")
                        .then(appendHelpText(HelpText.Op.Bulk.DISPLACE))
                        .then(Commands.argument("radius", FloatArgumentType.floatArg(0.1f))
                            .executes(context -> executeBulkDisplace(context, true))
                        )
                    )
                    .then(Commands.literal("transfer")
                        .then(appendHelpText(HelpText.Op.Bulk.TRANSFER))
                        .then(Commands.argument("newOwner", EntityArgument.player())
                        .then(Commands.argument("radius", FloatArgumentType.floatArg(0.1f))
                            .executes(context -> executeBulkTransfer(context, true))
                        ))
                    )
                    .then(Commands.literal("fill")
                        .then(appendHelpText(HelpText.Op.Bulk.FILL))
                        .then(Commands.argument("radius", FloatArgumentType.floatArg(0.1f, 10f))
                            .executes(CommandManager::executeBulkFill)
                        )
                    )
                )
                .then(((Supplier<LiteralArgumentBuilder<CommandSourceStack>>) () -> {
                    final var r = Commands.literal("give");
                    for(final var tier : DisplayTier.values()) {
                        r.then(Commands.literal(tier.name().toLowerCase())
                            .executes(context -> executeGiveDisplayItem(context, tier, 1L))
                            .then(Commands.argument("amount", LongArgumentType.longArg(1L, 100000L))
                                .executes(context -> executeGiveDisplayItem(context, tier, LongArgumentType.getLong(context, "amount")))
                            )
                        );
                    }
                    r.then(Commands.literal("all")
                        .executes(context -> executeGiveAllDisplayItems(context, 1L))
                        .then(Commands.argument("amount", LongArgumentType.longArg(1L, 100000L))
                            .executes(context -> executeGiveAllDisplayItems(context, LongArgumentType.getLong(context, "amount")))
                        )
                    );
                    return r;
                }).get())
            )


            // Bulk commands
            .then(Commands.literal("bulk")
            .requires(source -> source.hasPermission(2))
                    .then(appendHelpText(HelpText.Bulk._s))
                .then(Commands.literal("purge")
                    .then(appendHelpText(HelpText.Bulk.PURGE))
                    .then(Commands.argument("radius", FloatArgumentType.floatArg(0.1f))
                        .executes(context -> executeBulkPurge(context, false))
                    )
                )
                .then(Commands.literal("displace")
                    .then(appendHelpText(HelpText.Bulk.DISPLACE))
                    .then(Commands.argument("radius", FloatArgumentType.floatArg(0.1f))
                        .executes(context -> executeBulkDisplace(context, false))
                    )
                )
                .then(Commands.literal("transfer")
                    .then(appendHelpText(HelpText.Bulk.TRANSFER))
                    .then(Commands.argument("newOwner", EntityArgument.player())
                    .then(Commands.argument("radius", FloatArgumentType.floatArg(0.1f))
                        .executes(context -> executeBulkTransfer(context, false))
                    ))
                )
                .then(Commands.literal("move")
                    .then(appendHelpText(HelpText.Bulk.MOVE))
                    .then(Commands.argument("shopname", StringArgumentType.string())
                    .then(Commands.argument("radius", FloatArgumentType.floatArg(0.1f))
                        .executes(CommandManager::executeBulkMove)
                    ))
                )
            )
        );
    }); }


    private static @NotNull LiteralArgumentBuilder<CommandSourceStack> appendHelpText(final String[] message) {
        return Commands.literal("help").executes(context -> {
            final ServerPlayer player = context.getSource().getPlayer();
            player.displayClientMessage(new Txt()
                .cat(new Txt(message[0]).white())
                .cat(new Txt(": " + message[1]).lightGray())
            .get(), false);
            return 1;
        });
    }

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






    //TODO add a likes/score system to shops






    public static int executeForceRestock(final @NotNull CommandContext<CommandSourceStack> context) {
        final ServerPlayer player = context.getSource().getPlayer();
        ProductDisplay_Manager.forcePullItems();
        player.displayClientMessage(new Txt("Restocked all displays.").white().get(), false);
        return 1;
    }




    //TODO this command might become useless once everything uses the new system
    public static int executeSaveAll(final @NotNull CommandContext<CommandSourceStack> context) {
        final ServerPlayer player = context.getSource().getPlayer();
        StashManager.saveScheduledStashes();
        // ProductDisplayManager.REF.();
        // Shop_Manager.saveScheduledShops();
        player.displayClientMessage(new Txt("Saved shop data.").white().get(), false);
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
        //TODO feedback message
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
        StashManager.giveItem(player.getUUID(), ProductDisplay_Manager.getProductDisplayItemCopy(tier), count, true);
        return 1;
    }




    public static int executeBulkPurge(final @NotNull CommandContext<CommandSourceStack> context, final boolean admin) {
        final ServerPlayer player = context.getSource().getPlayer();
        final float radius = FloatArgumentType.getFloat(context, "radius");


        final var displays = ProductDisplay_BulkOperations.selectDisplays(
            Option.Some((ServerLevel)player.level()),
            Option.Some(Pair.from(player.getPosition(1f).toVector3f(), radius)),
            admin ? Option.None() : Option.Some(player)
        );
        requireConfirmation(
            player,
            "You are about to remove " + Utils.formatAmount(displays.size()) + " displays",
            () -> {
                final int n = ProductDisplay_BulkOperations.purge(displays, admin);
                player.displayClientMessage(new Txt("Purged " + n + " display" + (n > 1 ? "s" : "")).lightGray().get(), false);
            }
        );
        return 1;
    }




    public static int executeBulkDisplace(final @NotNull CommandContext<CommandSourceStack> context, final boolean admin) {
        final ServerPlayer player = context.getSource().getPlayer();
        final float radius = FloatArgumentType.getFloat(context, "radius");


        final var displays = ProductDisplay_BulkOperations.selectDisplays(
            Option.Some((ServerLevel)player.level()),
            Option.Some(Pair.from(player.getPosition(1f).toVector3f(), radius)),
            admin ? Option.None() : Option.Some(player)
        );
        requireConfirmation(
            player,
            "You are about to displace " + Utils.formatAmount(displays.size()) + " displays",
            () -> {
                final int n = ProductDisplay_BulkOperations.displace(displays, admin);
                player.displayClientMessage(new Txt("Converted " + n + " display" + (n > 1 ? "s" : "") + " into " + (n > 1 ? "items" : "an item")).lightGray().get(), false);
            }
        );
        return 1;
    }




    public static int executeBulkFill(final @NotNull CommandContext<CommandSourceStack> context) {
        final ServerPlayer player = context.getSource().getPlayer();
        final float radius = FloatArgumentType.getFloat(context, "radius");


        requireConfirmation(
            player,
            "You are about to fill the area around you with randomly generated displays",
            () -> {
                final int n = ProductDisplay_BulkOperations.fill(
                    (ServerLevel)player.level(),
                    player.getPosition(1f).toVector3f(),
                    radius,
                    player
                );
                player.displayClientMessage(new Txt("Created " + n + " display" + (n > 1 ? "s" : "")).lightGray().get(), false);
            }
        );
        return 1;
    }




    public static int executeBulkTransfer(final @NotNull CommandContext<CommandSourceStack> context, final boolean admin) {
        try {
            final ServerPlayer player = context.getSource().getPlayer();
            final ServerPlayer newOwner = EntityArgument.getPlayer(context, "newOwner");
            final float radius = FloatArgumentType.getFloat(context, "radius");


            final var displays = ProductDisplay_BulkOperations.selectDisplays(
                Option.Some((ServerLevel)player.level()),
                Option.Some(Pair.from(player.getPosition(1f).toVector3f(), radius)),
                admin ? Option.None() : Option.Some(player)
            );
            requireConfirmation(
                player,
                "You are about to transfer " + Utils.formatAmount(displays.size()) + " displays to " + newOwner.getName() + "?",
                () -> {
                    final int n = ProductDisplay_BulkOperations.transfer(displays, newOwner, admin);
                    player.displayClientMessage(new Txt("Transferred " + n + " display" + (n > 1 ? "s" : "")).lightGray().get(), false);
                }
            );
            return 1;
        }
        catch(CommandSyntaxException e) {
            context.getSource().sendFailure(new Txt("The specified player is not online!").get());
            return 0;
        }
    }




    public static int executeBulkMove(final @NotNull CommandContext<CommandSourceStack> context) {
        final ServerPlayer player = context.getSource().getPlayer();
        final String shopname = StringArgumentType.getString(context, "shopname");
        final float radius = FloatArgumentType.getFloat(context, "radius");


        final var validationResult = Shop_Manager.validateShopName(shopname);
        if(validationResult.isSome()) {
            context.getSource().sendFailure(new Txt(validationResult.unwrap() + "!").get());
            return 0;
        }
        else {
            final var displays = ProductDisplay_BulkOperations.selectDisplays(
                Option.Some((ServerLevel)player.level()),
                Option.Some(Pair.from(player.getPosition(1f).toVector3f(), radius)),
                Option.Some(player)
            );
            requireConfirmation(
                player,
                "You are about to move " + Utils.formatAmount(displays.size()) + " displays to \"" + shopname + "\"?",
                () -> {
                    final int n = ProductDisplay_BulkOperations.move(displays, shopname);
                    player.displayClientMessage(new Txt("Moved " + n + " display" + (n > 1 ? "s" : "")).lightGray().get(), false);
                }
            );
            return 1;
        }
    }
}
