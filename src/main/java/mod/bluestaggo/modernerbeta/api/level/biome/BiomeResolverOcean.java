package mod.bluestaggo.modernerbeta.api.level.biome;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public interface BiomeResolverOcean {
    /**
     * Gets an ocean biome to overwrite the original biome at given biome coordinates.
     *
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     *
     * @return A biome at given biome coordinates. May return null, in which case original biome is not replaced.
     */
    Holder<Biome> getOceanBiome(int biomeX, int biomeZ);

    /**
     * Gets a deep ocean biome to overwrite the original biome at given biome coordinates.
     *
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     *
     * @return A biome at given biome coordinates. May return null, in which case original biome is not replaced.
     */
    default Holder<Biome> getDeepOceanBiome(int biomeX, int biomeZ) {
        return this.getOceanBiome(biomeX, biomeZ);
    }

    /**
     * Gets an ocean biome to overwrite the original biome at given biome coordinates.
     * 
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeY y-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     *
     * @return A biome at given biome coordinates. May return null, in which case original biome is not replaced.
     * @deprecated The 3D variant of the method will be removed in 5.0.0
     */
    @Deprecated(forRemoval = true)
    default Holder<Biome> getOceanBiome(int biomeX, int biomeY, int biomeZ) {
        return this.getOceanBiome(biomeX, biomeZ);
    }
    
    /**
     * Gets a deep ocean biome to overwrite the original biome at given biome coordinates.
     * 
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeY y-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     *
     * @return A biome at given biome coordinates. May return null, in which case original biome is not replaced.
     * @deprecated The 3D variant of the method will be removed in 5.0.0
     */
    @Deprecated(forRemoval = true)
    default Holder<Biome> getDeepOceanBiome(int biomeX, int biomeY, int biomeZ) {
        return this.getOceanBiome(biomeX, biomeZ);
    }
}
