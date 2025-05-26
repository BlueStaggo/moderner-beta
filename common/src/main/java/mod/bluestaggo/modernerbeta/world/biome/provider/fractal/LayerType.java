package mod.bluestaggo.modernerbeta.world.biome.provider.fractal;

import com.mojang.serialization.MapCodec;
import mod.bluestaggo.modernerbeta.api.registry.ModernBetaBuiltInRegistries;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.*;

public record LayerType<L extends Layer>(Class<L> layerClass, MapCodec<L> codec) {
    public static final LayerType<AddLandLayer> ADD_LAND = register("add_land", AddLandLayer.class, AddLandLayer.CODEC);
    public static final LayerType<BorderLayer> BORDER = register("border", BorderLayer.class, BorderLayer.CODEC);
    public static final LayerType<ComputeRiverLayer> COMPUTE_RIVER = register("compute_river", ComputeRiverLayer.class, ComputeRiverLayer.CODEC);
    public static final LayerType<ConstantBiomeLayer> CONSTANT_BIOME = register("constant_biome", ConstantBiomeLayer.class, ConstantBiomeLayer.CODEC);
    public static final LayerType<DiagonalInnerMaskLayer> DIAGONAL_INNER_MASK = register("diagonal_inner_mask", DiagonalInnerMaskLayer.class, DiagonalInnerMaskLayer.CODEC);
    public static final LayerType<FuzzyZoomLayer> FUZZY_ZOOM = register("fuzzy_zoom", FuzzyZoomLayer.class, FuzzyZoomLayer.CODEC);
    public static final LayerType<InitLandLayer> INIT_LAND = register("init_land", InitLandLayer.class, InitLandLayer.CODEC);
    public static final LayerType<InnerMaskLayer> INNER_MASK = register("inner_mask", InnerMaskLayer.class, InnerMaskLayer.CODEC);
    public static final LayerType<InverseMaskLayer> INVERSE_MASK = register("inverse_mask", InverseMaskLayer.class, InverseMaskLayer.CODEC);
    public static final LayerType<MaskLayer> MASK = register("mask", MaskLayer.class, MaskLayer.CODEC);
    public static final LayerType<MixRiverLayer> MIX_RIVER = register("mix_river", MixRiverLayer.class, MixRiverLayer.CODEC);
    public static final LayerType<ModalZoomLayer> MODAL_ZOOM = register("modal_zoom", ModalZoomLayer.class, ModalZoomLayer.CODEC);
    public static final LayerType<PointZoomLayer> POINT_ZOOM = register("point_zoom", PointZoomLayer.class, PointZoomLayer.CODEC);
    public static final LayerType<RandomBiomeLayer> RANDOM_BIOME = register("random_biome", RandomBiomeLayer.class, RandomBiomeLayer.CODEC);
    public static final LayerType<InitRiverLayer> INIT_RIVER = register("init_river", InitRiverLayer.class, InitRiverLayer.CODEC);
    public static final LayerType<SmoothLayer> SMOOTH = register("smooth", SmoothLayer.class, SmoothLayer.CODEC);
    public static final LayerType<StackedZoomLayer> STACKED_ZOOM = register("stacked_zoom", StackedZoomLayer.class, StackedZoomLayer.CODEC);
    public static final LayerType<WeightedBiomeLayer> WEIGHTED_BIOME = register("weighted_biome", WeightedBiomeLayer.class, WeightedBiomeLayer.CODEC);

    private static <L extends Layer> LayerType<L> register(String id, Class<L> layerClass, MapCodec<L> codec) {
        LayerType<L> layerType = new LayerType<>(layerClass, codec);
        ModernBetaBuiltInRegistries.FRACTAL_LAYER.register(id, layerType);
        return layerType;
    }
}
