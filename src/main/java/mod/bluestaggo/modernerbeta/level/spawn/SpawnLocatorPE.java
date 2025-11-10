package mod.bluestaggo.modernerbeta.level.spawn;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProviderNoise;
import mod.bluestaggo.modernerbeta.api.level.spawn.SpawnLocator;
import mod.bluestaggo.modernerbeta.tags.ModernBetaBiomeTags;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkHeightmap;
import mod.bluestaggo.modernerbeta.util.random.mersenne.MTRandom;
import mod.bluestaggo.modernerbeta.util.noise.PerlinOctaveNoise;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import org.slf4j.event.Level;

import java.util.Optional;
import java.util.Random;

/*
 * Port of MCPE v0.6.0 alpha player spawn locator.
 * 
 */
public class SpawnLocatorPE implements SpawnLocator {
    private final Random rand;
    
    private final ChunkProvider chunkProvider;
    private final PerlinOctaveNoise beachOctaveNoise;
    
    public SpawnLocatorPE(ChunkProvider chunkProvider, PerlinOctaveNoise beachOctaveNoise, MTRandom rand) {
        this.rand = rand;
        
        this.chunkProvider = chunkProvider;
        this.beachOctaveNoise = beachOctaveNoise;
    }
    
    @Override
    public Optional<BlockPos> locateSpawn(LevelHeightAccessor level) {
        ModernerBeta.log(Level.INFO, "Setting a PE beach spawn..");
        
        int x = 0;
        int z = 0;
        int attempts = 0;
        
        while(!this.isSandAt(level, x, z)) {
            if (attempts > 10000) {
                ModernerBeta.log(Level.INFO, "Exceeded spawn attempts, spawning anyway at 128,128..");
                
                x = 128;
                z = 128;
                break;
            }
            
            x += this.rand.nextInt(32) - this.rand.nextInt(32);
            z += this.rand.nextInt(32) - this.rand.nextInt(32);
            
            // Keep spawn pos within bounds of original PE world size
            if (x < 4) x += 32;
            if (x >= 251) x -= 32;
            
            if (z < 4) z += 32;
            if (z >= 251) z -= 32;
            
            attempts++;
        }
        
        int y = (this.chunkProvider instanceof ChunkProviderNoise noiseChunkProvider) ?
            noiseChunkProvider.getHeight(level, x, z, ChunkHeightmap.Type.SURFACE_FLOOR) :
            this.chunkProvider.getHeight(level, x, z, Heightmap.Types.WORLD_SURFACE_WG);
        
        return Optional.of(new BlockPos(x, y, z));
    }

    private boolean isSandAt(LevelHeightAccessor level, int x, int z) {
        double eighth = 0.03125D;
        int seaLevel = this.chunkProvider.getSeaLevel();
        
        int y = (this.chunkProvider instanceof ChunkProviderNoise noiseChunkProvider) ?
            noiseChunkProvider.getHeight(level, x, z, ChunkHeightmap.Type.SURFACE_FLOOR) :
            this.chunkProvider.getHeight(level, x, z, Heightmap.Types.OCEAN_FLOOR_WG);
        
        Holder<Biome> biome = (this.chunkProvider.getChunkGenerator().getBiomeSource() instanceof ModernBetaBiomeSource modernBetaBiomeSource) ?
            modernBetaBiomeSource.getBiomeForSpawn(x, y, z) :
            this.chunkProvider.getBiome(x >> 2, y >> 2, z >> 2, null);
        
        return 
            (biome.is(ModernBetaBiomeTags.SURFACE_CONFIG_SAND) && y >= seaLevel) ||
            (this.beachOctaveNoise.sample(x * eighth, z * eighth, 0.0) > 0.0 && y >= seaLevel && y <= seaLevel + 2);
    }
}
