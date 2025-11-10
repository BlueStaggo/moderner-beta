package mod.bluestaggo.modernerbeta.api.level.biome;

import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeId;

public interface BiomeResolverExtendedIdStepped extends BiomeResolverExtendedId, BiomeResolverStepped {
    ExtendedBiomeId getExtendedBiomeIdForStep(int biomeX, int biomeY, int biomeZ, int step);
}
