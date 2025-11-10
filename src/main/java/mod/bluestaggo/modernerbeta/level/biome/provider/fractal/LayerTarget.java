package mod.bluestaggo.modernerbeta.level.biome.provider.fractal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers.Layer;
import net.minecraft.util.StringRepresentable;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public record LayerTarget(Type type, String value) {
    private static final LayerTarget NONE = new LayerTarget(Type.BIOME, ExtendedBiomeId.NULL.toString());

    public static final Codec<LayerTarget> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            StringRepresentable.fromEnum(Type::values).fieldOf("type").forGetter(LayerTarget::type),
            Codec.STRING.fieldOf("value").forGetter(LayerTarget::value)
        ).apply(instance, LayerTarget::new)
    );

    public static LayerTarget biome(String biome) {
        return new LayerTarget(Type.BIOME, biome);
    }

    public static LayerTarget biome(ExtendedBiomeId biome) {
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
            case BIOME -> new Configured.OfBiome(ExtendedBiomeId.of(this.value));
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
        ExtendedBiomeId sample(int x, int z);
        boolean isEquivalentToOrNull(Layer layer);
        void addPossibleBiomes(Set<ExtendedBiomeId> biomes);

        record OfLayer(Layer layer) implements Configured {
            @Override
            public Optional<Layer> asLayer() {
                return Optional.of(layer);
            }

            @Override
            public ExtendedBiomeId sample(int x, int z) {
                return this.layer.sample(x, z);
            }

            @Override
            public boolean isEquivalentToOrNull(Layer layer) {
                return this.layer == layer;
            }

            @Override
            public void addPossibleBiomes(Set<ExtendedBiomeId> biomes) {
                this.layer.addPossibleBiomesRecursive(biomes);
            }
        }

        record OfBiome(ExtendedBiomeId biome) implements Configured {
            @Override
            public Optional<Layer> asLayer() {
                return Optional.empty();
            }

            @Override
            public ExtendedBiomeId sample(int x, int z) {
                return this.biome;
            }

            @Override
            public boolean isEquivalentToOrNull(Layer layer) {
                return ExtendedBiomeId.NULL.equals(this.biome);
            }

            @Override
            public void addPossibleBiomes(Set<ExtendedBiomeId> biomes) {
                biomes.add(this.biome);
            }
        }
    }
}
