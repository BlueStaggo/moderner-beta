package mod.bluestaggo.modernerbeta.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gson.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.mixin.RegistryOpsAccessor;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.ExtraCodecs;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.function.Function;

public class CodecUtil {
    public static <T> RecordCodecBuilder<T, RegistryOps.RegistryInfoLookup> registryLookupCodec() {
        return ExtraCodecs.retrieveContext(
            dynamicOps -> dynamicOps instanceof RegistryOps<?> registryOps
                    ? DataResult.success(((RegistryOpsAccessor) registryOps).getLookupProvider())
                    : DataResult.error(() -> "Not a registry ops")
        ).forGetter(object -> null);
    }

    public static <A> MapCodec<A> lookupIfEmpty(
        MapCodec<A> codec,
        Function<RegistryOps.RegistryInfoLookup, A> getter
    ) {
        return codec.mapResult(new MapCodec.ResultFunction<>() {
            @Override
            public <T> DataResult<A> apply(DynamicOps<T> ops, MapLike<T> input, DataResult<A> a) {
                if (!(ops instanceof RegistryOps<?> registryOps))
                    return DataResult.error(() -> "Not a registry ops");

                RegistryOps.RegistryInfoLookup lookup = ((RegistryOpsAccessor) registryOps).getLookupProvider();
                return DataResult.success(a.result().orElseGet(() -> getter.apply(lookup)));
            }

            @Override
            public <T> RecordBuilder<T> coApply(DynamicOps<T> ops, A input, RecordBuilder<T> t) {
                return t;
            }
        });
    }

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

    /**
     * Transforms the given {@link Codec} into a {@link MapCodec} by assuming that the result of all elements is a map.
     * <p>
     * This {@link MapCodec} will fail to encode or decode as long as the given {@link Codec} does not return or receive
     * a map.
     */
    public static <A> MapCodec<A> assumeMapUnsafe(final Codec<A> codec) {
        //? if >=1.21 {
        return MapCodec.assumeMapUnsafe(codec);
        //? } else {
        /*return new MapCodec<>() {
            private static final String COMPRESSED_VALUE_KEY = "value";

            @Override
            public <T> java.util.stream.Stream<T> keys(final DynamicOps<T> ops) {
                return java.util.stream.Stream.of(ops.createString(COMPRESSED_VALUE_KEY));
            }

            @Override
            public <T> DataResult<A> decode(final DynamicOps<T> ops, final MapLike<T> input) {
                if (ops.compressMaps()) {
                    final T value = input.get(COMPRESSED_VALUE_KEY);
                    if (value == null) {
                        return DataResult.error(() -> "Missing value");
                    }
                    return codec.parse(ops, value);
                }
                return codec.parse(ops, ops.createMap(input.entries()));
            }

            @Override
            public <T> RecordBuilder<T> encode(final A input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
                final DataResult<T> encoded = codec.encodeStart(ops, input);
                if (ops.compressMaps()) {
                    return prefix.add(COMPRESSED_VALUE_KEY, encoded);
                }
                final DataResult<MapLike<T>> encodedMapResult = encoded.flatMap(ops::getMap);
                return encodedMapResult.map(encodedMap -> {
                    encodedMap.entries().forEach(pair -> prefix.add(pair.getFirst(), pair.getSecond()));
                    return prefix;
                }).result().orElseGet(() -> prefix.withErrorsFrom(encodedMapResult));
            }
        };
        *///? }
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

