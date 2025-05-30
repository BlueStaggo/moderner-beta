package mod.bluestaggo.modernerbeta.world.biome.provider.climate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.Map;
import java.util.stream.Stream;

public record ClimateMapping(Identifier biome, Identifier oceanBiome, Identifier deepOceanBiome) {
    public static final Codec<ClimateMapping> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Identifier.CODEC.fieldOf("biome").forGetter(ClimateMapping::biome),
            Identifier.CODEC.fieldOf("oceanBiome").forGetter(ClimateMapping::oceanBiome),
            Identifier.CODEC.fieldOf("deepOceanBiome").forGetter(ClimateMapping::deepOceanBiome)
        ).apply(instance, ClimateMapping::new)
    );

    public static final Codec<Map<String, ClimateMapping>> MAP_CODEC = Codec.simpleMap(
        Codec.STRING,
        ClimateMapping.CODEC,
        Keyable.forStrings(() -> Stream.of(
            "desert",
            "forest",
            "ice_desert",
            "plains",
            "rainforest",
            "savanna",
            "shrubland",
            "seasonal_forest",
            "swampland",
            "taiga",
            "tundra"
        ))
    ).codec();

    public static final Map<String, ClimateMapping> DEFAULT_MAPPINGS = Map.ofEntries(
        Map.entry("desert", new ClimateMapping(
            ModernBetaBiomes.BETA_DESERT.getValue(),
            ModernBetaBiomes.BETA_OCEAN.getValue()
        )),
        Map.entry("forest", new ClimateMapping(
            ModernBetaBiomes.BETA_FOREST.getValue(),
            ModernBetaBiomes.BETA_OCEAN.getValue()
        )),
        Map.entry("ice_desert", new ClimateMapping(
            ModernBetaBiomes.BETA_TUNDRA.getValue(),
            ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue()
        )),
        Map.entry("plains", new ClimateMapping(
            ModernBetaBiomes.BETA_PLAINS.getValue(),
            ModernBetaBiomes.BETA_OCEAN.getValue()
        )),
        Map.entry("rainforest", new ClimateMapping(
            ModernBetaBiomes.BETA_RAINFOREST.getValue(),
            ModernBetaBiomes.BETA_WARM_OCEAN.getValue()
        )),
        Map.entry("savanna", new ClimateMapping(
            ModernBetaBiomes.BETA_SAVANNA.getValue(),
            ModernBetaBiomes.BETA_OCEAN.getValue()
        )),
        Map.entry("shrubland", new ClimateMapping(
            ModernBetaBiomes.BETA_SHRUBLAND.getValue(),
            ModernBetaBiomes.BETA_OCEAN.getValue()
        )),
        Map.entry("seasonal_forest", new ClimateMapping(
            ModernBetaBiomes.BETA_SEASONAL_FOREST.getValue(),
            ModernBetaBiomes.BETA_LUKEWARM_OCEAN.getValue()
        )),
        Map.entry("swampland", new ClimateMapping(
            ModernBetaBiomes.BETA_SWAMPLAND.getValue(),
            ModernBetaBiomes.BETA_COLD_OCEAN.getValue()
        )),
        Map.entry("taiga", new ClimateMapping(
            ModernBetaBiomes.BETA_TAIGA.getValue(),
            ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue()
        )),
        Map.entry("tundra", new ClimateMapping(
            ModernBetaBiomes.BETA_TUNDRA.getValue(),
            ModernBetaBiomes.BETA_FROZEN_OCEAN.getValue()
        ))
    );

    public ClimateMapping(Identifier biome, Identifier oceanBiome) {
        this(biome, oceanBiome, oceanBiome);
    }
    
    public RegistryKey<Biome> getBiome(ClimateType type) {
        return switch(type) {
            case LAND -> RegistryKey.of(RegistryKeys.BIOME, this.biome);
            case OCEAN -> RegistryKey.of(RegistryKeys.BIOME, this.oceanBiome);
            case DEEP_OCEAN -> RegistryKey.of(RegistryKeys.BIOME, this.deepOceanBiome);
        };
    }
}
