package mod.bluestaggo.modernerbeta.level.feature.placement;

import mod.bluestaggo.modernerbeta.util.noise.OctaveNoise;
import mod.bluestaggo.modernerbeta.level.feature.placement.noise.NoiseBasedCount;
import mod.bluestaggo.modernerbeta.level.feature.placement.noise.NoiseBasedCountBeta;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraft.world.level.levelgen.placement.RepeatingPlacement;

//~ if >=26.3 'extends' -> 'implements'
public abstract class NoiseBasedCountPlacementModifier extends RepeatingPlacement {
    protected final int count;
    protected final double extraChance;
    protected final int extraCount;

    protected NoiseBasedCount noiseDecorator;
    
    protected NoiseBasedCountPlacementModifier(int count, double extraChance, int extraCount) {
        this.count = count;
        this.extraChance = extraChance;
        this.extraCount = extraCount;
        
        this.noiseDecorator = new NoiseBasedCountBeta(new SingleThreadedRandomSource(0L));
    }
    
    @Override
    public int count(RandomSource random, BlockPos pos) {
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;
        
        return this.noiseDecorator.sample(chunkX, chunkZ, random) + this.count + ((random.nextFloat() < this.extraChance) ? this.extraCount : 0);
    }

    public abstract void setOctaves(OctaveNoise octaves);
}
