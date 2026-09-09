package mod.bluestaggo.modernerbeta.level.chunk;

//? if <26.3 {
import com.google.common.collect.ImmutableList;
import mod.bluestaggo.modernerbeta.compat.ModCompat;
//? } else {
/*import mod.bluestaggo.modernerbeta.ModernerBeta;
*///? }
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomes;
import mod.bluestaggo.modernerbeta.level.chunk.surface.ModernBetaSurfaceRules;
import net.minecraft.core.HolderGetter;
//? if >=26.3 {
/*import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.material.VanillaMaterialConditions;
import net.minecraft.resources.Identifier;
*///? }
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
//? if >=26.3 {
/*import net.minecraft.world.level.levelgen.material.MaterialRules;
import net.minecraft.world.level.levelgen.material.condition.MaterialCondition;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;
*///? } else {
import net.minecraft.world.level.levelgen.SurfaceRules;
//? }
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

//? if <26.3
import java.util.List;

public final class ModernBetaMaterialRules {
    //? if >=26.3 {
    /*public static final ResourceKey<SurfaceRules.RuleSource> OVERWORLD = key("overworld");

    private static final ResourceKey<SurfaceRules.RuleSource> SURFACE = key("overworld/surface");
    private static final ResourceKey<SurfaceRules.RuleSource> SAND = key("overworld/sand_or_sandstone_if_ceiling");
    private static final ResourceKey<SurfaceRules.RuleSource> RED_SAND = key("overworld/red_sand_or_sandstone_if_ceiling");
    private static final ResourceKey<SurfaceRules.RuleSource> OCEAN_FLOOR = key("overworld/ocean_floor");
    *///? }

    private static final SurfaceRules.RuleSource AIR = state(Blocks.AIR);
    //? if >=26.2 {
    /*private static final SurfaceRules.RuleSource WHITE_TERRACOTTA = state(Blocks.DYED_TERRACOTTA.white());
    private static final SurfaceRules.RuleSource ORANGE_TERRACOTTA = state(Blocks.DYED_TERRACOTTA.orange());
    *///? } else {
    private static final SurfaceRules.RuleSource WHITE_TERRACOTTA = state(Blocks.WHITE_TERRACOTTA);
    private static final SurfaceRules.RuleSource ORANGE_TERRACOTTA = state(Blocks.ORANGE_TERRACOTTA);
    //? }
    private static final SurfaceRules.RuleSource TERRACOTTA = state(Blocks.TERRACOTTA);
    private static final SurfaceRules.RuleSource RED_SAND_BLOCK = state(Blocks.RED_SAND);
    private static final SurfaceRules.RuleSource RED_SANDSTONE = state(Blocks.RED_SANDSTONE);
    private static final SurfaceRules.RuleSource STONE = state(Blocks.STONE);
    private static final SurfaceRules.RuleSource DIRT = state(Blocks.DIRT);
    private static final SurfaceRules.RuleSource GRAVEL = state(Blocks.GRAVEL);
    private static final SurfaceRules.RuleSource SAND_BLOCK = state(Blocks.SAND);
    private static final SurfaceRules.RuleSource SANDSTONE = state(Blocks.SANDSTONE);
    private static final SurfaceRules.RuleSource ICE = state(Blocks.ICE);
    private static final SurfaceRules.RuleSource WATER = state(Blocks.WATER);

    //? if <26.3 {
    private static final SurfaceRules.RuleSource BEDROCK = state(Blocks.BEDROCK);
    private static final SurfaceRules.RuleSource DEEPSLATE = state(Blocks.DEEPSLATE);
    private static final SurfaceRules.RuleSource PODZOL = state(Blocks.PODZOL);
    private static final SurfaceRules.RuleSource COARSE_DIRT = state(Blocks.COARSE_DIRT);
    private static final SurfaceRules.RuleSource MYCELIUM = state(Blocks.MYCELIUM);
    private static final SurfaceRules.RuleSource CALCITE = state(Blocks.CALCITE);
    private static final SurfaceRules.RuleSource PACKED_ICE = state(Blocks.PACKED_ICE);
    private static final SurfaceRules.RuleSource SNOW_BLOCK = state(Blocks.SNOW_BLOCK);
    private static final SurfaceRules.RuleSource MUD = state(Blocks.MUD);
    private static final SurfaceRules.RuleSource POWDER_SNOW = state(Blocks.POWDER_SNOW);
    //? }

    private ModernBetaMaterialRules() {
    }

    //? if >=26.3 {
    /*public static void bootstrap(BootstrapContext<SurfaceRules.RuleSource> context) {
        HolderGetter<SurfaceRules.RuleSource> rules = context.lookup(Registries.MATERIAL_RULE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        SurfaceRules.RuleSource surface = surface(context);
        context.register(
            OVERWORLD,
            SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), surface),
                SurfaceRules.ifTrue(
                    isBiome(biomes, Biomes.SULFUR_CAVES),
                    SurfaceRules.getRule(rules, vanillaKey("overworld/sulfur_cave_bands"))
                )
            )
        );
    }

    *///? } else {
    public static SurfaceRules.RuleSource overworldLike(
        HolderGetter<Biome> biomes, boolean doPreliminarySurfaceCheck, boolean bedrockRoof, boolean bedrockFloor, boolean deepslate
    ) {
        SurfaceRules.RuleSource surface = surface(biomes);
        ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();

        builder.addAll(ModCompat.getPreBedrockCustomRules(/*? >=26.2 {*/ /*biomes *//*? }*/));
        if (bedrockRoof) {
            builder.add(SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK));
        }
        if (bedrockFloor) {
            builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK));
        }

        List<SurfaceRules.RuleSource> postBedrockCustomRules = ModCompat.getPostBedrockCustomRules(/*? >=26.2 {*/ /*biomes *//*? }*/);
        if (!postBedrockCustomRules.isEmpty()) {
            ImmutableList.Builder<SurfaceRules.RuleSource> surfaceBuilder = ImmutableList.builder();
            surfaceBuilder.addAll(postBedrockCustomRules);
            surfaceBuilder.add(surface);
            surface = SurfaceRules.sequence(surfaceBuilder.build().toArray(SurfaceRules.RuleSource[]::new));
        }

        builder.add(doPreliminarySurfaceCheck ? SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), surface) : surface);
        //? if >=26.2
        //builder.add(SurfaceRules.ifTrue(isBiome(biomes, Biomes.SULFUR_CAVES), sulfurCaveBands()));
        if (deepslate) {
            builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("deepslate", VerticalAnchor.absolute(0), VerticalAnchor.absolute(8)), DEEPSLATE));
        }

        return SurfaceRules.sequence(builder.build().toArray(SurfaceRules.RuleSource[]::new));
    }
    //? }

    // The old rules used fixed heights and ignored the preset's sandstone and gravel bed settings
    // custom conditions let the registered 26.3 rules read each world's settings during generation
    // the older rule tree was moved here too so both version paths use the same implementation
    // much of this class is that existing code
    private static SurfaceRules.RuleSource surface(
        //? if >=26.3 {
        /*BootstrapContext<SurfaceRules.RuleSource> context
        *///? } else {
        HolderGetter<Biome> biomes
        //? }
    ) {
        //? if >=26.3 {
        /*HolderGetter<SurfaceRules.RuleSource> rules = context.lookup(Registries.MATERIAL_RULE);
        HolderGetter<SurfaceRules.ConditionSource> conditions = context.lookup(Registries.MATERIAL_CONDITION);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        SurfaceRules.ConditionSource onFloor = SurfaceRules.getCondition(conditions, VanillaMaterialConditions.ON_FLOOR);
        SurfaceRules.ConditionSource onCeiling = SurfaceRules.getCondition(conditions, VanillaMaterialConditions.ON_CEILING);
        SurfaceRules.ConditionSource underFloor = SurfaceRules.getCondition(conditions, VanillaMaterialConditions.UNDER_FLOOR);
        SurfaceRules.ConditionSource veryDeepUnderFloor = SurfaceRules.getCondition(conditions, VanillaMaterialConditions.VERY_DEEP_UNDER_FLOOR);
        SurfaceRules.ConditionSource notUnderwater = SurfaceRules.getCondition(conditions, VanillaMaterialConditions.NOT_UNDERWATER);
        SurfaceRules.ConditionSource notUnderDeepWater = SurfaceRules.getCondition(conditions, VanillaMaterialConditions.NOT_UNDER_DEEP_WATER);
        *///? } else {
        SurfaceRules.ConditionSource onFloor = SurfaceRules.ON_FLOOR;
        SurfaceRules.ConditionSource onCeiling = SurfaceRules.ON_CEILING;
        SurfaceRules.ConditionSource underFloor = SurfaceRules.UNDER_FLOOR;
        SurfaceRules.ConditionSource veryDeepUnderFloor = SurfaceRules.VERY_DEEP_UNDER_FLOOR;
        SurfaceRules.ConditionSource notUnderwater = SurfaceRules.waterBlockCheck(-1, 0);
        SurfaceRules.ConditionSource notUnderDeepWater = SurfaceRules.waterStartCheck(-6, -1);
        //? }

        SurfaceRules.ConditionSource deepUnderFloor = SurfaceRules.stoneDepthCheck(0, true, 3, CaveSurface.FLOOR);
        SurfaceRules.ConditionSource aboveWater = SurfaceRules.waterBlockCheck(0, 0);
        SurfaceRules.ConditionSource hole = SurfaceRules.hole();

        SurfaceRules.ConditionSource woodedBadlandsTop = ModernBetaSurfaceRules.yBlockCheck(ModernBetaSurfaceRules.Height.SEA_LEVEL, 22, 2);
        SurfaceRules.ConditionSource badlandsTop = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(256), 0);
        SurfaceRules.ConditionSource badlandsHeight = ModernBetaSurfaceRules.yStartCheck(ModernBetaSurfaceRules.Height.SEA_LEVEL, -1, -1);
        SurfaceRules.ConditionSource badlandsMid = ModernBetaSurfaceRules.yStartCheck(ModernBetaSurfaceRules.Height.SEA_LEVEL, 2, 1);
        SurfaceRules.ConditionSource mangrovePuddle = ModernBetaSurfaceRules.yBlockCheck(ModernBetaSurfaceRules.Height.SEA_LEVEL, -4, 0);
        SurfaceRules.ConditionSource swampPuddle = ModernBetaSurfaceRules.yBlockCheck(ModernBetaSurfaceRules.Height.SEA_LEVEL, -2, 0);
        SurfaceRules.ConditionSource aboveSeaLevel = ModernBetaSurfaceRules.yBlockCheck(ModernBetaSurfaceRules.Height.SEA_LEVEL, -1, 0);
        SurfaceRules.ConditionSource aboveGravelBed = ModernBetaSurfaceRules.yBlockCheck(ModernBetaSurfaceRules.Height.GRAVEL_OCEAN_BED, 0, -1);
        SurfaceRules.ConditionSource generateSandstone = ModernBetaSurfaceRules.surfaceProperty(ModernBetaSurfaceRules.SurfaceProperty.GENERATE_SANDSTONE);
        SurfaceRules.ConditionSource gravelOceanBed = ModernBetaSurfaceRules.surfaceProperty(ModernBetaSurfaceRules.SurfaceProperty.GRAVEL_OCEAN_BED);

        SurfaceRules.RuleSource sand = SurfaceRules.sequence(
            SurfaceRules.ifTrue(generateSandstone, SurfaceRules.ifTrue(onCeiling, SANDSTONE)),
            SAND_BLOCK
        );
        SurfaceRules.RuleSource redSand = SurfaceRules.sequence(
            SurfaceRules.ifTrue(generateSandstone, SurfaceRules.ifTrue(onCeiling, RED_SANDSTONE)),
            RED_SAND_BLOCK
        );
        //? if >=26.3 {
        /*sand = SurfaceRules.registerAndWrap(context, SAND, sand);
        redSand = SurfaceRules.registerAndWrap(context, RED_SAND, redSand);
        *///? }

        SurfaceRules.ConditionSource shallowSandBiomes = isBiome(
            biomes,
            Biomes.WARM_OCEAN,
            Biomes.BEACH,
            Biomes.SNOWY_BEACH,
            ModernBetaBiomes.BETA_WARM_OCEAN
        );
        SurfaceRules.ConditionSource deepSandBiomes = isBiome(
            biomes,
            Biomes.DESERT,
            ModernBetaBiomes.BETA_DESERT,
            ModernBetaBiomes.BETA_ICE_DESERT
        );
        SurfaceRules.RuleSource grassOrDirt = SurfaceRules.sequence(SurfaceRules.ifTrue(aboveWater, state(Blocks.GRASS_BLOCK)), DIRT);
        SurfaceRules.RuleSource gravelOrStone = SurfaceRules.sequence(SurfaceRules.ifTrue(onCeiling, STONE), GRAVEL);

        //? if >=26.3 {
        /*SurfaceRules.ConditionSource sandBiomes = isBiome(
            biomes,
            Biomes.WARM_OCEAN,
            Biomes.BEACH,
            Biomes.SNOWY_BEACH,
            Biomes.DESERT,
            ModernBetaBiomes.BETA_WARM_OCEAN,
            ModernBetaBiomes.BETA_DESERT,
            ModernBetaBiomes.BETA_ICE_DESERT
        );

        // use our sand rules first or vanilla bypasses the sandstone setting
        SurfaceRules.RuleSource biomeSurface = SurfaceRules.sequence(
            SurfaceRules.ifTrue(sandBiomes, sand),
            SurfaceRules.getRule(rules, vanillaKey("overworld/biome_surface"))
        );
        SurfaceRules.RuleSource underBiomeSurface = SurfaceRules.sequence(
            SurfaceRules.ifTrue(sandBiomes, sand),
            SurfaceRules.getRule(rules, vanillaKey("overworld/under_biome_surface"))
        );
        *///? } else {
        SurfaceRules.ConditionSource steep = SurfaceRules.steep();
        SurfaceRules.RuleSource commonSurfaceAndUnderRules = SurfaceRules.sequence(
            SurfaceRules.ifTrue(
                isBiome(biomes, Biomes.STONY_PEAKS),
                SurfaceRules.sequence(SurfaceRules.ifTrue(noiseCondition2d(Noises.CALCITE, -0.0125, 0.0125), CALCITE), STONE)
            ),
            SurfaceRules.ifTrue(
                isBiome(biomes, Biomes.STONY_SHORE),
                SurfaceRules.sequence(SurfaceRules.ifTrue(noiseCondition2d(Noises.GRAVEL, -0.05, 0.05), gravelOrStone), STONE)
            ),
            SurfaceRules.ifTrue(isBiome(biomes, Biomes.WINDSWEPT_HILLS), SurfaceRules.ifTrue(surfaceNoiseAbove(1.0), STONE)),
            SurfaceRules.ifTrue(shallowSandBiomes, sand),
            SurfaceRules.ifTrue(deepSandBiomes, sand),
            SurfaceRules.ifTrue(isBiome(biomes, Biomes.DRIPSTONE_CAVES), STONE)
            //? if >=26.2
            //, SurfaceRules.ifTrue(isBiome(biomes, Biomes.SULFUR_CAVES), SurfaceRules.sequence(sulfurCaveBands(), STONE))
        );
        SurfaceRules.RuleSource powderSnowUnderRule = SurfaceRules.ifTrue(
            noiseCondition2d(Noises.POWDER_SNOW, 0.45, 0.58), SurfaceRules.ifTrue(aboveWater, POWDER_SNOW)
        );
        SurfaceRules.RuleSource powderSnowSurfaceRule = SurfaceRules.ifTrue(
            noiseCondition2d(Noises.POWDER_SNOW, 0.35, 0.6), SurfaceRules.ifTrue(aboveWater, POWDER_SNOW)
        );
        SurfaceRules.RuleSource underBiomeSurface = SurfaceRules.sequence(
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
                    SurfaceRules.ifTrue(surfaceNoiseAbove(2.0), gravelOrStone),
                    SurfaceRules.ifTrue(surfaceNoiseAbove(1.0), STONE),
                    SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0), DIRT),
                    gravelOrStone
                )
            ),
            SurfaceRules.ifTrue(isBiome(biomes, Biomes.MANGROVE_SWAMP), MUD),
            DIRT
        );
        SurfaceRules.RuleSource biomeSurface = SurfaceRules.sequence(
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
                    SurfaceRules.ifTrue(surfaceNoiseAbove(2.0), gravelOrStone),
                    SurfaceRules.ifTrue(surfaceNoiseAbove(1.0), STONE),
                    SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0), grassOrDirt),
                    gravelOrStone
                )
            ),
            SurfaceRules.ifTrue(
                isBiome(biomes, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA),
                SurfaceRules.sequence(SurfaceRules.ifTrue(surfaceNoiseAbove(1.75), COARSE_DIRT), SurfaceRules.ifTrue(surfaceNoiseAbove(-0.95), PODZOL))
            ),
            SurfaceRules.ifTrue(isBiome(biomes, Biomes.ICE_SPIKES), SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK)),
            SurfaceRules.ifTrue(isBiome(biomes, Biomes.MANGROVE_SWAMP), MUD),
            SurfaceRules.ifTrue(isBiome(biomes, Biomes.MUSHROOM_FIELDS), MYCELIUM),
            grassOrDirt
        );
        //? }

        SurfaceRules.RuleSource oceanFloor = SurfaceRules.sequence(
            SurfaceRules.ifTrue(gravelOceanBed, SurfaceRules.ifTrue(SurfaceRules.not(aboveGravelBed), GRAVEL)),
            SurfaceRules.ifTrue(isBiome(biomes, Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS), STONE),
            SurfaceRules.ifTrue(
                isBiome(
                    biomes,
                    Biomes.WARM_OCEAN,
                    Biomes.LUKEWARM_OCEAN,
                    Biomes.DEEP_LUKEWARM_OCEAN,
                    ModernBetaBiomes.BETA_WARM_OCEAN,
                    ModernBetaBiomes.BETA_LUKEWARM_OCEAN
                ),
                sand
            ),
            underBiomeSurface
        );
        //? if >=26.3
        //oceanFloor = SurfaceRules.registerAndWrap(context, OCEAN_FLOOR, oceanFloor);

        SurfaceRules.ConditionSource clayBand1 = noiseCondition2d(Noises.SURFACE, -0.909, -0.5454);
        SurfaceRules.ConditionSource clayBand2 = noiseCondition2d(Noises.SURFACE, -0.1818, 0.1818);
        SurfaceRules.ConditionSource clayBand3 = noiseCondition2d(Noises.SURFACE, 0.5454, 0.909);

        SurfaceRules.RuleSource surface = SurfaceRules.sequence(
            SurfaceRules.ifTrue(
                onFloor,
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(
                        isBiome(biomes, Biomes.WOODED_BADLANDS),
                        SurfaceRules.ifTrue(
                            woodedBadlandsTop,
                            SurfaceRules.sequence(
                                SurfaceRules.ifTrue(clayBand1, state(Blocks.COARSE_DIRT)),
                                SurfaceRules.ifTrue(clayBand2, state(Blocks.COARSE_DIRT)),
                                SurfaceRules.ifTrue(clayBand3, state(Blocks.COARSE_DIRT)),
                                grassOrDirt
                            )
                        )
                    ),
                    puddleRule(biomes, Biomes.SWAMP, swampPuddle, aboveSeaLevel),
                    puddleRule(biomes, Biomes.MANGROVE_SWAMP, mangrovePuddle, aboveSeaLevel)
                )
            ),
            SurfaceRules.ifTrue(
                isBiome(biomes, Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS),
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(
                        onFloor,
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
                            SurfaceRules.ifTrue(notUnderwater, redSand),
                            SurfaceRules.ifTrue(SurfaceRules.not(hole), ORANGE_TERRACOTTA),
                            SurfaceRules.ifTrue(notUnderDeepWater, WHITE_TERRACOTTA),
                            gravelOrStone
                        )
                    ),
                    SurfaceRules.ifTrue(
                        badlandsHeight,
                        SurfaceRules.sequence(
                            SurfaceRules.ifTrue(aboveSeaLevel, SurfaceRules.ifTrue(SurfaceRules.not(badlandsMid), ORANGE_TERRACOTTA)),
                            SurfaceRules.bandlands()
                        )
                    ),
                    SurfaceRules.ifTrue(underFloor, SurfaceRules.ifTrue(notUnderDeepWater, WHITE_TERRACOTTA))
                )
            ),
            SurfaceRules.ifTrue(
                onFloor,
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(
                        hole,
                        SurfaceRules.sequence(
                            SurfaceRules.ifTrue(
                                aboveWater,
                                SurfaceRules.sequence(
                                    SurfaceRules.ifTrue(aboveSeaLevel, AIR),
                                    SurfaceRules.ifTrue(SurfaceRules.temperature(), ICE)
                                )
                            ),
                            WATER
                        )
                    ),
                    SurfaceRules.ifTrue(notUnderwater, biomeSurface),
                    // check the ocean floor before the dirt fallback so the gravel bed setting takes effect
                    oceanFloor
                )
            ),
            SurfaceRules.ifTrue(
                notUnderDeepWater,
                SurfaceRules.ifTrue(
                    SurfaceRules.not(hole),
                    SurfaceRules.sequence(
                        SurfaceRules.ifTrue(underFloor, underBiomeSurface),
                        SurfaceRules.ifTrue(
                            generateSandstone,
                            SurfaceRules.sequence(
                                SurfaceRules.ifTrue(shallowSandBiomes, SurfaceRules.ifTrue(deepUnderFloor, SANDSTONE)),
                                SurfaceRules.ifTrue(deepSandBiomes, SurfaceRules.ifTrue(veryDeepUnderFloor, SANDSTONE))
                            )
                        )
                    )
                )
            )
        );
        //? if >=26.3
        //return SurfaceRules.registerAndWrap(context, SURFACE, surface);
        //? if <26.3
        return surface;
    }

    private static SurfaceRules.RuleSource puddleRule(
        HolderGetter<Biome> biomes,
        ResourceKey<Biome> biome,
        SurfaceRules.ConditionSource level,
        SurfaceRules.ConditionSource aboveSeaLevel
    ) {
        return SurfaceRules.ifTrue(
            isBiome(biomes, biome),
            SurfaceRules.ifTrue(
                level,
                SurfaceRules.ifTrue(
                    SurfaceRules.not(aboveSeaLevel),
                    SurfaceRules.ifTrue(noiseCondition2d(Noises.SWAMP, 0.0), WATER)
                )
            )
        );
    }

    //? if >=26.3 {
    /*private static ResourceKey<SurfaceRules.RuleSource> key(String name) {
        return ResourceKey.create(Registries.MATERIAL_RULE, ModernerBeta.createId(name));
    }

    private static ResourceKey<SurfaceRules.RuleSource> vanillaKey(String name) {
        return ResourceKey.create(Registries.MATERIAL_RULE, Identifier.withDefaultNamespace(name));
    }

    *///? }

    //? if >=26.2 && <26.3 {
    /*private static SurfaceRules.RuleSource sulfurCaveBands() {
        return SurfaceRules.sequence(
            SurfaceRules.ifTrue(SurfaceRules.noiseCondition3d(Noises.SULFUR_CAVE_GRADIENT, -0.4F, -0.1F), state(Blocks.CINNABAR)),
            SurfaceRules.ifTrue(SurfaceRules.noiseCondition3d(Noises.SULFUR_CAVE_GRADIENT, 0.0, 0.4F), state(Blocks.SULFUR)),
            SurfaceRules.ifTrue(SurfaceRules.noiseCondition3d(Noises.SULFUR_CAVE_GRADIENT, 0.4F), state(Blocks.CINNABAR))
        );
    }
    *///? }

    private static SurfaceRules.ConditionSource surfaceNoiseAbove(double threshold) {
        return noiseCondition2d(Noises.SURFACE, threshold / 8.25);
    }

    @SafeVarargs
    private static SurfaceRules.ConditionSource isBiome(HolderGetter<Biome> biomes, ResourceKey<Biome>... target) {
        return SurfaceRules.isBiome(/*? >=26.2 {*//*biomes, *//*?}*/ target);
    }

    private static SurfaceRules.ConditionSource noiseCondition2d(ResourceKey<NormalNoise.NoiseParameters> noise, double minRange) {
        return noiseCondition2d(noise, minRange, Double.MAX_VALUE);
    }

    private static SurfaceRules.ConditionSource noiseCondition2d(ResourceKey<NormalNoise.NoiseParameters> noise, double minRange, double maxRange) {
        //~ if >=26.2 'noiseCondition' -> 'noiseCondition2d'
        return SurfaceRules.noiseCondition(noise, minRange, maxRange);
    }

    private static SurfaceRules.RuleSource state(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
