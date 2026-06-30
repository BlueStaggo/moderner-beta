package mod.bluestaggo.modernerbeta.level.carver.configured;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.level.carver.*;
//? if <26.3
import mod.bluestaggo.modernerbeta.tags.ModernBetaBlockTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
//? if <26.3
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Block;
//? if <26.3
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.*;
import net.minecraft.world.level.levelgen.heightproviders.BiasedToBottomHeight;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;

import java.util.Optional;

public class ModernBetaConfiguredCarvers {
    public static final ResourceKey<ConfiguredWorldCarver<?>> BETA_CAVE = of("beta_cave");
    public static final ResourceKey<ConfiguredWorldCarver<?>> BETA_CAVE_DEEP = of("beta_cave_deep");
    public static final ResourceKey<ConfiguredWorldCarver<?>> BETA_CANYON = of("beta_canyon");

    @SuppressWarnings("unchecked")
    public static void bootstrap(BootstrapContext<?> context) {
        BootstrapContext<ConfiguredWorldCarver<?>> carverRegisterable = (BootstrapContext<ConfiguredWorldCarver<?>>)context;
        HolderGetter<Block> registryBlock = carverRegisterable.lookup(Registries.BLOCK);
        
        boolean useFixedCaves = false;
        boolean useAquifers = false;
        boolean useSurfaceRules = false;

        //~ if >=26.3 'BetaCaveCarverConfiguration' -> 'BetaCaveWorldCarver'
        BetaCaveCarverConfiguration configCave = new BetaCaveCarverConfiguration(
            0.0f,                                                                               // Probability, unused here
            BiasedToBottomHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.absolute(127), 8),       // Y Level
            ConstantFloat.of(0.5f),                                                 // Y scale, for large cave case(?)
            //? if <26.3 {
            VerticalAnchor.aboveBottom(10),                                                            // Lava Level
            CarverDebugSettings.of(false, Blocks.WARPED_BUTTON.defaultBlockState()),
            registryBlock.getOrThrow(ModernBetaBlockTags.OVERWORLD_CARVER_REPLACEABLES),
            //? }
            ConstantFloat.of(1.0f),                                                 // Tunnel horizontal scale
            ConstantFloat.of(1.0f),                                                 // Tunnel vertical scale
            ConstantFloat.of(-0.7f),                                                // Y Floor Level
            Optional.of(useFixedCaves),
            Optional.of(useAquifers)
            //? if <26.3
            , Optional.of(useSurfaceRules)
        );

        //~ if >=26.3 'BetaCaveCarverConfiguration' -> 'CaveWorldCarver'
        BetaCaveCarverConfiguration configCaveDeep = new BetaCaveCarverConfiguration(
            0.15f,                                                                              // Probability, unused here
            UniformHeight.of(VerticalAnchor.aboveBottom(0), VerticalAnchor.absolute(0)),             // Y Level
            //? if >=26.3 {
            /*VeryBiasedToBottomInt.of(0, 14),
            TrapezoidFloat.of(0.0F, 3.0F, 1.0F),
            true,
            *///? }
            UniformFloat.of(0.1f, 0.9f),                                            // Y scale, for large cave case(?)
            //? if <26.3 {
            VerticalAnchor.aboveBottom(10),                                                            // Lava Level
            CarverDebugSettings.of(false, Blocks.CRIMSON_BUTTON.defaultBlockState()),
            registryBlock.getOrThrow(ModernBetaBlockTags.OVERWORLD_CARVER_REPLACEABLES),
            //? }
            UniformFloat.of(0.7f, 1.4f),                                            // Tunnel horizontal scale
            UniformFloat.of(0.8f, 1.3f),                                            // Tunnel vertical scale
            //? if >=26.3
            //ConstantFloat.of(1.0F),
            UniformFloat.of(-1.0f, -0.4f)                                           // Y Floor Level
            //? if <26.3 {
            , Optional.of(useFixedCaves),
            Optional.of(useAquifers),
            Optional.of(useSurfaceRules)
            //? }
        );

        //~ if >=26.3 'CanyonCarverConfiguration' -> 'CanyonWorldCarver'
        CanyonCarverConfiguration configRavine = new CanyonCarverConfiguration(
            0.02f,                                                                              // Probability
            BiasedToBottomHeight.of(VerticalAnchor.aboveBottom(20), VerticalAnchor.absolute(67), 8), // Y Level
            //? if <26.3
            ConstantFloat.of(3.0F),                                                 // Y scale
            //? if <26.3 {
            VerticalAnchor.aboveBottom(10),                                                            // Lava Level
            CarverDebugSettings.of(false, Blocks.WARPED_BUTTON.defaultBlockState()),
            registryBlock.getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES),
            //? }
            UniformFloat.of(-0.125F, 0.125F),                                       // Vertical rotation
            //~ if >=26.3 'CanyonCarverConfiguration.CanyonShapeConfiguration' -> 'CanyonWorldCarver.Shape'
            new CanyonCarverConfiguration.CanyonShapeConfiguration(
                UniformFloat.of(0.75F, 1.0F),                                       // Distance factor
                TrapezoidFloat.of(0.0F, 6.0F, 2.0F),                                // Thickness
                3,                                                                              // Width smoothness
                UniformFloat.of(0.75F, 1.0F),                                       // Horizontal radius factor
                1.0F,                                                                           // Vertical radius default factor
                0.0F                                                                            // Vertical radius center factor
                //? if >=26.3
                //, ConstantFloat.of(3.0F)
            )
        );
    
        carverRegisterable.register(BETA_CAVE, /*? if >=26.3 {*/ /*configCave *//*? } else {*/ ModernBetaCarvers.BETA_CAVE.configured(configCave) /*? }*/);
        carverRegisterable.register(BETA_CAVE_DEEP, /*? if >=26.3 {*/ /*configCaveDeep *//*? } else {*/ WorldCarver.CAVE.configured(configCaveDeep) /*? }*/);
        carverRegisterable.register(BETA_CANYON, /*? if >=26.3 {*/ /*configRavine *//*? } else {*/ WorldCarver.CANYON.configured(configRavine) /*? }*/);
    }
    
    public static ResourceKey<ConfiguredWorldCarver<?>> of(String id) {
        //~ if >=26.3 'CONFIGURED_CARVER' -> 'CARVER'
        return ResourceKey.create(Registries.CONFIGURED_CARVER, ModernerBeta.createId(id));
    }
}
