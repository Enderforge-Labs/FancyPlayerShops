package com.snek.fancyplayershops.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.snek.frameworklib.data_types.containers.Pair;
import com.snek.frameworklib.utils.MinecraftUtils;
import com.snek.frameworklib.utils.UtilityClassBase;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;








public final class ProductDisplay_Serializer extends UtilityClassBase {

    public static @NotNull String serialize(final @NotNull ProductDisplay display) {
        final Gson gson = new Gson();
        final Map<String, Object> data = new HashMap<>();


        // Store basic data
        data.put("owner",      display.getOwnerUuid().toString());
        data.put("shop_uuid",  display.getShop().getUuid().toString());
        data.put("price",      display.getPrice());
        data.put("stock",      display.getStock());
        data.put("max_stock",  display.getMaxStock());
        data.put("rotation",   display.getDefaultRotation());
        data.put("hue",        display.getColorThemeHue());
        data.put("balance",    display.getBalance());
        data.put("nbt_filter", display.getNbtFilter());


        // Store position data
        final BlockPos pos = display.getPos();
        data.put("position", new int[]{ pos.getX(), pos.getY(), pos.getZ() });
        data.put("level_id", MinecraftUtils.getLevelId(display.getLevel()));


        // Serialize and store item
        final @Nullable String item = MinecraftUtils.serializeItem(display.getItem());
        data.put("item", item != null ? item : MinecraftUtils.serializeItem(Items.AIR.getDefaultInstance()));
        //! Error is printed by the serialize method


        // For each stored item
        final List<Object> storedItems = new ArrayList<>();
        for(final var set : display.getStoredItems().entrySet()) {

            // Serialize and store it
            final @Nullable String i = MinecraftUtils.serializeItem(set.getValue().getFirst());
            if(i != null) {
                final Map<String, Object> storedItem = new HashMap<>();
                storedItem.put("item", i);
                storedItem.put("count", set.getValue().getSecond());
                storedItems.add(storedItem);
            }
        }
        data.put("stored_items", storedItems);


        // Convert json to a json string and return it
        return gson.toJson(data);
    }








    public static @NotNull ProductDisplay deserialize(final @NotNull String json, final @Nullable ServerLevel serverLevelOverride, final @Nullable BlockPos blockPosOverride) {
        final Gson gson = new Gson();
        final Map<String, Object> data = gson.fromJson(json, new TypeToken<Map<String, Object>>(){}.getType());


        // Extract basic data
        final UUID    owner     = UUID.fromString((String)data.get("owner"));
        final UUID    shopUuid  = UUID.fromString((String)data.get("shop_uuid"));
        final long    price     = ((Number)data.get("price")).longValue();
        final int     stock     = ((Number)data.get("stock")).intValue();
        final int     maxStock  = ((Number)data.get("max_stock")).intValue();
        final float   rotation  = ((Number)data.get("rotation")).floatValue();
        final float   hue       = ((Number)data.get("hue")).floatValue();
        final long    balance   = ((Number)data.get("balance")).longValue();
        final boolean nbtFilter = (Boolean)data.get("nbt_filter");


        // Extract level and position data
        ServerLevel level;
        BlockPos position;
        if(serverLevelOverride != null) {
            level = serverLevelOverride;
        }
        else {
            final String levelId = (String)data.get("level_id");
            level = MinecraftUtils.findLevelFromId(levelId);
        }
        if(blockPosOverride != null) {
            position = new BlockPos(blockPosOverride);
        }
        else {
            @SuppressWarnings("unchecked")
            final List<Number> positionList = (List<Number>)data.get("position");
            final int x = positionList.get(0).intValue();
            final int y = positionList.get(1).intValue();
            final int z = positionList.get(2).intValue();
            position = new BlockPos(x, y, z);
        }


        // Deserialize item
        final String itemString = (String)data.get("item");
        ItemStack item = MinecraftUtils.deserializeItem(itemString);
        if(item == null) item = Items.AIR.getDefaultInstance();


        // Deserialize stored items
        @SuppressWarnings("unchecked")
        final List<Map<String, Object>> storedItemsList = (List<Map<String, Object>>)data.get("stored_items");
        final Map<UUID, Pair<ItemStack, Integer>> storedItems = new HashMap<>();

        for(final Map<String, Object> storedItemData : storedItemsList) {
            final String storedItemString = (String)storedItemData.get("item");
            final int count = ((Number)storedItemData.get("count")).intValue();
            final ItemStack storedItem = MinecraftUtils.deserializeItem(storedItemString);

            if(storedItem != null) {
                final UUID storedItemUuid = MinecraftUtils.calcItemUUID(storedItem);
                storedItems.put(storedItemUuid, Pair.from(storedItem, count));
            }
        }


        // Create shop and return
        return new ProductDisplay(
            owner, shopUuid, price, stock, maxStock, rotation, hue, balance,
            nbtFilter, position, level, item, storedItems
        );
    }
}
