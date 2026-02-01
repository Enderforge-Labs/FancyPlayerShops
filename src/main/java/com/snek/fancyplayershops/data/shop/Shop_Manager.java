package com.snek.fancyplayershops.data.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.frameworkconfig.data.DataManager;
import com.snek.fancyplayershops.data.display.ProductDisplay;
import com.snek.frameworklib.data_types.containers.Option;
import com.snek.frameworklib.utils.common.MinecraftUtils;

import net.minecraft.world.entity.player.Player;
















/**
 * A class that handles player shops.
 */
@SuppressWarnings("java:S6548") //! Singleton implementation
public class Shop_Manager extends DataManager<Shop> {

    // Static manager reference
    public static final Shop_Manager REF = new Shop_Manager();

    // Static data
    public static final String DEFAULT_SHOP_NAME = "Uncategorized";

    // Player shop data
    private static final @NotNull Map<UUID, List<Shop>> shopsByOwner = new HashMap<>();
    public  static final @NotNull Map<UUID, List<Shop>> getShopsByOwner() { return shopsByOwner; }




    private Shop_Manager() {
        super(FancyPlayerShops.MOD_ID, "shops", new Shop_Serializer());
    }




    @Override
    public void afterPut(final @NotNull UUID uuid, final @NotNull Shop shop) {

        // Update the list of shops of the owner
        shopsByOwner.computeIfAbsent(shop.getOwnerUuid(), _uuid -> new ArrayList<>()).add(shop);
    }


    @Override
    public void afterRemove(final @NotNull UUID uuid, final @NotNull Shop shop) {

        // Update the list of shops of the owner
        //! We expect this data to be valid. No need to check if the entry exists.
        //! A missing entry in shopsByOwner means something went wrong in the code.
        final var playerShops = shopsByOwner.get(shop.getOwnerUuid());
        playerShops.remove(shop);

        // Remove all the displays from the shop
        for(final ProductDisplay display : shop.getDisplays()) {
            Shop_Manager.unregisterDisplay(display);
        }
    }




    /**
     * Registers a display into the specified shop.
     * <p>
     * This updates the shop's stats and any context displaying its data.
     * @param display The display to register.
     * @param shopUUID The UUID of the shop.
     *     This must match an existing shop's UUID, or be null.
     *     Passing null will assign the display to its owner's default shop.
     * @return The Shop instance the display was assigned to.
     */
    public static Shop registerDisplay(final @NotNull ProductDisplay display, final @Nullable UUID shopUUID) {
        Shop shop = null;


        // Create or get default shop if needed
        //! This special shop is not stored to file or loaded
        if(shopUUID == null) {
            final var playerShops = shopsByOwner.get(display.getOwnerUuid());
            if(playerShops != null) for(final var playerShop : playerShops) {
                if(playerShop.isDefault()) {
                    shop = playerShop;
                    break;
                }
            }
            if(shop == null) {
                shop = new Shop(DEFAULT_SHOP_NAME, display.getOwnerUuid(), true);
                REF.put(shop.getUuid(), shop);
            }
            //TODO use italic grey once colors are implemented
            //TODO allow players to use &[0-9a-gulomkr&]
        }


        // Find shop and assign the display to it if valid
        if(shop == null) {
            shop = REF.get(shopUUID);
        }
        if(shop != null) {
            if(shop.getOwnerUuid().equals(display.getOwnerUuid())) {
                shop.addBalance(display.getBalance());
                shop.addDisplay(display);
                return shop;
            }


            // Print an error if the shop isn't owned by the right player, then assign to the default shop
            else {
                final @Nullable Player displayOwner = MinecraftUtils.getPlayerByUUID(display.getOwnerUuid());
                final @Nullable Player shopOwner    = MinecraftUtils.getPlayerByUUID(shop.getOwnerUuid());
                FancyPlayerShops.LOGGER.error(
                    "Display registration failed: the display is owned by {}, but the requested shop is owned by {}",
                    displayOwner == null ? "<unknown>" : displayOwner.getName().getString(),
                    shopOwner    == null ? "<unknown>" : shopOwner   .getName().getString(),
                    new RuntimeException()
                );
                return registerDisplay(display, null);
            }
        }


        // Print an error if the shop cannot be found, then assign to the default shop
        else {
            FancyPlayerShops.LOGGER.error("Display registration failed: the requested shop does not exist: {}", shopUUID, new RuntimeException());
            return registerDisplay(display, null);
        }
    }




    public static void unregisterDisplay(final @NotNull ProductDisplay display) {
        final Shop shop = display.getShop();
        shop.subBalance(display.getBalance());
        shop.removeDisplay(display);
    }




    /**
     * Checks if the provided string is a valid shop display name.
     * @param s The display name of the shop.
     * @return An Option containing a description of the error if the string is not a valid name, or an empty Option otherwise.
     */
    public static Option<String> validateShopName(final @NotNull String s) {
        final char c = s.charAt(0);
        if(c == '.' || c == ' ' || c == ',') {
            return Option.Some("Shop names can't start with \"" + c + "\"");
        }
        else if(Character.isDigit(c)) {
            return Option.Some("Shop names can't start with a number");
        }
        else {
            return Option.None();
        }
    }
}

