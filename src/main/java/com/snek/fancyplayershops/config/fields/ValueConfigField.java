package com.snek.fancyplayershops.config.fields;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;








//TODO make description multiline. use an array
public class ValueConfigField<T> {
    private final @NotNull  T      value;
    private final @Nullable String description;

    public @NotNull  T      getValue      () { return value; }
    public @Nullable String getDescription() { return description; }




    public ValueConfigField(final @Nullable String _description, final @NotNull T _defaul) {
        description = _description;
        value = _defaul;
    }


    public JsonElement serialize(final @NotNull JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("description", description);
        obj.add("value", context.serialize(value));
        return obj;
    }


    public static <T> ValueConfigField<T> deserialize(final @NotNull JsonElement json, final @NotNull Class<T> classType, final @NotNull JsonDeserializationContext context) {
        final JsonObject obj = json.getAsJsonObject();
        final T val = context.deserialize(obj.get("value"), classType);
        final String description = obj.has("description") ? obj.get("description").getAsString() : null;
        return new ValueConfigField<>(description, val);
    }
}