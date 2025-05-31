package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates.BiomePredicate;

public class ConditionalBiomeOverlayLayer extends SingleParentLayer {
    public static final MapCodec<ConditionalBiomeOverlayLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(instance.group(
                BiomePredicate.BASE_CODEC.fieldOf("predicate").forGetter(layer -> layer.predicate),
                ExtendedBiomeId.CODEC.fieldOf("onMatch").forGetter(layer -> layer.onMatch),
                ExtendedBiomeId.CODEC.fieldOf("otherwise").forGetter(layer -> layer.otherwise)
            ))
            .apply(instance, ConditionalBiomeOverlayLayer::new)
    );

    private final BiomePredicate predicate;
    private final ExtendedBiomeId onMatch;
    private final ExtendedBiomeId otherwise;

    public ConditionalBiomeOverlayLayer(String id, long seed, String parent, BiomePredicate predicate, ExtendedBiomeId onMatch, ExtendedBiomeId otherwise) {
        super(id, seed, parent);
        this.predicate = predicate;
        this.onMatch = onMatch;
        this.otherwise = otherwise;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.CONDITIONAL_BIOME_OVERLAY;
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        ExtendedBiomeId baseBiome = this.parentLayer.sample(x, z);
        ExtendedBiomeId outputBiome = this.predicate.matches(baseBiome, this.parentLayer, Suppliers.memoize(() -> this.getRandom(x, z)), x, z)
            ? this.onMatch : this.otherwise;
        return ExtendedBiomeId.NULL.equals(outputBiome) ? baseBiome : outputBiome;
    }
}
