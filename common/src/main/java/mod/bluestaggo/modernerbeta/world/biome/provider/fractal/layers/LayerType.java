package mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers;

import com.mojang.serialization.MapCodec;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;

public record LayerType<L extends Layer>(MapCodec<L> codec) {
    private static IRegistryHandler<LayerType<?>> registryHandler;

    public static LayerType<AddLandLayer> ADD_LAND;
    public static LayerType<ApplyOceanClimateLayer> APPLY_OCEAN_CLIMATE;
    public static LayerType<BiomeToLayerOverlayLayer> BIOME_TO_LAYER_OVERLAY;
    public static LayerType<ComputeRiverLayer> COMPUTE_RIVER;
    public static LayerType<ConditionalBiomeOverlayLayer> CONDITIONAL_BIOME_OVERLAY;
    public static LayerType<ConditionalLayerOverlayLayer> CONDITIONAL_LAYER_OVERLAY;
    public static LayerType<ConstantBiomeLayer> CONSTANT_BIOME;
    public static LayerType<FuzzyZoomLayer> FUZZY_ZOOM;
    public static LayerType<InitLandLayer> INIT_LAND;
    public static LayerType<InitRiverLayer> INIT_RIVER;
    public static LayerType<MappedNoiseLayer> MAPPED_NOISE;
    public static LayerType<MixRiverLayer> MIX_RIVER;
    public static LayerType<ModalZoomLayer> MODAL_ZOOM;
    public static LayerType<PointZoomLayer> POINT_ZOOM;
    public static LayerType<PredicateOverlayLayer> PREDICATE_OVERLAY;
    public static LayerType<PreSkipRandomLayer> PRE_SKIP_RANDOM;
    public static LayerType<RandomBiomeLayer> RANDOM_BIOME;
    public static LayerType<SimpleBiomeReplacementLayer> SIMPLE_BIOME_REPLACEMENT;
    public static LayerType<SmoothLayer> SMOOTH;
    public static LayerType<StackedZoomLayer> STACKED_ZOOM;
    public static LayerType<SupplyRandomLayer> SUPPLY_RANDOM;
    public static LayerType<UnsaltedLayer> UNSALTED;
    public static LayerType<WeightedBiomeLayer> WEIGHTED_BIOME;
    public static LayerType<WeightedLayerLayer> WEIGHTED_LAYER;

    private static <L extends Layer> LayerType<L> register(String id, MapCodec<L> codec) {
        LayerType<L> layerType = new LayerType<>(codec);
        return registryHandler.register(ModernerBeta.createId(id), layerType);
    }

    @SuppressWarnings("unchecked")
    public static void init(IRegistryHandler<?> handler) {
        registryHandler = (IRegistryHandler<LayerType<?>>) handler;

        ADD_LAND = register("add_land", AddLandLayer.CODEC);
        APPLY_OCEAN_CLIMATE = register("apply_ocean_climate", ApplyOceanClimateLayer.CODEC);
        BIOME_TO_LAYER_OVERLAY = register("biome_to_layer_overlay", BiomeToLayerOverlayLayer.CODEC);
        COMPUTE_RIVER = register("compute_river", ComputeRiverLayer.CODEC);
        CONDITIONAL_BIOME_OVERLAY = register("conditional_biome_overlay", ConditionalBiomeOverlayLayer.CODEC);
        CONDITIONAL_LAYER_OVERLAY = register("conditional_layer_overlay", ConditionalLayerOverlayLayer.CODEC);
        CONSTANT_BIOME = register("constant_biome", ConstantBiomeLayer.CODEC);
        FUZZY_ZOOM = register("fuzzy_zoom", FuzzyZoomLayer.CODEC);
        INIT_LAND = register("init_land", InitLandLayer.CODEC);
        INIT_RIVER = register("init_river", InitRiverLayer.CODEC);
        MAPPED_NOISE = register("mapped_noise", MappedNoiseLayer.CODEC);
        MIX_RIVER = register("mix_river", MixRiverLayer.CODEC);
        MODAL_ZOOM = register("modal_zoom", ModalZoomLayer.CODEC);
        POINT_ZOOM = register("point_zoom", PointZoomLayer.CODEC);
        PREDICATE_OVERLAY = register("predicate_overlay", PredicateOverlayLayer.CODEC);
        PRE_SKIP_RANDOM = register("pre_skip_random", PreSkipRandomLayer.CODEC);
        RANDOM_BIOME = register("random_biome", RandomBiomeLayer.CODEC);
        SIMPLE_BIOME_REPLACEMENT = register("simple_biome_replacement", SimpleBiomeReplacementLayer.CODEC);
        SMOOTH = register("smooth", SmoothLayer.CODEC);
        STACKED_ZOOM = register("stacked_zoom", StackedZoomLayer.CODEC);
        SUPPLY_RANDOM = register("supply_random", SupplyRandomLayer.CODEC);
        UNSALTED = register("unsalted", UnsaltedLayer.CODEC);
        WEIGHTED_BIOME = register("weighted_biome", WeightedBiomeLayer.CODEC);
        WEIGHTED_LAYER = register("weighted_layer", WeightedLayerLayer.CODEC);
    }
}
