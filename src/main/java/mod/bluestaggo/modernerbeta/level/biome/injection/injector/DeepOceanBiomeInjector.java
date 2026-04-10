package mod.bluestaggo.modernerbeta.level.biome.injection.injector;

import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public class DeepOceanBiomeInjector implements BiomeInjector {
    public static final com.mojang.serialization.MapCodec<DeepOceanBiomeInjector> CODEC =
            com.mojang.serialization.MapCodec.unit(new DeepOceanBiomeInjector());

    @Override
    public BiomeInjectorType<?> getType() {
        return BiomeInjectorType.DEEP_OCEAN;
    }

    @Override
    public Holder<Biome> apply(BiomeInjectionContext context, int biomeX, int biomeY, int biomeZ) {
        return context.biomeSource.getDeepOceanBiome(biomeX, biomeZ);
    }
}
