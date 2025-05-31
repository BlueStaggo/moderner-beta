package mod.bluestaggo.modernerbeta.api.world.biome;

import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.world.biome.Biome;

import java.util.List;

public abstract class BiomeProvider {
    protected final ModernBetaSettings settings;
    protected final RegistryEntryLookup<Biome> biomeRegistry;
    protected final long seed;
    
    /**
     * Constructs a Modern Beta biome provider initialized with seed.
     * Additional settings are supplied in NbtCompound parameter.
     * 
     * @param settings Biome settings.
     * @param biomeRegistry Minecraft biome registry.
     */
    public BiomeProvider(ModernBetaSettings settings, RegistryEntryLookup<Biome> biomeRegistry, long seed) {
        this.settings = settings;
        this.biomeRegistry = biomeRegistry;
        this.seed = seed;
    }
    
    /**
     * Gets a biome for biome source at given biome coordinates.
     * Note that a single biome coordinate unit equals 4 blocks.
     * 
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeY y-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     * 
     * @return A biome at given biome coordinates.
     */
    public abstract RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ);
    
    /**
     * Gets a list of biomes for biome source, for the purpose of locating structures, etc.
     * 
     * @return A list of biomes.
     */
    public List<RegistryEntry<Biome>> getBiomes() {
        return List.of();
    }

    /**
     * Gets the biome provider settings.
     *
     * @return The biome provider settings.
     */
    public ModernBetaSettings getSettings() {
        return this.settings;
    }

    /**
     * Gets the name of a biome at the given biome coordinates.
     * Used for the biome preview in the graphical settings menu.
     *
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeY y-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     *
     * @return The name of the biome at given biome coordinates.
     */
    public Text getBiomeName(int biomeX, int biomeY, int biomeZ) {
        return this.getBiome(biomeX, biomeY, biomeZ).getKey()
            .map(key -> Text.translatable(key.getValue().toTranslationKey("biome")))
            .orElse(Text.literal("[unregistered]"));
    }
}
