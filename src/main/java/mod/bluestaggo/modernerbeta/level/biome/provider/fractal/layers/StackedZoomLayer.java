package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.function.Function;
import java.util.function.LongFunction;

public class StackedZoomLayer extends SingleParentLayer {
    public static final com.mojang.serialization.MapCodec<StackedZoomLayer> CODEC = VersionCompat.createMaybeMapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(instance.group(
                Codec.INT.fieldOf("level").orElse(1).forGetter(layer -> layer.level),
                Codec.INT.fieldOf("seedModifier").orElse(1).forGetter(layer -> layer.seedModifier),
                StringRepresentable.fromEnum(Type::values).fieldOf("zoomType").orElse(Type.MODAL).forGetter(layer -> layer.zoomType)
            ))
            .apply(instance, StackedZoomLayer::new)
    );

    private final int level;
    private final int seedModifier;
    private final Type zoomType;
    private transient Layer stackedLayer;

    public StackedZoomLayer(String id, long seed, String parent, int level, int seedModifier, Type zoomType) {
        super(id, seed, parent);
        this.level = level;
        this.seedModifier = seedModifier;
        this.zoomType = zoomType;
    }

    @Override
    public void configure(Function<String, Layer> layerMap) {
        super.configure(layerMap);
        Layer layer = this.parentLayer;
        for (int i = 0; i < this.level; i++) {
            Layer zoomParent = layer;
            Layer zoomLayer = this.zoomType.constructor.apply(this.seed + i * this.seedModifier);
            zoomLayer.configure(key -> zoomParent);
            layer = zoomLayer;
        }
        this.stackedLayer = layer;
    }

    @Override
    public LayerType<?> getType() {
        return LayerType.STACKED_ZOOM;
    }

    @Override
    protected ExtendedHolder<Biome> generate(int x, int z) {
        return null;
    }

    @Override
    public ExtendedHolder<Biome> sample(int x, int z) {
        return this.stackedLayer.sample(x, z);
    }

    @Override
    protected List<Layer> getParents() {
        return List.of(this.parentLayer, this.stackedLayer);
    }

    @Override
    protected String getName() {
        return super.getName() + "(" + this.zoomType.id + ")";
    }

    public static StackedZoomLayer modal(String id, long seed, String parent, int level) {
        return modal(id, seed, parent, level, 1);
    }

    public static StackedZoomLayer modal(String id, long seed, String parent, int level, int seedModifier) {
        return new StackedZoomLayer(id, seed, parent, level, seedModifier, Type.MODAL);
    }

    public static StackedZoomLayer fuzzy(String id, long seed, String parent, int level) {
        return fuzzy(id, seed, parent, level, 1);
    }

    public static StackedZoomLayer fuzzy(String id, long seed, String parent, int level, int seedModifier) {
        return new StackedZoomLayer(id, seed, parent, level, seedModifier, Type.FUZZY);
    }

    public enum Type implements StringRepresentable {
        MODAL("modal", seed -> new ModalZoomLayer("", seed, "")),
        FUZZY("fuzzy", seed -> new FuzzyZoomLayer("", seed, ""));

        public final String id;
        public final LongFunction<Layer> constructor;

        Type(String id, LongFunction<Layer> constructor) {
            this.id = id;
            this.constructor = constructor;
        }

        @Override
        public String getSerializedName() {
            return this.id;
        }
    }
}
