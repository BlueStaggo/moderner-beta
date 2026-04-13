package mod.bluestaggo.modernerbeta.level.biome.injection.injector;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.level.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.level.biome.injection.BiomeInjectionContext;
import mod.bluestaggo.modernerbeta.level.biome.injection.InjectionNeeds;
import mod.bluestaggo.modernerbeta.mixin.BiomeAccessor;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.core.Holder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.biome.Biome;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MappedClimateBiomeInjector implements BiomeInjector {
    public static final com.mojang.serialization.MapCodec<MappedClimateBiomeInjector> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            StringRepresentable.fromEnum(Type::values).fieldOf("climate_type")
                    .forGetter(injector -> injector.type),
            Entry.CODEC
                .listOf()
                .fieldOf("values")
                .forGetter(injector -> Stream.concat(
                    injector.lowerBiomes.stream(),
                    Stream.concat(
                        Stream.of(new Entry(0.0, injector.middleBiome)),
                        injector.upperBiomes.stream()
                    )
                ).toList())
        ).apply(instance, MappedClimateBiomeInjector::new)
    );

    private final List<Entry> lowerBiomes;
    private final List<Entry> upperBiomes;
    private final Holder<Biome> middleBiome;

    private final Type type;

    public MappedClimateBiomeInjector(Type type, List<Entry> values) {
        this.type = type;

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
    }

    @Override
    public BiomeInjectorType<?> getType() {
        return BiomeInjectorType.MAPPED_CLIMATE;
    }

    @Override
    public Holder<Biome> apply(BiomeInjectionContext context, int biomeX, int biomeY, int biomeZ) {
        double value;
        if (context.getFulfillableNeeds().contains(InjectionNeeds.CLIMATE)) {
            int x = context.getX();
            int z = context.getZ();

            ClimateSampler sampler = ((ClimateSampler) context.biomeSource.getBiomeProvider());
            Clime climate = sampler.sample(x, z);
            value = switch (type) {
                case TEMPERATURE -> climate.temp();
                case DOWNFALL -> climate.rain();
                case WEIRDNESS -> climate.weird();
            };
        } else {
            Biome biome = context.getBiome().value();
            Biome.ClimateSettings weather = ((BiomeAccessor)(Object)biome).getClimateSettings();

            value = switch (type) {
                case TEMPERATURE -> weather.temperature();
                case DOWNFALL -> weather.downfall();
                default -> 0.0F;
            };
        }

        value -= 0.5D;
        for (Entry lowerBiome : this.lowerBiomes) {
            if (value < lowerBiome.value) {
                return lowerBiome.biome;
            }
        }

        for (Entry upperBiome : this.upperBiomes) {
            if (value > upperBiome.value) {
                return upperBiome.biome;
            }
        }

        return this.middleBiome;
    }

    @Override
    public Set<Holder<Biome>> getPossibleBiomes() {
        ImmutableSet.Builder<Holder<Biome>> builder = ImmutableSet.builder();

        Set<Holder<Biome>> lower = this.lowerBiomes.stream()
            .map(b -> b.biome)
            .collect(Collectors.toSet());

        Set<Holder<Biome>> upper = this.upperBiomes.stream()
            .map(b -> b.biome)
            .collect(Collectors.toSet());

        builder.addAll(lower);
        builder.add(this.middleBiome);
        builder.addAll(upper);

        return builder.build();
    }

    @Override
    public EnumSet<InjectionNeeds> needs() {
        return EnumSet.of(InjectionNeeds.BIOMES);
    }

    public enum Type implements StringRepresentable {
        TEMPERATURE("temperature"),
        DOWNFALL("downfall"),
        WEIRDNESS("weirdness");

        private final String type;

        Type(String type) {
            this.type = type;
        }

        @Override
        public String getSerializedName() {
            return this.type;
        }
    }

    public record Entry(double value, Holder<Biome> biome) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                Codec.DOUBLE.fieldOf("value").forGetter(Entry::value),
                Biome.CODEC.fieldOf("biome").forGetter(Entry::biome)
            ).apply(instance, Entry::new)
        );
    }
}
