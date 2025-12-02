package mod.bluestaggo.modernerbeta.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.RegistryOps;

import java.lang.reflect.Type;
import java.util.Set;

public class CodecUtil {
    public static <T> Codec<Set<T>> set(Codec<T> elementType) {
        return elementType.listOf().xmap(ImmutableSet::copyOf, ImmutableList::copyOf);
    }

    public static <T> void registerTypeAdapter(GsonBuilder gson, Class<T> clazz, Codec<T> codec) {
        gson.registerTypeAdapter(clazz, new JsonSerializer<>(codec));
        gson.registerTypeAdapter(clazz, new JsonDeserializer<>(codec));
    }

    // Relies on the fact that all fields in the codec have default values
    public static <T> T getDefaultByMap(Codec<T> codec) {
        return VersionCompat.getOrThrow(codec.decode(JsonOps.INSTANCE, new JsonObject())).getFirst();
    }

    public record JsonSerializer<T>(HolderLookup.Provider registries, Codec<T> codec) implements com.google.gson.JsonSerializer<T> {
        public JsonSerializer(Codec<T> codec) {
            this(null, codec);
        }

        @Override
        public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
            DynamicOps<JsonElement> ops = this.registries != null ?
                    RegistryOps.create(JsonOps.INSTANCE, this.registries) : JsonOps.INSTANCE;
            return VersionCompat.getOrThrow(codec.encodeStart(ops, src));
        }
    }

    public record JsonDeserializer<T>(HolderLookup.Provider registries, Codec<T> codec) implements com.google.gson.JsonDeserializer<T> {
        public JsonDeserializer(Codec<T> codec) {
            this(null, codec);
        }

        @Override
        public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            DynamicOps<JsonElement> ops = this.registries != null ?
                    RegistryOps.create(JsonOps.INSTANCE, this.registries) : JsonOps.INSTANCE;
            return VersionCompat.getOrThrow(codec.decode(ops, json)).getFirst();
        }
    }
}

