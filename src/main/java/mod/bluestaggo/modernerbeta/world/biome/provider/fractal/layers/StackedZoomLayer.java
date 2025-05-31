package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import net.minecraft.util.StringIdentifiable;

import java.util.List;
import java.util.function.Function;
import java.util.function.LongFunction;

public class StackedZoomLayer extends SingleParentLayer {
    public static final MapCodec<StackedZoomLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(instance.group(
                Codec.INT.fieldOf("level").orElse(1).forGetter(layer -> layer.level),
                StringIdentifiable.createCodec(Type::values).fieldOf("zoomType").orElse(Type.MODAL).forGetter(layer -> layer.zoomType)
            ))
            .apply(instance, StackedZoomLayer::new)
    );

    private final int level;
    private final Type zoomType;
    private transient Layer stackedLayer;

    public StackedZoomLayer(String id, long seed, String parent, int level, Type zoomType) {
        super(id, seed, parent);
        this.level = level;
        this.zoomType = zoomType;
    }

    @Override
    public void configure(Function<String, Layer> layerMap) {
        super.configure(layerMap);
        Layer layer = this.parentLayer;
        for (int i = 0; i < this.level; i++) {
            Layer zoomParent = layer;
            BaseZoomLayer zoomLayer = this.zoomType.constructor.apply(this.seed + i);
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
    protected ExtendedBiomeId generate(int x, int z) {
        return null;
    }

    @Override
    public synchronized ExtendedBiomeId sample(int x, int z) {
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
        return new StackedZoomLayer(id, seed, parent, level, Type.MODAL);
    }

    public static StackedZoomLayer fuzzy(String id, long seed, String parent, int level) {
        return new StackedZoomLayer(id, seed, parent, level, Type.FUZZY);
    }

    public enum Type implements StringIdentifiable {
        MODAL("modal", seed -> new ModalZoomLayer("", seed, "")),
        FUZZY("fuzzy", seed -> new FuzzyZoomLayer("", seed, ""));

        public final String id;
        public final LongFunction<BaseZoomLayer> constructor;

        Type(String id, LongFunction<BaseZoomLayer> constructor) {
            this.id = id;
            this.constructor = constructor;
        }

        @Override
        public String asString() {
            return this.id;
        }
    }
}
