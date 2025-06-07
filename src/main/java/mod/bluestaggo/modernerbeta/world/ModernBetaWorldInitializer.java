package mod.bluestaggo.modernerbeta.world;

import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaWorld;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class ModernBetaWorldInitializer {
    public static void initStarting(MinecraftServer server) {
        Registry<DimensionOptions> registryDimensionOptions = server.getCombinedDynamicRegistries().getCombinedRegistryManager().getOrThrow(RegistryKeys.DIMENSION);
        long seed = server.getSaveProperties().getGeneratorOptions().getSeed();
        
        registryDimensionOptions.getEntrySet().forEach(entry -> {
            DimensionOptions dimensionOptions = entry.getValue();
            
            ChunkGenerator chunkGenerator = dimensionOptions.chunkGenerator();
            BiomeSource biomeSource = chunkGenerator.getBiomeSource();
            
            if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
                modernBetaChunkGenerator.initProvider(seed);
            }
            
            if (biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
                modernBetaBiomeSource.initProvider(seed);
            }
        });
    }

    public static void initStarted(MinecraftServer server) {
        for (ServerWorld world : server.getWorlds()) {
            ModernBetaWorld modernBetaWorld = (ModernBetaWorld)world;
            if (world.getChunkManager().getChunkGenerator() instanceof ModernBetaChunkGenerator chunkGenerator
                && chunkGenerator.getBiomeSource() instanceof ModernBetaBiomeSource biomeSource) {
                modernBetaWorld.modernerBeta$setModded(true);
                if (biomeSource.getBiomeProvider() instanceof ClimateSampler climateSampler) {
                    modernBetaWorld.modernerBeta$setClimateSampler(climateSampler);
                }
                modernBetaWorld.modernerBeta$setTemperatureHeightScaling(biomeSource.getBiomeProvider().getTemperatureHeightScaling());
            }
        }
    }
}
