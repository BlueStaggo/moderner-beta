package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.LocalRandom;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class MappedNoiseLayer extends Layer {
    public static final com.mojang.serialization.MapCodec<MappedNoiseLayer> CODEC = RecordCodecBuilder.mapCodec(
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
                Codec.DOUBLE.fieldOf("scale").forGetter(layer -> layer.scale),
                Codec.BOOL.fieldOf("useSaltedSeed").orElse(true).forGetter(layer -> layer.useSaltedSeed)
            ))
            .apply(instance, MappedNoiseLayer::new)
    );

    private final List<Entry> lowerBiomes;
    private final List<Entry> upperBiomes;
    private final ExtendedBiomeId middleBiome;
    private final double scale;
    private final boolean useSaltedSeed;
    private transient PerlinNoiseSampler noiseSampler;

    public MappedNoiseLayer(String id, long seed, List<Entry> values, double scale, boolean useSaltedSeed) {
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
            // I don't know why I needed to specify that but I had to
            .sorted(Comparator.comparingDouble(Entry::value).reversed())
            .toList();
        this.scale = scale;
        this.useSaltedSeed = useSaltedSeed;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.MAPPED_NOISE;
    }

    @Override
    public void init(long worldSeed) {
        super.init(worldSeed);
        long noiseSeed = this.useSaltedSeed ? this.getSaltedSeed() : worldSeed;
        this.noiseSampler = new PerlinNoiseSampler(new LocalRandom(noiseSeed));
    }

    @Override
    public void initUnsalted() {
        super.initUnsalted();
        this.noiseSampler = new PerlinNoiseSampler(new LocalRandom(0));
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        double noiseValue = this.noiseSampler.sample(x / this.scale, z / this.scale, 0.0);

        for (Entry lowerBiome : this.lowerBiomes) {
            if (noiseValue < lowerBiome.value) {
                return lowerBiome.biome;
            }
        }

        for (Entry upperBiome : this.upperBiomes) {
            if (noiseValue > upperBiome.value) {
                return upperBiome.biome;
            }
        }

        return this.middleBiome;
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedBiomeId> biomes) {
        this.lowerBiomes.forEach(pair -> biomes.add(pair.biome));
        this.upperBiomes.forEach(pair -> biomes.add(pair.biome));
        biomes.add(this.middleBiome);
    }

    public record Entry(double value, ExtendedBiomeId biome) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                Codec.DOUBLE.fieldOf("value").forGetter(Entry::value),
                ExtendedBiomeId.CODEC.fieldOf("biome").forGetter(Entry::biome)
            )
                .apply(instance, Entry::new)
        );
    }
}
