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

public record ClimateMapping(ResourceLocation biome) {
    public static final Codec<ClimateMapping> CODEC =
            ResourceLocation.CODEC.xmap(ClimateMapping::new, ClimateMapping::biome);

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
        Map.entry("desert", new ClimateMapping(ModernBetaBiomes.BETA_DESERT.location())),
        Map.entry("forest", new ClimateMapping(ModernBetaBiomes.BETA_FOREST.location())),
        Map.entry("ice_desert", new ClimateMapping(ModernBetaBiomes.BETA_TUNDRA.location())),
        Map.entry("plains", new ClimateMapping(ModernBetaBiomes.BETA_PLAINS.location())),
        Map.entry("rainforest", new ClimateMapping(ModernBetaBiomes.BETA_RAINFOREST.location())),
        Map.entry("savanna", new ClimateMapping(ModernBetaBiomes.BETA_SAVANNA.location())),
        Map.entry("shrubland", new ClimateMapping(ModernBetaBiomes.BETA_SHRUBLAND.location())),
        Map.entry("seasonal_forest", new ClimateMapping(ModernBetaBiomes.BETA_SEASONAL_FOREST.location())),
        Map.entry("swampland", new ClimateMapping(ModernBetaBiomes.BETA_SWAMPLAND.location())),
        Map.entry("taiga", new ClimateMapping(ModernBetaBiomes.BETA_TAIGA.location())),
        Map.entry("tundra", new ClimateMapping(ModernBetaBiomes.BETA_TUNDRA.location()))
    );
    
    public ResourceKey<Biome> getBiome() {
        return ResourceKey.create(Registries.BIOME, this.biome);
    }
}
