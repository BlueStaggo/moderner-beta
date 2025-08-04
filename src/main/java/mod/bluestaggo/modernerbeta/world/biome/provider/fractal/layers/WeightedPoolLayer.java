//~datapool
package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.LayerTarget;
import net.minecraft.util.collection.Pool;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class WeightedPoolLayer extends Layer {
    public static final com.mojang.serialization.MapCodec<WeightedPoolLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillLayerFields(instance)
            .and(Pool.createCodec(LayerTarget.CODEC).fieldOf("targets").forGetter(layer -> layer.targets))
            .apply(instance, WeightedPoolLayer::new)
    );

    private final Pool<LayerTarget> targets;
    private transient Pool<LayerTarget.Configured> configuredTargets;

    public WeightedPoolLayer(String id, long seed, Pool<LayerTarget> targets) {
        super(id, seed);
        this.targets = targets;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.WEIGHTED_POOL;
    }

    @Override
    public void configure(Function<String, Layer> layerMap) {
        //? if >=1.21.5 {
        this.configuredTargets = this.targets.transform(target -> target.configure(layerMap));
        //?} else {
        /*Pool.Builder<LayerTarget.Configured> poolBuilder = Pool.builder();
        for (net.minecraft.util.collection.Weighted.Present<LayerTarget> entry : this.targets.getEntries()) {
            poolBuilder.add(
                VersionCompat.getWeightedValue(entry).configure(layerMap),
                entry.getWeight().getValue()
            );
        }
        this.configuredTargets = poolBuilder.build();
        *///?}
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        return VersionCompat.accessPool(this.configuredTargets, this.getRandom(x, z)).sample(x, z);
    }

    @Override
    protected List<Layer> getParents() {
        return this.configuredTargets.getEntries().stream()
            .map(VersionCompat::getWeightedValue)
            .map(LayerTarget.Configured::asLayer)
            .flatMap(Optional::stream)
            .toList();
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedBiomeId> biomes) {
        this.configuredTargets.getEntries().stream()
            .map(VersionCompat::getWeightedValue)
            .forEach(target -> target.addPossibleBiomes(biomes));
    }
}
