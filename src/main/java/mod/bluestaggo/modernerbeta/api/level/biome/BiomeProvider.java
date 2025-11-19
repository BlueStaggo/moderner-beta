//~dotLocation
package mod.bluestaggo.modernerbeta.api.level.biome;

import mod.bluestaggo.modernerbeta.api.level.biome.climate.TemperatureHeightScaling;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public abstract class BiomeProvider {
    protected final ModernBetaSettings settings;
    protected final HolderGetter<Biome> biomeRegistry;
    protected final long seed;

    private final TemperatureHeightScaling temperatureHeightScaling;
    
    /**
     * Constructs a Modern Beta biome provider initialized with seed.
     * Additional settings are supplied in NbtCompound parameter.
     * 
     * @param settings Biome settings.
     * @param biomeRegistry Minecraft biome registry.
     */
    public BiomeProvider(ModernBetaSettings settings, HolderGetter<Biome> biomeRegistry, long seed) {
        this.settings = settings;
        this.biomeRegistry = biomeRegistry;
        this.seed = seed;

        this.temperatureHeightScaling = settings.getOrDefault(SettingsComponentTypes.TEMPERATURE_HEIGHT_SCALING);
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
    public abstract Holder<Biome> getBiome(int biomeX, int biomeY, int biomeZ);
    
    /**
     * Gets a list of biomes for biome source, for the purpose of locating structures, etc.
     * 
     * @return A list of biomes.
     */
    public List<Holder<Biome>> getBiomes() {
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
     * Gets the temperature heigh scaling.
     *
     * @return The temperature heigh scaling.
     */
    public TemperatureHeightScaling getTemperatureHeightScaling() {
        return temperatureHeightScaling;
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
    public Component getBiomeName(int biomeX, int biomeY, int biomeZ) {
        return this.getBiome(biomeX, biomeY, biomeZ).unwrapKey()
            .map(key -> Component.translatable(key.location().toLanguageKey("biome")))
            .orElse(Component.literal("[unregistered]"));
    }
}
