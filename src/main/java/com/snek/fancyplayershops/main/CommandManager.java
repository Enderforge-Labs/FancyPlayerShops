package com.snek.fancyplayershops.main;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.snek.fancyplayershops.data.BalanceManager;
import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.data.StashManager;
import com.snek.fancyplayershops.hud_ui._elements.Hud;
import com.snek.fancyplayershops.hud_ui._elements.HudCanvas;
import com.snek.framework.utils.Txt;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;










/**
 * A utility class that registers and handles in-game commands.
 */
public abstract class CommandManager {
    private CommandManager() {}


    /**
     * Registers the /shop command
     */
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(LiteralArgumentBuilder.<CommandSourceStack>literal("shop")


                // Operator commands
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("op")
                .requires(source -> source.hasPermission(2))


                    // Item give command
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("give")
                    .executes(context -> {
                        final ServerPlayer player = context.getSource().getPlayer();
                        player.getInventory().add(ShopManager.getShopItemCopy());
                        return 1;
                    }))


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


                    // Fill command
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("fill")
                    .then(RequiredArgumentBuilder.<CommandSourceStack, Float>argument("radius", FloatArgumentType.floatArg(0.1f, 10f))
                    .executes(context -> {
                        final ServerPlayer player = context.getSource().getPlayer();
                        final float radius = FloatArgumentType.getFloat(context, "radius");
                        final int n = ShopManager.fill((ServerLevel)player.level(), player.getPosition(1f).toVector3f(), radius, player);
                        player.displayClientMessage(new Txt("Created " + n + " shops.").get(), false);
                        return 1;
                    })))
                )


                // Balance claim
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("claim")
                .executes(context -> {
                    final ServerPlayer player = context.getSource().getPlayer();
                    BalanceManager.claim(player);
                    return 1;
                }))


                // Force close HUD
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("close-hud")
                .executes(context -> {
                    final ServerPlayer player = context.getSource().getPlayer();
                    Hud.closeHud(player);
                    return 1;
                }))


                // Shop statistics
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("stats")
                .executes(context -> {
                    final ServerPlayer player = context.getSource().getPlayer();
                    player.displayClientMessage(new Txt("opened stats //todo remove message").get(), false);
                    return 1;
                }))


                // Shop stash view
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("stash")
                .executes(context -> {
                    final ServerPlayer player = context.getSource().getPlayer();
                    StashManager.openStashView(player);
                    return 1;
                }))


                // Shop mod info
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("help")
                .executes(context -> {
                    final ServerPlayer player = context.getSource().getPlayer();
                    player.displayClientMessage(new Txt(
                        """
                        Craft an Item Shop in the crafting table and place it to get started!
                        You can see details about your shops and sales history using the command /shop stats.
                        """
                    ).color(ShopManager.SHOP_ITEM_NAME_COLOR).get(), false);
                    return 1;
                }))
            );
        });
    }
}
