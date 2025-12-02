package mod.bluestaggo.modernerbeta.settings;

import com.mojang.serialization.*;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.settings.component.validation.ComponentValidator;
import net.minecraft.resources.RegistryOps;

import java.util.Map;
import java.util.function.Function;

//? if <1.20.5 {
/*import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
*///?}

public record SettingsComponentType<T>(Codec<T> codec, Function<RegistryOps.RegistryInfoLookup, T> defaultValueGetter, ComponentValidator<T> validator) {
    public static final Codec<SettingsComponentType<?>> CODEC =
        //? if >=1.20.5 {
        Codec.lazyInitialized(ModernBetaRegistries.SETTINGS_COMPONENT_TYPE::byNameCodec);
        //?} else {
        /*ModernBetaRegistries.SETTINGS_COMPONENT_TYPE.byNameCodec();
        *///?}
    //? if <1.20.5
    //@SuppressWarnings("unchecked")
    public static final Codec<Map<SettingsComponentType<?>, Object>> TYPE_TO_VALUE_MAP_CODEC =
        //? if >=1.20.5 {
        Codec.dispatchedMap(CODEC, SettingsComponentType::codec);
        //?} else {
        /*Codec.of(
            new Encoder<>() {
                @Override
                public <T2> DataResult<T2> encode(Map<SettingsComponentType<?>, Object> input, DynamicOps<T2> ops, T2 prefix) {
                    var recordBuilder = (RecordBuilder<Object>) ops.mapBuilder();
                    for (Map.Entry<SettingsComponentType<?>, Object> entry : input.entrySet()) {
                        DataResult<T2> identifierResult = ResourceLocation.CODEC.encodeStart(
                            ops, ModernBetaRegistries.SETTINGS_COMPONENT_TYPE.getKey(entry.getKey()));
                        if (identifierResult.result().isEmpty()) {
                            return DataResult.error(identifierResult.error().orElseThrow()::message);
                        }

                        DataResult<T2> entryResult = ((Codec<Object>) entry.getKey().codec()).encodeStart(ops, entry.getValue());
                        if (entryResult.result().isEmpty()) {
                            return DataResult.error(entryResult.error().orElseThrow()::message);
                        }

                        recordBuilder.add(
                            identifierResult.result().get(),
                            entryResult.result().get()
                        );
                    }
                    return (DataResult<T2>) recordBuilder.build(prefix);
                }
            },
            new Decoder<>() {
                @Override
                public <T2> DataResult<Pair<Map<SettingsComponentType<?>, Object>, T2>> decode(DynamicOps<T2> ops, T2 input) {
                    DataResult<MapLike<T2>> mapResult = ops.getMap(input);
                    if (mapResult.result().isEmpty()) return DataResult.error(mapResult.error().orElseThrow()::message);

                    List<DataResult<Pair<SettingsComponentType<?>, Object>>> entries = mapResult.result().orElseThrow().entries()
                        .<DataResult<Pair<SettingsComponentType<?>, Object>>>map(pair -> {
                            DataResult<ResourceLocation> identifierResult = ResourceLocation.CODEC.decode(ops, pair.getFirst()).map(Pair::getFirst);
                            if (identifierResult.result().isEmpty()) {
                                return DataResult.error(identifierResult.error().orElseThrow()::message);
                            }

                            SettingsComponentType<?> settingsComponentType
                                = ModernBetaRegistries.SETTINGS_COMPONENT_TYPE.get(identifierResult.result().get());
                            if (settingsComponentType == null) {
                                return DataResult.error(() -> "Settings component type \"" + identifierResult.result());
                            }

                            DataResult<? extends Pair<?, T2>> settingsComponentResult = settingsComponentType.codec().decode(ops, pair.getSecond());
                            if (settingsComponentResult.result().isEmpty()) {
                                return DataResult.error(settingsComponentResult.error().orElseThrow()::message);
                            }

                            return DataResult.success(Pair.of(
                                settingsComponentType,
                                settingsComponentResult.result().get().getFirst()
                            ));
                        })
                        .toList();

                    ImmutableMap.Builder<SettingsComponentType<?>, Object> mapBuilder = ImmutableMap.builder();
                    for (var result : entries) {
                        if (result.result().isPresent()) {
                            var entry = result.result().get();
                            mapBuilder.put(entry.getFirst(), entry.getSecond());
                        } else {
                            return DataResult.error(result.error().orElseThrow()::message);
                        }
                    }

                    return DataResult.success(Pair.of(mapBuilder.build(), input));
                }
            }
        );
        *///?}

    public SettingsComponentType(Codec<T> codec, T defaultValue, ComponentValidator<T> validator) {
        this(codec, registries -> defaultValue, validator);
    }

    public T defaultValue() {
        return defaultValueGetter.apply(null);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
}
