package mod.bluestaggo.modernerbeta.world.carver.configured;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.tags.ModernBetaBlockTags;
import mod.bluestaggo.modernerbeta.world.carver.BetaCaveCarverConfig;
import mod.bluestaggo.modernerbeta.world.carver.ModernBetaCarvers;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.TrapezoidFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.BiasedToBottomHeight;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;

import java.util.Optional;

public class ModernBetaConfiguredCarvers {
    public static final ResourceKey<ConfiguredWorldCarver<?>> BETA_CAVE = of("beta_cave");
    public static final ResourceKey<ConfiguredWorldCarver<?>> BETA_CAVE_DEEP = of("beta_cave_deep");
    public static final ResourceKey<ConfiguredWorldCarver<?>> BETA_CANYON = of("beta_canyon");

    @SuppressWarnings("unchecked")
    public static void bootstrap(BootstrapContext<?> registerable) {
        BootstrapContext<ConfiguredWorldCarver<?>> carverRegisterable = (BootstrapContext<ConfiguredWorldCarver<?>>)registerable;
        HolderGetter<Block> registryBlock = carverRegisterable.lookup(Registries.BLOCK);
        
        boolean useFixedCaves = false;
        boolean useAquifers = false;
        boolean useSurfaceRules = false;

        BetaCaveCarverConfig configCave = new BetaCaveCarverConfig(
            0.0f,                                                                               // Probability, unused here
            BiasedToBottomHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.absolute(127), 8),       // Y Level
            ConstantFloat.of(0.5f),                                                 // Y scale, for large cave case(?)
            VerticalAnchor.aboveBottom(10),                                                            // Lava Level
            CarverDebugSettings.of(false, Blocks.WARPED_BUTTON.defaultBlockState()),
            registryBlock.getOrThrow(ModernBetaBlockTags.OVERWORLD_CARVER_REPLACEABLES),
            ConstantFloat.of(1.0f),                                                 // Tunnel horizontal scale
            ConstantFloat.of(1.0f),                                                 // Tunnel vertical scale
            ConstantFloat.of(-0.7f),                                                // Y Floor Level
            Optional.of(useFixedCaves),
            Optional.of(useAquifers),
            Optional.of(useSurfaceRules)
        );
        
        BetaCaveCarverConfig configCaveDeep = new BetaCaveCarverConfig(
            0.15f,                                                                              // Probability, unused here
            UniformHeight.of(VerticalAnchor.aboveBottom(0), VerticalAnchor.absolute(0)),             // Y Level
            UniformFloat.of(0.1f, 0.9f),                                            // Y scale, for large cave case(?)
            VerticalAnchor.aboveBottom(10),                                                            // Lava Level
            CarverDebugSettings.of(false, Blocks.CRIMSON_BUTTON.defaultBlockState()),
            registryBlock.getOrThrow(ModernBetaBlockTags.OVERWORLD_CARVER_REPLACEABLES),
            UniformFloat.of(0.7f, 1.4f),                                            // Tunnel horizontal scale
            UniformFloat.of(0.8f, 1.3f),                                            // Tunnel vertical scale
            UniformFloat.of(-1.0f, -0.4f),                                          // Y Floor Level
            Optional.of(useFixedCaves),
            Optional.of(useAquifers),
            Optional.of(useSurfaceRules)
        );

        CanyonCarverConfiguration configRavine = new CanyonCarverConfiguration(
            0.02f,                                                                              // Probability
            BiasedToBottomHeight.of(VerticalAnchor.aboveBottom(20), VerticalAnchor.absolute(67), 8), // Y Level
            ConstantFloat.of(3.0F),                                                 // Y scale
            VerticalAnchor.aboveBottom(10),                                                            // Lava Level
            CarverDebugSettings.of(false, Blocks.WARPED_BUTTON.defaultBlockState()),
            registryBlock.getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES),
            UniformFloat.of(-0.125F, 0.125F),                                       // Vertical rotation
            new CanyonCarverConfiguration.CanyonShapeConfiguration(
                UniformFloat.of(0.75F, 1.0F),                                       // Distance factor
                TrapezoidFloat.of(0.0F, 6.0F, 2.0F),                                // Thickness
                3,                                                                              // Width smoothness
                UniformFloat.of(0.75F, 1.0F),                                       // Horizontal radius factor
                1.0F,                                                                           // Vertical radius default factor
                0.0F                                                                            // Vertical radius center factor
            )
        );
    
        carverRegisterable.register(BETA_CAVE, ModernBetaCarvers.BETA_CAVE.configured(configCave));
        carverRegisterable.register(BETA_CAVE_DEEP, WorldCarver.CAVE.configured(configCaveDeep));
        carverRegisterable.register(BETA_CANYON, WorldCarver.CANYON.configured(configRavine));
    }
    
    public static ResourceKey<ConfiguredWorldCarver<?>> of(String id) {
        return ResourceKey.create(Registries.CONFIGURED_CARVER, ModernerBeta.createId(id));
    }
}
