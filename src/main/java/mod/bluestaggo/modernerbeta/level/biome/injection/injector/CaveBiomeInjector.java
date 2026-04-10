package mod.bluestaggo.modernerbeta.level.biome.injection.injector;

import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public class CaveBiomeInjector implements BiomeInjector {
    public static final com.mojang.serialization.MapCodec<CaveBiomeInjector> CODEC =
            com.mojang.serialization.MapCodec.unit(new CaveBiomeInjector());

    @Override
    public BiomeInjectorType<?> getType() {
        return BiomeInjectorType.CAVE_BIOME;
    }

    @Override
    public Holder<Biome> apply(BiomeInjectionContext context, int biomeX, int biomeY, int biomeZ) {
        return context.biomeSource.getCaveBiome(biomeX, biomeY, biomeZ);
    }
}
