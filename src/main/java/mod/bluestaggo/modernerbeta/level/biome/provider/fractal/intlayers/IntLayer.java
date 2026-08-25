package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.intlayers;

import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.LayerRandom;
import net.minecraft.util.LinearCongruentialGenerator;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;

public abstract class IntLayer {
    private transient final long seed;
    private transient long saltedSeed;
    private transient ThreadLocal<LayerRandom> random;
    private transient final ThreadLocal<Long2IntLinkedOpenHashMap> cache
        = ThreadLocal.withInitial(() -> {
            Long2IntLinkedOpenHashMap map = new Long2IntLinkedOpenHashMap(Layer.CACHE_CAPACITY);
            map.defaultReturnValue(Integer.MIN_VALUE);
            return map;
        });

    public IntLayer(long seed) {
        this.seed = seed;

        long saltedSeed = seed;
        for (int i = 0; i < 3; i++) {
            saltedSeed = LinearCongruentialGenerator.next(saltedSeed, seed);
        }
        this.saltedSeed = saltedSeed;
    }

    public abstract int generate(int x, int z);

    public int sample(int x, int z) {
        Long2IntLinkedOpenHashMap cache = this.cache.get();

        long coord = ChunkPos.pack(x, z);
        int value = cache.get(coord);

        if (value != Integer.MIN_VALUE) {
            return value;
        }

        value = this.generate(x, z);
        if (cache.size() == Layer.CACHE_CAPACITY) {
            cache.removeFirstInt();
        }
        cache.put(coord, value);
        return value;
    }

    public void init(long worldSeed) {
        this.saltedSeed = this.seed;
        for (int i = 0; i < 3; i++) {
            this.saltedSeed = LinearCongruentialGenerator.next(this.saltedSeed, this.seed);
        }
        long preWorldSeed = this.saltedSeed;

        this.saltedSeed = worldSeed;
        for (int i = 0; i < 3; i++) {
            this.saltedSeed = LinearCongruentialGenerator.next(this.saltedSeed, preWorldSeed);
        }

        this.random = ThreadLocal.withInitial(() -> new LayerRandom(this.saltedSeed));
    }

    protected final LayerRandom getRandom(long x, long z) {
        LayerRandom random = this.random.get();
        random.init(x, z);
        return random;
    }

    protected static Biome getBiomeFromLayer(Layer layer, int x, int z) {
        return layer.sample(x, z).value();
    }
}
