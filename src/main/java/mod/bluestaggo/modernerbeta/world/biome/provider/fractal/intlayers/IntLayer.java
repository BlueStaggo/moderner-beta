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
    private transient LayerRandom random;
    private transient final Long2IntLinkedOpenHashMap cache = new Long2IntLinkedOpenHashMap(); {
        this.cache.defaultReturnValue(Integer.MIN_VALUE);
    }

    public IntLayer(long seed) {
        this.seed = seed;

        long saltedSeed = seed;
        for (int i = 0; i < 3; i++) {
            saltedSeed = SeedMixer.mixSeed(saltedSeed, seed);
        }
        this.saltedSeed = saltedSeed;
    }

    public abstract int generate(RegistryEntryLookup<Biome> biomeRegistry, int x, int z);

    public synchronized int sample(RegistryEntryLookup<Biome> biomeRegistry, int x, int z) {
        long coord = ChunkPos.toLong(x, z);
        int value = this.cache.get(coord);

        if (value != Integer.MIN_VALUE) {
            return value;
        }

        value = this.generate(biomeRegistry, x, z);
        while (this.cache.size() >= Layer.CACHE_CAPACITY) {
            this.cache.removeFirstInt();
        }
        this.cache.put(coord, value);
        return value;
    }

    public void init(long worldSeed) {
        this.cache.clear();

        this.saltedSeed = this.seed;
        for (int i = 0; i < 3; i++) {
            this.saltedSeed = SeedMixer.mixSeed(this.saltedSeed, this.seed);
        }
        long preWorldSeed = this.saltedSeed;

        this.saltedSeed = worldSeed;
        for (int i = 0; i < 3; i++) {
            this.saltedSeed = SeedMixer.mixSeed(this.saltedSeed, preWorldSeed);
        }

        this.random = new LayerRandom(this.saltedSeed);
    }

    protected final LayerRandom getRandom(long x, long z) {
        this.random.init(x, z);
        return this.random;
    }

    protected static Biome getBiomeFromLayer(RegistryEntryLookup<Biome> biomeRegistry, Layer layer, int x, int z) {
        ExtendedBiomeId extendedBiomeId = layer.sample(x, z);
        return biomeRegistry.getOptional(RegistryKey.of(RegistryKeys.BIOME, extendedBiomeId.baseId()))
            .map(RegistryEntry::value)
            .orElse(null);
    }
}
