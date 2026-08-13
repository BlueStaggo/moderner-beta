package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates;

import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerRandom;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Supplier;

public record SingleMatchBiomePredicate(ExtendedIdentifier biome) implements BiomePredicate {
    public static final com.mojang.serialization.MapCodec<SingleMatchBiomePredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            ExtendedIdentifier.CODEC.fieldOf("biome").forGetter(predicate -> predicate.biome)
        ).apply(instance, SingleMatchBiomePredicate::new)
    );

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.SINGLE_MATCH;
    }

    @Override
    public boolean matches(ExtendedHolder<Biome> biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        return biome.is(this.biome);
    }
}
