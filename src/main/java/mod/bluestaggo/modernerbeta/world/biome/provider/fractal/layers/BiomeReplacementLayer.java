package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BiomeReplacementLayer extends SingleParentLayer {
    public static final com.mojang.serialization.MapCodec<BiomeReplacementLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(Codec.unboundedMap(ExtendedBiomeId.CODEC, LayerTarget.CODEC).fieldOf("targets").forGetter(layer -> layer.targets))
            .apply(instance, BiomeReplacementLayer::new)
    );

    private final Map<ExtendedBiomeId, LayerTarget> targets;
    private transient Map<ExtendedBiomeId, LayerTarget.Configured> configuredTargets;

    public BiomeReplacementLayer(String id, long seed, String parent, Map<ExtendedBiomeId, LayerTarget> targets) {
        super(id, seed, parent);
        this.targets = targets;
    }

    public static BiomeReplacementLayer toBiomes(String id, long seed, String parent, Map<ExtendedBiomeId, ExtendedBiomeId> targets) {
        return new BiomeReplacementLayer(id, seed, parent, targets.entrySet().stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> LayerTarget.biome(entry.getValue())
                )
            ));
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.BIOME_REPLACEMENT;
    }

    @Override
    protected List<Layer> getParents() {
        return Stream.concat(
            Stream.of(this.parentLayer),
            this.configuredTargets.values().stream()
                .map(LayerTarget.Configured::asLayer)
                .flatMap(Optional::stream)
        ).toList();
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        ExtendedBiomeId baseBiome = this.parentLayer.sample(x, z);
        LayerTarget.Configured target = this.configuredTargets.get(baseBiome);
        if (target == null) {
            return baseBiome;
        }

        ExtendedBiomeId replacementBiome = target.sample(x, z);
        if (ExtendedBiomeId.NULL.equals(replacementBiome)) {
            return baseBiome;
        }

        return replacementBiome;
    }

    @Override
    public void configure(Function<String, Layer> layerMap) {
        super.configure(layerMap);
        this.configuredTargets = this.targets.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().configure(layerMap)));
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedBiomeId> biomes) {
        for (LayerTarget.Configured target : this.configuredTargets.values()) {
            if (target instanceof LayerTarget.Configured.OfLayer) {
                continue;
            }
            target.addPossibleBiomes(biomes);
        }
    }
}
