package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.MapCodec;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import net.minecraft.registry.Registry;

public record LayerType<L extends Layer>(MapCodec<L> codec) {
    public static final LayerType<AddLandLayer> ADD_LAND = register("add_land", AddLandLayer.CODEC);
    public static final LayerType<BiomeToLayerOverlayLayer> BIOME_TO_LAYER_OVERLAY = register("biome_to_layer_overlay", BiomeToLayerOverlayLayer.CODEC);
    public static final LayerType<ComputeRiverLayer> COMPUTE_RIVER = register("compute_river", ComputeRiverLayer.CODEC);
    public static final LayerType<ConditionalMaskLayer> CONDITIONAL_MASK = register("conditional_mask", ConditionalMaskLayer.CODEC);
    public static final LayerType<ConstantBiomeLayer> CONSTANT_BIOME = register("constant_biome", ConstantBiomeLayer.CODEC);
    public static final LayerType<FuzzyZoomLayer> FUZZY_ZOOM = register("fuzzy_zoom", FuzzyZoomLayer.CODEC);
    public static final LayerType<InitLandLayer> INIT_LAND = register("init_land", InitLandLayer.CODEC);
    public static final LayerType<InitRiverLayer> INIT_RIVER = register("init_river", InitRiverLayer.CODEC);
    public static final LayerType<MixRiverLayer> MIX_RIVER = register("mix_river", MixRiverLayer.CODEC);
    public static final LayerType<ModalZoomLayer> MODAL_ZOOM = register("modal_zoom", ModalZoomLayer.CODEC);
    public static final LayerType<PointZoomLayer> POINT_ZOOM = register("point_zoom", PointZoomLayer.CODEC);
    public static final LayerType<PredicateOverlayLayer> PREDICATE_OVERLAY = register("predicate_overlay", PredicateOverlayLayer.CODEC);
    public static final LayerType<RandomBiomeLayer> RANDOM_BIOME = register("random_biome", RandomBiomeLayer.CODEC);
    public static final LayerType<SimpleBiomeReplacementLayer> SIMPLE_BIOME_REPLACEMENT = register("simple_biome_replacement", SimpleBiomeReplacementLayer.CODEC);
    public static final LayerType<SmoothLayer> SMOOTH = register("smooth", SmoothLayer.CODEC);
    public static final LayerType<StackedZoomLayer> STACKED_ZOOM = register("stacked_zoom", StackedZoomLayer.CODEC);
    public static final LayerType<WeightedBiomeLayer> WEIGHTED_BIOME = register("weighted_biome", WeightedBiomeLayer.CODEC);

    private static <L extends Layer> LayerType<L> register(String id, MapCodec<L> codec) {
        LayerType<L> layerType = new LayerType<>(codec);
        Registry.register(ModernBetaRegistries.FRACTAL_LAYER, ModernerBeta.createId(id), layerType);
        return layerType;
    }

    public static void init() {
    }
}
