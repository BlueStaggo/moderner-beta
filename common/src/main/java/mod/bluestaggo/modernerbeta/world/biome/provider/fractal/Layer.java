package mod.bluestaggo.modernerbeta.world.biome.provider.fractal;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import mod.bluestaggo.modernerbeta.api.registry.ModernBetaBuiltInRegistries;
import net.minecraft.util.math.ColumnPos;
import net.minecraft.world.biome.source.SeedMixer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class Layer {
    private final static int CACHE_CAPACITY = 25;
    public static final Codec<Layer> TYPE_CODEC = ModernBetaBuiltInRegistries.FRACTAL_LAYER.getCodec().dispatch(Layer::getType, LayerType::codec);

    public final String id;
    public final long seed;

    private transient long saltedSeed;
    private transient LayerRandom random;

    private transient final Long2ObjectLinkedOpenHashMap<ExtendedBiomeId> cache = new Long2ObjectLinkedOpenHashMap<>(CACHE_CAPACITY);

    protected static <L extends Layer> Products.P2<
        RecordCodecBuilder.Mu<L>,
        String,
        Long
    > fillLayerFields(RecordCodecBuilder.Instance<L> instance) {
        return instance.group(
            Codec.STRING.fieldOf("id").forGetter(layer -> layer.id),
            Codec.LONG.fieldOf("seed").orElse(0L).forGetter(layer -> layer.seed)
        );
    }

    public Layer(String id, long seed) {
        this.id = id;
        this.seed = seed;

        long saltedSeed = seed;
        for (int i = 0; i < 3; i++) {
            saltedSeed = SeedMixer.mixSeed(saltedSeed, seed);
        }
        this.saltedSeed = saltedSeed;
    }

    protected abstract LayerType<?> getType();

    protected abstract ExtendedBiomeId generate(int x, int z);

    public void configure(Function<String, Layer> layerMap) {
    }

    protected void addPossibleBiomes(Set<ExtendedBiomeId> biomes) {
    }

    protected List<Layer> getParents() {
        return Collections.emptyList();
    }

    public void init(long worldSeed) {
        for (Layer parent : this.getParents()) {
            parent.init(worldSeed);
        }
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

    public synchronized ExtendedBiomeId sample(int x, int z) {
        long pos = ColumnPos.pack(x, z);
        ExtendedBiomeId biome = this.cache.get(pos);
        if (biome != null) {
            return biome;
        }

        biome = this.generate(x, z);
        while (this.cache.size() >= CACHE_CAPACITY) {
            this.cache.removeFirst();
        }
        this.cache.put(pos, biome);
        return biome;
    }

    public final ExtendedBiomeId[] sampleNeighbors(int x, int z) {
        return new ExtendedBiomeId[] {
            this.sample(x - 1, z),
            this.sample(x + 1, z),
            this.sample(x, z - 1),
            this.sample(x, z + 1),
        };
    }

    public final ExtendedBiomeId[] sampleDiagonalNeighbors(int x, int z) {
        return new ExtendedBiomeId[] {
            this.sample(x - 1, z - 1),
            this.sample(x + 1, z - 1),
            this.sample(x - 1, z + 1),
            this.sample(x + 1, z + 1),
        };
    }

    protected static boolean allNeighborsEqual(ExtendedBiomeId[] neighbors, ExtendedBiomeId i) {
        return neighbors[0].equals(i) && neighbors[1].equals(i) && neighbors[2].equals(i) && neighbors[3].equals(i);
    }

    protected static boolean allNeighborsInSet(ExtendedBiomeId[] neighbors, Collection<ExtendedBiomeId> set) {
        return set.contains(neighbors[0]) && set.contains(neighbors[1]) && set.contains(neighbors[2]) && set.contains(neighbors[3]);
    }

    protected static boolean neighborsContain(ExtendedBiomeId[] neighbors, ExtendedBiomeId i) {
        return neighbors[0].equals(i) || neighbors[1].equals(i) || neighbors[2].equals(i) || neighbors[3].equals(i);
    }

    protected static boolean anyNeighborsInSet(ExtendedBiomeId[] neighbors, Collection<ExtendedBiomeId> set) {
        return set.contains(neighbors[0]) || set.contains(neighbors[1]) || set.contains(neighbors[2]) || set.contains(neighbors[3]);
    }

    public final void addPossibleBiomesRecursive(Set<ExtendedBiomeId> biomes) {
        for (Layer parent : this.getParents()) {
            parent.addPossibleBiomesRecursive(biomes);
        }
        this.addPossibleBiomes(biomes);
    }

    @Override
    public String toString() {
        String string = this.id + ": " + ModernBetaBuiltInRegistries.FRACTAL_LAYER.getKey(this.getType()) + " " + this.seed;
        List<Layer> parents = this.getParents();
        if (!parents.isEmpty()) {
            string += " <- " + parents.stream()
                .map(parent -> parent.id)
                .distinct()
                .collect(Collectors.joining(", "));
        }
        return string;
    }
}
