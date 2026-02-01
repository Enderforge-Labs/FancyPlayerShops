package com.snek.fancyplayershops.data.stash;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworkconfig.data.DataEntry;
import com.snek.frameworklib.utils.common.MinecraftUtils;

import net.minecraft.world.item.ItemStack;








public class PlayerStash extends DataEntry {
    private final Map<UUID, StashEntry> items = new HashMap<>();


    /**
     * Creates a new PlayerStash containing no items.
     */
    public PlayerStash() {
        //Empty
    }


    /**
     * Retrieves the list of items in this stash.
     * @return A map containing the items in this stash, each associated with its ItemStack's UUID.
    */
    public @NotNull Map<UUID, StashEntry> getItems() {
        return items;
    }




    /**
     * Adds an item to this player's stash.
     * <p>
     * This fires stash item addition events and/or stash item change events //FIXME fix name of the events
     * @param item The type of item to add.
     * @param count The amount of items to add.
     */
    public void add(final @NotNull ItemStack item, final long count) {
        if(count == 0) return;
        add(MinecraftUtils.calcItemUUID(item), item, count);
    }



    /**
     * Adds an item with known UUID to this player's stash.
     * <p>
     * This fires stash item addition events and/or stash item change events //FIXME fix name of the events
     * @param uuid The UUID of the item.
     * @param item The type of item to add.
     * @param count The amount of items to add.
     */
    public void add(final @NotNull UUID uuid, final @NotNull ItemStack item, final long count) {
        if(count == 0) return;
        final StashEntry stashEntry = items.get(uuid);
        if(stashEntry == null) {
            items.put(uuid, new StashEntry(item, count));
            //FIXME actually fire events
        }
        else {
            stashEntry.add(count);
            //! Events are fired by StashEntry.add
        }
    }




    /**
     * Removes the specified amount of items from this player's stash.
     * <p>
     * This fires stash item removal events and/or stash item change events //FIXME fix name of the events
     * @param uuid The UUID of the item to remove.
     * @param count The amount of items to remove.
     */
    public void remove(final @NotNull UUID uuid, final long count) {
        final StashEntry stashEntry = items.get(uuid);
        if(stashEntry != null) {
            if(stashEntry.getCount() <= count) {
                items.remove(uuid);
                //FIXME actually fire events
            }
            else {
                stashEntry.remove(count);
                //! Events are fired by StashEntry.remove
            }
        }
        else {
            //FIXME log error
        }
    }
}
