package mod.bluestaggo.modernerbeta.level.spawn;

import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.api.level.chunk.ChunkProviderNoise;
import mod.bluestaggo.modernerbeta.api.level.spawn.SpawnLocator;
import mod.bluestaggo.modernerbeta.tags.ModernBetaBiomeTags;
import mod.bluestaggo.modernerbeta.util.LoggingUtil;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkHeightmap;
import mod.bluestaggo.modernerbeta.util.noise.OctaveNoise;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import org.slf4j.event.Level;

import java.util.Optional;

/*
 * Port of Beta 1.7.3 player spawn locator.
 * 
 */
public class SpawnLocatorBeta implements SpawnLocator {
    private final RandomSource rand;
    
    private final ChunkProvider chunkProvider;
    private final OctaveNoise beachOctaveNoise;
    
    public SpawnLocatorBeta(ChunkProvider chunkProvider, OctaveNoise beachOctaveNoise, RandomSource rand) {
        this.rand = rand;
        
        this.chunkProvider = chunkProvider;
        this.beachOctaveNoise = beachOctaveNoise;
    }

    @Override
    public Optional<BlockPos> locateSpawn(LevelHeightAccessor level) {
        LoggingUtil.log(Level.INFO, "Setting a beach spawn..");
        
        int x = 0;
        int z = 0;
        int attempts = 0;
        
        while(!this.isSandAt(level, x, z)) {
            if (attempts > 10000) {
                LoggingUtil.log(Level.INFO, "Exceeded spawn attempts, spawning anyway at 0,0..");
                
                x = 0;
                z = 0;
                break;
            }
            
            x += this.rand.nextInt(64) - this.rand.nextInt(64);
            z += this.rand.nextInt(64) - this.rand.nextInt(64);
            
            attempts++;
        }
        
        int y = (this.chunkProvider instanceof ChunkProviderNoise chunkProviderNoise) ?
            chunkProviderNoise.getHeight(level, x, z, ChunkHeightmap.Type.SURFACE_FLOOR) :
            this.chunkProvider.getHeight(level, x, z, Heightmap.Types.WORLD_SURFACE_WG);
        
        return Optional.of(new BlockPos(x, y, z));
    }

    private boolean isSandAt(LevelHeightAccessor level, int x, int z) {
        double eighth = 0.03125D;
        int seaLevel = this.chunkProvider.getSeaLevel();
        
        int y = (this.chunkProvider instanceof ChunkProviderNoise chunkProviderNoise) ?
            chunkProviderNoise.getHeight(level, x, z, ChunkHeightmap.Type.SURFACE_FLOOR) :
            this.chunkProvider.getHeight(level, x, z, Heightmap.Types.OCEAN_FLOOR_WG);

        Holder<Biome> biome = (this.chunkProvider.getChunkGenerator().getBiomeSource() instanceof ModernBetaBiomeSource modernBetaBiomeSource) ?
            modernBetaBiomeSource.getBiomeForSpawn(x, y, z) :
            this.chunkProvider.getBiome(x >> 2, y >> 2, z >> 2, null);
        
        return
            (biome.is(ModernBetaBiomeTags.SURFACE_CONFIG_SAND) && y >= seaLevel) ||
            (this.beachOctaveNoise.sample(x * eighth, z * eighth, 0.0) > 0.0 && y >= seaLevel && y <= seaLevel + 2);
    }

}
