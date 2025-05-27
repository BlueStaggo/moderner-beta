package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;

import java.util.Set;
import java.util.function.Supplier;

public class SpecificCaseBiomePredicate extends BiomePredicate {
    public static final MapCodec<SpecificCaseBiomePredicate> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance
            .group(
                BiomePredicate.BASE_CODEC.fieldOf("predicate").forGetter(predicate -> predicate.predicate),
                CodecUtil.set(ExtendedBiomeId.CODEC).fieldOf("biomes").forGetter(predicate -> predicate.biomes)
            )
            .apply(instance, SpecificCaseBiomePredicate::new)
    );

    private final BiomePredicate predicate;
    private final Set<ExtendedBiomeId> biomes;

    public SpecificCaseBiomePredicate(BiomePredicate predicate, Set<ExtendedBiomeId> biomes) {
        this.predicate = predicate;
        this.biomes = biomes;
    }

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.SPECIFIC_CASE;
    }

    @Override
    public boolean satisfies(ExtendedBiomeId biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        return !this.biomes.contains(biome) || this.predicate.satisfies(biome, layer, randomSupplier, x, z);
    }
}
