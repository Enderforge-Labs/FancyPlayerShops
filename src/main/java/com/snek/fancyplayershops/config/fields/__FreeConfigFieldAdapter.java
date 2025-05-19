package com.snek.fancyplayershops.config.fields;

import java.lang.reflect.Type;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;








public class __FreeConfigFieldAdapter<T> implements JsonSerializer<FreeConfigField<T>>, JsonDeserializer<FreeConfigField<T>> {
    private final Class<T> classType;


    public __FreeConfigFieldAdapter(final @NotNull Class<T> _classType) {
        classType = _classType;
    }


    @Override
    public JsonElement serialize(final @NotNull FreeConfigField<T> src, Type typeOfSrc, final @NotNull JsonSerializationContext context) {
        return src.serialize(context);
    }


    @Override
    public FreeConfigField<T> deserialize(final @NotNull JsonElement json, final @NotNull Type typeOfT, final @NotNull JsonDeserializationContext context) throws JsonParseException {
        return FreeConfigField.deserialize(json, classType, context);
    }
}
