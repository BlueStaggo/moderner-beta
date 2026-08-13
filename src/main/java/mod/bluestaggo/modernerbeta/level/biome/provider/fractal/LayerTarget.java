package mod.bluestaggo.modernerbeta.level.biome.provider.fractal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public record LayerTarget(Type type, String value) {
    private static final LayerTarget NONE = new LayerTarget(Type.BIOME, ExtendedBiomeIds.NULL.toString());

    public static final Codec<LayerTarget> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            StringRepresentable.fromEnum(Type::values).fieldOf("type").forGetter(LayerTarget::type),
            Codec.STRING.fieldOf("value").forGetter(LayerTarget::value)
        ).apply(instance, LayerTarget::new)
    );

    public static LayerTarget biome(String biome) {
        return new LayerTarget(Type.BIOME, biome);
    }

    public static LayerTarget biome(ExtendedIdentifier biome) {
        return new LayerTarget(Type.BIOME, biome.toString());
    }

    public static LayerTarget layer(String biome) {
        return new LayerTarget(Type.LAYER, biome);
    }

    public static LayerTarget none() {
        return NONE;
    }

    public Configured configure(Function<String, Layer> layerMap) {
        return switch (this.type) {
            case LAYER -> new Configured.OfLayer(layerMap.apply(this.value));
            case BIOME -> new Configured.OfBiome(ExtendedIdentifier.of(this.value));
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

    public interface Configured {
        Optional<Layer> asLayer();
        ExtendedHolder<Biome> sample(int x, int z);
        boolean isEquivalentToOrNull(Layer layer);
        void addPossibleBiomes(Set<ExtendedHolder<Biome>> biomes);
        void bindBiomes(ExtendedBiomeResolver biomeResolver);

        record OfLayer(Layer layer) implements Configured {
            @Override
            public Optional<Layer> asLayer() {
                return Optional.of(layer);
            }

            @Override
            public ExtendedHolder<Biome> sample(int x, int z) {
                return this.layer.sample(x, z);
            }

            @Override
            public boolean isEquivalentToOrNull(Layer layer) {
                return this.layer == layer;
            }

            @Override
            public void addPossibleBiomes(Set<ExtendedHolder<Biome>> biomes) {
                this.layer.addPossibleBiomesRecursive(biomes);
            }

            @Override
            public void bindBiomes(ExtendedBiomeResolver biomeResolver) {
            }
        }

        final class OfBiome implements Configured {
            private final ExtendedIdentifier biomeId;
            private ExtendedHolder<Biome> biome;

            private OfBiome(ExtendedIdentifier biomeId) {
                this.biomeId = biomeId;
            }

            @Override
            public Optional<Layer> asLayer() {
                return Optional.empty();
            }

            @Override
            public ExtendedHolder<Biome> sample(int x, int z) {
                return this.biome;
            }

            @Override
            public boolean isEquivalentToOrNull(Layer layer) {
                return ExtendedBiomeIds.NULL.equals(this.biomeId);
            }

            @Override
            public void addPossibleBiomes(Set<ExtendedHolder<Biome>> biomes) {
                biomes.add(this.biome);
            }

            @Override
            public void bindBiomes(ExtendedBiomeResolver biomeResolver) {
                this.biome = biomeResolver.resolve(this.biomeId);
            }
        }
    }
}
