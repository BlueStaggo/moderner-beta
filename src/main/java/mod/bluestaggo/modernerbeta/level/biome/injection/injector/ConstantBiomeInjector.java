package mod.bluestaggo.modernerbeta.level.biome.injection.injector;

import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.Set;

public record ConstantBiomeInjector(Holder<Biome> biome) implements BiomeInjector {
    public static final com.mojang.serialization.MapCodec<ConstantBiomeInjector> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            Biome.CODEC.fieldOf("biome").forGetter(ConstantBiomeInjector::biome)
        ).apply(instance, ConstantBiomeInjector::new)
    );

    @Override
    public BiomeInjectorType<?> getType() {
        return BiomeInjectorType.CONSTANT_BIOME;
    }

    @Override
    public Holder<Biome> apply(BiomeInjectionContext context, int biomeX, int biomeY, int biomeZ) {
        return biome;
    }

    @Override
    public Set<Holder<Biome>> getPossibleBiomes() {
        return Set.of(biome);
    }
}
