package mod.bluestaggo.modernerbeta.world.feature;

import com.mojang.serialization.MapCodec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.world.feature.foliage.Oak14a08FoliagePlacer;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

public class ModernBetaFoliagePlacers {
	public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPE = DeferredRegister.create(ModernerBeta.MOD_ID, RegistryKeys.FOLIAGE_PLACER_TYPE);
	public static final RegistrySupplier<FoliagePlacerType<Oak14a08FoliagePlacer>> OAK_14A_08_FOLIAGE_PLACER = register(
			ModernBetaFeatureTags.OAK_14A_08_FOLIAGE_PLACER, Oak14a08FoliagePlacer.CODEC
	);

	private static <P extends FoliagePlacer> RegistrySupplier<FoliagePlacerType<P>> register(String id, MapCodec<P> codec) {
		return FOLIAGE_PLACER_TYPE.register(ModernerBeta.createId(id), () -> new FoliagePlacerType<>(codec));
    }

	public static void register() {
		FOLIAGE_PLACER_TYPE.register();
	}
}
