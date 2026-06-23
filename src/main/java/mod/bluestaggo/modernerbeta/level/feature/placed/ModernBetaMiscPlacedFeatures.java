package mod.bluestaggo.modernerbeta.level.feature.placed;

import mod.bluestaggo.modernerbeta.level.feature.ModernBetaFeatureTags;
import mod.bluestaggo.modernerbeta.level.feature.configured.ModernBetaMiscConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
//? if >=26.3 {
/*import net.minecraft.world.level.levelgen.feature.Feature;
*///? } else {
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
//? }
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModernBetaMiscPlacedFeatures {
    public static final ResourceKey<PlacedFeature> FREEZE_TOP_LAYER = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.SNOW_AND_FREEZE);
    
    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        //~ if >=26.3 'ConfiguredFeature<?, ?>>' -> 'Feature>', 'CONFIGURED_FEATURE' -> 'FEATURE' {
        HolderGetter<ConfiguredFeature<?, ?>> registryConfigured = context.lookup(Registries.CONFIGURED_FEATURE);

        Holder.Reference<ConfiguredFeature<?, ?>> freezeTopLayer = registryConfigured.getOrThrow(ModernBetaMiscConfiguredFeatures.FREEZE_TOP_LAYER);
        //~ }

        PlacementUtils.register(context, FREEZE_TOP_LAYER, freezeTopLayer, BiomeFilter.biome());
    }
}
