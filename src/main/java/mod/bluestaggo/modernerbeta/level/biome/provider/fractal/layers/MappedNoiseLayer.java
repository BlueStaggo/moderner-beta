package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.doubles.DoubleImmutableList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeResolver;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.minecraft.world.level.biome.Biome;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class MappedNoiseLayer extends Layer {
    public static final com.mojang.serialization.MapCodec<MappedNoiseLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillLayerFields(instance)
            .and(instance.group(
                Entry.CODEC
                    .listOf()
                    .fieldOf("values")
                    .forGetter(layer -> Stream.concat(
                        layer.lowerBiomes.stream(),
                        Stream.concat(
                            Stream.of(new Entry(0.0, layer.middleBiome)),
                            layer.upperBiomes.stream()
                        )
                    ).toList()),
                Codec.DOUBLE.fieldOf("scale").orElse(1.0).forGetter(layer -> layer.scale),
                Codec.DOUBLE.listOf().fieldOf("amplitudes").orElse(List.of(1.0)).forGetter(layer -> layer.amplitudes),
                Codec.BOOL.fieldOf("useSaltedSeed").orElse(true).forGetter(layer -> layer.useSaltedSeed)
            ))
            .apply(instance, MappedNoiseLayer::new)
    );

    private final List<Entry> lowerBiomes;
    private final List<Entry> upperBiomes;
    private final ExtendedIdentifier middleBiome;
    private final double scale;
    private final DoubleList amplitudes;
    private final boolean useSaltedSeed;
    private transient PerlinNoise noiseSampler;
    private transient List<ResolvedEntry> resolvedLowerBiomes;
    private transient List<ResolvedEntry> resolvedUpperBiomes;
    private transient ExtendedHolder<Biome> resolvedMiddleBiome;

    public MappedNoiseLayer(String id, long seed, List<Entry> values, double scale, List<Double> amplitudes, boolean useSaltedSeed) {
        this(id, seed, values, scale, new DoubleImmutableList(amplitudes), useSaltedSeed);
    }

    public MappedNoiseLayer(String id, long seed, List<Entry> values, double scale, DoubleList amplitudes, boolean useSaltedSeed) {
        super(id, seed);
        this.middleBiome = values.stream()
            .filter(pair -> pair.value == 0.0)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No biome at noise value 0.0 provided!"))
            .biome;
        this.lowerBiomes = values.stream()
            .filter(pair -> pair.value < 0.0)
            .sorted(Comparator.comparingDouble(Entry::value))
            .toList();
        this.upperBiomes = values.stream()
            .filter(pair -> pair.value > 0.0)
            .sorted(Comparator.comparingDouble(Entry::value).reversed())
            .toList();
        this.scale = scale;
        this.amplitudes = amplitudes;
        this.useSaltedSeed = useSaltedSeed;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.MAPPED_NOISE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void init(long worldSeed) {
        super.init(worldSeed);
        long noiseSeed = this.useSaltedSeed ? this.getSaltedSeed() : worldSeed;
        this.noiseSampler = PerlinNoise.createLegacyForLegacyNetherBiome(new SingleThreadedRandomSource(noiseSeed), 0, amplitudes);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void initUnsalted() {
        super.initUnsalted();
        this.noiseSampler = PerlinNoise.createLegacyForLegacyNetherBiome(new SingleThreadedRandomSource(0), 0, amplitudes);
    }

    @Override
    protected ExtendedHolder<Biome> generate(int x, int z) {
        double noiseValue = this.noiseSampler.getValue(x / this.scale, z / this.scale, 0.0);

        for (ResolvedEntry lowerBiome : this.resolvedLowerBiomes) {
            if (noiseValue < lowerBiome.value) {
                return lowerBiome.biome;
            }
        }

        for (ResolvedEntry upperBiome : this.resolvedUpperBiomes) {
            if (noiseValue > upperBiome.value) {
                return upperBiome.biome;
            }
        }

        return this.resolvedMiddleBiome;
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedHolder<Biome>> biomes) {
        this.resolvedLowerBiomes.forEach(pair -> biomes.add(pair.biome));
        this.resolvedUpperBiomes.forEach(pair -> biomes.add(pair.biome));
        biomes.add(this.resolvedMiddleBiome);
    }

    @Override
    protected void bindOwnBiomes(ExtendedBiomeResolver biomeResolver) {
        this.resolvedLowerBiomes = this.lowerBiomes.stream()
            .map(entry -> new ResolvedEntry(entry.value, biomeResolver.resolve(entry.biome)))
            .toList();
        this.resolvedUpperBiomes = this.upperBiomes.stream()
            .map(entry -> new ResolvedEntry(entry.value, biomeResolver.resolve(entry.biome)))
            .toList();
        this.resolvedMiddleBiome = biomeResolver.resolve(this.middleBiome);
    }

    public record Entry(double value, ExtendedIdentifier biome) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                Codec.DOUBLE.fieldOf("value").forGetter(Entry::value),
                ExtendedIdentifier.CODEC.fieldOf("biome").forGetter(Entry::biome)
            ).apply(instance, Entry::new)
        );
    }

    private record ResolvedEntry(double value, ExtendedHolder<Biome> biome) {
    }
}
