package mod.bluestaggo.modernerbeta.world.feature.placed;

import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFeatureTags;
import mod.bluestaggo.modernerbeta.world.feature.configured.ModernBetaTreeConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModernBetaTreePlacedFeatures {
    public static final ResourceKey<PlacedFeature> FANCY_OAK = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.FANCY_OAK);
    public static final ResourceKey<PlacedFeature> OAK_14A_08 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.OAK_14A_08);
    public static final ResourceKey<PlacedFeature> OAK_14A_08_BEES_0002 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.OAK_14A_08_BEES_0002);

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> registryConfigured = context.lookup(Registries.CONFIGURED_FEATURE);
        
        Holder.Reference<ConfiguredFeature<?, ?>> fancyOak = registryConfigured.getOrThrow(ModernBetaTreeConfiguredFeatures.FANCY_OAK);
        Holder.Reference<ConfiguredFeature<?, ?>> oak14a08 = registryConfigured.getOrThrow(ModernBetaTreeConfiguredFeatures.OAK_14A_08);
        Holder.Reference<ConfiguredFeature<?, ?>> oak14a08bees0002 = registryConfigured.getOrThrow(ModernBetaTreeConfiguredFeatures.OAK_14A_08_BEES_0002);

        PlacementUtils.register(context, FANCY_OAK, fancyOak, PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING));
        PlacementUtils.register(context, OAK_14A_08, oak14a08, PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING));
        PlacementUtils.register(context, OAK_14A_08_BEES_0002, oak14a08bees0002, PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING));
    }
}
