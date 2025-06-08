package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.intlayers;

import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.SeedMixer;

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
            saltedSeed = SeedMixer.mixSeed(saltedSeed, seed);
        }
        this.saltedSeed = saltedSeed;
    }

    public abstract int generate(RegistryEntryLookup<Biome> biomeRegistry, int x, int z);

    public int sample(RegistryEntryLookup<Biome> biomeRegistry, int x, int z) {
        Long2IntLinkedOpenHashMap cache = this.cache.get();

        long coord = ChunkPos.toLong(x, z);
        int value = cache.get(coord);

        if (value != Integer.MIN_VALUE) {
            return value;
        }

        value = this.generate(biomeRegistry, x, z);
        if (cache.size() == Layer.CACHE_CAPACITY) {
            cache.removeFirstInt();
        }
        cache.put(coord, value);
        return value;
    }

    public void init(long worldSeed) {
        this.saltedSeed = this.seed;
        for (int i = 0; i < 3; i++) {
            this.saltedSeed = SeedMixer.mixSeed(this.saltedSeed, this.seed);
        }
        long preWorldSeed = this.saltedSeed;

        this.saltedSeed = worldSeed;
        for (int i = 0; i < 3; i++) {
            this.saltedSeed = SeedMixer.mixSeed(this.saltedSeed, preWorldSeed);
        }

        this.random = ThreadLocal.withInitial(() -> new LayerRandom(this.saltedSeed));
    }

    protected final LayerRandom getRandom(long x, long z) {
        LayerRandom random = this.random.get();
        random.init(x, z);
        return random;
    }

    protected static Biome getBiomeFromLayer(RegistryEntryLookup<Biome> biomeRegistry, Layer layer, int x, int z) {
        ExtendedBiomeId extendedBiomeId = layer.sample(x, z);
        return biomeRegistry.getOptional(RegistryKey.of(RegistryKeys.BIOME, extendedBiomeId.baseId()))
            .map(RegistryEntry::value)
            .orElse(null);
    }
}
