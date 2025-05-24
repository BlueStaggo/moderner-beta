package mod.bluestaggo.modernerbeta.world.biome.provider.fractal;

import com.mojang.serialization.MapCodec;
import mod.bluestaggo.modernerbeta.api.registry.ModernBetaBuiltInRegistries;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.DirtyZoomLayer;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.RandomBiomeLayer;

public record LayerType<L extends Layer>(Class<L> layerClass, MapCodec<L> codec) {
    public static final LayerType<RandomBiomeLayer> RANDOM_BIOME = register("random_biome", RandomBiomeLayer.class, RandomBiomeLayer.CODEC);
    public static final LayerType<DirtyZoomLayer> DIRTY_ZOOM = register("dirty_zoom", DirtyZoomLayer.class, DirtyZoomLayer.CODEC);

    private static <L extends Layer> LayerType<L> register(String id, Class<L> layerClass, MapCodec<L> codec) {
        LayerType<L> layerType = new LayerType<>(layerClass, codec);
        ModernBetaBuiltInRegistries.FRACTAL_LAYER.register(id, layerType);
        return layerType;
    }
}
