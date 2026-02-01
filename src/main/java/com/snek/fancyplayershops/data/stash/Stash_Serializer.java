package com.snek.fancyplayershops.data.stash;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.snek.fancyplayershops.main.FancyPlayerShops;
import com.snek.frameworkconfig.data.DataEntrySerializer;
import com.snek.frameworklib.utils.common.MinecraftUtils;

import net.minecraft.world.item.ItemStack;








public class Stash_Serializer extends DataEntrySerializer<PlayerStash> {

    @Override
    @SuppressWarnings("java:S2629")
    public @NotNull String serialize(@NotNull PlayerStash entry) {
        final Gson gson = new Gson();
        final Map<UUID, Object> data = new HashMap<>();

        // Store data
        for(final Entry<UUID, StashEntry> stashEntry : entry.getItems().entrySet()) {
            final ItemStack entryItem = stashEntry.getValue().getItem();
            final long     entryCount = stashEntry.getValue().getCount();
            final @Nullable String serializedItem = MinecraftUtils.serializeItem(entryItem);
            if(serializedItem == null) {
                FancyPlayerShops.LOGGER.error(
                    "An item in the stash of a player couldn't be saved. Item ID: {}, Count: {}",
                    MinecraftUtils.getItemId(entryItem), entryCount, new RuntimeException()
                );
            }
            else {
                final Map<String, Object> stashEntryData = new HashMap<>();
                stashEntryData.put("item", serializedItem);
                stashEntryData.put("count", entryCount);
                data.put(stashEntry.getKey(), stashEntryData);
            }
        }


        // Convert json to a json string and return it
        return gson.toJson(data);
    }




    @Override
    public @NotNull PlayerStash deserialize(@NotNull String string) {
        final Gson gson = new Gson();
        final Map<UUID, Map<String, Object>> data = gson.fromJson(string, new TypeToken<Map<UUID, Map<String, Object>>>(){}.getType());
        final var r = new PlayerStash();

        // Extract data
        for(final var serializedStashEntry : data.entrySet()) {
            final String itemString = (String)serializedStashEntry.getValue().get("item");
            final @Nullable ItemStack deserializedItem = MinecraftUtils.deserializeItem(itemString);
            if(deserializedItem == null) {
                FancyPlayerShops.LOGGER.error(
                    "An item in the stash of a player couldn't be loaded. Serialized item data: {}",
                    itemString, new RuntimeException()
                );
            }
            else {
                final long deserializedCount = ((Number)serializedStashEntry.getValue().get("count")).longValue();
                r.add(serializedStashEntry.getKey(), deserializedItem, deserializedCount);
            }
        }

        // Create a new Shop and return it
        return r;
    }
}
