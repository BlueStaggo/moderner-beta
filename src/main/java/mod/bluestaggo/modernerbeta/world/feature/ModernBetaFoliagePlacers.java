package mod.bluestaggo.modernerbeta.world.feature;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.world.feature.foliage.Oak14a08FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

public class ModernBetaFoliagePlacers {
	private static IRegistryHandler<FoliagePlacerType<?>> registryHandler;
	public static FoliagePlacerType<Oak14a08FoliagePlacer> OAK_14A_08_FOLIAGE_PLACER;

	private static <P extends FoliagePlacer> FoliagePlacerType<P> register(
		String id,
		//? if >=1.20.5 {
		/*com.mojang.serialization.MapCodec<P> codec
		*///?} else {
		com.mojang.serialization.Codec<P> codec
		//?}
	) {
		return registryHandler.register(ModernerBeta.createId(id), new FoliagePlacerType<>(codec));
    }

	@SuppressWarnings("unchecked")
    public static void register(IRegistryHandler<?> handler) {
		registryHandler = (IRegistryHandler<FoliagePlacerType<?>>) handler;
		OAK_14A_08_FOLIAGE_PLACER = register(
				ModernBetaFeatureTags.OAK_14A_08_FOLIAGE_PLACER, Oak14a08FoliagePlacer.CODEC
		);
	}
}
