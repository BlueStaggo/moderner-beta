package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerRandom;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Supplier;

public record RandomChanceBiomePredicate(int numerator, int denominator) implements BiomePredicate {
    public static final com.mojang.serialization.MapCodec<RandomChanceBiomePredicate> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            Codec.INT.fieldOf("numerator").orElse(1).forGetter(predicate -> predicate.numerator),
            Codec.INT.fieldOf("denominator").forGetter(predicate -> predicate.denominator)
        ).apply(instance, RandomChanceBiomePredicate::new)
    );

    @Override
    public BiomePredicateType<?> getType() {
        return BiomePredicateType.RANDOM_CHANCE;
    }

    @Override
    public boolean matches(ExtendedHolder<Biome> biome, Layer layer, Supplier<LayerRandom> randomSupplier, int x, int z) {
        return randomSupplier.get().nextInt(this.denominator) < this.numerator;
    }
}
