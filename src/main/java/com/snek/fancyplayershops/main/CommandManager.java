package com.snek.fancyplayershops.main;

import org.joml.Vector3d;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.graphics.hud._mainmenu_._MainMenuCanvas;
import com.snek.fancyplayershops.graphics.hud.mainmenu.ManageShopsCanvas;
import com.snek.frameworklib.graphics.core.Context;
import com.snek.frameworklib.graphics.core.HudContext;
import com.snek.frameworklib.utils.Txt;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;










/**
 * A utility class that registers and handles in-game commands.
 */
public abstract class CommandManager {
    private CommandManager() {}




    public static final Component HELP_TEXT_SHOP = new Txt()
        .cat(new Txt("/shop").bold().italic().lightGray())
        .cat(new Txt(": Open the main menu of FancyPlayerShops. From there, you will be able to view your shops, claim balances and access your stash").italic().gray())
    .get();

        public static final Component HELP_TEXT_SHOP_OP = new Txt()
            .cat(new Txt("/shop op").bold().italic().lightGray())
            .cat(new Txt(": A collection of shop management commands only available to server operators.").italic().lightGray())
        .get();

        public static final Component HELP_TEXT_SHOP_BULK = new Txt()
            .cat(new Txt("/shop bulk").bold().italic().lightGray())
            .cat(new Txt(": A collection of shop bulk management commands only available to server operators.").italic().lightGray())
        .get();

            public static final Component HELP_TEXT_SHOP_BULK_FILL = new Txt()
                .cat(new Txt("/shop bulk fill <radius>").bold().italic().lightGray())
                .cat(new Txt(": Create randomized product displays in every block within a specified radius. This is meant for testing.").italic().lightGray())
            .get();

            public static final Component HELP_TEXT_SHOP_BULK_PURGE = new Txt()
                .cat(new Txt("/shop bulk purge <radius>").bold().italic().lightGray())
                .cat(new Txt(": Remove all product displays within a specified radius. The stock and balance of deleted displays are automatically sent to their owner.").italic().lightGray())
            .get();

            public static final Component HELP_TEXT_SHOP_BULK_DISPLACE = new Txt()
                .cat(new Txt("/shop bulk displace <radius>").bold().italic().lightGray())
                .cat(new Txt(": Converts all product displays within a specified radius into their item form. The display snapshots are automatically sent to their owner.").italic().lightGray())
            .get();
        ;

        public static final Component HELP_TEXT_SHOP_CLOSEHUD = new Txt()
            .cat(new Txt("/shop close-hud").bold().italic().lightGray())
            .cat(new Txt(": Forcibly close any currently open HUD.").italic().lightGray())
        .get();

        public static final Component HELP_TEXT_SHOP_CLAIM = new Txt()
            .cat(new Txt("/shop claim").bold().italic().lightGray())
            .cat(new Txt(": Claim all of your shops' balances.").italic().lightGray())
        .get();
    ;







    /**
     * Registers the /shop command
     */
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {


            // Shop main menu
            dispatcher.register(LiteralArgumentBuilder.<CommandSourceStack>literal("shop")
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("help")
                .executes(context -> {
                    final ServerPlayer player = context.getSource().getPlayer();
                    player.displayClientMessage(HELP_TEXT_SHOP, false);
                    return 1;
                }))
                .executes(context -> {
                    final ServerPlayer player = context.getSource().getPlayer();
                    final Vec3 pos = player.getPosition(1f);
                    final HudContext hud = new HudContext(player);
                    hud.spawn(new Vector3d(pos.x, pos.y, pos.z), true);
                    hud.changeCanvas(new _MainMenuCanvas(hud));
                    return 1;
                })




                // Balance claim
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("claim")
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("help")
                    .executes(context -> {
                        final ServerPlayer player = context.getSource().getPlayer();
                        player.displayClientMessage(HELP_TEXT_SHOP_CLAIM, false);
                        return 1;
                    }))
                    .executes(context -> {
                        final ServerPlayer player = context.getSource().getPlayer();
                        // BalanceManager.claim(player);
                        //FIXME claim all the groups
                        return 1;
                    })
                )




                // Force close HUD
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("close-hud")
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("help")
                    .executes(context -> {
                        final ServerPlayer player = context.getSource().getPlayer();
                        player.displayClientMessage(HELP_TEXT_SHOP_CLOSEHUD, false);
                        return 1;
                    }))
                    .executes(context -> {
                        final ServerPlayer player = context.getSource().getPlayer();
                        Context.closeContexts(player);
                        return 1;
                    })
                )




                // Operator commands
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("op")
                .requires(source -> source.hasPermission(2))
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("help")
                    .executes(context -> {
                        final ServerPlayer player = context.getSource().getPlayer();
                        player.displayClientMessage(HELP_TEXT_SHOP_OP, false);
                        return 1;
                    }))


                    // Item give command
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("give")
                    .executes(context -> {
                        final ServerPlayer player = context.getSource().getPlayer();
                        player.getInventory().add(ProductDisplayManager.getProductDisplayItemCopy());
                        return 1;
                    }))
                )




                // Operator bulk commands
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("bulk")
                .requires(source -> source.hasPermission(2))
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("help")
                    .executes(context -> {
                        final ServerPlayer player = context.getSource().getPlayer();
                        player.displayClientMessage(HELP_TEXT_SHOP_BULK, false);
                        return 1;
                    }))


                    // Purge command
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("purge")
                        .then(LiteralArgumentBuilder.<CommandSourceStack>literal("help")
                        .executes(context -> {
                            final ServerPlayer player = context.getSource().getPlayer();
                            player.displayClientMessage(HELP_TEXT_SHOP_BULK_PURGE, false);
                            return 1;
                        }))
                        .then(RequiredArgumentBuilder.<CommandSourceStack, Float>argument("radius", FloatArgumentType.floatArg(0.1f))
                        .executes(context -> {
                            final ServerPlayer player = context.getSource().getPlayer();
                            final float radius = FloatArgumentType.getFloat(context, "radius");
                            final int n = ProductDisplay_BulkOperations.purge((ServerLevel)player.level(), player.getPosition(1f).toVector3f(), radius);
                            player.displayClientMessage(new Txt("Purged " + n + " shops").get(), false);
                            return 1;
                        }))
                    )


                    // Displace command
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("displace")
                        .then(LiteralArgumentBuilder.<CommandSourceStack>literal("help")
                        .executes(context -> {
                            final ServerPlayer player = context.getSource().getPlayer();
                            player.displayClientMessage(HELP_TEXT_SHOP_BULK_DISPLACE, false);
                            return 1;
                        }))
                        .then(RequiredArgumentBuilder.<CommandSourceStack, Float>argument("radius", FloatArgumentType.floatArg(0.1f))
                        .executes(context -> {
                            final ServerPlayer player = context.getSource().getPlayer();
                            final float radius = FloatArgumentType.getFloat(context, "radius");
                            final int n = ProductDisplay_BulkOperations.displace((ServerLevel)player.level(), player.getPosition(1f).toVector3f(), radius);
                            player.displayClientMessage(new Txt("Converted " + n + " shops into items").get(), false);
                            return 1;
                        }))
                    )


                    // Fill command
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("fill")
                        .then(LiteralArgumentBuilder.<CommandSourceStack>literal("help")
                        .executes(context -> {
                            final ServerPlayer player = context.getSource().getPlayer();
                            player.displayClientMessage(HELP_TEXT_SHOP_BULK_FILL, false);
                            return 1;
                        }))
                        .then(RequiredArgumentBuilder.<CommandSourceStack, Float>argument("radius", FloatArgumentType.floatArg(0.1f, 10f))
                        .executes(context -> {
                            final ServerPlayer player = context.getSource().getPlayer();
                            final float radius = FloatArgumentType.getFloat(context, "radius");
                            final int n = ProductDisplay_BulkOperations.fill((ServerLevel)player.level(), player.getPosition(1f).toVector3f(), radius, player);
                            player.displayClientMessage(new Txt("Created " + n + " shops").get(), false);
                            return 1;
                        })
                    )
                ))
            );
        });
    }

    //TODO add /shop op view stash <playerName|playerUUID>
    //TODO add /shop op view balance <playerName|playerUUID>
    //! ^ HUD can't delete or edit the items. use /shop op void to delete stuff

    //TODO add /shop op void balance <playerName|playerUUID>
    //TODO add /shop op void stash <playerName|playerUUID>
    //TODO add /shop op void shops <playerName|playerUUID>
    //TODO add /shop op void group <playerName|playerUUID>  <groupName>
    //! ^ force /shop op confirm
    //! ^ "are you sure you want to delete 37,482 shops? This action cannot be undone. Use /shop op confirm to confirm"
    //FIXME LOG ALL OF THESE ACTIONS. THE OWNER SHOULD BE ABLE TO SEE THIS FROM MAIN-MENU > RECENT ACTIONS

    //TODO add /shop op collect balance <playerName|playerUUID>
    //TODO add /shop op collect stash <playerName|playerUUID>
    //TODO add /shop op collect shops <playerName|playerUUID>
    //TODO add /shop op collect group <playerName|playerUUID>


    //FIXME add suggestions for all of the completable commands

    //FIXME add "@Nullable Player operator" parameter to shop manager / shop methods
    //FIXME This is for operators that opened a player's stash or other view. it should stop them from modifying it, or add specific buttons for that





    //TODO add /shop op save-all
    //! ^ Saves all of the data instantly, skipping configured save cooldowns









    //TODO add a command that lets owners transfer all of the shop blocks in a radius to the specified shop.








    //TODO add a likes/score system to shop groups
    //TODO maybe rename "groups" to "shops" and shop items to something else?
}
