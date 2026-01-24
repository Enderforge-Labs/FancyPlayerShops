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

import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.data.ShopManager;
import com.snek.fancyplayershops.events.DisplayEvents;
import com.snek.fancyplayershops.events.data.DisplayCreationReason;
import com.snek.fancyplayershops.events.data.DisplayRemovalReason;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.data_types.containers.Option;
import com.snek.frameworklib.data_types.graphics.Direction;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.UtilityClassBase;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;








public final class ProductDisplay_BulkOperations extends UtilityClassBase {
    private static final Random rnd = new Random();




    /**
     * Removes all displays near the specified position, sending all the items to their owner's stash.
     * @param level The target level.
     * @param pos The center of the purge radius.
     * @param radius The maximum distance from pos displays can have in order to be purged.
     * @param enforceOwned Whether to affect only displays owned by the specified player.
     * @return The number of displays that were removed.
     */
    public static int purge(final @NotNull ServerLevel level, final @NotNull Vector3f pos, final float radius, final Option<Player> enforceOwner) {
        int r = 0;
        final Map<UUID, List<String>> displayNames = new HashMap<>();
        final List<ProductDisplay> displays = new ArrayList<>(ProductDisplayManager.getDisplaysByCoords().values());
        for(final ProductDisplay display : displays) {
            if(display.getLevel() == level && display.calcDisplayPos().sub(pos).length() <= radius) {
                if(enforceOwner.isSomeAnd(p -> { return !p.getUUID().equals(display.getOwnerUuid()); })) {
                    continue;
                }

                // Add display name to the feedback message
                if(!display.getItem().is(Items.AIR)) {
                    final List<String> _displayNames = displayNames.computeIfAbsent(display.getOwnerUuid(), k -> new ArrayList<>());
                    _displayNames.add(MinecraftUtils.getFancyItemName(display.getItem()).getString());
                }

                // Stash, claim and delete the display, then increase the purged displays counter
                display.stash(false);
                display.claimBalance();
                display.remove();
                DisplayEvents.DISPLAY_REMOVED.invoker().onDisplayRemove(display, DisplayRemovalReason.DELETED);
                ++r;
            }
        }


        sendBulkOperationFeedbackMessages(
            enforceOwner.isNone(), displayNames,
            "%1$s of your product displays %3$s been removed%6$s: %2$s. " +
            "%4$s balance%5$s %3$s been added to your personal balance. " +
            "You will find any remaining stock in your inventory and/or your stash"
        );
        return r;
    }




    /**
     * Forces the owners to pick up any of their displays near the specified position, sending the snapshots to their stashes.
     * @param level The target level.
     * @param pos The center of the radius.
     * @param radius The maximum distance from pos displays can have in order to be picked up.
     * @param enforceOwned Whether to affect only displays owned by the specified player.
     * @return The number of displays that were picked up.
     */
    public static int displace(final @NotNull ServerLevel level, final @NotNull Vector3f pos, final float radius, final Option<Player> enforceOwner) {
        int r = 0;
        final Map<UUID, List<String>> displayNames = new HashMap<>();
        final List<ProductDisplay> displays = new ArrayList<>(ProductDisplayManager.getDisplaysByCoords().values());
        for(final ProductDisplay display : displays) {
            if(display.getLevel() == level && display.calcDisplayPos().sub(pos).length() <= radius) {
                if(enforceOwner.isSomeAnd(p -> { return !p.getUUID().equals(display.getOwnerUuid()); })) {
                    continue;
                }

                // Add display name to the feedback message
                if(!display.getItem().is(Items.AIR)) {
                    final List<String> _displayNames = displayNames.computeIfAbsent(display.getOwnerUuid(), k -> new ArrayList<>());
                    _displayNames.add(MinecraftUtils.getFancyItemName(display.getItem()).getString());
                }

                // Stash and delete the display, then increase the displaced displays counter
                display.pickUp(false);
                display.remove();
                DisplayEvents.DISPLAY_REMOVED.invoker().onDisplayRemove(display, DisplayRemovalReason.PICKED_UP);
                ++r;
            }
        }


        sendBulkOperationFeedbackMessages(
            enforceOwner.isNone(), displayNames,
            "%1$s of your product displays %3$s been converted into an item%6$s: %2$s. " +
            "You will find %7$s in your inventory and/or your stash"
        );
        return r;
    }




    /**
     * Fill an area around the specified position with display.
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
                            /* shopUUID    */ ShopManager.DEFAULT_SHOP_UUID,
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
     * @param admin Whether the operation was performed by an admin or by the player. This changes the displayed message
     * @param affectedDisplaysNames A map that associates each affected player's UUID with a list
     *     containing the names of the displays owned by the player that were affected by this operation.
     * @param formatString The format string to use for the message.
     * <ul>
     * <li> %1$s The amount of affected displays.</li>
     * <li> %2$s The list of names of the affected displays.</li>
     * <li> %3$s either "has" or "have", depending on the amount of effected displays.</li>
     * <li> %4$s either "its" or "their", depending on the amount of effected displays.</li>
     * <li> %5$s either an empty string or "s", depending on the amount of effected displays.</li>
     * <li> %6$s either " by an admin" or an empty string, depending on the {@code admin} parameter.</li>
     * <li> %7$s either "it" or "them", depending on the amount of effected displays.</li>
     * </ul>
     */
    private static void sendBulkOperationFeedbackMessages(final boolean admin, final @NotNull Map<@NotNull UUID, @NotNull List<String>> affectedDisplaysNames, final @NotNull String formatString) {
        // Create feedback messages and send them to the affected players, then return
        for(final var entry : affectedDisplaysNames.entrySet()) {
            final Player owner = MinecraftUtils.getPlayerByUUID(entry.getKey());
            if(owner != null) {
                final var names = entry.getValue();
                final int affectedAmount = names.size();
                StringBuilder namesString = new StringBuilder();
                for(int i = 0; i < affectedAmount; ++i) {
                    namesString.append(" \"").append(names.get(i)).append("\"");
                    if(i < affectedAmount - 1) namesString.append(",");
                }
                owner.displayClientMessage(new Txt(String.format(
                    formatString,
                    Utils.formatAmount(affectedAmount),
                    namesString,
                    affectedAmount == 1 ? "has" : "have",
                    affectedAmount == 1 ? "its" : "their",
                    affectedAmount == 1 ? "" : "s",
                    admin ? " by an admin" : "",
                    affectedAmount == 1 ? "it" : "them"
                )).lightGray().get(), false);
            }
        }
    }
}
