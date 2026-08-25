package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeResolver;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.LayerTarget;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class WeightedPoolLayer extends Layer {
    public static final com.mojang.serialization.MapCodec<WeightedPoolLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillLayerFields(instance)
            .and(WeightedList.codec(LayerTarget.CODEC).fieldOf("targets").forGetter(layer -> layer.targets))
            .apply(instance, WeightedPoolLayer::new)
    );

    private final WeightedList<LayerTarget> targets;
    private transient WeightedList<LayerTarget.Configured> configuredTargets;

    public WeightedPoolLayer(String id, long seed, WeightedList<LayerTarget> targets) {
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
        this.configuredTargets = this.targets.map(target -> target.configure(layerMap));
        //?} else {
        /*WeightedList.Builder<LayerTarget.Configured> poolBuilder = WeightedList.builder();
        for (net.minecraft.util.random.WeightedEntry.Wrapper<LayerTarget> entry : this.targets.unwrap()) {
            poolBuilder.add(
                VersionCompat.getWeightedValue(entry).configure(layerMap),
                entry.getWeight().asInt()
            );
        }
        this.configuredTargets = poolBuilder.build();
        *///?}
    }

    @Override
    protected void bindOwnBiomes(ExtendedBiomeResolver biomeResolver) {
        this.configuredTargets.unwrap().stream()
            .map(VersionCompat::getWeightedValue)
            .forEach(target -> target.bindBiomes(biomeResolver));
    }

    @Override
    protected ExtendedHolder<Biome> generate(int x, int z) {
        return VersionCompat.accessPool(this.configuredTargets, this.getRandom(x, z)).sample(x, z);
    }

    @Override
    protected List<Layer> getParents() {
        return this.configuredTargets.unwrap().stream()
            .map(VersionCompat::getWeightedValue)
            .map(LayerTarget.Configured::asLayer)
            .flatMap(Optional::stream)
            .toList();
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedHolder<Biome>> biomes) {
        this.configuredTargets.unwrap().stream()
            .map(VersionCompat::getWeightedValue)
            .forEach(target -> target.addPossibleBiomes(biomes));
    }
}
