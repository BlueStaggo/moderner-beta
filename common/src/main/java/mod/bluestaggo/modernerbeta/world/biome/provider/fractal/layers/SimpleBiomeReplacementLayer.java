package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleBiomeReplacementLayer extends SingleParentLayer {
    public static final MapCodec<SimpleBiomeReplacementLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(Codec.unboundedMap(ExtendedBiomeId.CODEC, ExtendedBiomeId.CODEC).fieldOf("targets").forGetter(layer -> layer.targets))
            .apply(instance, SimpleBiomeReplacementLayer::new)
    );

    private final Map<ExtendedBiomeId, ExtendedBiomeId> targets;

    public SimpleBiomeReplacementLayer(String id, long seed, String parent, Map<ExtendedBiomeId, ExtendedBiomeId> targets) {
        super(id, seed, parent);
        this.targets = targets;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.SIMPLE_BIOME_REPLACEMENT;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        ExtendedBiomeId biome = this.parentLayer.sample(x, z);
        return this.targets.getOrDefault(biome, biome);
    }

    @Override
    public synchronized ExtendedBiomeId sample(int x, int z) {
        return this.generate(x, z);
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedBiomeId> biomes) {
        this.targets.entrySet().stream()
            .filter(target -> biomes.contains(target.getKey()))
            .map(Map.Entry::getValue)
            .forEach(biomes::add);
    }
}
