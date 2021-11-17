package com.bespectacled.modernbeta.world.feature.placed;

import java.util.List;

import com.bespectacled.modernbeta.world.feature.configured.OldOreConfiguredFeatures;

import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.BiomePlacementModifier;
import net.minecraft.world.gen.decorator.CountPlacementModifier;
import net.minecraft.world.gen.decorator.HeightRangePlacementModifier;
import net.minecraft.world.gen.decorator.PlacementModifier;
import net.minecraft.world.gen.decorator.SquarePlacementModifier;
import net.minecraft.world.gen.feature.PlacedFeature;

public class OldOrePlacedFeatures {
    private static List<PlacementModifier> modifiers(PlacementModifier first, PlacementModifier second) {
        return List.of(first, SquarePlacementModifier.of(), second, BiomePlacementModifier.of());
    }
    
    private static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier modifier) {
        return modifiers(CountPlacementModifier.of(count), modifier);
    }
    
    public static final PlacedFeature ORE_CLAY = OldPlacedFeatures.register(
        "ore_clay", OldOreConfiguredFeatures.ORE_CLAY.withPlacement(modifiersWithCount(33, HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(127))))
    );
    
    public static final PlacedFeature ORE_EMERALD_Y95 = OldPlacedFeatures.register(
        "ore_emerald_y95",
        OldOreConfiguredFeatures.ORE_EMERALD_Y95.withPlacement(modifiersWithCount(11, HeightRangePlacementModifier.uniform(YOffset.fixed(95), YOffset.fixed(256))))
    );
}
