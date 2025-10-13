package mod.bluestaggo.modernerbeta.world.feature.foliage;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFoliagePlacers;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class Oak14a08FoliagePlacer extends FoliagePlacer {
	public static final com.mojang.serialization./*Map*/Codec<Oak14a08FoliagePlacer> CODEC = VersionCompat.createMaybeMapCodec(instance -> foliagePlacerParts(instance)
			.and(Codec.intRange(0, 16).fieldOf("height").forGetter(placer -> placer.height))
			.apply(instance, Oak14a08FoliagePlacer::new));

	private final int height;

	public Oak14a08FoliagePlacer(IntProvider radius, IntProvider offset, int height) {
		super(radius, offset);
		this.height = height;
	}

	@Override
	protected FoliagePlacerType<?> type() {
		return ModernBetaFoliagePlacers.OAK_14A_08_FOLIAGE_PLACER;
	}

	@Override
	protected void createFoliage(LevelSimulatedReader world, FoliageSetter placer, RandomSource random, TreeConfiguration config, int trunkHeight, FoliageAttachment treeNode, int foliageHeight, int radius, int offset) {
		for (int y = offset; y >= offset - foliageHeight; --y) {
			this.placeLeavesRow(world, placer, random, config, treeNode.pos(), radius, y, treeNode.doubleTrunk());
		}
	}

	@Override
	public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
		return this.height;
	}

	@Override
	protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int radius, boolean giantTrunk) {
		return y == 0 && Math.abs(dx) + Math.abs(dz) >= radius * 2;
	}
}
