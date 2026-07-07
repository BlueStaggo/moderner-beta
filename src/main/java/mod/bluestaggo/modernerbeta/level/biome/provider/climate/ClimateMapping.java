//~dotLocation
package mod.bluestaggo.modernerbeta.level.biome.provider.climate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.Biome;

import java.util.Map;
import java.util.stream.Stream;

public record ClimateMapping(Holder<Biome> biome) {
    public static final Codec<ClimateMapping> CODEC =
            Biome.CODEC.xmap(ClimateMapping::new, ClimateMapping::biome);

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

    public static Map<String, ClimateMapping> getDefaultMappings(RegistryOps.RegistryInfoLookup lookup) {
        //~ if >=26.3 '.getter();' -> ';'
        HolderGetter<Biome> biomeRegistry = lookup.lookup(Registries.BIOME).orElseThrow().getter();

        return Map.ofEntries(
            Map.entry("desert", new ClimateMapping(biomeRegistry.getOrThrow(ModernBetaBiomes.BETA_DESERT))),
            Map.entry("forest", new ClimateMapping(biomeRegistry.getOrThrow(ModernBetaBiomes.BETA_FOREST))),
            Map.entry("ice_desert", new ClimateMapping(biomeRegistry.getOrThrow(ModernBetaBiomes.BETA_TUNDRA))),
            Map.entry("plains", new ClimateMapping(biomeRegistry.getOrThrow(ModernBetaBiomes.BETA_PLAINS))),
            Map.entry("rainforest", new ClimateMapping(biomeRegistry.getOrThrow(ModernBetaBiomes.BETA_RAINFOREST))),
            Map.entry("savanna", new ClimateMapping(biomeRegistry.getOrThrow(ModernBetaBiomes.BETA_SAVANNA))),
            Map.entry("shrubland", new ClimateMapping(biomeRegistry.getOrThrow(ModernBetaBiomes.BETA_SHRUBLAND))),
            Map.entry("seasonal_forest", new ClimateMapping(biomeRegistry.getOrThrow(ModernBetaBiomes.BETA_SEASONAL_FOREST))),
            Map.entry("swampland", new ClimateMapping(biomeRegistry.getOrThrow(ModernBetaBiomes.BETA_SWAMPLAND))),
            Map.entry("taiga", new ClimateMapping(biomeRegistry.getOrThrow(ModernBetaBiomes.BETA_TAIGA))),
            Map.entry("tundra", new ClimateMapping(biomeRegistry.getOrThrow(ModernBetaBiomes.BETA_TUNDRA)))
        );
    }
}
