package mod.bluestaggo.modernerbeta.fabric.data;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.world.chunk.surface.SurfaceBlocks;
import mod.bluestaggo.modernerbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
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
    public static final ResourceKey<SurfaceConfig> THEEND = keyOf("theend");
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
        context.register(SAND, new SurfaceConfig(SurfaceBlocks.SAND, SurfaceBlocks.SAND, SurfaceBlocks.GRAVEL));
        context.register(RED_SAND, new SurfaceConfig(SurfaceBlocks.RED_SAND, SurfaceBlocks.RED_SAND, SurfaceBlocks.GRAVEL));
        context.register(BADLANDS, new SurfaceConfig(SurfaceBlocks.BADLANDS, SurfaceBlocks.RED_SAND, SurfaceBlocks.GRAVEL));
        context.register(NETHER, new SurfaceConfig(SurfaceBlocks.NETHER, SurfaceBlocks.NETHER_SOUL_SAND, SurfaceBlocks.NETHER_GRAVEL));
        context.register(WARPED_NYLIUM, new SurfaceConfig(SurfaceBlocks.WARPED_NYLIUM, SurfaceBlocks.NETHER_SOUL_SAND, SurfaceBlocks.NETHER_GRAVEL));
        context.register(CRIMSON_NYLIUM, new SurfaceConfig(SurfaceBlocks.CRIMSON_NYLIUM, SurfaceBlocks.NETHER_SOUL_SAND, SurfaceBlocks.NETHER_GRAVEL));
        context.register(BASALT, new SurfaceConfig(SurfaceBlocks.BASALT));
        context.register(SOUL_SOIL, new SurfaceConfig(SurfaceBlocks.SOUL_SOIL));
        context.register(THEEND, new SurfaceConfig(SurfaceBlocks.THEEND));
        context.register(GRASS, new SurfaceConfig(SurfaceBlocks.GRASS));
        context.register(MUD, new SurfaceConfig(SurfaceBlocks.MUD));
        context.register(MYCELIUM, new SurfaceConfig(SurfaceBlocks.MYCELIUM));
        context.register(PODZOL, new SurfaceConfig(SurfaceBlocks.PODZOL));
        context.register(STONE, new SurfaceConfig(SurfaceBlocks.STONE));
        context.register(SNOW, new SurfaceConfig(SurfaceBlocks.SNOW));
        context.register(SNOW_DIRT, new SurfaceConfig(SurfaceBlocks.SNOW_DIRT));
        context.register(SNOW_PACKED_ICE, new SurfaceConfig(SurfaceBlocks.SNOW_PACKED_ICE));
        context.register(SNOW_STONE, new SurfaceConfig(SurfaceBlocks.SNOW_STONE));
    }

    private static ResourceKey<SurfaceConfig> keyOf(String id) {
        return ResourceKey.create(ModernBetaResourceKeys.SURFACE_CONFIG, ModernerBeta.createId(id));
    }
}
