//~dotLocation
package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ColumnPos;
import net.minecraft.util.LinearCongruentialGenerator;
import net.minecraft.world.level.biome.Biome;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class Layer {
    public final static int CACHE_CAPACITY = 25;
    public static final Codec<Layer> TYPE_CODEC = ModernBetaRegistries.FRACTAL_LAYER.byNameCodec()
        .dispatch(Layer::getType, LayerType::codec);

    public final String id;
    public final long seed;

    private transient long saltedSeed;
    private transient ThreadLocal<LayerRandom> random = ThreadLocal.withInitial(() -> new LayerRandom(0));
    private transient int initialSkip;

    private transient ThreadLocal<Long2ObjectLinkedOpenHashMap<ExtendedHolder<Biome>>> cache = createCache();

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
            saltedSeed = LinearCongruentialGenerator.next(saltedSeed, seed);
        }
        this.saltedSeed = saltedSeed;
    }

    public abstract LayerType<?> getType();

    protected abstract ExtendedHolder<Biome> generate(int x, int z);

    public void configure(Function<String, Layer> layerMap) {
    }

    protected void setInitialSkip(int initialSkip) {
        this.initialSkip = initialSkip;
    }

    protected void addPossibleBiomes(Set<ExtendedHolder<Biome>> biomes) {
    }

    protected List<Layer> getParents() {
        return Collections.emptyList();
    }

    public void init(long worldSeed) {
        for (Layer parent : this.getParents()) {
            parent.init(worldSeed);
        }
        this.cache = createCache();

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

    public void initUnsalted() {
        for (Layer parent : this.getParents()) {
            parent.initUnsalted();
        }
        this.cache = createCache();

        this.saltedSeed = 0;
        this.random = ThreadLocal.withInitial(() -> new LayerRandom(0));
    }

    public ExtendedHolder<Biome> sample(int x, int z) {
        Long2ObjectLinkedOpenHashMap<ExtendedHolder<Biome>> cache = this.cache.get();
        long pos = ColumnPos.asLong(x, z);
        ExtendedHolder<Biome> biome = cache.get(pos);
        if (biome != null) {
            return biome;
        }

        biome = this.generate(x, z);
        if (cache.size() == CACHE_CAPACITY) {
            cache.removeFirst();
        }
        cache.put(pos, biome);
        return biome;
    }

    protected final LayerRandom getRandom(long x, long z) {
        LayerRandom random = this.random.get();
        random.init(x, z);
        if (this.initialSkip > 0) {
            random.consumeCount(this.initialSkip);
        }
        return random;
    }

    protected final long getSaltedSeed() {
        return this.saltedSeed;
    }

    public final void addPossibleBiomesRecursive(Set<ExtendedHolder<Biome>> biomes) {
        for (Layer parent : this.getParents()) {
            parent.addPossibleBiomesRecursive(biomes);
        }
        this.addPossibleBiomes(biomes);
    }

    @SuppressWarnings("unchecked")
    public final ExtendedHolder<Biome>[] sampleNeighbors(int x, int z) {
        return new ExtendedHolder[] {
            this.sample(x - 1, z),
            this.sample(x + 1, z),
            this.sample(x, z - 1),
            this.sample(x, z + 1),
        };
    }

    @SuppressWarnings("unchecked")
    public final ExtendedHolder<Biome>[] sampleDiagonalNeighbors(int x, int z) {
        return new ExtendedHolder[] {
            this.sample(x - 1, z - 1),
            this.sample(x + 1, z - 1),
            this.sample(x - 1, z + 1),
            this.sample(x + 1, z + 1),
        };
    }

    public final Layer skipRandom(int amount) {
        return new PreSkipRandomLayer(this.id, 0, this, amount);
    }

    public final Layer unsalted() {
        return new UnsaltedLayer(this.id, 0, this);
    }

    protected String getName() {
        return ModernBetaRegistries.FRACTAL_LAYER.getResourceKey(this.getType())
            .map(key -> {
                ResourceLocation identifier = key.location();
                if (ModernerBeta.MOD_ID.equals(identifier.getNamespace())) {
                    return identifier.getPath();
                }
                return identifier.toString();
            })
            .orElse("[unregistered]");
    }

    @Override
    public String toString() {
        String string = this.id + ": " + this.getName();
        if (this.seed != 0) {
            string += " " + this.seed;
        }

        List<Layer> parents = this.getParents();
        if (!parents.isEmpty()) {
            string += " <- " + parents.stream()
                .map(parent -> parent.id)
                .distinct()
                .collect(Collectors.joining(", "));
        }
        return string;
    }

    public static boolean allNeighborsEqual(ExtendedHolder<Biome>[] neighbors, ExtendedHolder<Biome> i) {
        return neighbors[0].is(i) && neighbors[1].is(i) && neighbors[2].is(i) && neighbors[3].is(i);
    }

    public static boolean neighborsContain(ExtendedHolder<Biome>[] neighbors, ExtendedHolder<Biome> i) {
        return neighbors[0].is(i) || neighbors[1].is(i) || neighbors[2].is(i) || neighbors[3].is(i);
    }

    private static ThreadLocal<Long2ObjectLinkedOpenHashMap<ExtendedHolder<Biome>>> createCache() {
        return ThreadLocal.withInitial(() -> new Long2ObjectLinkedOpenHashMap<>(CACHE_CAPACITY));
    }
}
