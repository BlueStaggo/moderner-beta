//~registryOr
package mod.bluestaggo.modernerbeta.level;

import mod.bluestaggo.modernerbeta.api.level.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaLevel;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.LevelStem;

public class ModernBetaLevelInitializer {
    public static void initStarting(MinecraftServer server) {
        Registry<LevelStem> registryDimensionOptions = server.registries().compositeAccess().lookupOrThrow(Registries.LEVEL_STEM);
        //? if >=26.1 {
        long seed = server.getWorldGenSettings().options().seed();
        //? } else {
        /*long seed = server.getWorldData().worldGenOptions().seed();
        *///? }
        
        registryDimensionOptions.entrySet().forEach(entry -> {
            LevelStem dimensionOptions = entry.getValue();
            
            ChunkGenerator chunkGenerator = dimensionOptions.generator();
            BiomeSource biomeSource = chunkGenerator.getBiomeSource();
            
            if (biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
                modernBetaBiomeSource.initProvider(seed);
            }

            if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
                modernBetaChunkGenerator.initProvider(seed);
            }
        });
    }

    public static void initStarted(MinecraftServer server) {
        for (ServerLevel level : server.getAllLevels()) {
            ModernBetaLevel modernBetaLevel = (ModernBetaLevel)level;
            if (level.getChunkSource().getGenerator() instanceof ModernBetaChunkGenerator chunkGenerator
                && chunkGenerator.getBiomeSource() instanceof ModernBetaBiomeSource biomeSource) {
                modernBetaLevel.modernerBeta$setModded(true);
                if (biomeSource.getBiomeProvider() instanceof ClimateSampler climateSampler) {
                    modernBetaLevel.modernerBeta$setClimateSampler(climateSampler);
                }
                modernBetaLevel.modernerBeta$setTemperatureHeightScaling(biomeSource.getBiomeProvider().getTemperatureHeightScaling());
            }
        }
    }
}
