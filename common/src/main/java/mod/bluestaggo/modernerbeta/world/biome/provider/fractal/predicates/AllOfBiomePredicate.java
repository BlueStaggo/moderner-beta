package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AllOfBiomePredicate extends BiomePredicate {
    public static final MapCodec<AllOfBiomePredicate> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance
            .group(
                BiomePredicate.BASE_CODEC.listOf().fieldOf("terms").forGetter(predicate -> predicate.terms)
            )
            .apply(instance, AllOfBiomePredicate::new)
    );

    private final List<BiomePredicate> terms;

    public AllOfBiomePredicate(List<BiomePredicate> terms) {
        this.terms = terms;
    }

    public BiomePredicate and(BiomePredicate other) {
        List<BiomePredicate> terms = new ArrayList<>(this.terms);
        terms.add(other);
        return new AllOfBiomePredicate(terms);
    }

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.ALL_OF;
    }

    @Override
    public boolean satisfies(ExtendedBiomeId biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        for (BiomePredicate term : this.terms) {
            if (!term.satisfies(biome, layer, randomSupplier, x, z)) {
                return false;
            }
        }
        return true;
    }
}
