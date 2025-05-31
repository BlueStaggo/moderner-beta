package mod.bluestaggo.modernerbeta.api.world.biome;

import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;

public interface BiomeResolverExtendedId {
     /**
     * Gets a biome with its extended id at given biome coordinates.
     * Note that a single biome coordinate unit equals 4 blocks.
     *
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeY y-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     *
     * @return A biome at given biome coordinates.
     */
    ExtendedBiomeId getExtendedBiomeId(int biomeX, int biomeY, int biomeZ);
}
