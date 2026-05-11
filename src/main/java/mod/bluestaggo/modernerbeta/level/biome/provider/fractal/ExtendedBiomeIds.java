package mod.bluestaggo.modernerbeta.level.biome.provider.fractal;

import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.*;
import java.util.stream.IntStream;

import static mod.bluestaggo.modernerbeta.util.ExtendedIdentifier.*;

public final class ExtendedBiomeIds {
    public static final String TRANSLATION_KEY = "createWorld.customize.modern_beta.settings.preview.extended_biome_id";

    public static final ExtendedIdentifier
        OCEAN = of(Biomes.OCEAN),
        DEEP_OCEAN = of(Biomes.DEEP_OCEAN),
        PLAINS = of(Biomes.PLAINS),
        RIVER = of(Biomes.RIVER),
        FROZEN_OCEAN = of(Biomes.FROZEN_OCEAN),
        SNOWY_PLAINS = of(Biomes.SNOWY_PLAINS),
        FROZEN_RIVER = of(Biomes.FROZEN_RIVER),
        BEACH = of(Biomes.BEACH),
        MUSHROOM_ISLAND = of(Biomes.MUSHROOM_FIELDS),
        MUSHROOM_SHORE = of(Biomes.MUSHROOM_FIELDS, "shore"),
        CLIMATE_WARM = of(Biomes.DESERT, "climate"),
        CLIMATE_TEMPERATE = of(Biomes.PLAINS, "climate"),
        CLIMATE_COOL = of(Biomes.TAIGA, "climate"),
        CLIMATE_SNOWY = of(Biomes.SNOWY_PLAINS, "climate"),
        RIVER_REGION_A = ExtendedBiomeIds.RIVER.withExt("region_a"),
        RIVER_REGION_B = ExtendedBiomeIds.RIVER.withExt("region_b"),
        RANDOM = of(Biomes.THE_VOID, "mutation"),
        WARM_OCEAN = of(Biomes.WARM_OCEAN),
        LUKEWARM_OCEAN = of(Biomes.LUKEWARM_OCEAN),
        COLD_OCEAN = of(Biomes.COLD_OCEAN),
        DEEP_LUKEWARM_OCEAN = of(Biomes.DEEP_LUKEWARM_OCEAN),
        DEEP_COLD_OCEAN = of(Biomes.DEEP_COLD_OCEAN),
        DEEP_FROZEN_OCEAN = of(Biomes.DEEP_FROZEN_OCEAN),
        NULL = of(Biomes.THE_VOID, "null");
    public static final List<ExtendedIdentifier>
        CLIMATE_WARM_RARE = rareClimate(Biomes.BADLANDS),
        CLIMATE_TEMPERATE_RARE = rareClimate(Biomes.JUNGLE),
        CLIMATE_COOL_RARE = rareClimate(Biomes.OLD_GROWTH_PINE_TAIGA),
        CLIMATE_SNOWY_RARE = rareClimate(Biomes.ICE_SPIKES);

    private static List<ExtendedIdentifier> rareClimate(ResourceKey<Biome> baseId) {
        return IntStream.range(0, 15)
            .mapToObj(i -> ExtendedIdentifier.of(baseId, "climate_" + i))
            .toList();
    }
}
