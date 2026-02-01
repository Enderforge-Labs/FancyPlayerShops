package com.snek.fancyplayershops.data.display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.include.com.google.gson.GsonBuilder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.snek.frameworkconfig.data.DataEntrySerializer;
import com.snek.frameworklib.data_types.containers.Pair;
import com.snek.frameworklib.data_types.graphics.Direction;
import com.snek.frameworklib.utils.common.MinecraftUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;








public class ProductDisplay_Serializer extends DataEntrySerializer<ProductDisplay> {


    @Override
    public @NotNull String serialize(final @NotNull ProductDisplay data) {
        final Gson gson = new Gson();
        final Map<String, Object> r = new HashMap<>();


        // Store basic data
        r.put("owner",      data.getOwnerUuid().toString());
        r.put("shop_uuid",  data.getShop().isDefault() ? "" : data.getShop().getUuid().toString());
        r.put("price",      data.getPrice());
        r.put("stock",      data.getStock());
        r.put("max_stock",  data.getMaxStock());
        r.put("direction",  data.getDefaultDirection().getEighths());
        r.put("hue",        data.getColorThemeHue());
        r.put("balance",    data.getBalance());
        r.put("nbt_filter", data.getNbtFilter());
        r.put("tier",       data.getTier().getNumericalId());


        // Store position data
        final BlockPos pos = data.getPos();
        r.put("position", new int[]{ pos.getX(), pos.getY(), pos.getZ() });
        r.put("level_id", MinecraftUtils.getLevelId(data.getLevel()));


        // Serialize and store item
        final @Nullable String item = MinecraftUtils.serializeItem(data.getItem());
        r.put("item", item != null ? item : MinecraftUtils.serializeItem(Items.AIR.getDefaultInstance()));
        //! Error is printed by the serialize method


        // For each stored item
        final List<Object> storedItems = new ArrayList<>();
        for(final var set : data.getStoredItems().entrySet()) {

            // Serialize and store it
            final @Nullable String i = MinecraftUtils.serializeItem(set.getValue().getFirst());
            if(i != null) {
                final Map<String, Object> storedItem = new HashMap<>();
                storedItem.put("item", i);
                storedItem.put("count", set.getValue().getSecond());
                storedItems.add(storedItem);
            }
        }
        r.put("stored_items", storedItems);


        // Convert json to a json string and return it
        return gson.toJson(r);
    }








    @Override
    public @NotNull ProductDisplay deserialize(final @NotNull String string) {
        return deserialize(string, null, null);
    }




    public @NotNull ProductDisplay deserialize(final @NotNull String string, final @Nullable ServerLevel serverLevelOverride, final @Nullable BlockPos blockPosOverride) {
        final Gson gson = new Gson();
        final Map<String, Object> data = gson.fromJson(string, new TypeToken<Map<String, Object>>(){}.getType());


        // Extract basic data
        final @NotNull String rawShopUuidValue = (String)data.get("shop_uuid");
        final UUID        owner     = UUID.fromString((String)data.get("owner"));
        final UUID        shopUuid  = rawShopUuidValue.isEmpty() ? null : UUID.fromString(rawShopUuidValue);
        final long        price     = ((Number)data.get("price")).longValue();
        final long        stock     = ((Number)data.get("stock")).longValue();
        final long        maxStock  = ((Number)data.get("max_stock")).longValue();
        final Direction   direction = Direction.fromEighths(((Number)data.get("direction")).intValue());
        final float       hue       = ((Number)data.get("hue")).floatValue();
        final long        balance   = ((Number)data.get("balance")).longValue();
        final boolean     nbtFilter = (Boolean)data.get("nbt_filter");
        final DisplayTier tier      = DisplayTier.fromNumericalId(((Number)data.get("tier")).intValue());


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
        final Map<UUID, Pair<ItemStack, Long>> storedItems = new HashMap<>();

        for(final Map<String, Object> storedItemData : storedItemsList) {
            final String storedItemString = (String)storedItemData.get("item");
            final long count = ((Number)storedItemData.get("count")).intValue();
            final ItemStack storedItem = MinecraftUtils.deserializeItem(storedItemString);

            if(storedItem != null) {
                final UUID storedItemUuid = MinecraftUtils.calcItemUUID(storedItem);
                storedItems.put(storedItemUuid, Pair.from(storedItem, count));
            }
        }


        // Create shop and return
        return new ProductDisplay(
            owner, shopUuid, price, stock, maxStock, direction, hue, balance,
            nbtFilter, position, level, tier, item, storedItems
        );
    }
}
