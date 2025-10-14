package mod.bluestaggo.modernerbeta.world.feature;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.mixin.AccessorFoliagePlacerType;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.world.feature.foliage.BetaLargeOakFoliagePlacer;
import mod.bluestaggo.modernerbeta.world.feature.foliage.Oak14a08FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class ModernBetaFoliagePlacers {
	private static IRegistryHandler<FoliagePlacerType<?>> registryHandler;
	public static FoliagePlacerType<Oak14a08FoliagePlacer> OAK_14A_08_FOLIAGE_PLACER;
	public static FoliagePlacerType<BetaLargeOakFoliagePlacer> BETA_LARGE_OAK_FOLIAGE_PLACER;

	private static <P extends FoliagePlacer> FoliagePlacerType<P> register(
		String id,
		com.mojang.serialization.MapCodec<P> codec
	) {
		return registryHandler.register(ModernerBeta.createId(id), AccessorFoliagePlacerType.create(codec));
    }

	@SuppressWarnings("unchecked")
    public static void register(IRegistryHandler<?> handler) {
		registryHandler = (IRegistryHandler<FoliagePlacerType<?>>) handler;
		OAK_14A_08_FOLIAGE_PLACER = register(
				ModernBetaFeatureTags.OAK_14A_08_FOLIAGE_PLACER, Oak14a08FoliagePlacer.CODEC
		);
		BETA_LARGE_OAK_FOLIAGE_PLACER = register(
				ModernBetaFeatureTags.BETA_LARGE_OAK_FOLIAGE_PLACER, BetaLargeOakFoliagePlacer.CODEC
		);
	}
}
