package mod.bluestaggo.modernerbeta.fabric.data;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.level.chunk.surface.SurfaceBlocks;
import mod.bluestaggo.modernerbeta.api.level.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.tags.ModernBetaBiomeTags;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public class ModernBetaSurfaceConfigs {
    public static final ResourceKey<SurfaceConfig> SAND = keyOf("sand");
    public static final ResourceKey<SurfaceConfig> RED_SAND = keyOf("red_sand");
    public static final ResourceKey<SurfaceConfig> BADLANDS = keyOf("badlands");
    public static final ResourceKey<SurfaceConfig> NETHER = keyOf("nether");
    public static final ResourceKey<SurfaceConfig> WARPED_NYLIUM = keyOf("warped_nylium");
    public static final ResourceKey<SurfaceConfig> CRIMSON_NYLIUM = keyOf("crimson_nylium");
    public static final ResourceKey<SurfaceConfig> BASALT = keyOf("basalt");
    public static final ResourceKey<SurfaceConfig> SOUL_SOIL = keyOf("soul_soil");
    public static final ResourceKey<SurfaceConfig> END = keyOf("end");
    public static final ResourceKey<SurfaceConfig> GRASS = keyOf("grass");
    public static final ResourceKey<SurfaceConfig> MUD = keyOf("mud");
    public static final ResourceKey<SurfaceConfig> MYCELIUM = keyOf("mycelium");
    public static final ResourceKey<SurfaceConfig> PODZOL = keyOf("podzol");
    public static final ResourceKey<SurfaceConfig> STONE = keyOf("stone");
    public static final ResourceKey<SurfaceConfig> SNOW = keyOf("snow");
    public static final ResourceKey<SurfaceConfig> SNOW_DIRT = keyOf("snow_dirt");
    public static final ResourceKey<SurfaceConfig> SNOW_PACKED_ICE = keyOf("snow_packed_ice");
    public static final ResourceKey<SurfaceConfig> SNOW_STONE = keyOf("snow_stone");

    public static void bootstrap(BootstrapContext<SurfaceConfig> context) {
        context.register(SAND, new SurfaceConfig(SurfaceBlocks.SAND, SurfaceBlocks.SAND, SurfaceBlocks.GRAVEL, ModernBetaBiomeTags.SURFACE_CONFIG_SAND));
        context.register(RED_SAND, new SurfaceConfig(SurfaceBlocks.RED_SAND, SurfaceBlocks.RED_SAND, SurfaceBlocks.GRAVEL, ModernBetaBiomeTags.SURFACE_CONFIG_RED_SAND));
        context.register(BADLANDS, new SurfaceConfig(SurfaceBlocks.BADLANDS, SurfaceBlocks.RED_SAND, SurfaceBlocks.GRAVEL, ModernBetaBiomeTags.SURFACE_CONFIG_BADLANDS));
        context.register(NETHER, new SurfaceConfig(SurfaceBlocks.NETHER, SurfaceBlocks.NETHER_SOUL_SAND, SurfaceBlocks.NETHER_GRAVEL, ModernBetaBiomeTags.SURFACE_CONFIG_NETHER));
        context.register(WARPED_NYLIUM, new SurfaceConfig(SurfaceBlocks.WARPED_NYLIUM, SurfaceBlocks.NETHER_SOUL_SAND, SurfaceBlocks.NETHER_GRAVEL, ModernBetaBiomeTags.SURFACE_CONFIG_WARPED_NYLIUM));
        context.register(CRIMSON_NYLIUM, new SurfaceConfig(SurfaceBlocks.CRIMSON_NYLIUM, SurfaceBlocks.NETHER_SOUL_SAND, SurfaceBlocks.NETHER_GRAVEL, ModernBetaBiomeTags.SURFACE_CONFIG_CRIMSON_NYLIUM));
        context.register(BASALT, new SurfaceConfig(SurfaceBlocks.BASALT, ModernBetaBiomeTags.SURFACE_CONFIG_BASALT));
        context.register(SOUL_SOIL, new SurfaceConfig(SurfaceBlocks.SOUL_SOIL, ModernBetaBiomeTags.SURFACE_CONFIG_SOUL_SOIL));
        context.register(END, new SurfaceConfig(SurfaceBlocks.END, ModernBetaBiomeTags.SURFACE_CONFIG_END));
        context.register(GRASS, new SurfaceConfig(SurfaceBlocks.GRASS, ModernBetaBiomeTags.SURFACE_CONFIG_GRASS));
        context.register(MUD, new SurfaceConfig(SurfaceBlocks.MUD, ModernBetaBiomeTags.SURFACE_CONFIG_MUD));
        context.register(MYCELIUM, new SurfaceConfig(SurfaceBlocks.MYCELIUM, ModernBetaBiomeTags.SURFACE_CONFIG_MYCELIUM));
        context.register(PODZOL, new SurfaceConfig(SurfaceBlocks.PODZOL, ModernBetaBiomeTags.SURFACE_CONFIG_PODZOL));
        context.register(STONE, new SurfaceConfig(SurfaceBlocks.STONE, ModernBetaBiomeTags.SURFACE_CONFIG_STONE));
        context.register(SNOW, new SurfaceConfig(SurfaceBlocks.SNOW, ModernBetaBiomeTags.SURFACE_CONFIG_SNOW));
        context.register(SNOW_DIRT, new SurfaceConfig(SurfaceBlocks.SNOW_DIRT, ModernBetaBiomeTags.SURFACE_CONFIG_SNOW_DIRT));
        context.register(SNOW_PACKED_ICE, new SurfaceConfig(SurfaceBlocks.SNOW_PACKED_ICE, ModernBetaBiomeTags.SURFACE_CONFIG_SNOW_PACKED_ICE));
        context.register(SNOW_STONE, new SurfaceConfig(SurfaceBlocks.SNOW_STONE, ModernBetaBiomeTags.SURFACE_CONFIG_SNOW_STONE));
    }

    private static ResourceKey<SurfaceConfig> keyOf(String id) {
        return ResourceKey.create(ModernBetaResourceKeys.SURFACE_CONFIG, ModernerBeta.createId(id));
    }
}
