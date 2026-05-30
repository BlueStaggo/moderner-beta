package mod.bluestaggo.modernerbeta.level.chunk;

import com.google.common.collect.ImmutableList;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomes;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class ModernBetaSurfaceRuleData {
    private static final SurfaceRules.RuleSource AIR = makeStateRule(Blocks.AIR);
    private static final SurfaceRules.RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);
    //? if >=26.2 {
    /*private static final SurfaceRules.RuleSource WHITE_TERRACOTTA = makeStateRule(Blocks.DYED_TERRACOTTA.white());
    private static final SurfaceRules.RuleSource ORANGE_TERRACOTTA = makeStateRule(Blocks.DYED_TERRACOTTA.orange());
    *///? } else {
    private static final SurfaceRules.RuleSource WHITE_TERRACOTTA = makeStateRule(Blocks.WHITE_TERRACOTTA);
    private static final SurfaceRules.RuleSource ORANGE_TERRACOTTA = makeStateRule(Blocks.ORANGE_TERRACOTTA);
    //? }
    private static final SurfaceRules.RuleSource TERRACOTTA = makeStateRule(Blocks.TERRACOTTA);
    private static final SurfaceRules.RuleSource RED_SAND = makeStateRule(Blocks.RED_SAND);
    private static final SurfaceRules.RuleSource RED_SANDSTONE = makeStateRule(Blocks.RED_SANDSTONE);
    private static final SurfaceRules.RuleSource STONE = makeStateRule(Blocks.STONE);
    private static final SurfaceRules.RuleSource DEEPSLATE = makeStateRule(Blocks.DEEPSLATE);
    private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
    private static final SurfaceRules.RuleSource PODZOL = makeStateRule(Blocks.PODZOL);
    private static final SurfaceRules.RuleSource COARSE_DIRT = makeStateRule(Blocks.COARSE_DIRT);
    private static final SurfaceRules.RuleSource MYCELIUM = makeStateRule(Blocks.MYCELIUM);
    private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
    private static final SurfaceRules.RuleSource CALCITE = makeStateRule(Blocks.CALCITE);
    private static final SurfaceRules.RuleSource GRAVEL = makeStateRule(Blocks.GRAVEL);
    private static final SurfaceRules.RuleSource SAND = makeStateRule(Blocks.SAND);
    private static final SurfaceRules.RuleSource SANDSTONE = makeStateRule(Blocks.SANDSTONE);
    private static final SurfaceRules.RuleSource PACKED_ICE = makeStateRule(Blocks.PACKED_ICE);
    private static final SurfaceRules.RuleSource SNOW_BLOCK = makeStateRule(Blocks.SNOW_BLOCK);
    private static final SurfaceRules.RuleSource MUD = makeStateRule(Blocks.MUD);
    private static final SurfaceRules.RuleSource POWDER_SNOW = makeStateRule(Blocks.POWDER_SNOW);
    private static final SurfaceRules.RuleSource ICE = makeStateRule(Blocks.ICE);
    private static final SurfaceRules.RuleSource WATER = makeStateRule(Blocks.WATER);
    //? if >=26.2 {
    /*private static final SurfaceRules.RuleSource CINNABAR = makeStateRule(Blocks.CINNABAR);
    private static final SurfaceRules.RuleSource SULFUR = makeStateRule(Blocks.SULFUR);
    *///? }

    private static SurfaceRules.RuleSource makeStateRule(final Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }

    //Modified copy of SurfaceRuleData::overworldLike from 26.2
    //Any modifications are appended with comments, noting changes in case
    //  modifications have to be redone.
    public static SurfaceRules.RuleSource overworldLike(
        HolderGetter<Biome> biomes, boolean doPreliminarySurfaceCheck, boolean bedrockRoof, boolean bedrockFloor, boolean deepslate, int seaLevel
    ) {
        //Moderner Beta: changed constant from 97
        SurfaceRules.ConditionSource woodedBadlandsTop = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(86), 2);
        SurfaceRules.ConditionSource badlandsTop = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(256), 0);
        SurfaceRules.ConditionSource badlandsHeightCondition = SurfaceRules.yStartCheck(VerticalAnchor.absolute(63), -1);
        //Moderner Beta: changed constant from 74
        SurfaceRules.ConditionSource badlandsMid = SurfaceRules.yStartCheck(VerticalAnchor.absolute(66), 1);
        SurfaceRules.ConditionSource mangroveSwampPuddleLevel = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(60), 0);
        SurfaceRules.ConditionSource swampPuddleLevel = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(62), 0);
        SurfaceRules.ConditionSource aboveOverworldSeaLevel = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(63), 0);
        SurfaceRules.ConditionSource notUnderwater = SurfaceRules.waterBlockCheck(-1, 0);
        SurfaceRules.ConditionSource aboveWater = SurfaceRules.waterBlockCheck(0, 0);
        SurfaceRules.ConditionSource notUnderDeepWater = SurfaceRules.waterStartCheck(-6, -1);
        SurfaceRules.ConditionSource hole = SurfaceRules.hole();
        SurfaceRules.ConditionSource steep = SurfaceRules.steep();
        SurfaceRules.RuleSource grassOrDirtIfUnderwater = SurfaceRules.sequence(SurfaceRules.ifTrue(aboveWater, GRASS_BLOCK), DIRT);
        SurfaceRules.RuleSource sandOrSandstoneIfCeiling = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, SANDSTONE), SAND);
        SurfaceRules.RuleSource gravelOrStoneIfCeiling = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, STONE), GRAVEL);
        //Moderner Beta: added mb variant
        SurfaceRules.ConditionSource biomesWithSandAndSandstone = isBiome(biomes, Biomes.WARM_OCEAN, Biomes.BEACH, Biomes.SNOWY_BEACH, ModernBetaBiomes.BETA_WARM_OCEAN);
        //Moderner Beta: added mb variants
        SurfaceRules.ConditionSource biomesWithSandAndVeryDeepSandstone = isBiome(biomes, Biomes.DESERT, ModernBetaBiomes.BETA_DESERT, ModernBetaBiomes.BETA_ICE_DESERT);
        //? if >=26.2 {
        /*SurfaceRules.RuleSource sulfurCaveBands = SurfaceRules.sequence(
            SurfaceRules.ifTrue(SurfaceRules.noiseCondition3d(Noises.SULFUR_CAVE_GRADIENT, -0.4F, -0.1F), CINNABAR),
            SurfaceRules.ifTrue(SurfaceRules.noiseCondition3d(Noises.SULFUR_CAVE_GRADIENT, 0.0, 0.4F), SULFUR),
            SurfaceRules.ifTrue(SurfaceRules.noiseCondition3d(Noises.SULFUR_CAVE_GRADIENT, 0.4F), CINNABAR)
        );
        *///? }
        SurfaceRules.RuleSource commonSurfaceAndUnderRules = SurfaceRules.sequence(
            SurfaceRules.ifTrue(
                isBiome(biomes, Biomes.STONY_PEAKS),
                SurfaceRules.sequence(SurfaceRules.ifTrue(noiseCondition2d(Noises.CALCITE, -0.0125, 0.0125), CALCITE), STONE)
            ),
            SurfaceRules.ifTrue(
                isBiome(biomes, Biomes.STONY_SHORE),
                SurfaceRules.sequence(SurfaceRules.ifTrue(noiseCondition2d(Noises.GRAVEL, -0.05, 0.05), gravelOrStoneIfCeiling), STONE)
            ),
            SurfaceRules.ifTrue(isBiome(biomes, Biomes.WINDSWEPT_HILLS), SurfaceRules.ifTrue(surfaceNoiseAbove(1.0), STONE)),
            SurfaceRules.ifTrue(biomesWithSandAndSandstone, sandOrSandstoneIfCeiling),
            SurfaceRules.ifTrue(biomesWithSandAndVeryDeepSandstone, sandOrSandstoneIfCeiling),
            SurfaceRules.ifTrue(isBiome(biomes, Biomes.DRIPSTONE_CAVES), STONE)
            //? if >=26.2
            //, SurfaceRules.ifTrue(isBiome(biomes, Biomes.SULFUR_CAVES), SurfaceRules.sequence(sulfurCaveBands, STONE))
        );
        SurfaceRules.RuleSource powderSnowUnderRule = SurfaceRules.ifTrue(
            noiseCondition2d(Noises.POWDER_SNOW, 0.45, 0.58), SurfaceRules.ifTrue(aboveWater, POWDER_SNOW)
        );
        SurfaceRules.RuleSource powderSnowSurfaceRule = SurfaceRules.ifTrue(
            noiseCondition2d(Noises.POWDER_SNOW, 0.35, 0.6), SurfaceRules.ifTrue(aboveWater, POWDER_SNOW)
        );
        SurfaceRules.RuleSource biomeUnderSurfaceRule = SurfaceRules.sequence(
            SurfaceRules.ifTrue(
                isBiome(biomes, Biomes.FROZEN_PEAKS),
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(steep, PACKED_ICE),
                    SurfaceRules.ifTrue(noiseCondition2d(Noises.PACKED_ICE, -0.5, 0.2), PACKED_ICE),
                    SurfaceRules.ifTrue(noiseCondition2d(Noises.ICE, -0.0625, 0.025), ICE),
                    SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK)
                )
            ),
            SurfaceRules.ifTrue(
                isBiome(biomes, Biomes.SNOWY_SLOPES),
                SurfaceRules.sequence(SurfaceRules.ifTrue(steep, STONE), powderSnowUnderRule, SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK))
            ),
            SurfaceRules.ifTrue(isBiome(biomes, Biomes.JAGGED_PEAKS), STONE),
            SurfaceRules.ifTrue(isBiome(biomes, Biomes.GROVE), SurfaceRules.sequence(powderSnowUnderRule, DIRT)),
            commonSurfaceAndUnderRules,
            SurfaceRules.ifTrue(isBiome(biomes, Biomes.WINDSWEPT_SAVANNA), SurfaceRules.ifTrue(surfaceNoiseAbove(1.75), STONE)),
            SurfaceRules.ifTrue(
                isBiome(biomes, Biomes.WINDSWEPT_GRAVELLY_HILLS),
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(surfaceNoiseAbove(2.0), gravelOrStoneIfCeiling),
                    SurfaceRules.ifTrue(surfaceNoiseAbove(1.0), STONE),
                    SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0), DIRT),
                    gravelOrStoneIfCeiling
                )
            ),
            SurfaceRules.ifTrue(isBiome(biomes, Biomes.MANGROVE_SWAMP), MUD),
            DIRT
        );
        SurfaceRules.RuleSource biomeSurfaceRule = SurfaceRules.sequence(
            SurfaceRules.ifTrue(
                isBiome(biomes, Biomes.FROZEN_PEAKS),
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(steep, PACKED_ICE),
                    SurfaceRules.ifTrue(noiseCondition2d(Noises.PACKED_ICE, 0.0, 0.2), PACKED_ICE),
                    SurfaceRules.ifTrue(noiseCondition2d(Noises.ICE, 0.0, 0.025), ICE),
                    SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK)
                )
            ),
            SurfaceRules.ifTrue(
                isBiome(biomes, Biomes.SNOWY_SLOPES),
                SurfaceRules.sequence(SurfaceRules.ifTrue(steep, STONE), powderSnowSurfaceRule, SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK))
            ),
            SurfaceRules.ifTrue(
                isBiome(biomes, Biomes.JAGGED_PEAKS), SurfaceRules.sequence(SurfaceRules.ifTrue(steep, STONE), SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK))
            ),
            SurfaceRules.ifTrue(isBiome(biomes, Biomes.GROVE), SurfaceRules.sequence(powderSnowSurfaceRule, SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK))),
            commonSurfaceAndUnderRules,
            SurfaceRules.ifTrue(
                isBiome(biomes, Biomes.WINDSWEPT_SAVANNA),
                SurfaceRules.sequence(SurfaceRules.ifTrue(surfaceNoiseAbove(1.75), STONE), SurfaceRules.ifTrue(surfaceNoiseAbove(-0.5), COARSE_DIRT))
            ),
            SurfaceRules.ifTrue(
                isBiome(biomes, Biomes.WINDSWEPT_GRAVELLY_HILLS),
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(surfaceNoiseAbove(2.0), gravelOrStoneIfCeiling),
                    SurfaceRules.ifTrue(surfaceNoiseAbove(1.0), STONE),
                    SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0), grassOrDirtIfUnderwater),
                    gravelOrStoneIfCeiling
                )
            ),
            SurfaceRules.ifTrue(
                isBiome(biomes, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA),
                SurfaceRules.sequence(SurfaceRules.ifTrue(surfaceNoiseAbove(1.75), COARSE_DIRT), SurfaceRules.ifTrue(surfaceNoiseAbove(-0.95), PODZOL))
            ),
            SurfaceRules.ifTrue(isBiome(biomes, Biomes.ICE_SPIKES), SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK)),
            SurfaceRules.ifTrue(isBiome(biomes, Biomes.MANGROVE_SWAMP), MUD),
            SurfaceRules.ifTrue(isBiome(biomes, Biomes.MUSHROOM_FIELDS), MYCELIUM),
            grassOrDirtIfUnderwater
        );
        SurfaceRules.ConditionSource clayBand1 = noiseCondition2d(Noises.SURFACE, -0.909, -0.5454);
        SurfaceRules.ConditionSource clayBand2 = noiseCondition2d(Noises.SURFACE, -0.1818, 0.1818);
        SurfaceRules.ConditionSource clayBand3 = noiseCondition2d(Noises.SURFACE, 0.5454, 0.909);
        SurfaceRules.RuleSource mainRuleCloseToSurface = SurfaceRules.sequence(
            SurfaceRules.ifTrue(
                SurfaceRules.ON_FLOOR,
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(
                        isBiome(biomes, Biomes.WOODED_BADLANDS),
                        SurfaceRules.ifTrue(
                            woodedBadlandsTop,
                            SurfaceRules.sequence(
                                SurfaceRules.ifTrue(clayBand1, COARSE_DIRT),
                                SurfaceRules.ifTrue(clayBand2, COARSE_DIRT),
                                SurfaceRules.ifTrue(clayBand3, COARSE_DIRT),
                                grassOrDirtIfUnderwater
                            )
                        )
                    ),
                    SurfaceRules.ifTrue(
                        isBiome(biomes, Biomes.SWAMP),
                        SurfaceRules.ifTrue(
                            swampPuddleLevel,
                            SurfaceRules.ifTrue(SurfaceRules.not(aboveOverworldSeaLevel), SurfaceRules.ifTrue(noiseCondition2d(Noises.SWAMP, 0.0), WATER))
                        )
                    ),
                    SurfaceRules.ifTrue(
                        isBiome(biomes, Biomes.MANGROVE_SWAMP),
                        SurfaceRules.ifTrue(
                            mangroveSwampPuddleLevel,
                            SurfaceRules.ifTrue(SurfaceRules.not(aboveOverworldSeaLevel), SurfaceRules.ifTrue(noiseCondition2d(Noises.SWAMP, 0.0), WATER))
                        )
                    )
                )
            ),
            SurfaceRules.ifTrue(
                isBiome(biomes, Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS),
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(
                        SurfaceRules.ON_FLOOR,
                        SurfaceRules.sequence(
                            SurfaceRules.ifTrue(badlandsTop, ORANGE_TERRACOTTA),
                            SurfaceRules.ifTrue(
                                badlandsMid,
                                SurfaceRules.sequence(
                                    SurfaceRules.ifTrue(clayBand1, TERRACOTTA),
                                    SurfaceRules.ifTrue(clayBand2, TERRACOTTA),
                                    SurfaceRules.ifTrue(clayBand3, TERRACOTTA),
                                    SurfaceRules.bandlands()
                                )
                            ),
                            SurfaceRules.ifTrue(notUnderwater, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, RED_SANDSTONE), RED_SAND)),
                            SurfaceRules.ifTrue(SurfaceRules.not(hole), ORANGE_TERRACOTTA),
                            SurfaceRules.ifTrue(notUnderDeepWater, WHITE_TERRACOTTA),
                            gravelOrStoneIfCeiling
                        )
                    ),
                    SurfaceRules.ifTrue(
                        badlandsHeightCondition,
                        SurfaceRules.sequence(
                            SurfaceRules.ifTrue(aboveOverworldSeaLevel, SurfaceRules.ifTrue(SurfaceRules.not(badlandsMid), ORANGE_TERRACOTTA)), SurfaceRules.bandlands()
                        )
                    ),
                    SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(notUnderDeepWater, WHITE_TERRACOTTA))
                )
            ),
            SurfaceRules.ifTrue(
                SurfaceRules.ON_FLOOR,
                //Moderner Beta: removed not underwater check
                SurfaceRules.sequence(
                    //Moderner Beta: removed frozen ocean condition
                    SurfaceRules.ifTrue(
                        hole,
                        SurfaceRules.sequence(
                            SurfaceRules.ifTrue(aboveWater, AIR),
                            //Moderner Beta: ice only applies to surface
                            SurfaceRules.ifTrue(notUnderwater, SurfaceRules.ifTrue(SurfaceRules.temperature(), ICE)),
                            WATER
                        )
                    ),
                    //Moderner Beta: added not underwater check here
                    SurfaceRules.ifTrue(notUnderwater, biomeSurfaceRule)
                )
            ),
            SurfaceRules.ifTrue(
                notUnderDeepWater,
                SurfaceRules.sequence(
                    //Moderner Beta: removed hole rules here?
                    //SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(hole, WATER)),
                    SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, biomeUnderSurfaceRule),
                    SurfaceRules.ifTrue(biomesWithSandAndSandstone, SurfaceRules.ifTrue(SurfaceRules.DEEP_UNDER_FLOOR, SANDSTONE)),
                    SurfaceRules.ifTrue(biomesWithSandAndVeryDeepSandstone, SurfaceRules.ifTrue(SurfaceRules.VERY_DEEP_UNDER_FLOOR, SANDSTONE))
                )
            ),
            SurfaceRules.ifTrue(
                SurfaceRules.ON_FLOOR,
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(isBiome(biomes, Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS), STONE),
                    //Moderner Beta: added mb variants
                    SurfaceRules.ifTrue(isBiome(biomes, Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, ModernBetaBiomes.BETA_WARM_OCEAN, ModernBetaBiomes.BETA_LUKEWARM_OCEAN), sandOrSandstoneIfCeiling),
                    gravelOrStoneIfCeiling
                )
            )
        );
        ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();
        if (bedrockRoof) {
            builder.add(SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK));
        }

        if (bedrockFloor) {
            builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK));
        }

        SurfaceRules.RuleSource ruleAbovePreliminarySurface = SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), mainRuleCloseToSurface);
        builder.add(doPreliminarySurfaceCheck ? ruleAbovePreliminarySurface : mainRuleCloseToSurface);
        //? if >=26.2
        //builder.add(SurfaceRules.ifTrue(isBiome(biomes, Biomes.SULFUR_CAVES), sulfurCaveBands));

        //Moderner Beta: added condition for deepslate
        if (deepslate) {
            builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("deepslate", VerticalAnchor.absolute(0), VerticalAnchor.absolute(8)), DEEPSLATE));
        }

        return SurfaceRules.sequence(builder.build().toArray(SurfaceRules.RuleSource[]::new));
    }

    private static SurfaceRules.ConditionSource surfaceNoiseAbove(final double threshold) {
        //~ if >=26.2 'noiseCondition' -> 'noiseCondition2d'
        return SurfaceRules.noiseCondition(Noises.SURFACE, threshold / 8.25, Double.MAX_VALUE);
    }

    @SafeVarargs
    public static SurfaceRules.ConditionSource isBiome(final HolderGetter<Biome> biomes, final ResourceKey<Biome>... target) {
        return SurfaceRules.isBiome(/*? >=26.2 {*//*biomes, *//*?}*/ target);
    }

    public static SurfaceRules.ConditionSource noiseCondition2d(final ResourceKey<NormalNoise.NoiseParameters> noise, final double minRange) {
        return noiseCondition2d(noise, minRange, Double.MAX_VALUE);
    }

    public static SurfaceRules.ConditionSource noiseCondition2d(final ResourceKey<NormalNoise.NoiseParameters> noise, final double minRange, final double maxRange) {
        //~ if >=26.2 'noiseCondition' -> 'noiseCondition2d'
        return SurfaceRules.noiseCondition(noise, minRange, maxRange);
    }
}
