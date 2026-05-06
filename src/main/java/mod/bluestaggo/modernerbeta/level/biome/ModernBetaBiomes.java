package mod.bluestaggo.modernerbeta.level.biome;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.level.biome.biomes.alpha.AlphaBiome;
import mod.bluestaggo.modernerbeta.level.biome.biomes.alpha.AlphaWinterBiome;
import mod.bluestaggo.modernerbeta.level.biome.biomes.beta.*;
import mod.bluestaggo.modernerbeta.level.biome.biomes.earlyrelease.EarlyReleaseIcePlainsBiome;
import mod.bluestaggo.modernerbeta.level.biome.biomes.earlyrelease.EarlyReleaseSwamplandBiome;
import mod.bluestaggo.modernerbeta.level.biome.biomes.earlyrelease.EarlyReleaseTaigaBiome;
import mod.bluestaggo.modernerbeta.level.biome.biomes.indev.*;
import mod.bluestaggo.modernerbeta.level.biome.biomes.infdev.*;
import mod.bluestaggo.modernerbeta.level.biome.biomes.latebeta.*;
import mod.bluestaggo.modernerbeta.level.biome.biomes.pe.*;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModernBetaBiomes {
    public static final ResourceKey<Biome> BETA_FOREST = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_FOREST));
    public static final ResourceKey<Biome> BETA_OAK_FOREST = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_OAK_FOREST));
    public static final ResourceKey<Biome> BETA_SHRUBLAND = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_SHRUBLAND));
    public static final ResourceKey<Biome> BETA_DESERT = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_DESERT));
    public static final ResourceKey<Biome> BETA_SAVANNA = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_SAVANNA));
    public static final ResourceKey<Biome> BETA_PLAINS = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_PLAINS));
    public static final ResourceKey<Biome> BETA_SEASONAL_FOREST = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_SEASONAL_FOREST));
    public static final ResourceKey<Biome> BETA_RAINFOREST = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_RAINFOREST));
    public static final ResourceKey<Biome> BETA_SWAMPLAND = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_SWAMPLAND));
    public static final ResourceKey<Biome> BETA_TAIGA = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_TAIGA));
    public static final ResourceKey<Biome> BETA_OAK_TAIGA = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_OAK_TAIGA));
    public static final ResourceKey<Biome> BETA_TUNDRA = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_TUNDRA));
    public static final ResourceKey<Biome> BETA_ICE_DESERT = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_ICE_DESERT));

    public static final ResourceKey<Biome> BETA_OCEAN = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_OCEAN));
    public static final ResourceKey<Biome> BETA_LUKEWARM_OCEAN = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_LUKEWARM_OCEAN));
    public static final ResourceKey<Biome> BETA_WARM_OCEAN = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_WARM_OCEAN));
    public static final ResourceKey<Biome> BETA_COLD_OCEAN = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_COLD_OCEAN));
    public static final ResourceKey<Biome> BETA_FROZEN_OCEAN = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_FROZEN_OCEAN));
    
    public static final ResourceKey<Biome> BETA_SKY = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_SKY));
    
    public static final ResourceKey<Biome> PE_FOREST = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_FOREST));
    public static final ResourceKey<Biome> PE_SHRUBLAND = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_SHRUBLAND));
    public static final ResourceKey<Biome> PE_DESERT = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_DESERT));
    public static final ResourceKey<Biome> PE_SAVANNA = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_SAVANNA));
    public static final ResourceKey<Biome> PE_PLAINS = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_PLAINS));
    public static final ResourceKey<Biome> PE_SEASONAL_FOREST = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_SEASONAL_FOREST));
    public static final ResourceKey<Biome> PE_RAINFOREST = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_RAINFOREST));
    public static final ResourceKey<Biome> PE_SWAMPLAND = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_SWAMPLAND));
    public static final ResourceKey<Biome> PE_TAIGA = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_TAIGA));
    public static final ResourceKey<Biome> PE_TUNDRA = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_TUNDRA));
    public static final ResourceKey<Biome> PE_ICE_DESERT = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_ICE_DESERT));
    
    public static final ResourceKey<Biome> PE_OCEAN = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_OCEAN));
    public static final ResourceKey<Biome> PE_LUKEWARM_OCEAN = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_LUKEWARM_OCEAN));
    public static final ResourceKey<Biome> PE_WARM_OCEAN = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_WARM_OCEAN));
    public static final ResourceKey<Biome> PE_COLD_OCEAN = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_COLD_OCEAN));
    public static final ResourceKey<Biome> PE_FROZEN_OCEAN = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_FROZEN_OCEAN));
    
    public static final ResourceKey<Biome> ALPHA = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.ALPHA));
    public static final ResourceKey<Biome> ALPHA_WINTER = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.ALPHA_WINTER));
    
    public static final ResourceKey<Biome> INFDEV_611 = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.INFDEV_611));
    public static final ResourceKey<Biome> INFDEV_420 = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.INFDEV_420));
    public static final ResourceKey<Biome> INFDEV_415 = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.INFDEV_415));
    public static final ResourceKey<Biome> INFDEV_325 = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.INFDEV_325));
    public static final ResourceKey<Biome> INFDEV_227 = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.INFDEV_227));

    public static final ResourceKey<Biome> INDEV_NORMAL = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.INDEV_NORMAL));
    public static final ResourceKey<Biome> INDEV_HELL = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.INDEV_HELL));
    public static final ResourceKey<Biome> INDEV_PARADISE = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.INDEV_PARADISE));
    public static final ResourceKey<Biome> INDEV_WOODS = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.INDEV_WOODS));
    public static final ResourceKey<Biome> CLASSIC_14A_08 = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.CLASSIC_14A_08));

    public static final ResourceKey<Biome> LATE_BETA_EXTREME_HILLS = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.LATE_BETA_EXTREME_HILLS));
    public static final ResourceKey<Biome> LATE_BETA_ICE_PLAINS = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.LATE_BETA_ICE_PLAINS));
    public static final ResourceKey<Biome> LATE_BETA_SWAMPLAND = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.LATE_BETA_SWAMPLAND));
    public static final ResourceKey<Biome> LATE_BETA_PLAINS = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.LATE_BETA_PLAINS));
    public static final ResourceKey<Biome> LATE_BETA_TAIGA = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.LATE_BETA_TAIGA));

    public static final ResourceKey<Biome> EARLY_RELEASE_ICE_PLAINS = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.EARLY_RELEASE_ICE_PLAINS));
    public static final ResourceKey<Biome> EARLY_RELEASE_SWAMPLAND = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.EARLY_RELEASE_SWAMPLAND));
    public static final ResourceKey<Biome> EARLY_RELEASE_EXTREME_HILLS = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.EARLY_RELEASE_EXTREME_HILLS));
    public static final ResourceKey<Biome> EARLY_RELEASE_TAIGA = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.EARLY_RELEASE_TAIGA));

    public static void bootstrap(BootstrapContext<Biome> biomeRegisterable) {
        register(biomeRegisterable, BETA_FOREST, BetaForestBiome::create);
        register(biomeRegisterable, BETA_OAK_FOREST, BetaOakForestBiome::create);
        register(biomeRegisterable, BETA_SHRUBLAND, BetaShrublandBiome::create);
        register(biomeRegisterable, BETA_DESERT, BetaDesertBiome::create);
        register(biomeRegisterable, BETA_SAVANNA, BetaSavannaBiome::create);
        register(biomeRegisterable, BETA_PLAINS, BetaPlainsBiome::create);
        register(biomeRegisterable, BETA_SEASONAL_FOREST, BetaSeasonalForestBiome::create);
        register(biomeRegisterable, BETA_RAINFOREST, BetaRainforestBiome::create);
        register(biomeRegisterable, BETA_SWAMPLAND, BetaSwamplandBiome::create);
        register(biomeRegisterable, BETA_TAIGA, BetaTaigaBiome::create);
        register(biomeRegisterable, BETA_OAK_TAIGA, BetaOakTaigaBiome::create);
        register(biomeRegisterable, BETA_TUNDRA, BetaTundraBiome::create);
        register(biomeRegisterable, BETA_ICE_DESERT, BetaIceDesertBiome::create);

        register(biomeRegisterable, BETA_OCEAN, BetaOceanBiome::create);
        register(biomeRegisterable, BETA_LUKEWARM_OCEAN, BetaLukewarmOceanBiome::create);
        register(biomeRegisterable, BETA_WARM_OCEAN, BetaWarmOceanBiome::create);
        register(biomeRegisterable, BETA_COLD_OCEAN, BetaColdOceanBiome::create);
        register(biomeRegisterable, BETA_FROZEN_OCEAN, BetaFrozenOceanBiome::create);
        
        register(biomeRegisterable, BETA_SKY, BetaSkyBiome::create);

        register(biomeRegisterable, PE_FOREST, PEForestBiome::create);
        register(biomeRegisterable, PE_SHRUBLAND, PEShrublandBiome::create);
        register(biomeRegisterable, PE_DESERT, PEDesertBiome::create);
        register(biomeRegisterable, PE_SAVANNA, PESavannaBiome::create);
        register(biomeRegisterable, PE_PLAINS, PEPlainsBiome::create);
        register(biomeRegisterable, PE_SEASONAL_FOREST, PESeasonalForestBiome::create);
        register(biomeRegisterable, PE_RAINFOREST, PERainforestBiome::create);
        register(biomeRegisterable, PE_SWAMPLAND, PESwamplandBiome::create);
        register(biomeRegisterable, PE_TAIGA, PETaigaBiome::create);
        register(biomeRegisterable, PE_TUNDRA, PETundraBiome::create);
        register(biomeRegisterable, PE_ICE_DESERT, PEIceDesertBiome::create);

        register(biomeRegisterable, PE_OCEAN, PEOceanBiome::create);
        register(biomeRegisterable, PE_LUKEWARM_OCEAN, PELukewarmOceanBiome::create);
        register(biomeRegisterable, PE_WARM_OCEAN, PEWarmOceanBiome::create);
        register(biomeRegisterable, PE_COLD_OCEAN, PEColdOceanBiome::create);
        register(biomeRegisterable, PE_FROZEN_OCEAN, PEFrozenOceanBiome::create);

        register(biomeRegisterable, ALPHA, AlphaBiome::create);
        register(biomeRegisterable, ALPHA_WINTER, AlphaWinterBiome::create);

        register(biomeRegisterable, INFDEV_611, Infdev611Biome::create);
        register(biomeRegisterable, INFDEV_420, Infdev420Biome::create);
        register(biomeRegisterable, INFDEV_415, Infdev415Biome::create);
        register(biomeRegisterable, INFDEV_325, Infdev325Biome::create);
        register(biomeRegisterable, INFDEV_227, Infdev227Biome::create);

        register(biomeRegisterable, INDEV_NORMAL, IndevNormalBiome::create);
        register(biomeRegisterable, INDEV_HELL, IndevHellBiome::create);
        register(biomeRegisterable, INDEV_PARADISE, IndevParadiseBiome::create);
        register(biomeRegisterable, INDEV_WOODS, IndevWoodsBiome::create);
        register(biomeRegisterable, CLASSIC_14A_08, Classic14a08Biome::create);

        register(biomeRegisterable, LATE_BETA_EXTREME_HILLS, LateBetaExtremeHillsBiome::create);
        register(biomeRegisterable, LATE_BETA_ICE_PLAINS, LateBetaIcePlainsBiome::create);
        register(biomeRegisterable, LATE_BETA_SWAMPLAND, LateBetaSwamplandBiome::create);
        register(biomeRegisterable, LATE_BETA_PLAINS, LateBetaPlainsBiome::create);
        register(biomeRegisterable, LATE_BETA_TAIGA, LateBetaTaigaBiome::create);

        register(biomeRegisterable, EARLY_RELEASE_ICE_PLAINS, EarlyReleaseIcePlainsBiome::create);
        register(biomeRegisterable, EARLY_RELEASE_SWAMPLAND, EarlyReleaseSwamplandBiome::create);
        register(biomeRegisterable, EARLY_RELEASE_EXTREME_HILLS, LateBetaExtremeHillsBiome::create);
        register(biomeRegisterable, EARLY_RELEASE_TAIGA, EarlyReleaseTaigaBiome::create);
    }
    
    private static void register(BootstrapContext<Biome> biomeRegisterable, ResourceKey<Biome> biome, BiomeCreator biomeCreator) {
        HolderGetter<PlacedFeature> registryFeature = biomeRegisterable.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> registryCarver = biomeRegisterable.lookup(Registries.CONFIGURED_CARVER);
        
        biomeRegisterable.register(biome, biomeCreator.create(registryFeature, registryCarver));
    }
    
    private static ResourceKey<Biome> keyOf(ResourceLocation id) {
        return ResourceKey.create(Registries.BIOME, id);
    }

    @FunctionalInterface
    private interface BiomeCreator {
        Biome create(HolderGetter<PlacedFeature> registryFeature, HolderGetter<ConfiguredWorldCarver<?>> registryCarver);
    }
}
