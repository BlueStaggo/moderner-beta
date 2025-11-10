package mod.bluestaggo.modernerbeta.level.feature;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.mixin.FoliagePlacerTypeAccessor;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.level.feature.foliage.BetaLargeOakFoliagePlacer;
import mod.bluestaggo.modernerbeta.level.feature.foliage.Oak14a08FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class ModernBetaFoliagePlacers {
	private static IRegistryHandler<FoliagePlacerType<?>> registryHandler;
	public static FoliagePlacerType<Oak14a08FoliagePlacer> OAK_14A_08;
	public static FoliagePlacerType<BetaLargeOakFoliagePlacer> BETA_LARGE_OAK;

	private static <P extends FoliagePlacer> FoliagePlacerType<P> register(
		String id,
		com.mojang.serialization.MapCodec<P> codec
	) {
		return registryHandler.register(ModernerBeta.createId(id), FoliagePlacerTypeAccessor.create(codec));
    }

	@SuppressWarnings("unchecked")
    public static void register(IRegistryHandler<?> handler) {
		registryHandler = (IRegistryHandler<FoliagePlacerType<?>>) handler;
		OAK_14A_08 = register(
				ModernBetaFeatureTags.OAK_14A_08_FOLIAGE_PLACER, Oak14a08FoliagePlacer.CODEC
		);
		BETA_LARGE_OAK = register(
				ModernBetaFeatureTags.BETA_LARGE_OAK_FOLIAGE_PLACER, BetaLargeOakFoliagePlacer.CODEC
		);
	}
}
