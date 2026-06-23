package mod.bluestaggo.modernerbeta.level.feature.placed;

import mod.bluestaggo.modernerbeta.level.feature.ModernBetaFeatureTags;
import mod.bluestaggo.modernerbeta.level.feature.configured.ModernBetaOreConfiguredFeatures;
import mod.bluestaggo.modernerbeta.level.feature.placement.Infdev325CavePlacementModifier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
//? if >=26.3 {
/*import net.minecraft.world.level.levelgen.feature.Feature;
*///? } else {
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
//? }
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class ModernBetaOrePlacedFeatures {
    public static final ResourceKey<PlacedFeature> ORE_CLAY = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.ORE_CLAY);
    public static final ResourceKey<PlacedFeature> ORE_EMERALD_Y95 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.ORE_EMERALD_Y95);
    public static final ResourceKey<PlacedFeature> CAVE_INFDEV_325 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.CAVE_INFDEV_325);

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        //~ if >=26.3 'ConfiguredFeature<?, ?>>' -> 'Feature>', 'CONFIGURED_FEATURE' -> 'FEATURE' {
        HolderGetter<ConfiguredFeature<?, ?>> registryConfigured = context.lookup(Registries.CONFIGURED_FEATURE);
        
        Holder.Reference<ConfiguredFeature<?, ?>> oreClay = registryConfigured.getOrThrow(ModernBetaOreConfiguredFeatures.ORE_CLAY);
        Holder.Reference<ConfiguredFeature<?, ?>> oreEmeraldY95 = registryConfigured.getOrThrow(ModernBetaOreConfiguredFeatures.ORE_EMERALD_Y95);
        Holder.Reference<ConfiguredFeature<?, ?>> caveInfdev325 = registryConfigured.getOrThrow(ModernBetaOreConfiguredFeatures.CAVE_INFDEV_325);
        //~ }

        PlacementUtils.register(context, ORE_CLAY, oreClay, modifiersWithCount(33, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(127))));
        PlacementUtils.register(context, ORE_EMERALD_Y95, oreEmeraldY95, modifiersWithCount(11, HeightRangePlacement.uniform(VerticalAnchor.absolute(95), VerticalAnchor.top())));

        List<Fluid> empty = List.of(Fluids.EMPTY);
        PlacementUtils.register(context, CAVE_INFDEV_325, caveInfdev325,
            Infdev325CavePlacementModifier.of(-4, 15),
            BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                BlockPredicate.matchesFluids(new Vec3i(0, 0, 0), empty),
                BlockPredicate.matchesFluids(new Vec3i(4, 4, 4), empty),
                BlockPredicate.matchesFluids(new Vec3i(4, 4, -4), empty),
                BlockPredicate.matchesFluids(new Vec3i(-4, 4, 4), empty),
                BlockPredicate.matchesFluids(new Vec3i(-4, 4, -4), empty),
                BlockPredicate.matchesFluids(new Vec3i(4, -4, 4), empty),
                BlockPredicate.matchesFluids(new Vec3i(4, -4, -4), empty),
                BlockPredicate.matchesFluids(new Vec3i(-4, -4, 4), empty),
                BlockPredicate.matchesFluids(new Vec3i(-4, -4, -4), empty)
            )),
            BiomeFilter.biome()
        );
    }

    private static List<PlacementModifier> modifiers(PlacementModifier first, PlacementModifier second) {
        return List.of(first, InSquarePlacement.spread(), second, BiomeFilter.biome());
    }
    
    private static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier modifier) {
        return modifiers(CountPlacement.of(count), modifier);
    }
}
