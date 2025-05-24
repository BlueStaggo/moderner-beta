package mod.bluestaggo.modernerbeta.world.biome.provider.fractal;

import net.minecraft.world.biome.source.SeedMixer;

import java.util.List;

public class LayerRandom {
    private final long baseSeed;
    private long seed;

    public LayerRandom(long seed) {
        this.baseSeed = seed;
    }

    public void init(long x, long z) {
        this.seed = this.baseSeed;
        for (int i = 0; i < 2; i++) {
            this.seed = SeedMixer.mixSeed(this.seed, x);
            this.seed = SeedMixer.mixSeed(this.seed, z);
        }
    }

    public int nextInt(int bound) {
        int result = (int)((this.seed >> 24) % (long)bound);
        if (result < 0) {
            result += bound;
        }
        this.seed = SeedMixer.mixSeed(this.seed, this.baseSeed);
        return result;
    }

    public <T> T nextItem(List<T> list) {
        return list.get(this.nextInt(list.size()));
    }
}
