package com.snek.fancyplayershops.data.shop;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.snek.frameworkconfig.data.DataEntrySerializer;




public class Shop_Serializer extends DataEntrySerializer<Shop> {

    @Override
    public @NotNull String serialize(@NotNull Shop entry) {
        final Gson gson = new Gson();
        final Map<String, Object> data = new HashMap<>();

        // Store data
        data.put("displayName", entry.getDisplayName());
        data.put("ownerUUID",   entry.getOwnerUuid().toString());
        data.put("uuid",        entry.getUuid().toString());
        //! Balance is recalculated on startup
        //! Display list is recalculated on startup


        // Convert json to a json string and return it
        return gson.toJson(data);
    }




    @Override
    public @NotNull Shop deserialize(@NotNull String string) {
        final Gson gson = new Gson();
        final Map<String, Object> data = gson.fromJson(string, new TypeToken<Map<String, Object>>(){}.getType());

        // Extract data
        final String displayName = (String)data.get("displayName");
        final UUID   ownerUuid   = UUID.fromString((String)data.get("ownerUUID"));
        final UUID   uuid        = UUID.fromString((String)data.get("uuid"));
        //! Balance is recalculated on startup
        //! Display list is recalculated on startup

        // Create a new Shop and return it
        return new Shop(displayName, uuid, ownerUuid, false);
    }

}
