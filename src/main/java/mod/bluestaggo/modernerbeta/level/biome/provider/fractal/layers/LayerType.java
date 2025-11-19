package mod.bluestaggo.modernerbeta.level.biome.provider.fractal.layers;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;

public record LayerType<L extends Layer>(com.mojang.serialization.MapCodec<L> codec) {
    private static IRegistryHandler<LayerType<?>> registryHandler;

    public static LayerType<AddLandLayer> ADD_LAND;
    public static LayerType<ApplyOceanClimateLayer> APPLY_OCEAN_CLIMATE;
    public static LayerType<BiomeReplacementLayer> BIOME_REPLACEMENT;
    public static LayerType<ComputeRiverLayer> COMPUTE_RIVER;
    public static LayerType<ConditionalOverlayLayer> CONDITIONAL_OVERLAY;
    public static LayerType<ConstantBiomeLayer> CONSTANT_BIOME;
    public static LayerType<FuzzyZoomLayer> FUZZY_ZOOM;
    public static LayerType<InitLandLayer> INIT_LAND;
    public static LayerType<InitRiverLayer> INIT_RIVER;
    public static LayerType<MappedNoiseLayer> MAPPED_NOISE;
    public static LayerType<MixRiverLayer> MIX_RIVER;
    public static LayerType<ModalZoomLayer> MODAL_ZOOM;
    public static LayerType<PerlinZoomLayer> PERLIN_ZOOM;
    public static LayerType<PointZoomLayer> POINT_ZOOM;
    public static LayerType<PredicateOverlayLayer> PREDICATE_OVERLAY;
    public static LayerType<ProxyLayer> PROXY_LAYER;
    public static LayerType<PreSkipRandomLayer> PRE_SKIP_RANDOM;
    public static LayerType<RandomBiomeLayer> RANDOM_BIOME;
    public static LayerType<SmoothLayer> SMOOTH;
    public static LayerType<StackedZoomLayer> STACKED_ZOOM;
    public static LayerType<SupplyRandomLayer> SUPPLY_RANDOM;
    public static LayerType<UnsaltedLayer> UNSALTED;
    public static LayerType<VoronoiZoomLayer> VORONOI_ZOOM;
    public static LayerType<WeightedPoolLayer> WEIGHTED_POOL;

    private static <L extends Layer> LayerType<L> register(String id, com.mojang.serialization.MapCodec<L> codec) {
        LayerType<L> layerType = new LayerType<>(codec);
        return registryHandler.register(ModernerBeta.createId(id), layerType);
    }

    @SuppressWarnings("unchecked")
    public static void init(IRegistryHandler<?> handler) {
        registryHandler = (IRegistryHandler<LayerType<?>>) handler;

        ADD_LAND = register("add_land", AddLandLayer.CODEC);
        APPLY_OCEAN_CLIMATE = register("apply_ocean_climate", ApplyOceanClimateLayer.CODEC);
        BIOME_REPLACEMENT = register("biome_replacement", BiomeReplacementLayer.CODEC);
        COMPUTE_RIVER = register("compute_river", ComputeRiverLayer.CODEC);
        CONDITIONAL_OVERLAY = register("conditional_overlay", ConditionalOverlayLayer.CODEC);
        CONSTANT_BIOME = register("constant_biome", ConstantBiomeLayer.CODEC);
        FUZZY_ZOOM = register("fuzzy_zoom", FuzzyZoomLayer.CODEC);
        INIT_LAND = register("init_land", InitLandLayer.CODEC);
        INIT_RIVER = register("init_river", InitRiverLayer.CODEC);
        MAPPED_NOISE = register("mapped_noise", MappedNoiseLayer.CODEC);
        MIX_RIVER = register("mix_river", MixRiverLayer.CODEC);
        MODAL_ZOOM = register("modal_zoom", ModalZoomLayer.CODEC);
        PERLIN_ZOOM = register("perlin_zoom", PerlinZoomLayer.CODEC);
        POINT_ZOOM = register("point_zoom", PointZoomLayer.CODEC);
        PREDICATE_OVERLAY = register("predicate_overlay", PredicateOverlayLayer.CODEC);
        PRE_SKIP_RANDOM = register("pre_skip_random", PreSkipRandomLayer.CODEC);
        RANDOM_BIOME = register("random_biome", RandomBiomeLayer.CODEC);
        SMOOTH = register("smooth", SmoothLayer.CODEC);
        STACKED_ZOOM = register("stacked_zoom", StackedZoomLayer.CODEC);
        SUPPLY_RANDOM = register("supply_random", SupplyRandomLayer.CODEC);
        UNSALTED = register("unsalted", UnsaltedLayer.CODEC);
        VORONOI_ZOOM = register("voronoi_zoom", VoronoiZoomLayer.CODEC);
        WEIGHTED_POOL = register("weighted_pool", WeightedPoolLayer.CODEC);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
}
