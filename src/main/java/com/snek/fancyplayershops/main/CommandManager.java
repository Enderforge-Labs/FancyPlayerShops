package com.snek.fancyplayershops.main;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.inventories.ShopConfigUI_Factory;
import com.snek.framework.utils.Txt;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;










//TODO add admin only test commands that spawn thousands of shops
//TODO add admin only test commands that remove all shops in a radius
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

                    // UI test
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("config_test")
                    .executes(context -> {
                        final Player player = context.getSource().getPlayer();
                        try {
                            player.displayClientMessage(new Txt("Opening shop inventory...").get(), false);
                            player.openMenu(new ShopConfigUI_Factory());
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                        }
                        return 1;
                    }))


                    // Item give command
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("give")
                    .executes(context -> {
                        final Player player = context.getSource().getPlayer();
                        player.getInventory().add(FancyPlayerShops.getShopItemCopy());
                        return 1;
                    }))


                    // Purge command
                    .then(LiteralArgumentBuilder.<CommandSourceStack>literal("purge")
                    .then(RequiredArgumentBuilder.<CommandSourceStack, Float>argument("radius", FloatArgumentType.floatArg(0.1f))
                    .executes(context -> {
                        final Player player = context.getSource().getPlayer();
                        final float radius = FloatArgumentType.getFloat(context, "radius");
                        final int n = ShopManager.purge(player.level(), player.getPosition(1f).toVector3f(), radius);
                        player.displayClientMessage(new Txt("Purged " + n + " shops.").get(), false);
                        return 1;
                    })))
                )


                // Balance claim
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("claim")
                .executes(context -> {
                    final Player player = context.getSource().getPlayer();
                    player.displayClientMessage(new Txt("You claimed your shop balance.").get(), false);
                    return 1;
                }))


                // Shop statistics
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("stats")
                .executes(context -> {
                    final Player player = context.getSource().getPlayer();
                    player.displayClientMessage(new Txt("opened stats //todo remove message").get(), false);
                    return 1;
                }))


                // Shop mod info
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("help")
                .executes(context -> {
                    final Player player = context.getSource().getPlayer();
                    player.displayClientMessage(new Txt(
                        """
                        Craft an Item Shop in the crafting table and place it to get started!
                        You can see details about your shops and sales history using the command /shop stats.
                        """
                    ).color(FancyPlayerShops.SHOP_ITEM_NAME_COLOR).get(), false);
                    return 1;
                }))
            );
        });
    }
}
