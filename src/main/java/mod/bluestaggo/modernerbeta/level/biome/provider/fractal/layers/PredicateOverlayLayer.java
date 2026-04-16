package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeIds;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.predicates.BiomePredicate;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class PredicateOverlayLayer extends SingleParentLayer {
    public static final com.mojang.serialization.MapCodec<PredicateOverlayLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(Target.CODEC.listOf().fieldOf("targets").forGetter(layer -> layer.targets))
            .apply(instance, PredicateOverlayLayer::new)
    );

    private final List<Target> targets;
    private transient List<ConfiguredTarget> configuredTargets;

    public PredicateOverlayLayer(String id, long seed, String parent, List<Target> targets) {
        super(id, seed, parent);
        this.targets = targets;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.PREDICATE_OVERLAY;
    }

    @Override
    public void configure(Function<String, Layer> layerMap) {
        super.configure(layerMap);
        this.configuredTargets = this.targets.stream()
            .map(target -> target.configure(layerMap))
            .toList();
    }

    @Override
    protected List<Layer> getParents() {
        return Stream.concat(
            Stream.of(this.parentLayer),
            this.configuredTargets.stream()
                .map(ConfiguredTarget::layer)
                .filter(Objects::nonNull)
        ).toList();
    }

    @Override
    protected ExtendedHolder<Biome> generate(int x, int z) {
        ExtendedHolder<Biome> baseBiome = this.parentLayer.sample(x, z);
        for (ConfiguredTarget target : this.configuredTargets) {
            Supplier<LayerRandom> randomSupplier = Suppliers.memoize(() -> this.getRandom(x, z));
            if (target.predicate().matches(baseBiome, this.parentLayer, randomSupplier, x, z)) {
                ExtendedHolder<Biome> result = target.sample(x, z);
                if (result.is(ExtendedBiomeIds.NULL)) {
                    return baseBiome;
                }
                return result;
            }
        }
        return baseBiome;
    }

    @Override
    protected void addPossibleBiomes(Set<ExtendedHolder<Biome>> biomes) {
        for (ConfiguredTarget target : this.configuredTargets) {
            target.addPossibleBiomes(biomes);
        }
    }

    public record Target(BiomePredicate predicate, String result, Type type) {
        public static final Codec<Target> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                BiomePredicate.BASE_CODEC.fieldOf("predicate").forGetter(Target::predicate),
                Codec.STRING.fieldOf("result").forGetter(Target::result),
                StringRepresentable.fromEnum(Type::values).fieldOf("type").orElse(Type.BIOME).forGetter(Target::type)
            ).apply(instance, Target::new)
        );

        public static final Target MUSHROOM_SHORE = biome(
            BiomePredicate.of(ExtendedBiomeIds.MUSHROOM_ISLAND)
                .and(BiomePredicate.neighborsMatch(ExtendedBiomeIds.OCEAN, 1)),
            ExtendedBiomeIds.MUSHROOM_SHORE
        );

        public static Target layer(BiomePredicate predicate, String layer) {
            return new Target(predicate, layer, Type.LAYER);
        }

        public static Target biome(BiomePredicate predicate, ExtendedHolder<Biome> biome) {
            return new Target(predicate, biome.unwrapExtendedKey().orElseThrow().toString(), Type.BIOME);
        }

        public static Target inclusiveBeach(Set<ExtendedIdentifier> exceptions, ExtendedIdentifier beach) {
            return inclusiveBeach(exceptions, BiomePredicate.of(ExtendedBiomeIds.OCEAN), beach);
        }

        public static Target inclusiveBeach(Set<ExtendedHolder<Biome>> exceptions, BiomePredicate ocean, ExtendedHolder<Biome> beach) {
            return biome(
                BiomePredicate.noneInSet(exceptions)
                    .and(BiomePredicate.neighborsMatch(ocean, 1)),
                beach
            );
        }

        public static Target exclusiveBeach(Set<ExtendedHolder<Biome>> biomes, ExtendedHolder<Biome> beach) {
            return exclusiveBeach(biomes, BiomePredicate.of(ExtendedBiomeIds.OCEAN), beach);
        }

        public static Target exclusiveBeach(Set<ExtendedHolder<Biome>> biomes, BiomePredicate ocean, ExtendedHolder<Biome> beach) {
            return biome(
                BiomePredicate.inSet(biomes)
                    .and(BiomePredicate.neighborsMatch(ocean, 1)),
                beach
            );
        }

        public static Target simpleHills(Set<ExtendedHolder<Biome>> affectedBiomes, String layer) {
            return layer(
                BiomePredicate.inSet(affectedBiomes)
                    .and(BiomePredicate.interior())
                    .and(BiomePredicate.oneIn(3)),
                layer
            );
        }

        public static Target borderTransition(ExtendedHolder<Biome> from, Set<ExtendedHolder<Biome>> similarBiomes, ExtendedHolder<Biome> to) {
            return biome(
                BiomePredicate.of(from)
                    .and(BiomePredicate.neighborsMatch(
                        BiomePredicate.inSet(similarBiomes), 4).invert()),
                to
            );
        }

        private ConfiguredTarget configure(Function<String, Layer> layerMap) {
            return switch (this.type) {
                case LAYER -> new ConfiguredLayerTarget(this.predicate, layerMap.apply(this.result));
                case BIOME -> new ConfiguredBiomeTarget(this.predicate, ExtendedIdentifier.of(this.result));
            };
        }

        public enum Type implements StringRepresentable {
            LAYER("layer"),
            BIOME("biome");

            private final String id;

            Type(String id) {
                this.id = id;
            }

            @Override
            public String getSerializedName() {
                return this.id;
            }
        }
    }

    private interface ConfiguredTarget {
        BiomePredicate predicate();
        Layer layer();
        ExtendedHolder<Biome> sample(int x, int z);
        void addPossibleBiomes(Set<ExtendedHolder<Biome>> biomes);
    }

    private record ConfiguredLayerTarget(BiomePredicate predicate, Layer layer) implements ConfiguredTarget {
        @Override
        public ExtendedHolder<Biome> sample(int x, int z) {
            return this.layer.sample(x, z);
        }

        @Override
        public void addPossibleBiomes(Set<ExtendedHolder<Biome>> biomes) {
            layer.addPossibleBiomesRecursive(biomes);
        }
    }

    private record ConfiguredBiomeTarget(BiomePredicate predicate, ExtendedHolder<Biome> biome) implements ConfiguredTarget {
        @Override
        public Layer layer() {
            return null;
        }

        @Override
        public ExtendedHolder<Biome> sample(int x, int z) {
            return this.biome;
        }

        @Override
        public void addPossibleBiomes(Set<ExtendedHolder<Biome>> biomes) {
            biomes.add(biome);
        }
    }
}
