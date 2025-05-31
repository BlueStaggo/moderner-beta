package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BiomeToLayerOverlayLayer extends SingleParentLayer {
    public static final MapCodec<BiomeToLayerOverlayLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(Codec.unboundedMap(ExtendedBiomeId.CODEC, Codec.STRING).fieldOf("targets").forGetter(layer -> layer.targets))
            .apply(instance, BiomeToLayerOverlayLayer::new)
    );

    private final Map<ExtendedBiomeId, String> targets;
    private transient Map<ExtendedBiomeId, Layer> targetLayers;

    public BiomeToLayerOverlayLayer(String id, long seed, String parent, Map<ExtendedBiomeId, String> targets) {
        super(id, seed, parent);
        this.targets = targets;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.BIOME_TO_LAYER_OVERLAY;
    }

    @Override
    protected List<Layer> getParents() {
        return Stream.concat(
            Stream.of(this.parentLayer),
            this.targetLayers.values().stream()
        ).toList();
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        ExtendedBiomeId baseBiome = this.parentLayer.sample(x, z);
        Layer targetLayer = this.targetLayers.get(baseBiome);
        if (targetLayer == null) {
            return baseBiome;
        }

        ExtendedBiomeId replacementBiome = targetLayer.sample(x, z);
        if (ExtendedBiomeId.NULL.equals(replacementBiome)) {
            return baseBiome;
        }

        return replacementBiome;
    }

    @Override
    public void configure(Function<String, Layer> layerMap) {
        super.configure(layerMap);
        this.targetLayers = this.targets.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> layerMap.apply(entry.getValue())));
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedBiomeId> biomes) {
        biomes.addAll(this.targets.keySet());
    }
}
