package mod.bespectacled.modernbeta.world.feature;

import com.mojang.serialization.Codec;
import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.world.feature.foliage.Oak14a08FoliagePlacer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

public class ModernBetaFoliagePlacers {
	public static final FoliagePlacerType<Oak14a08FoliagePlacer> OAK_14A_08_FOLIAGE_PLACER = register(
			ModernBetaFeatureTags.OAK_14A_08_FOLIAGE_PLACER, Oak14a08FoliagePlacer.CODEC
	);

	private static <P extends FoliagePlacer> FoliagePlacerType<P> register(String id, Codec<P> codec) {
        return Registry.register(Registries.FOLIAGE_PLACER_TYPE, ModernBeta.createId(id), new FoliagePlacerType<>(codec));
    }

	public static void register() {}
}
