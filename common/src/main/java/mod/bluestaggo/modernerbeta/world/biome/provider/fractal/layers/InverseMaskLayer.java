package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.Layer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.LayerType;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class InverseMaskLayer extends SingleParentLayer {
    public static final MapCodec<InverseMaskLayer> CODEC = RecordCodecBuilder.mapCodec(
        instance -> fillSingleParentLayerFields(instance)
            .and(instance.group(
                Codec.STRING.fieldOf("operator").forGetter(layer -> layer.operator),
                CodecUtil.set(ExtendedBiomeId.CODEC).fieldOf("excludedBiomes").forGetter(layer -> layer.excludedBiomes)
            ))
            .apply(instance, InverseMaskLayer::new)
    );

    private final String operator;
    private final Set<ExtendedBiomeId> excludedBiomes;
    private transient Layer operatorLayer;

    public InverseMaskLayer(String id, long seed, String parent, String operator, Set<ExtendedBiomeId> excludedBiomes) {
        super(id, seed, parent);
        this.operator = operator;
        this.excludedBiomes = excludedBiomes;
    }

    @Override
    public void configure(Function<String, Layer> layerMap) {
        super.configure(layerMap);
        this.operatorLayer = layerMap.apply(this.operator);
    }

    @Override
    protected LayerType<?> getType() {
        return LayerType.INVERSE_MASK;
    }

    @Override
    protected List<Layer> getParents() {
        return List.of(this.parentLayer, this.operatorLayer);
    }

    @Override
    protected ExtendedBiomeId generate(int x, int z) {
        ExtendedBiomeId biome = this.parentLayer.sample(x, z);
        if (this.excludedBiomes.contains(biome)) {
            return biome;
        }

        ExtendedBiomeId operatorOutput = this.operatorLayer.sample(x, z);
        if (ExtendedBiomeId.NULL.equals(operatorOutput)) {
            return biome;
        }

        return operatorOutput;
    }
}
