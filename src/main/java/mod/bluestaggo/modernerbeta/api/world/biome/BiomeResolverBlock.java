package mod.bluestaggo.modernerbeta.api.world.biome;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public interface BiomeResolverBlock {
    /**
     * Gets a biome at given block coordinates, for purpose of surface generation.
     * 
     * @param x x-coordinate in block coordinates.
     * @param y y-coordinate in block coordinates.
     * @param z z-coordinate in block coordinates.
     * 
     * @return A biome at given block coordinates.
     */
    Holder<Biome> getBiomeBlock(int x, int y, int z);
}
