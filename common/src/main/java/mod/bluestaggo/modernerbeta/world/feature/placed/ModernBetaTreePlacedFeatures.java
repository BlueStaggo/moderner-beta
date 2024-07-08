package mod.bluestaggo.modernerbeta.world.feature.placed;

import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFeatureTags;
import mod.bluestaggo.modernerbeta.world.feature.configured.ModernBetaTreeConfiguredFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;

public class ModernBetaTreePlacedFeatures {
    public static final RegistryKey<PlacedFeature> FANCY_OAK = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.FANCY_OAK);
    public static final RegistryKey<PlacedFeature> OAK_14A_08 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.OAK_14A_08);
    public static final RegistryKey<PlacedFeature> OAK_14A_08_BEES_0002 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.OAK_14A_08_BEES_0002);

    public static void bootstrap(Registerable<PlacedFeature> featureRegisterable) {
        RegistryEntryLookup<ConfiguredFeature<?, ?>> registryConfigured = featureRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> fancyOak = registryConfigured.getOrThrow(ModernBetaTreeConfiguredFeatures.FANCY_OAK);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> oak14a08 = registryConfigured.getOrThrow(ModernBetaTreeConfiguredFeatures.OAK_14A_08);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> oak14a08bees0002 = registryConfigured.getOrThrow(ModernBetaTreeConfiguredFeatures.OAK_14A_08_BEES_0002);

        PlacedFeatures.register(featureRegisterable, FANCY_OAK, fancyOak, PlacedFeatures.wouldSurvive(Blocks.OAK_SAPLING));
        PlacedFeatures.register(featureRegisterable, OAK_14A_08, oak14a08, PlacedFeatures.wouldSurvive(Blocks.OAK_SAPLING));
        PlacedFeatures.register(featureRegisterable, OAK_14A_08_BEES_0002, oak14a08bees0002, PlacedFeatures.wouldSurvive(Blocks.OAK_SAPLING));
    }
}
