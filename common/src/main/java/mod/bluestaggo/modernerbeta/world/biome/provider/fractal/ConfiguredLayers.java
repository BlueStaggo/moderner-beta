package mod.bluestaggo.modernerbeta.world.biome.provider.fractal;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.ConstantBiomeLayer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class ConfiguredLayers {
    public static final Codec<ConfiguredLayers> CODEC = Layer.TYPE_CODEC.listOf().xmap(ConfiguredLayers::new, configuredLayers -> configuredLayers.layers);

    public static final ConfiguredLayers DEFAULT
        = new ConfiguredLayers(List.of(
            new ConstantBiomeLayer("land", 0, ExtendedBiomeId.PLAINS)));

    private final List<Layer> layers;
    private final Layer finalLayer;

    public ConfiguredLayers(List<Layer> layers) {
        this.layers = Collections.unmodifiableList(layers);
        this.finalLayer = layers.get(layers.size() - 1);

        Map<String, Layer> layerMap = new HashMap<>();
        AtomicInteger index = new AtomicInteger();
        Function<String, Layer> layerMapAccessor = id -> {
            Layer layer = layerMap.get(id);
            if (layer != null) {
                return layer;
            }
            throw new IllegalArgumentException("Layer of id \"" + id + "\" not present at layer at index \"" + index.get() + "\"");
        };

        for (Layer layer : layers) {
            layer.configure(layerMapAccessor);
            layerMap.put(layer.id, layer);
            index.getAndIncrement();
        }
    }

    public List<Layer> getAllLayers() {
        return this.layers;
    }

    public Layer getFinalLayer() {
        return this.finalLayer;
    }

    @Override
    public String toString() {
        return "ConfiguredLayers {" + this.layers.toString() + "}";
    }
}
