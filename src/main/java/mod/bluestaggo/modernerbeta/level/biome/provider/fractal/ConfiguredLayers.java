package mod.bluestaggo.modernerbeta.level.biome.provider.fractal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.ConstantBiomeLayer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConfiguredLayers {
    public static final Codec<ConfiguredLayers> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Layer.TYPE_CODEC.listOf().fieldOf("pipeline").forGetter(configuredLayers -> configuredLayers.pipeline),
            Codec.unboundedMap(ResourceLocation.CODEC, Codec.STRING).fieldOf("outputs")
                .forGetter(configuredLayers -> configuredLayers.outputs.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().id)))
        ).apply(instance, ConfiguredLayers::new)
    );

    public static final ConfiguredLayers DEFAULT
        = new ConfiguredLayers(
            List.of(new ConstantBiomeLayer("land", 0, ExtendedBiomeId.PLAINS)),
            Map.of(ModernBetaBuiltInTypes.LayerOutput.BIOME.id, "land")
        );

    private final List<Layer> pipeline;
    private final Map<ResourceLocation, Layer> outputs;

    public ConfiguredLayers(List<Layer> pipeline, Map<ResourceLocation, String> outputs) {
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

    public Optional<Layer> getOutput(ResourceLocation output) {
        return Optional.ofNullable(this.outputs.get(output));
    }

    public Layer getOutputOrThrow(ResourceLocation output) {
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
