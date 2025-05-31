package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AnyOfBiomePredicate extends BiomePredicate {
    public static final MapCodec<AnyOfBiomePredicate> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance
            .group(
                BiomePredicate.BASE_CODEC.listOf().fieldOf("terms").forGetter(predicate -> predicate.terms)
            )
            .apply(instance, AnyOfBiomePredicate::new)
    );

    private final List<BiomePredicate> terms;

    public AnyOfBiomePredicate(List<BiomePredicate> terms) {
        this.terms = terms;
    }

    public BiomePredicate or(BiomePredicate other) {
        List<BiomePredicate> terms = new ArrayList<>(this.terms);
        terms.add(other);
        return new AnyOfBiomePredicate(terms);
    }

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.ANY_OF;
    }

    @Override
    public boolean matches(ExtendedBiomeId biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        for (BiomePredicate term : this.terms) {
            if (term.matches(biome, layer, randomSupplier, x, z)) {
                return true;
            }
        }
        return false;
    }
}
