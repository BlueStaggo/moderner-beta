package mod.bluestaggo.modernerbeta.level.biome.injection.injector;

import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public class OceanBiomeInjector implements BiomeInjector {
    public static final com.mojang.serialization.MapCodec<OceanBiomeInjector> CODEC =
            com.mojang.serialization.MapCodec.unit(new OceanBiomeInjector());

    @Override
    public BiomeInjectorType<?> getType() {
        return BiomeInjectorType.OCEAN;
    }

    @Override
    public Holder<Biome> apply(BiomeInjectionContext context, int biomeX, int biomeY, int biomeZ) {
        return context.biomeSource.getOceanBiome(biomeX, biomeZ);
    }
}
