package com.snek.fancyplayershops.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.snek.fancyplayershops.data.shop.Shop;
import com.snek.fancyplayershops.data.display.DisplayTier;
import com.snek.fancyplayershops.data.display.ProductDisplay;
import com.snek.fancyplayershops.data.display.ProductDisplay_Manager;
import com.snek.fancyplayershops.events.DisplayEvents;
import com.snek.fancyplayershops.events.data.DisplayCreationReason;
import com.snek.fancyplayershops.events.data.DisplayRemovalReason;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.data_types.containers.Option;
import com.snek.frameworklib.data_types.containers.Pair;
import com.snek.frameworklib.data_types.graphics.Direction;
import com.snek.frameworklib.utils.common.MinecraftUtils;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.UtilityClassBase;
import com.snek.frameworklib.utils.common.Utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;








public final class ProductDisplay_BulkOperations extends UtilityClassBase {
    private static final Random rnd = new Random();


    /**
     * Selects displays based on the specified conditions.
     * @param level The target level. Selects from all levels if None.
     * @param distance A pair containing the position and the maximum distance from it displays can have in order to be selected. Selects all displays if None.
     * @param owner The target owner. Only displays owned by the specified player are selected. Selects displays owned by any player if None.
     * @param shop The target shop. Only displays that are part of the specified shop are selected. Selects displays in any shop if None.
     */
    public static @NotNull List<@NotNull ProductDisplay> selectDisplays(
        final @NotNull Option<ServerLevel> level,
        final @NotNull Option<Pair<Vector3f, Float>> distance,
        final @NotNull Option<Player> owner,
        final @NotNull Option<Shop> shop
    ) {

        // For each active display
        final List<ProductDisplay> r = new ArrayList<>();
        final var displays = owner.isSome() ?
            ProductDisplay_Manager.getDisplaysByOwner().get(owner.unwrap().getUUID()).values() :
            ProductDisplay_Manager.REF.getCache().values()
        ;
        for(final ProductDisplay display : displays) {

            // Check level
            if(level.isSomeAnd(p -> { return p.getLevel() != display.getLevel(); })) continue;

            // Check distance
            if(distance.isSomeAnd(p -> { return display.calcDisplayPos().sub(p.getFirst()).length() > p.getSecond(); })) continue;

            // ! Check owner
            // ! This is done before the for loop starts for performance reasons

            // Check shop
            if(shop.isSomeAnd(p -> { return p != display.getShop(); })) continue;

            // Save in the return list if all checks have passed
            r.add(display);
        }

        // Return the list of selected displays
        return r;
    }


    /**
     * Selects displays based on the specified conditions.
     * @param level The target level. Selects from all levels if None.
     * @param distance A pair containing the position and the maximum distance from it displays can have in order to be selected. Selects all displays if None.
     * @param owner The target owner. Only displays owned by the specified player are selected. Selects displays owned by any player if None.
     */
    public static @NotNull List<@NotNull ProductDisplay> selectDisplays(
        final @NotNull Option<ServerLevel> level,
        final @NotNull Option<Pair<Vector3f, Float>> distance,
        final @NotNull Option<Player> owner
    ) {
        return selectDisplays(level, distance, owner, Option.None());
    }








    /**
     * Removes the specified displays, sending all the items to their owner's stash.
     * <p>
     * Use {@link #selectDisplays} to select them.
     * @param displays The list of displays to remove.
     * @param admin Whether the action was performed by an admin.
     * @return The number of displays that were removed.
     */
    public static int purge(final @NotNull List<ProductDisplay> displays, final boolean admin) {
        int r = 0;
        final Map<UUID, List<String>> displayNames = new HashMap<>();
        for(final var display : displays) {

            // Add display name to the feedback message
            final List<String> _displayNames = displayNames.computeIfAbsent(display.getOwnerUuid(), k -> new ArrayList<>());
            _displayNames.add(display.getStandaloneName());

            // Stash, claim and delete the display, then increase the purged displays counter
            display.stash(false);
            display.claimBalance();
            display.remove();
            DisplayEvents.DISPLAY_REMOVED.invoker().onDisplayRemove(display, DisplayRemovalReason.DELETED);
            ++r;
        }


        if(r > 0) sendBulkOperationFeedbackMessages(
            admin, displayNames,
            "%1$ of your product displays %3$ been removed%6$: %2$. " +
            "%4$ balance%5$ %3$ been added to your personal balance. " +
            "You will find any remaining stock in your inventory and/or your stash"
        );
        return r;
    }




    /**
     * Forces the owners to pick up the specified displays, sending the snapshots to their stashes.
     * <p>
     * Use {@link #selectDisplays} to select them.
     * @param displays The list of displays to pick up.
     * @param admin Whether the action was performed by an admin.
     * @return The number of displays that were picked up.
     */
    public static int displace(final @NotNull List<ProductDisplay> displays, final boolean admin) {
        int r = 0;
        final Map<UUID, List<String>> displayNames = new HashMap<>();
        for(final var display : displays) {

            // Add display name to the feedback message
            final List<String> _displayNames = displayNames.computeIfAbsent(display.getOwnerUuid(), k -> new ArrayList<>());
            _displayNames.add(display.getStandaloneName());

            // Stash and delete the display, then increase the displaced displays counter
            display.pickUp(false);
            display.remove();
            DisplayEvents.DISPLAY_REMOVED.invoker().onDisplayRemove(display, DisplayRemovalReason.PICKED_UP);
            ++r;
        }


        if(r > 0) sendBulkOperationFeedbackMessages(
            admin, displayNames,
            "%1$ of your product displays %3$ been converted into an item%6$: %2$. " +
            "You will find %7$ in your inventory and/or your stash"
        );
        return r;
    }




    /**
     * Moves multiple displays to another shop.
     * <p>
     * Use {@link #selectDisplays} to select them.
     * @param displays The list of displays to move.
     * @param shopName The name of the shop to move the displays to. Shops that don't already exist are created.
     * @return The number of displays that were moved.
     */
    public static int move(final @NotNull List<ProductDisplay> displays, final @NotNull String shopName) {
        int r = 0;
        final Map<UUID, List<String>> displayNames = new HashMap<>();
        String actualShopName = null;
        for(final var display : displays) {

            // Add display name to the feedback message
            final List<String> _displayNames = displayNames.computeIfAbsent(display.getOwnerUuid(), k -> new ArrayList<>());
            _displayNames.add(display.getStandaloneName());

            // Move the display, then increase the moved displays counter
            final Shop oldShop = display.getShop();
            display.changeShop(shopName);
            if(actualShopName == null) actualShopName = display.getShop().getDisplayName();
            DisplayEvents.DISPLAY_MOVED.invoker().onDisplayMove(display, oldShop, display.getShop());
            ++r;
        }


        if(r > 0) sendBulkOperationFeedbackMessages(
            false, displayNames,
            "%1$ of your product displays %3$ been moved to the shop \"" + actualShopName + "\": %2$. "
        );
        return r;
    }




    /**
     * Transfers multiple displays to the specified player.
     * <p>
     * Use {@link #selectDisplays} to select them.
     * @param displays The list of displays to move.
     * @param newOwner The player to transfer the displays to.
     * @param admin Whether the action was performed by an admin.
     * @return The number of displays that were transferred.
     */
    public static int transfer(final @NotNull List<ProductDisplay> displays, final @NotNull Player newOwner, final boolean admin) {
        int r = 0;
        final List<String> allNames = new ArrayList<>();
        final Map<UUID, List<String>> displayNames = new HashMap<>();
        for(final var display : displays) {
            if(display.getOwnerUuid().equals(newOwner.getUUID())) {
                continue;
            }

            // Add display name to the feedback message
            final List<String> _displayNames = displayNames.computeIfAbsent(display.getOwnerUuid(), k -> new ArrayList<>());
            final String name = display.getStandaloneName();
            _displayNames.add(name);
            allNames.add(name);

            // Stash and delete the display, then increase the transferred displays counter
            final var oldOwner = MinecraftUtils.getPlayerByUUID(display.getOwnerUuid());
            display.changeOwner(newOwner, false);
            DisplayEvents.DISPLAY_TRANSFERRED.invoker().onDisplayTransfer(display, oldOwner, newOwner);
            ++r;
        }


        if(r > 0) {

            // Send feedback to previous owners
            sendBulkOperationFeedbackMessages(
                admin, displayNames,
            "%1$ of your product displays %3$ been transferred to " + newOwner.getName().getString() + "%6$: %2$. "
            );

            // Send feedback to the new owner
            sendBulkOperationFeedbackMessages(
                admin, Map.of(newOwner.getUUID(), allNames),
                "%1$ product displays %3$ been transferred to you%6$: %2$. "
            );
        }

        return r;
    }




    /**
     * Fill an area around the specified position with displays.
     * @param level The target level.
     * @param pos The center of the fill area.
     * @param radius The maximum distance to reach on each cardinal direction.
     * @param owner The owner of the newly created displays.
     * @return The number of displays that were created.
     */
    public static int fill(final @NotNull ServerLevel level, final @NotNull Vector3f pos, final float radius, final @NotNull Player owner) {

        // Get a list of all registered items
        final Registry<Item> itemRegistry = FrameworkLib.getServer().registryAccess().registryOrThrow(Registries.ITEM);
        final List<Item> itemList = new ArrayList<>();
        for(final Item item : itemRegistry) {
            itemList.add(item);
        }


        int r = 0;
        for(float i = pos.x - radius; i < pos.x + radius; ++i) {
            for(float j = pos.y - radius; j < pos.y + radius; ++j) {
                for(float k = pos.z - radius; k < pos.z + radius; ++k) {
                    final BlockPos blockPos = new BlockPos(MinecraftUtils.doubleToBlockCoords(new Vector3d(i, j, k)));
                    if(new Vector3f(i, j, k).distance(pos) <= radius && level.getBlockState(blockPos).isAir()) {
                        final var tier = DisplayTier.values()[Math.abs(rnd.nextInt() % DisplayTier.values().length)];
                        final ProductDisplay display = new ProductDisplay(
                            /* ownerUUID   */ owner.getUUID(),
                            /* shopUUID    */ null,
                            /* price       */ Math.abs(rnd.nextLong() % 100_000),
                            /* stock       */ 0,
                            /* maxStock    */ tier.getCapacity(),
                            /* direction   */ Direction.fromEighths(rnd.nextInt() % 8),
                            /* hue         */ 0,
                            /* balance     */ Math.abs(rnd.nextLong() % 100),
                            /* nbtFilter   */ true,
                            /* position    */ new BlockPos((int)i, (int)j, (int)k),
                            /* level       */ level,
                            /* tier        */ tier,
                            /* item        */ itemList.get(Math.abs(rnd.nextInt() % itemList.size())).getDefaultInstance(),
                            /* storedItems */ new HashMap<>()
                        );
                        display.invalidateItemDisplay();
                        display.storeItems(display.getItemUUID(), display.getItem(), Math.abs(rnd.nextInt() % 1_000_000));
                        DisplayEvents.DISPLAY_CREATED.invoker().onDisplayCreate(display, DisplayCreationReason.NEW);
                        ++r;
                    }
                }
            }
        }
        return r;
    }





    /**
     * Creates and sends bulk operation feedback messages to the affected players.
     * <ul><li><b>Format string parameters</b></li><ul>
     * <li> <b>%1$</b> The amount of affected displays.</li>
     * <li> <b>%2$</b> The list of names of the affected displays. Identical names are merged together.</li>
     * <li> <b>%3$</b> either "has" or "have", depending on the amount of affected displays.</li>
     * <li> <b>%4$</b> either "its" or "their", depending on the amount of affected displays.</li>
     * <li> <b>%5$</b> either an empty string or "s", depending on the amount of affected displays.</li>
     * <li> <b>%6$</b> either " by an admin" or an empty string, depending on the {@code admin} parameter.</li>
     * <li> <b>%7$</b> either "it" or "them", depending on the amount of affected displays.</li>
     * </ul></ul>
     * @param admin Whether the operation was performed by an admin or by the player. This changes the displayed message
     * @param affectedDisplaysNames A map that associates each affected player's UUID with a list
     *     containing the names of the displays owned by the player that were affected by this operation.
     * @param formatString The format string to use for the message.
     */
    private static void sendBulkOperationFeedbackMessages(
        final boolean admin,
        final @NotNull Map<@NotNull UUID, @NotNull List<String>> affectedDisplaysNames,
        final @NotNull String formatString
    ) {

        // For each affected player
        for(final var entry : affectedDisplaysNames.entrySet()) {
            final Player owner = MinecraftUtils.getPlayerByUUID(entry.getKey());
            if(owner != null) {


                // Merge identical names
                final Map<String, Integer> mergedNames = new HashMap<>();
                for(final String s : entry.getValue()) {
                    mergedNames.put(s, mergedNames.getOrDefault(s, 0) + 1);
                }


                // Create list of names
                final var names = entry.getValue();
                final int affectedAmount = names.size();
                StringBuilder namesString = new StringBuilder();
                if(!mergedNames.isEmpty()) {
                    for(final var mergedName : mergedNames.entrySet()) {
                        if(mergedName.getValue() > 1) {
                            namesString.append(Utils.formatAmount(mergedName.getValue(), true, true));
                            namesString.append(" ");
                        }
                        namesString.append("\"");
                        namesString.append(mergedName.getKey());
                        namesString.append("\"");
                        namesString.append(", ");
                    }
                    namesString.delete(namesString.length() - 2, namesString.length());
                }


                // Create feedback message and send it to the player
                owner.displayClientMessage(Txt.Format(
                    formatString,
                    new Txt(Utils.formatAmount(affectedAmount)).white(),
                    new Txt(namesString.toString()).lightGray(),
                    new Txt(affectedAmount == 1 ? "has"          : "have" ).white(),
                    new Txt(affectedAmount == 1 ? "its"          : "their").white(),
                    new Txt(affectedAmount == 1 ? ""             : "s"    ).white(),
                    new Txt(admin               ? " by an admin" : ""     ).white(),
                    new Txt(affectedAmount == 1 ? "it"           : "them" ).white()
                ).white().get(), false);
            }
        }
    }
}
