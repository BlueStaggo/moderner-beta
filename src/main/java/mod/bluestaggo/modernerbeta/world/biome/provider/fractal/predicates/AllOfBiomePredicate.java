package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.predicates;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record AllOfBiomePredicate(List<BiomePredicate> terms) implements BiomePredicate {
    public static final com.mojang.serialization./*Map*/Codec<AllOfBiomePredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            BiomePredicate.BASE_CODEC.listOf().fieldOf("terms").forGetter(AllOfBiomePredicate::terms)
        ).apply(instance, AllOfBiomePredicate::new)
    );

    @Override
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
    public boolean matches(ExtendedBiomeId biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        for (BiomePredicate term : this.terms) {
            if (!term.matches(biome, layer, randomSupplier, x, z)) {
                return false;
            }
        }
        return true;
    }
}
