package com.snek.fancyplayershops.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.snek.fancyplayershops.data.ProductDisplayManager;
import com.snek.fancyplayershops.data.ShopManager;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.UtilityClassBase;

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
     * @return The number of displays that were removed.
     */
    public static int purge(final @NotNull ServerLevel level, final @NotNull Vector3f pos, final float radius) {
        int r = 0;
        final List<ProductDisplay> displays = new ArrayList<>(ProductDisplayManager.getDisplaysByCoords().values());
        for(final ProductDisplay display : displays) {
            if(display.getLevel() == level && display.calcDisplayPos().sub(pos).length() <= radius) {

                // Send feedback to affected player if they are online
                final Player owner = MinecraftUtils.getPlayerByUUID(display.getOwnerUuid());
                if(owner != null && !display.getItem().is(Items.AIR)) owner.displayClientMessage(new Txt()
                    .cat(new Txt("Your " + display.getDecoratedName() + " has been removed by an admin").red())
                .get(), false);

                // Stash, claim and delete the display, then increase the purged displays counter
                display.stash();
                display.claimBalance();
                display.delete();
                ++r;
            }
        }
        return r;
    }




    /**
     * Forces the owners to pick up any of their displays near the specified position, sending the snapshots to their stashes.
     * @param level The target level.
     * @param pos The center of the radius.
     * @param radius The maximum distance from pos displays can have in order to be picked up.
     * @return The number of displays that were picked up.
     */
    public static int displace(final @NotNull ServerLevel level, final @NotNull Vector3f pos, final float radius) {
        int r = 0;
        final List<ProductDisplay> displays = new ArrayList<>(ProductDisplayManager.getDisplaysByCoords().values());
        for(final ProductDisplay display : displays) {
            if(display.getLevel() == level && display.calcDisplayPos().sub(pos).length() <= radius) {

                // Send feedback to affected player if they are online
                final Player owner = MinecraftUtils.getPlayerByUUID(display.getOwnerUuid());
                if(owner != null && !display.getItem().is(Items.AIR)) owner.displayClientMessage(new Txt()
                    .cat(new Txt("Your " + display.getDecoratedName() + " was converted into an item by an admin").red())
                .get(), false);

                // Stash and delete the display, then increase the displaced displays counter
                display.pickUp(false);
                display.delete();
                ++r;
            }
        }
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
                        final ProductDisplay display = new ProductDisplay(
                            /* ownerUUID   */ owner.getUUID(),
                            /* shopUUID    */ ShopManager.DEFAULT_SHOP_UUID,
                            /* price       */ Math.abs(rnd.nextLong() % 100_000),
                            /* stock       */ Math.abs(rnd.nextInt() % 1_000_000),
                            /* maxStock    */ 1_000_000,
                            /* rotation    */ (float)Math.toRadians(45f) * (rnd.nextInt() % 8),
                            /* hue         */ 0,
                            /* balance     */ Math.abs(rnd.nextLong() % 100),
                            /* nbtFilter   */ true,
                            /* position    */ new BlockPos((int)pos.x, (int)pos.y, (int)pos.z),
                            /* level       */ level,
                            /* item        */ itemList.get(Math.abs(rnd.nextInt() % itemList.size())).getDefaultInstance(),
                            /* storedItems */ new HashMap<>()
                        );
                        display.invalidateItemDisplay();
                        ++r;
                    }
                }
            }
        }
        return r;
    }
}
