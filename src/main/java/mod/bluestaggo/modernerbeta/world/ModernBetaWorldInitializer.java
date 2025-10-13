//~registryOr
package mod.bluestaggo.modernerbeta.world;

import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaWorld;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.LevelStem;

public class ModernBetaWorldInitializer {
    public static void initStarting(MinecraftServer server) {
        Registry<LevelStem> registryDimensionOptions = server.registries().compositeAccess().lookupOrThrow(Registries.LEVEL_STEM);
        long seed = server.getWorldData().worldGenOptions().seed();
        
        registryDimensionOptions.entrySet().forEach(entry -> {
            LevelStem dimensionOptions = entry.getValue();
            
            ChunkGenerator chunkGenerator = dimensionOptions.generator();
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
        for (ServerLevel world : server.getAllLevels()) {
            ModernBetaWorld modernBetaWorld = (ModernBetaWorld)world;
            if (world.getChunkSource().getGenerator() instanceof ModernBetaChunkGenerator chunkGenerator
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
