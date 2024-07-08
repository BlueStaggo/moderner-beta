package mod.bluestaggo.modernerbeta.world.biome;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.world.biome.biomes.alpha.BiomeAlpha;
import mod.bluestaggo.modernerbeta.world.biome.biomes.alpha.BiomeAlphaWinter;
import mod.bluestaggo.modernerbeta.world.biome.biomes.beta.*;
import mod.bluestaggo.modernerbeta.world.biome.biomes.earlyrelease.BiomeEarlyReleaseIcePlains;
import mod.bluestaggo.modernerbeta.world.biome.biomes.earlyrelease.BiomeEarlyReleaseSwampland;
import mod.bluestaggo.modernerbeta.world.biome.biomes.earlyrelease.BiomeEarlyReleaseTaiga;
import mod.bluestaggo.modernerbeta.world.biome.biomes.indev.*;
import mod.bluestaggo.modernerbeta.world.biome.biomes.infdev.*;
import mod.bluestaggo.modernerbeta.world.biome.biomes.latebeta.*;
import mod.bluestaggo.modernerbeta.world.biome.biomes.pe.*;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.function.BiFunction;

public class ModernBetaBiomes {
    public static final RegistryKey<Biome> BETA_FOREST = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_FOREST));
    public static final RegistryKey<Biome> BETA_OAK_FOREST = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_OAK_FOREST));
    public static final RegistryKey<Biome> BETA_SHRUBLAND = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_SHRUBLAND));
    public static final RegistryKey<Biome> BETA_DESERT = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_DESERT));
    public static final RegistryKey<Biome> BETA_SAVANNA = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_SAVANNA));
    public static final RegistryKey<Biome> BETA_PLAINS = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_PLAINS));
    public static final RegistryKey<Biome> BETA_SEASONAL_FOREST = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_SEASONAL_FOREST));
    public static final RegistryKey<Biome> BETA_RAINFOREST = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_RAINFOREST));
    public static final RegistryKey<Biome> BETA_SWAMPLAND = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_SWAMPLAND));
    public static final RegistryKey<Biome> BETA_TAIGA = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_TAIGA));
    public static final RegistryKey<Biome> BETA_OAK_TAIGA = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_OAK_TAIGA));
    public static final RegistryKey<Biome> BETA_TUNDRA = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_TUNDRA));
    public static final RegistryKey<Biome> BETA_ICE_DESERT = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_ICE_DESERT));

    public static final RegistryKey<Biome> BETA_OCEAN = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_OCEAN));
    public static final RegistryKey<Biome> BETA_LUKEWARM_OCEAN = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_LUKEWARM_OCEAN));
    public static final RegistryKey<Biome> BETA_WARM_OCEAN = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_WARM_OCEAN));
    public static final RegistryKey<Biome> BETA_COLD_OCEAN = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_COLD_OCEAN));
    public static final RegistryKey<Biome> BETA_FROZEN_OCEAN = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_FROZEN_OCEAN));
    
    public static final RegistryKey<Biome> BETA_SKY = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.BETA_SKY));
    
    public static final RegistryKey<Biome> PE_FOREST = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_FOREST));
    public static final RegistryKey<Biome> PE_SHRUBLAND = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_SHRUBLAND));
    public static final RegistryKey<Biome> PE_DESERT = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_DESERT));
    public static final RegistryKey<Biome> PE_SAVANNA = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_SAVANNA));
    public static final RegistryKey<Biome> PE_PLAINS = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_PLAINS));
    public static final RegistryKey<Biome> PE_SEASONAL_FOREST = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_SEASONAL_FOREST));
    public static final RegistryKey<Biome> PE_RAINFOREST = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_RAINFOREST));
    public static final RegistryKey<Biome> PE_SWAMPLAND = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_SWAMPLAND));
    public static final RegistryKey<Biome> PE_TAIGA = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_TAIGA));
    public static final RegistryKey<Biome> PE_TUNDRA = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_TUNDRA));
    public static final RegistryKey<Biome> PE_ICE_DESERT = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_ICE_DESERT));
    
    public static final RegistryKey<Biome> PE_OCEAN = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_OCEAN));
    public static final RegistryKey<Biome> PE_LUKEWARM_OCEAN = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_LUKEWARM_OCEAN));
    public static final RegistryKey<Biome> PE_WARM_OCEAN = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_WARM_OCEAN));
    public static final RegistryKey<Biome> PE_COLD_OCEAN = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_COLD_OCEAN));
    public static final RegistryKey<Biome> PE_FROZEN_OCEAN = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.PE_FROZEN_OCEAN));
    
    public static final RegistryKey<Biome> ALPHA = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.ALPHA));
    public static final RegistryKey<Biome> ALPHA_WINTER = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.ALPHA_WINTER));
    
    public static final RegistryKey<Biome> INFDEV_611 = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.INFDEV_611));
    public static final RegistryKey<Biome> INFDEV_420 = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.INFDEV_420));
    public static final RegistryKey<Biome> INFDEV_415 = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.INFDEV_415));
    public static final RegistryKey<Biome> INFDEV_325 = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.INFDEV_325));
    public static final RegistryKey<Biome> INFDEV_227 = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.INFDEV_227));

    public static final RegistryKey<Biome> INDEV_NORMAL = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.INDEV_NORMAL));
    public static final RegistryKey<Biome> INDEV_HELL = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.INDEV_HELL));
    public static final RegistryKey<Biome> INDEV_PARADISE = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.INDEV_PARADISE));
    public static final RegistryKey<Biome> INDEV_WOODS = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.INDEV_WOODS));
    public static final RegistryKey<Biome> CLASSIC_14A_08 = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.CLASSIC_14A_08));

    public static final RegistryKey<Biome> LATE_BETA_EXTREME_HILLS = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.LATE_BETA_EXTREME_HILLS));
    public static final RegistryKey<Biome> LATE_BETA_ICE_PLAINS = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.LATE_BETA_ICE_PLAINS));
    public static final RegistryKey<Biome> LATE_BETA_SWAMPLAND = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.LATE_BETA_SWAMPLAND));
    public static final RegistryKey<Biome> LATE_BETA_PLAINS = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.LATE_BETA_PLAINS));
    public static final RegistryKey<Biome> LATE_BETA_TAIGA = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.LATE_BETA_TAIGA));

    public static final RegistryKey<Biome> EARLY_RELEASE_ICE_PLAINS = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.EARLY_RELEASE_ICE_PLAINS));
    public static final RegistryKey<Biome> EARLY_RELEASE_SWAMPLAND = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.EARLY_RELEASE_SWAMPLAND));
    public static final RegistryKey<Biome> EARLY_RELEASE_EXTREME_HILLS = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.EARLY_RELEASE_EXTREME_HILLS));
    public static final RegistryKey<Biome> EARLY_RELEASE_TAIGA = keyOf(ModernerBeta.createId(ModernBetaBiomeIDs.EARLY_RELEASE_TAIGA));

    public static void bootstrap(Registerable<Biome> biomeRegisterable) {
        register(biomeRegisterable, BETA_FOREST, BiomeBetaForest::create);
        register(biomeRegisterable, BETA_OAK_FOREST, BiomeBetaOakForest::create);
        register(biomeRegisterable, BETA_SHRUBLAND, BiomeBetaShrubland::create);
        register(biomeRegisterable, BETA_DESERT, BiomeBetaDesert::create);
        register(biomeRegisterable, BETA_SAVANNA, BiomeBetaSavanna::create);
        register(biomeRegisterable, BETA_PLAINS, BiomeBetaPlains::create);
        register(biomeRegisterable, BETA_SEASONAL_FOREST, BiomeBetaSeasonalForest::create);
        register(biomeRegisterable, BETA_RAINFOREST, BiomeBetaRainforest::create);
        register(biomeRegisterable, BETA_SWAMPLAND, BiomeBetaSwampland::create);
        register(biomeRegisterable, BETA_TAIGA, BiomeBetaTaiga::create);
        register(biomeRegisterable, BETA_OAK_TAIGA, BiomeBetaOakTaiga::create);
        register(biomeRegisterable, BETA_TUNDRA, BiomeBetaTundra::create);
        register(biomeRegisterable, BETA_ICE_DESERT, BiomeBetaIceDesert::create);

        register(biomeRegisterable, BETA_OCEAN, BiomeBetaOcean::create);
        register(biomeRegisterable, BETA_LUKEWARM_OCEAN, BiomeBetaLukewarmOcean::create);
        register(biomeRegisterable, BETA_WARM_OCEAN, BiomeBetaWarmOcean::create);
        register(biomeRegisterable, BETA_COLD_OCEAN, BiomeBetaColdOcean::create);
        register(biomeRegisterable, BETA_FROZEN_OCEAN, BiomeBetaFrozenOcean::create);
        
        register(biomeRegisterable, BETA_SKY, BiomeBetaSky::create);

        register(biomeRegisterable, PE_FOREST, BiomePEForest::create);
        register(biomeRegisterable, PE_SHRUBLAND, BiomePEShrubland::create);
        register(biomeRegisterable, PE_DESERT, BiomePEDesert::create);
        register(biomeRegisterable, PE_SAVANNA, BiomePESavanna::create);
        register(biomeRegisterable, PE_PLAINS, BiomePEPlains::create);
        register(biomeRegisterable, PE_SEASONAL_FOREST, BiomePESeasonalForest::create);
        register(biomeRegisterable, PE_RAINFOREST, BiomePERainforest::create);
        register(biomeRegisterable, PE_SWAMPLAND, BiomePESwampland::create);
        register(biomeRegisterable, PE_TAIGA, BiomePETaiga::create);
        register(biomeRegisterable, PE_TUNDRA, BiomePETundra::create);
        register(biomeRegisterable, PE_ICE_DESERT, BiomePEIceDesert::create);

        register(biomeRegisterable, PE_OCEAN, BiomePEOcean::create);
        register(biomeRegisterable, PE_LUKEWARM_OCEAN, BiomePELukewarmOcean::create);
        register(biomeRegisterable, PE_WARM_OCEAN, BiomePEWarmOcean::create);
        register(biomeRegisterable, PE_COLD_OCEAN, BiomePEColdOcean::create);
        register(biomeRegisterable, PE_FROZEN_OCEAN, BiomePEFrozenOcean::create);

        register(biomeRegisterable, ALPHA, BiomeAlpha::create);
        register(biomeRegisterable, ALPHA_WINTER, BiomeAlphaWinter::create);

        register(biomeRegisterable, INFDEV_611, BiomeInfdev611::create);
        register(biomeRegisterable, INFDEV_420, BiomeInfdev420::create);
        register(biomeRegisterable, INFDEV_415, BiomeInfdev415::create);
        register(biomeRegisterable, INFDEV_325, BiomeInfdev325::create);
        register(biomeRegisterable, INFDEV_227, BiomeInfdev227::create);

        register(biomeRegisterable, INDEV_NORMAL, BiomeIndevNormal::create);
        register(biomeRegisterable, INDEV_HELL, BiomeIndevHell::create);
        register(biomeRegisterable, INDEV_PARADISE, BiomeIndevParadise::create);
        register(biomeRegisterable, INDEV_WOODS, BiomeIndevWoods::create);
        register(biomeRegisterable, CLASSIC_14A_08, BiomeClassic14a08::create);

        register(biomeRegisterable, LATE_BETA_EXTREME_HILLS, BiomeLateBetaExtremeHills::create);
        register(biomeRegisterable, LATE_BETA_ICE_PLAINS, BiomeLateBetaIcePlains::create);
        register(biomeRegisterable, LATE_BETA_SWAMPLAND, BiomeLateBetaSwampland::create);
        register(biomeRegisterable, LATE_BETA_PLAINS, BiomeLateBetaPlains::create);
        register(biomeRegisterable, LATE_BETA_TAIGA, BiomeLateBetaTaiga::create);

        register(biomeRegisterable, EARLY_RELEASE_ICE_PLAINS, BiomeEarlyReleaseIcePlains::create);
        register(biomeRegisterable, EARLY_RELEASE_SWAMPLAND, BiomeEarlyReleaseSwampland::create);
        register(biomeRegisterable, EARLY_RELEASE_EXTREME_HILLS, BiomeLateBetaExtremeHills::create);
        register(biomeRegisterable, EARLY_RELEASE_TAIGA, BiomeEarlyReleaseTaiga::create);
    }
    
    private static void register(Registerable<Biome> biomeRegisterable, RegistryKey<Biome> biome, BiFunction<RegistryEntryLookup<PlacedFeature>, RegistryEntryLookup<ConfiguredCarver<?>>, Biome> biomeCreator) {
        RegistryEntryLookup<PlacedFeature> registryFeature = biomeRegisterable.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
        RegistryEntryLookup<ConfiguredCarver<?>> registryCarver = biomeRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER);
        
        biomeRegisterable.register(biome, biomeCreator.apply(registryFeature, registryCarver));
    }
    
    private static RegistryKey<Biome> keyOf(Identifier id) {
        return RegistryKey.of(RegistryKeys.BIOME, id);
    }
}
