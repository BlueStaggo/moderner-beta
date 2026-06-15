package mod.bluestaggo.modernerbeta.api.level.biome;

import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;

public interface BiomeResolverExtendedIdStepped extends BiomeResolverExtendedId, BiomeResolverStepped {
    ExtendedIdentifier getExtendedBiomeIdForStep(int biomeX, int biomeY, int biomeZ, int step);
}
