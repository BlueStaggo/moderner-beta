package mod.bluestaggo.modernerbeta.world.feature.placed;

import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFeatureTags;
import mod.bluestaggo.modernerbeta.world.feature.configured.ModernBetaMiscConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModernBetaMiscPlacedFeatures {
    public static final ResourceKey<PlacedFeature> FREEZE_TOP_LAYER = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.FREEZE_TOP_LAYER);
    
    public static void bootstrap(BootstrapContext<PlacedFeature> featureRegisterable) {
        HolderGetter<ConfiguredFeature<?, ?>> registryConfigured = featureRegisterable.lookup(Registries.CONFIGURED_FEATURE);

        Holder.Reference<ConfiguredFeature<?, ?>> freezeTopLayer = registryConfigured.getOrThrow(ModernBetaMiscConfiguredFeatures.FREEZE_TOP_LAYER);
        
        PlacementUtils.register(featureRegisterable, FREEZE_TOP_LAYER, freezeTopLayer, BiomeFilter.biome());
    }
}
