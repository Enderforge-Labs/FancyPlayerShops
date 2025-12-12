package com.snek.fancyplayershops.main;

import org.joml.Vector3d;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.snek.fancyplayershops.data.BalanceManager;
import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.graphics.hud.mainmenu.MainMenuCanvas;
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
        .cat(new Txt("/shop").bold().italic().gray())
        .cat(new Txt(": Open the main menu of player shops. From there, you will be able to view your shops, claim balances and access your stash").italic().gray())
    .get();

    public static final Component HELP_TEXT_SHOP_OP = new Txt()
        .cat(new Txt("/shop op").bold().italic().gray())
        .cat(new Txt(": A collection of shop management commands only available to server operators.").italic().gray())
    .get();

    public static final Component HELP_TEXT_SHOP_BULK = new Txt()
        .cat(new Txt("/shop bulk").bold().italic().gray())
        .cat(new Txt(": A collection of shop bulk management commands only available to server operators.").italic().gray())
    .get();

    public static final Component HELP_TEXT_SHOP_CLOSEHUD = new Txt()
        .cat(new Txt("/shop close-hud").bold().italic().gray())
        .cat(new Txt(": Forcibly close any currently open HUD.").italic().gray())
    .get();

    public static final Component HELP_TEXT_SHOP_CLAIM = new Txt()
        .cat(new Txt("/shop claim").bold().italic().gray())
        .cat(new Txt(": Claim all of your shops' balances.").italic().gray())
    .get();








    /**
     * Registers the /shop command
     */
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(LiteralArgumentBuilder.<CommandSourceStack>literal("shop")
                // Shop main menu
                .executes(context -> {
                    final ServerPlayer player = context.getSource().getPlayer();
                    final Vec3 pos = player.getPosition(1f);
                    final HudContext hud = new HudContext(player);
                    hud.spawn(new Vector3d(pos.x, pos.y, pos.z), true);
                    hud.changeCanvas(new MainMenuCanvas(hud));
                    return 1;
                })
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("help")
                    .executes(context -> {
                        final ServerPlayer player = context.getSource().getPlayer();
                        player.displayClientMessage(HELP_TEXT_SHOP, false);
                        return 1;
                    })
                )




                // Balance claim
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("claim")
                    .executes(context -> {
                        final ServerPlayer player = context.getSource().getPlayer();
                        BalanceManager.claim(player);
                        return 1;
                    })
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("help")
                        .executes(context -> {
                            final ServerPlayer player = context.getSource().getPlayer();
                            player.displayClientMessage(HELP_TEXT_SHOP_CLAIM, false);
                            return 1;
                        })
                    )
                )




                // Force close HUD
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("close-hud")
                    .executes(context -> {
                        final ServerPlayer player = context.getSource().getPlayer();
                        Context.closeContexts(player);
                        return 1;
                    })
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("help")
                        .executes(context -> {
                            final ServerPlayer player = context.getSource().getPlayer();
                            player.displayClientMessage(HELP_TEXT_SHOP_CLOSEHUD, false);
                            return 1;
                        })
                    )
                )




                // Operator commands
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("op")
                .requires(source -> source.hasPermission(2))
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("help")
                        .executes(context -> {
                            final ServerPlayer player = context.getSource().getPlayer();
                            player.displayClientMessage(HELP_TEXT_SHOP_OP, false);
                            return 1;
                        })
                    )


                    // Item give command
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("give")
                    .executes(context -> {
                        final ServerPlayer player = context.getSource().getPlayer();
                        player.getInventory().add(ShopManager.getShopItemCopy());
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
                        })
                    )


                    // Purge command
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("purge")
                    .then(RequiredArgumentBuilder.<CommandSourceStack, Float>argument("radius", FloatArgumentType.floatArg(0.1f))
                    .executes(context -> {
                        final ServerPlayer player = context.getSource().getPlayer();
                        final float radius = FloatArgumentType.getFloat(context, "radius");
                        final int n = ShopManager.purge((ServerLevel)player.level(), player.getPosition(1f).toVector3f(), radius);
                        player.displayClientMessage(new Txt("Purged " + n + " shops.").get(), false);
                        return 1;
                    })))


                    // Displace command
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("displace")
                    .then(RequiredArgumentBuilder.<CommandSourceStack, Float>argument("radius", FloatArgumentType.floatArg(0.1f))
                    .executes(context -> {
                        final ServerPlayer player = context.getSource().getPlayer();
                        final float radius = FloatArgumentType.getFloat(context, "radius");
                        final int n = ShopManager.displace((ServerLevel)player.level(), player.getPosition(1f).toVector3f(), radius);
                        player.displayClientMessage(new Txt("Displaced " + n + " shops.").get(), false);
                        return 1;
                    })))


                    // Fill command
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("fill")
                    .then(RequiredArgumentBuilder.<CommandSourceStack, Float>argument("radius", FloatArgumentType.floatArg(0.1f, 10f))
                    .executes(context -> {
                        final ServerPlayer player = context.getSource().getPlayer();
                        final float radius = FloatArgumentType.getFloat(context, "radius");
                        final int n = ShopManager.fill((ServerLevel)player.level(), player.getPosition(1f).toVector3f(), radius, player);
                        player.displayClientMessage(new Txt("Created " + n + " shops.").get(), false);
                        return 1;
                    }))
                ))
            );
        });
    }
}
