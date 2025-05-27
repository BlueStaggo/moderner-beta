package mod.bluestaggo.modernerbeta.util;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CodecUtil {
    public static <T> Codec<Set<T>> set(Codec<T> elementType) {
        return elementType.listOf().xmap(HashSet::new, ArrayList::new);
    }

    public static <T> void registerTypeAdapter(GsonBuilder gson, Class<T> clazz, Codec<T> codec) {
        gson.registerTypeAdapter(clazz, new JsonSerializer<>(codec));
        gson.registerTypeAdapter(clazz, new JsonDeserializer<>(codec));
    }

    public record JsonSerializer<T>(Codec<T> codec) implements com.google.gson.JsonSerializer<T> {
        @Override
        public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
            return codec.encodeStart(JsonOps.INSTANCE, src).getOrThrow();
        }
    }

    public record JsonDeserializer<T>(Codec<T> codec) implements com.google.gson.JsonDeserializer<T> {
        @Override
        public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return codec.decode(JsonOps.INSTANCE, json).getOrThrow().getFirst();
        }
    }
}
