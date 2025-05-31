package mod.bluestaggo.modernerbeta.api.world.biome;

import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;

public interface BiomeResolverExtendedIdStepped extends BiomeResolverExtendedId, BiomeResolverStepped {
    ExtendedBiomeId getExtendedBiomeIdForStep(int biomeX, int biomeY, int biomeZ, int step);
}
