package mod.bluestaggo.modernerbeta.level.biome.provider.fractal;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.ConstantBiomeLayer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConfiguredLayers {
    private static final Codec<ConfiguredLayers> BASE_CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Layer.TYPE_CODEC.listOf().fieldOf("pipeline").forGetter(configuredLayers -> configuredLayers.pipeline),
            Codec.unboundedMap(Identifier.CODEC, Codec.STRING).fieldOf("outputs")
                .forGetter(configuredLayers -> configuredLayers.outputs.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().id)))
        ).apply(instance, ConfiguredLayers::new)
    );
    public static final Codec<ConfiguredLayers> CODEC = createCodec(false);
    public static final Codec<ConfiguredLayers> LENIENT_CODEC = createCodec(true);

    public static final ConfiguredLayers DEFAULT
        = new ConfiguredLayers(
            List.of(new ConstantBiomeLayer("land", 0, ExtendedBiomeIds.PLAINS)),
            Map.of(ModernBetaBuiltInTypes.LayerOutput.BIOME.id, "land")
        );

    private final List<Layer> pipeline;
    private final Map<Identifier, Layer> outputs;

    public ConfiguredLayers(List<Layer> pipeline, Map<Identifier, String> outputs) {
        this.pipeline = Collections.unmodifiableList(pipeline);

        Map<String, Layer> layerMap = new HashMap<>();
        AtomicInteger index = new AtomicInteger();
        Function<String, Layer> layerMapAccessor = id -> {
            Layer layer = layerMap.get(id);
            if (layer != null) {
                return layer;
            }
            throw new IllegalArgumentException("Layer of id \"" + id + "\" not present at layer at index \"" + index.get() + "\"");
        };

        for (Layer layer : pipeline) {
            layer.configure(layerMapAccessor);
            layerMap.put(layer.id, layer);
            index.getAndIncrement();
        }

        this.outputs = outputs.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> layerMap.get(entry.getValue())));
    }

    public List<Layer> getPipeline() {
        return this.pipeline;
    }

    public ConfiguredLayers copy(HolderGetter<Biome> biomeRegistry) {
        RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, new RegistryOps.RegistryInfoLookup() {
            @Override
            @SuppressWarnings("unchecked")
            //~ if >=26.3 'RegistryOps.RegistryInfo<T>' -> 'HolderGetter<T>'
            public <T> Optional<RegistryOps.RegistryInfo<T>> lookup(
                ResourceKey<? extends Registry<? extends T>> registryKey
            ) {
                if (!registryKey.equals(Registries.BIOME)) {
                    return Optional.empty();
                }

                //? if >=26.3 {
                /*return Optional.of((HolderGetter<T>) biomeRegistry);
                *///? } else {
                return Optional.of(new RegistryOps.RegistryInfo<>(
                    null,
                    (HolderGetter<T>) biomeRegistry,
                    Lifecycle.stable()
                ));
                //? }
            }
        });

        return VersionCompat.getOrThrow(
            CODEC.encodeStart(ops, this)
                .flatMap(value -> CODEC.parse(ops, value))
        );
    }

    public void bindBiomes(HolderGetter<Biome> biomeRegistry) {
        ExtendedBiomeResolver resolver = new ExtendedBiomeResolver(biomeRegistry);
        for (Layer layer : this.pipeline) {
            layer.bindBiomes(resolver);
        }
    }

    private static <T> DataResult<Pair<ConfiguredLayers, T>> bindFromOps(
        DynamicOps<T> ops,
        Pair<ConfiguredLayers, T> pair,
        boolean lenient
    ) {
        if (!(ops instanceof RegistryOps<?> registryOps)) {
            return lenient
                ? DataResult.success(pair)
                : DataResult.error(() -> "RegistryOps is required to resolve fractal biome holders");
        }
        Optional<HolderGetter<Biome>> biomeRegistry = registryOps.getter(Registries.BIOME);
        if (biomeRegistry.isEmpty()) {
            return DataResult.error(() -> "Biome registry is unavailable");
        }
        try {
            pair.getFirst().bindBiomes(biomeRegistry.get());
            return DataResult.success(pair);
        } catch (RuntimeException exception) {
            return DataResult.error(() -> "Unable to resolve fractal biome holders: " + exception.getMessage());
        }
    }

    private static Codec<ConfiguredLayers> createCodec(boolean lenient) {
        return Codec.of(BASE_CODEC, new Decoder<>() {
            @Override
            public <T> DataResult<Pair<ConfiguredLayers, T>> decode(DynamicOps<T> ops, T input) {
                return BASE_CODEC.decode(ops, input)
                    .flatMap(pair -> bindFromOps(ops, pair, lenient));
            }
        });
    }

    public Optional<Layer> getOutput(Identifier output) {
        return Optional.ofNullable(this.outputs.get(output));
    }

    public Layer getOutputOrThrow(Identifier output) {
        Layer layer = this.outputs.get(output);
        if (layer == null) {
            throw new IllegalArgumentException("No layer provided for required output \"" + output + "\"");
        }
        return layer;
    }

    @Override
    public String toString() {
        return "ConfiguredLayers {" + this.pipeline.toString() + "}";
    }
}
