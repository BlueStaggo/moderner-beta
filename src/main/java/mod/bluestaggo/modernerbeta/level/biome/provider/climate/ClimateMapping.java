//~dotLocation
package mod.bluestaggo.modernerbeta.level.biome.provider.climate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.Map;
import java.util.stream.Stream;

public record ClimateMapping(ResourceLocation biome, ResourceLocation oceanBiome, ResourceLocation deepOceanBiome) {
    public static final Codec<ClimateMapping> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("biome").forGetter(ClimateMapping::biome),
            ResourceLocation.CODEC.fieldOf("oceanBiome").forGetter(ClimateMapping::oceanBiome),
            ResourceLocation.CODEC.fieldOf("deepOceanBiome").forGetter(ClimateMapping::deepOceanBiome)
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
            ModernBetaBiomes.BETA_DESERT.location(),
            ModernBetaBiomes.BETA_OCEAN.location()
        )),
        Map.entry("forest", new ClimateMapping(
            ModernBetaBiomes.BETA_FOREST.location(),
            ModernBetaBiomes.BETA_OCEAN.location()
        )),
        Map.entry("ice_desert", new ClimateMapping(
            ModernBetaBiomes.BETA_TUNDRA.location(),
            ModernBetaBiomes.BETA_FROZEN_OCEAN.location()
        )),
        Map.entry("plains", new ClimateMapping(
            ModernBetaBiomes.BETA_PLAINS.location(),
            ModernBetaBiomes.BETA_OCEAN.location()
        )),
        Map.entry("rainforest", new ClimateMapping(
            ModernBetaBiomes.BETA_RAINFOREST.location(),
            ModernBetaBiomes.BETA_WARM_OCEAN.location()
        )),
        Map.entry("savanna", new ClimateMapping(
            ModernBetaBiomes.BETA_SAVANNA.location(),
            ModernBetaBiomes.BETA_OCEAN.location()
        )),
        Map.entry("shrubland", new ClimateMapping(
            ModernBetaBiomes.BETA_SHRUBLAND.location(),
            ModernBetaBiomes.BETA_OCEAN.location()
        )),
        Map.entry("seasonal_forest", new ClimateMapping(
            ModernBetaBiomes.BETA_SEASONAL_FOREST.location(),
            ModernBetaBiomes.BETA_LUKEWARM_OCEAN.location()
        )),
        Map.entry("swampland", new ClimateMapping(
            ModernBetaBiomes.BETA_SWAMPLAND.location(),
            ModernBetaBiomes.BETA_COLD_OCEAN.location()
        )),
        Map.entry("taiga", new ClimateMapping(
            ModernBetaBiomes.BETA_TAIGA.location(),
            ModernBetaBiomes.BETA_FROZEN_OCEAN.location()
        )),
        Map.entry("tundra", new ClimateMapping(
            ModernBetaBiomes.BETA_TUNDRA.location(),
            ModernBetaBiomes.BETA_FROZEN_OCEAN.location()
        ))
    );

    public ClimateMapping(ResourceLocation biome, ResourceLocation oceanBiome) {
        this(biome, oceanBiome, oceanBiome);
    }
    
    public ResourceKey<Biome> getBiome(ClimateType type) {
        return switch(type) {
            case LAND -> ResourceKey.create(Registries.BIOME, this.biome);
            case OCEAN -> ResourceKey.create(Registries.BIOME, this.oceanBiome);
            case DEEP_OCEAN -> ResourceKey.create(Registries.BIOME, this.deepOceanBiome);
        };
    }
}
