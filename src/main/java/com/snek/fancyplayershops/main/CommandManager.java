package com.snek.fancyplayershops.main;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.snek.fancyplayershops.inventories.ShopConfigUI_Factory;
import com.snek.framework.utils.Txt;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;








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
            dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("shop")


                //TODO add admin only test commands that spawn thousands of shops
                //TODO add admin only test commands that remove all shops in a radius


                // UI test
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("config_test").executes(context -> {
                    final PlayerEntity player = context.getSource().getPlayer();
                    try {
                        player.sendMessage(new Txt("Opening shop inventory...").get(), false);
                        player.openHandledScreen(new ShopConfigUI_Factory());
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                    return 1;
                }).requires(source -> source.hasPermissionLevel(2)))


                // Item give command (pperator only)
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("give").executes(context -> {
                    final PlayerEntity player = context.getSource().getPlayer();
                    player.dropStack(FancyPlayerShops.getShopItemCopy());
                    return 1;
                }).requires(source -> source.hasPermissionLevel(2)))


                // Balance claim
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("claim").executes(context -> {
                    final PlayerEntity player = context.getSource().getPlayer();
                    player.sendMessage(new Txt("You claimed your shop balance.").get(), false);
                    return 1;
                }))


                // Shop statistics
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("stats").executes(context -> {
                    final PlayerEntity player = context.getSource().getPlayer();
                    player.sendMessage(new Txt("opened stats //todo remove message").get(), false);
                    return 1;
                }))


                // Shop mod info
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("help").executes(context -> {
                    final PlayerEntity player = context.getSource().getPlayer();
                    //TODO add colors and styles
                    player.sendMessage(new Txt(
                        """
                        Craft an Item Shop in the crafting table and place it to get started!.
                        You can see details about your shops and sales history using the command /shop stats.
                        """
                    ).get(), false);
                    return 1;
                }))
            );
        });
    }
}
