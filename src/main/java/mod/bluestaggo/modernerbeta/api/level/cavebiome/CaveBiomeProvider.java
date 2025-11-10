package mod.bluestaggo.modernerbeta.api.level.cavebiome;

import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public abstract class CaveBiomeProvider {
    protected final ModernBetaSettings settings;
    protected final HolderGetter<Biome> biomeRegistry;
    protected final long seed;
    
    /**
     * Constructs a Modern Beta cave biome provider initialized with seed.
     * Additional settings are supplied in NbtCompound parameter.
     * 
     * @param seed World seed.
     * @param settings Biome settings.
     */
    public CaveBiomeProvider(ModernBetaSettings settings, HolderGetter<Biome> biomeRegistry, long seed) {
        this.settings = settings;
        this.biomeRegistry = biomeRegistry;
        this.seed = seed;
    }
    
    /**
     * Gets a cave biome to overwrite the original biome at given biome coordinates.
     * 
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeY y-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     * 
     * @return A biome at given biome coordinates. May return null, in which case original biome is not replaced.
     */
    public abstract Holder<Biome> getBiome(int biomeX, int biomeY, int biomeZ);
    
    /**
     * Gets a list of biomes for biome source, for the purpose of locating structures, etc.
     * 
     * @return A list of biomes.
     */
    public List<Holder<Biome>> getBiomes() {
        return List.of();
    }
}
