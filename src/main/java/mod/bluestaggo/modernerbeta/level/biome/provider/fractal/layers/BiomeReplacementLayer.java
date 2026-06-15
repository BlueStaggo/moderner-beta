package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeIds;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.LayerTarget;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BiomeReplacementLayer extends SingleParentLayer {
    public static final com.mojang.serialization.MapCodec<BiomeReplacementLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(Codec.unboundedMap(ExtendedIdentifier.CODEC, LayerTarget.CODEC).fieldOf("targets").forGetter(layer -> layer.targets))
            .apply(instance, BiomeReplacementLayer::new)
    );

    private final Map<ExtendedIdentifier, LayerTarget> targets;
    private transient Map<ExtendedIdentifier, LayerTarget.Configured> configuredTargets;

    public BiomeReplacementLayer(String id, long seed, String parent, Map<ExtendedIdentifier, LayerTarget> targets) {
        super(id, seed, parent);
        this.targets = targets;
    }

    public static BiomeReplacementLayer toBiomes(String id, long seed, String parent, Map<ExtendedIdentifier, ExtendedIdentifier> targets) {
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
    protected ExtendedIdentifier generate(int x, int z) {
        ExtendedIdentifier baseBiome = this.parentLayer.sample(x, z);
        LayerTarget.Configured target = this.configuredTargets.get(baseBiome);
        if (target == null) {
            return baseBiome;
        }

        ExtendedIdentifier replacementBiome = target.sample(x, z);
        if (ExtendedBiomeIds.NULL.equals(replacementBiome)) {
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
    protected void addPossibleBiomes(Set<ExtendedIdentifier> biomes) {
        for (LayerTarget.Configured target : this.configuredTargets.values()) {
            if (target instanceof LayerTarget.Configured.OfLayer) {
                continue;
            }
            target.addPossibleBiomes(biomes);
        }
    }
}
