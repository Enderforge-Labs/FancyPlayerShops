package com.snek.fancyplayershops.config.fields;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;







//TODO make description multiline. use an array
public class ConstrainedConfigField<T> {
    private final @NotNull  T      min;
    private final @NotNull  T      defaultValue;
    private final @NotNull  T      max;
    private final @Nullable String description;

    public @NotNull  T      getMin        () { return min; }
    public @NotNull  T      getDefault    () { return defaultValue; }
    public @NotNull  T      getMax        () { return max; }
    public @Nullable String getDescription() { return description; }




    public ConstrainedConfigField(final @Nullable String _description, final @NotNull T _min, final @NotNull T _default, final @NotNull T _max) {
        description = _description;
        min = _min;
        defaultValue = _default;
        max = _max;
    }


    public JsonElement serialize(final @NotNull JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("description", description);
        obj.add("default", context.serialize(defaultValue));
        obj.add("min",     context.serialize(min));
        obj.add("max",     context.serialize(max));
        return obj;
    }


    public static <T> ConstrainedConfigField<T> deserialize(final @NotNull JsonElement json, final @NotNull Class<T> classType, final @NotNull JsonDeserializationContext context) {
        final JsonObject obj = json.getAsJsonObject();
        final T min = context.deserialize(obj.get("min"    ), classType);
        final T val = context.deserialize(obj.get("default"), classType);
        final T max = context.deserialize(obj.get("max"    ), classType);
        final String description = obj.has("description") ? obj.get("description").getAsString() : null;
        System.out.println("TEST HERE");
        return new ConstrainedConfigField<>(description, min, val, max);
    }
}