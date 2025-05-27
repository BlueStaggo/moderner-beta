package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;

import java.util.Set;
import java.util.function.Supplier;

public class InSetBiomePredicate extends BiomePredicate {
    public static final MapCodec<InSetBiomePredicate> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance
            .group(
                CodecUtil.set(ExtendedBiomeId.CODEC).fieldOf("biomes").forGetter(predicate -> predicate.biomes)
            )
            .apply(instance, InSetBiomePredicate::new)
    );

    private final Set<ExtendedBiomeId> biomes;

    public InSetBiomePredicate(Set<ExtendedBiomeId> biomes) {
        this.biomes = biomes;
    }

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.IN_SET;
    }

    @Override
    public boolean satisfies(ExtendedBiomeId biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        return this.biomes.contains(biome);
    }
}
