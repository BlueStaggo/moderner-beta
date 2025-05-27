package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;

import java.util.function.Supplier;

public class SingleMatchBiomePredicate extends BiomePredicate {
    public static final MapCodec<SingleMatchBiomePredicate> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance
            .group(
                ExtendedBiomeId.CODEC.fieldOf("biome").forGetter(predicate -> predicate.biome)
            )
            .apply(instance, SingleMatchBiomePredicate::new)
    );

    private final ExtendedBiomeId biome;

    public SingleMatchBiomePredicate(ExtendedBiomeId biome) {
        this.biome = biome;
    }

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.SINGLE_MATCH;
    }

    @Override
    public boolean matches(ExtendedBiomeId biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        return this.biome.equals(biome);
    }
}
